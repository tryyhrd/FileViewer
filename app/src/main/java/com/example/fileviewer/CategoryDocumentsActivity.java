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
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_page);

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
        apiService.getAllDocuments(new APIService.DocumentsListener() {
            @Override
            public void onSuccess(List<Document> documents) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Document> filteredDocuments = new ArrayList<>();
                        for (Document doc : documents) {
                            if (doc.title != null && doc.title.equals(categoryName)) {
                                filteredDocuments.add(doc);
                            }
                        }

                        documentAdapter.updateData(filteredDocuments);
                        Toast.makeText(CategoryDocumentsActivity.this,
                                "Найдено документов: " + filteredDocuments.size(),
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
