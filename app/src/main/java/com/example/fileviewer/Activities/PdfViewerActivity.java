package com.example.fileviewer.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.example.fileviewer.Common.APIService;
import com.example.fileviewer.Models.Document;
import com.example.fileviewer.R;

public class PdfViewerActivity extends AppCompatActivity {

    private int documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        documentId = getIntent().getIntExtra("document_id", -1);
        initViews();
        openPdf(APIService.getPdfUrl() + documentId + ".pdf");

        finish();
    }

    private void initViews() {
        ImageButton backButton = findViewById(R.id.backButton);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }
    }

    private void openPdf(String pdfUrl) {
        try {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build();

            customTabsIntent.intent.setPackage("com.android.chrome");

            customTabsIntent.launchUrl(this, Uri.parse(pdfUrl));

        } catch (Exception e) {
                openInBrowser(pdfUrl);
        }
    }

    private void openInBrowser(String pdfUrl) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(pdfUrl));
            startActivity(intent);

        } catch (Exception ex) {}
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}