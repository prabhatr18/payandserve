package com.digital.payandserve.views.contact;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.permission.AppPermissions;
import com.digital.payandserve.permission.PermissionHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AndroidContactList extends AppCompatActivity {
    public static final int RequestPermissionCode = 1;
    public Context context;
    private Cursor cursor;
    private RecyclerView listView;
    private List<ContactNumberModel> mobileArrayAll, mobileArray;
    private ContactsListAdapter adapter;
    private EditText etSearch;

    private void init() {
        context = AndroidContactList.this;
        mobileArray = new ArrayList<>();
        mobileArrayAll = new ArrayList<>();
        listView = findViewById(R.id.listView);
        etSearch = findViewById(R.id.etSearch);
        initList();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.search_with_list_activity);
        init();

        String[] permissions = {Manifest.permission.READ_CONTACTS};
        AppPermissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                getContact();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mobileArray = new ArrayList<>();
                for (ContactNumberModel d : mobileArrayAll) {
                    if (d.getUserName().toLowerCase().contains(s.toString().toLowerCase()) ||
                            d.getContact().toLowerCase().contains(s.toString().toLowerCase())) {
                        mobileArray.add(d);
                    }
                }
                adapter.UpdateList(mobileArray);
            }
        });

        listView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                listView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ContactNumberModel model = mobileArray.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("contact", model.getContact());
                setResult(100, returnIntent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void initList() {
        LinearLayoutManager nearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(nearLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ContactsListAdapter(this, mobileArray);
        listView.setAdapter(adapter);
    }

    public void getContact() {
        List<Integer> allColors = getAllMaterialColors();
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            int randomColor = allColors.get(new Random().nextInt(allColors.size()));
            mobileArray.add(new ContactNumberModel(name, phonenumber, randomColor));
        }
        Collections.sort(mobileArray, ContactNumberModel.ContactNameComparator);
        mobileArrayAll.addAll(mobileArray);
        adapter.UpdateList(mobileArray);
        cursor.close();

    }

    private List<Integer> getAllMaterialColors() {
        List<Integer> allColors = new ArrayList<>();
        try {
            XmlResourceParser xrp = getResources().getXml(R.xml.android_material_design_colours);
            int nextEvent;
            while ((nextEvent = xrp.next()) != XmlResourceParser.END_DOCUMENT) {
                String s = xrp.getName();
                if ("color".equals(s)) {
                    String color = xrp.nextText();
                    allColors.add(Color.parseColor(color));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allColors;
    }

    private void toast(String m) {
        Toast.makeText(context, m, Toast.LENGTH_SHORT).show();
    }
}
