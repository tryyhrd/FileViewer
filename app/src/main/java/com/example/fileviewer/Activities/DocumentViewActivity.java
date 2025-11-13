package com.example.fileviewer.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DocumentViewActivity extends AppCompatActivity {

    private TextView tvDocumentTitle;
    private TextView tvDocumentDate;
    private TextView tvDocumentStatus;
    private RecyclerView recyclerViewSections;
    private ProgressBar progressBar;

    private SectionAdapter sectionAdapter;
    private APIService apiService;
    private List<Section> sectionList = new ArrayList<>();
    private int documentId;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        documentId = getIntent().getIntExtra("document_id", -1);

        if (documentId == -1) {
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
        progressBar = findViewById(R.id.progressBar);

        showLoading(true);
    }

    private void loadDocumentData(int docId) {
        if (isLoading) return;

        isLoading = true;
        showLoading(true);

        apiService.getDocumentById(docId, new APIService.DocumentListener() {
            @Override
            public void onSuccess(Document document) {
                displayDocument(document);
                loadDocumentSections(docId);
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLoading(false);
                        isLoading = false;
                        finish();
                    }
                });
            }
        });
    }

    private void displayDocument(Document document) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (document.title != null && !document.title.isEmpty()) {
                    tvDocumentTitle.setText(document.title);
                }
                if (document.publication_date != null &&
                        !document.publication_date.equals("null")) {
                    String formattedDate = formatDate(String.valueOf(document.publication_date));
                    tvDocumentDate.setText("Дата публикации: " + formattedDate);
                    tvDocumentDate.setVisibility(View.VISIBLE);
                } else {
                    tvDocumentDate.setVisibility(View.GONE);
                }

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
        if (dateString == null || dateString.isEmpty() || dateString.equals("null")) {
            return "не указана";
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

            Date date = inputFormat.parse(dateString);
            String result = outputFormat.format(date);

            Log.d("DateDebug", "Formatted date: " + result);
            return result;

        } catch (Exception e) {
            try {
                String[] parts = dateString.split(" ");
                if (parts.length >= 6) {
                    String day = parts[2];
                    String month = convertMonth(parts[1]);
                    String year = parts[5];
                    return day + "." + month + "." + year;
                }
            } catch (Exception ex) {}

            return dateString;
        }
    }
    private String convertMonth(String monthName) {
        switch (monthName.toLowerCase()) {
            case "jan": return "01";
            case "feb": return "02";
            case "mar": return "03";
            case "apr": return "04";
            case "may": return "05";
            case "jun": return "06";
            case "jul": return "07";
            case "aug": return "08";
            case "sep": return "09";
            case "oct": return "10";
            case "nov": return "11";
            case "dec": return "12";
            default: return "01";
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

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showLoading(false);
                                isLoading = false;
                                updateUIAfterLoading();
                            }
                        }, 300);
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLoading(false);
                        isLoading = false;
                        updateUIAfterLoading();
                    }
                });
            }
        });
    }

    private void updateUIAfterLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sectionList.isEmpty()) {
                    recyclerViewSections.setVisibility(View.GONE);
                } else {
                    recyclerViewSections.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showLoading(boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressBar != null) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
                if (recyclerViewSections != null) {
                    recyclerViewSections.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        isLoading = false;
    }
}