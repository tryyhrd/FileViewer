package com.example.fileviewer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Adapters.SectionAdapter;
import com.example.fileviewer.Common.APIService;
import com.example.fileviewer.Models.Document;
import com.example.fileviewer.Models.Section;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DocumentViewActivity extends AppCompatActivity {

    private TextView tvDocumentTitle;
    private TextView tvDocumentDate;
    private TextView tvDocumentStatus;
    private RecyclerView recyclerViewSections;

    private SectionAdapter sectionAdapter;
    private List<Section> sectionList = new ArrayList<>();
    private APIService apiService;
    private Document currentDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail);

        currentDocument = (Document) getIntent().getSerializableExtra("document");

        if (currentDocument == null) {
            Toast.makeText(this, "Ошибка: документ не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initComponents();
        setupUI();
        setupRecyclerView();
        setupBackButton();
        loadDocumentSections();
    }

    private void initComponents() {
        apiService = new APIService(this);
        tvDocumentTitle = findViewById(R.id.tvDocumentTitle);
        tvDocumentDate = findViewById(R.id.tvDocumentDate);
        tvDocumentStatus = findViewById(R.id.tvDocumentStatus);
        recyclerViewSections = findViewById(R.id.recyclerViewSections);
    }

    private void setupUI() {
        if (currentDocument.title != null && !currentDocument.title.isEmpty()) {
            tvDocumentTitle.setText(currentDocument.title);
        } else {
            tvDocumentTitle.setText("Без названия");
        }

        if (currentDocument.publication_date != null &&
                !currentDocument.publication_date.equals("null")) {
            String formattedDate = formatDate(String.valueOf(currentDocument.publication_date));
            tvDocumentDate.setText("Дата публикации: " + formattedDate);
            tvDocumentDate.setVisibility(View.VISIBLE);
        } else {
            tvDocumentDate.setVisibility(View.GONE);
        }

        // Статус документа
        if (currentDocument.status != null &&
                !currentDocument.status.isEmpty() &&
                !currentDocument.status.equals("null")) {
            tvDocumentStatus.setText("Статус: " + currentDocument.status);
            tvDocumentStatus.setVisibility(View.VISIBLE);
        } else {
            tvDocumentStatus.setVisibility(View.GONE);
        }
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateString;
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSections.setLayoutManager(layoutManager);

        sectionAdapter = new SectionAdapter(sectionList);
        recyclerViewSections.setAdapter(sectionAdapter);
    }

    private void setupBackButton() {
        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void loadDocumentSections() {

        apiService.getDocumentSections(currentDocument.id, new APIService.SectionsListener() {
            @Override
            public void onSuccess(List<Section> sections) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sectionAdapter.updateData(sections);

                        if (sections.isEmpty()) {
                            Toast.makeText(DocumentViewActivity.this,
                                    "Содержание документа отсутствует",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DocumentViewActivity.this,
                                "Ошибка загрузки содержания: " + error,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
