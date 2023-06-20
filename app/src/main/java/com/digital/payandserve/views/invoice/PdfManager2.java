package com.digital.payandserve.views.invoice;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.digital.payandserve.R;
import com.tejpratapsingh.pdfcreator.activity.PDFCreatorActivity;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;
import com.tejpratapsingh.pdfcreator.views.PDFBody;
import com.tejpratapsingh.pdfcreator.views.PDFHeaderView;
import com.tejpratapsingh.pdfcreator.views.PDFTableView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFHorizontalView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFImageView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFLineSeparatorView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFTextView;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.invoice.model.InvoiceModel;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class PdfManager2 extends PDFCreatorActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("" + getIntent().getStringExtra("title"));
        }

        createPDF("test", new PDFUtil.PDFUtilListener() {
            @Override
            public void pdfGenerationSuccess(File savedPDFFile) {
                Toast.makeText(PdfManager2.this, "PDF Created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void pdfGenerationFailure(Exception exception) {
                Toast.makeText(PdfManager2.this, "PDF NOT Created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected PDFHeaderView getHeaderView(int page) {
        PDFHeaderView headerView = new PDFHeaderView(getApplicationContext());

        PDFTextView pdfTextViewPage = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfTextViewPage.setText(String.format(Locale.getDefault(), "Page: %d", page + 1));
        pdfTextViewPage.setLayout(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0));
        pdfTextViewPage.getView().setGravity(Gravity.CENTER_HORIZONTAL);
        headerView.addView(pdfTextViewPage);

        PDFHorizontalView horizontalView = new PDFHorizontalView(getApplicationContext());
        PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H1);
        SpannableString word = new SpannableString("" + getIntent().getStringExtra("title"));
        word.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pdfTextView.setText(word);
        pdfTextView.setLayout(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        pdfTextView.getView().setGravity(Gravity.CENTER_VERTICAL);
        pdfTextView.getView().setTypeface(pdfTextView.getView().getTypeface(), Typeface.BOLD);

        horizontalView.addView(pdfTextView);
        PDFImageView imageView = new PDFImageView(getApplicationContext());
        LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(60, 60, 0);
        imageView.setImageScale(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageLayoutParam.setMargins(0, 0, 10, 0);
        imageView.setLayout(imageLayoutParam);

        horizontalView.addView(imageView);
        headerView.addView(horizontalView);

        PDFLineSeparatorView lineSeparatorView1 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        headerView.addView(lineSeparatorView1);

        return headerView;
    }

    @Override
    protected PDFBody getBodyViews() {
        PDFBody pdfBody = new PDFBody();

        PDFTextView pdfCompanyNameView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        pdfCompanyNameView.setText(getString(R.string.app_name));
        pdfBody.addView(pdfCompanyNameView);
        PDFLineSeparatorView lineSeparatorView1 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        pdfBody.addView(lineSeparatorView1);
        PDFTextView pdfAddressView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        pdfAddressView.setText(SharedPrefs.getValue(this, SharedPrefs.ADDRESS));
        pdfBody.addView(pdfAddressView);
        PDFLineSeparatorView lineSeparatorView2 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        pdfBody.addView(lineSeparatorView2);

        pdfBody.addView(new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE));
        pdfBody.addView(new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE));
        pdfBody.addView(new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.BLACK));
        PDFTableView.PDFTableRowView tableHeader = new PDFTableView.PDFTableRowView(getApplicationContext());
        for (String s : Constants.REPORT_PDF_MODEL.getKeys()) {
            PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
            pdfTextView.setText("" + s);
            tableHeader.addToRow(pdfTextView);
        }

        PDFTableView.PDFTableRowView tableRowView1 = new PDFTableView.PDFTableRowView(getApplicationContext());
        List<InvoiceModel> invoiceModelList = Constants.REPORT_PDF_MODEL.getValueModels().get(0);
        for (int j = 0; j < Constants.REPORT_PDF_MODEL.getKeys().length; j++) {
            String s = invoiceModelList.get(j).getValue();
            PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
            pdfTextView.setText("" + s);
            tableRowView1.addToRow(pdfTextView);
        }

        PDFTableView tableView = new PDFTableView(getApplicationContext(), tableHeader, tableRowView1);
        for (int i = 0; i < Constants.REPORT_PDF_MODEL.getValueModels().size(); i++) {
            PDFTableView.PDFTableRowView tableRowView = new PDFTableView.PDFTableRowView(getApplicationContext());
            invoiceModelList = Constants.REPORT_PDF_MODEL.getValueModels().get(i);
            for (int j = 0; j < invoiceModelList.size(); j++) {
                PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
                pdfTextView.setText("" + invoiceModelList.get(j).getValue());
                tableRowView.addToRow(pdfTextView);
            }
            tableView.addRow(tableRowView);
        }
        pdfBody.addView(tableView);

        PDFLineSeparatorView lineSeparatorView3 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.BLACK);
        pdfBody.addView(lineSeparatorView3);

        //PDFTextView pdfIconLicenseView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        //Spanned icon8Link = Html.fromHtml("Icon from <a href='https://icons8.com'>https://icons8.com</a>");
        //pdfIconLicenseView.getView().setText(icon8Link);
        //pdfBody.addView(pdfIconLicenseView);
        return pdfBody;
    }

    @Override
    protected void onNextClicked(File savedPDFFile) {
        Uri pdfUri = Uri.fromFile(savedPDFFile);
        Intent intentPdfViewer = new Intent(PdfManager2.this, PdfViewerActivity.class);
        intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);
        startActivity(intentPdfViewer);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
