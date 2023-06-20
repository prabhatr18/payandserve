package com.digital.payandserve.views.notification;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.database.AppDatabase;
import com.digital.payandserve.database.dao.NotificationDAO;
import com.digital.payandserve.database.entity.NotificationTable;
import com.digital.payandserve.utill.MyUtil;


public class NotificationsListing extends AppCompatActivity {
    private NotificationDAO dao ;
    private NotificationAdapter adapter ;
    RecyclerView noList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.explodeAnimation(this);
        setContentView(R.layout.notification_list);
        gridInitReport();
    }

    private void gridInitReport() {
        noList = findViewById(R.id.noList);
        LinearLayoutManager nearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        noList.setLayoutManager(nearLayoutManager);
        noList.setItemAnimator(new DefaultItemAnimator());
        dao = AppDatabase.getInstance(this).getNotificationDao();
        adapter = new NotificationAdapter(this, dao.get()) {
            @Override
            void onClickDelete(NotificationTable model) {
                dao.delete(model);
                adapter.setData(dao.get());
                if (dao.get().size()==0){
                    errorView("Notifications are not available");
                }
            }
        };
        noList.setAdapter(adapter);
        if (dao.get().size()==0){
            errorView("Notifications are not available");
        }
    }

    private void errorView(String string) {
        TextView tvMsg = findViewById(R.id.tvMsg);
        tvMsg.setText(string);
        noList.setVisibility(View.GONE);
        findViewById(R.id.noData).setVisibility(View.VISIBLE);
    }
}
