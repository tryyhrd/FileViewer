package com.example.fileviewer.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Adapters.SectionAdapter;
import com.example.fileviewer.Common.APIService;
import com.example.fileviewer.Models.Document;
import com.example.fileviewer.Models.Section;
import com.example.fileviewer.R;

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
    private int documentId; // Изменено на int
    private Document currentDocument; // Добавлено поле для хранения документа

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail);

        // Получаем ID документа из Intent
        documentId = getIntent().getIntExtra("document_id", -1);

        if (documentId == -1) {
            Toast.makeText(this, "Ошибка: не передан ID документа", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initComponents();
        setupRecyclerView();
        setupBackButton();
        loadDocumentData(documentId);
    }

    private void initComponents() {
        apiService = new APIService(this);
        tvDocumentTitle = findViewById(R.id.tvDocumentTitle);
        tvDocumentDate = findViewById(R.id.tvDocumentDate);
        tvDocumentStatus = findViewById(R.id.tvDocumentStatus);
        recyclerViewSections = findViewById(R.id.recyclerViewSections);
    }

    private void loadDocumentData(int docId) {
        apiService.getDocumentById(docId, new APIService.DocumentListener() {
            @Override
            public void onSuccess(Document document) {
                currentDocument = document; // Сохраняем документ
                displayDocument(document); // Отображаем основную информацию
                loadDocumentSections(docId); // Загружаем секции
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DocumentViewActivity.this,
                                "Ошибка загрузки документа: " + error,
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });
    }

    // МЕТОД displayDocument - отображает основную информацию о документе
    private void displayDocument(Document document) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Отображаем заголовок
                if (document.title != null && !document.title.isEmpty()) {
                    tvDocumentTitle.setText(document.title);
                } else {
                    tvDocumentTitle.setText("Без названия");
                }

                // Отображаем дату публикации
                if (document.publication_date != null &&
                        !document.publication_date.equals("null")) {
                    String formattedDate = formatDate(String.valueOf(document.publication_date));
                    tvDocumentDate.setText("Дата публикации: " + formattedDate);
                    tvDocumentDate.setVisibility(View.VISIBLE);
                } else {
                    tvDocumentDate.setVisibility(View.GONE);
                }

                // Отображаем статус
                if (document.status != null &&
                        !document.status.isEmpty() &&
                        !document.status.equals("null")) {
                    tvDocumentStatus.setText("Статус: " + document.status);
                    tvDocumentStatus.setVisibility(View.VISIBLE);
                } else {
                    tvDocumentStatus.setVisibility(View.GONE);
                }
            }
        });
    }

    private String formatDate(String dateString) {
        try {
            // Пробуем разные форматы даты
            SimpleDateFormat inputFormat;

            if (dateString.contains("T")) {
                // Формат с временем: "2024-01-01T00:00:00"
                inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            } else {
                // Простой формат даты: "2024-01-01"
                inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            }

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateString; // Возвращаем оригинальную строку при ошибке
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

    private void loadDocumentSections(int docId) {
        apiService.getDocumentSections(docId, new APIService.SectionsListener() {
            @Override
            public void onSuccess(List<Section> sections) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sectionList.clear();
                        sectionList.addAll(sections);
                        sectionAdapter.notifyDataSetChanged();

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