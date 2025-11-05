package com.example.fileviewer;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Adapters.DocumentAdapter;
import com.example.fileviewer.Common.APIService;
import com.example.fileviewer.Models.Document;

import java.util.ArrayList;
import java.util.List;

public class CategoryDocumentsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDocuments;
    private DocumentAdapter documentAdapter;
    private List<Document> documentList = new ArrayList<>();
    private APIService apiService;
    private TextView tvHeader;
    private int categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_page);

        categoryId = getIntent().getIntExtra("category_id", -1);
        categoryName = getIntent().getStringExtra("category_name");

        initComponents();
        setupUI(categoryName);
        setupRecyclerView();
        loadDocumentsByCategory();
    }

    private void initComponents() {
        apiService = new APIService(this);
        recyclerViewDocuments = findViewById(R.id.gostEducation);
        tvHeader = findViewById(R.id.header);
    }

    private void setupUI(String categoryName) {
        tvHeader.setText(categoryName);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewDocuments.setLayoutManager(layoutManager);

        documentAdapter = new DocumentAdapter(documentList, new DocumentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Document document) {
                Toast.makeText(CategoryDocumentsActivity.this,
                        "Открыт документ: " + document.title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(Document document) {
            }
        });
        recyclerViewDocuments.setAdapter(documentAdapter);
    }

    private void loadDocumentsByCategory() {

        if (categoryId == -1) {
            Toast.makeText(this, "Ошибка: категория не выбрана", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getDocumentsByCategory(categoryId, new APIService.DocumentsListener() {
            @Override
            public void onSuccess(List<Document> documents) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        documentAdapter.updateData(documents);
                        Toast.makeText(CategoryDocumentsActivity.this,
                                "Найдено документов: " + documents.size(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CategoryDocumentsActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
