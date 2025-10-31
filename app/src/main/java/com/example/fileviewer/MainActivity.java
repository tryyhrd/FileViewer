package com.example.fileviewer;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Adapters.DocumentAdapter;
import com.example.fileviewer.Common.APIService;
import com.example.fileviewer.Models.Document;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Объявление переменных класса
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerViewDocuments;
    private DocumentAdapter documentAdapter;
    private List<Document> documentList = new ArrayList<>();
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_page);

        initComponents();
        setupRecyclerView();
        loadDocumentsFromAPI();
    }
    private void initComponents() {
        dbHelper = new DatabaseHelper(this);
        apiService = new APIService(this);
        recyclerViewDocuments = findViewById(R.id.gostEducation);
    }
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewDocuments.setLayoutManager(layoutManager);

        documentAdapter = new DocumentAdapter(documentList, new DocumentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Document document) {
            }

            @Override
            public void onItemLongClick(Document document) {
            }
        });
        recyclerViewDocuments.setAdapter(documentAdapter);
    }
    private void loadDocumentsFromAPI() {

        apiService.getAllDocuments(new APIService.DocumentsListener() {
            @Override
            public void onSuccess(List<Document> documents) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        documentAdapter.updateData(documents);
                        Toast.makeText(MainActivity.this,
                                "Загружено документов: " + documents.size(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}