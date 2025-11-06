package com.example.fileviewer.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fileviewer.Common.APIService;
import com.example.fileviewer.Models.Document;
import com.example.fileviewer.R;

public class PdfViewerActivity extends AppCompatActivity {

    private TextView tvDocumentTitle;
    private APIService apiService;
    private int documentId;
    private String pdfUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        documentId = getIntent().getIntExtra("document_id", -1);

        if (documentId == -1) {
            finish();
            return;
        }

        initViews();
        loadDocumentInfo();
    }

    private void initViews() {
        apiService = new APIService(this);
        tvDocumentTitle = findViewById(R.id.tvDocumentTitle);

        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }
    }

    private void loadDocumentInfo() {
        apiService.getDocumentById(documentId, new APIService.DocumentListener() {
            @Override
            public void onSuccess(Document document) {
                generatePdfUrl(document);
                openPdfWithGoogleIntents();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PdfViewerActivity.this,
                        "Ошибка загрузки документа: " + error, Toast.LENGTH_LONG).show();
                generatePdfUrl(null);
            }
        });
    }

    private void generatePdfUrl(Document document) {
        pdfUrl = "http://10.111.66.23:5068/api/documents/" + documentId + ".pdf";
    }

    private void openPdfWithGoogleIntents() {
        if (pdfUrl == null || pdfUrl.isEmpty()) {
            Toast.makeText(this, "URL документа не доступен", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String docsViewerUrl = pdfUrl;

            intent.setData(Uri.parse(docsViewerUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.android.chrome");

            try {
                startActivity(intent);
                Toast.makeText(this, "Открываю PDF...", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                intent.setPackage(null);
                startActivity(intent);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка открытия PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}