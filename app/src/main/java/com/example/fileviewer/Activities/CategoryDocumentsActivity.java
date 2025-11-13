package com.example.fileviewer.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Adapters.DocumentAdapter;
import com.example.fileviewer.Common.APIService;
import com.example.fileviewer.Models.Category;
import com.example.fileviewer.Models.Document;
import com.example.fileviewer.Models.Level;
import com.example.fileviewer.Models.Section;
import com.example.fileviewer.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryDocumentsActivity extends AppCompatActivity {

    private List<Document> documentList = new ArrayList<>();
    private List<Document> allDocuments = new ArrayList<>();
    private List<Category> allCategories = new ArrayList<>();
    private List<Level> allLevels = new ArrayList<>();
    private DocumentAdapter documentAdapter;
    private APIService apiService;
    private TextView tvHeader;
    private android.widget.EditText searchEditText;
    private ImageView categoryImage;
    private int categoryId;
    private String categoryName;
    private String currentSearchQuery = "";
    private boolean isLoading = false;

    private RecyclerView recyclerViewDocuments;
    private ProgressBar progressBar;
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryId = getIntent().getIntExtra("category_id", -1);
        categoryName = getIntent().getStringExtra("category_name");

        allLevels = (List<Level>) getIntent().getSerializableExtra("all_levels");
        if (allLevels == null) {
            allLevels = new ArrayList<>();
        }

        initComponents();
        setupUI(categoryName);
        setupSearch();
        setupRecyclerView();
        loadAllData();
        setupBackButton();
    }

    private void initComponents() {
        apiService = new APIService(this);
        recyclerViewDocuments = findViewById(R.id.gostEducation);
        tvHeader = findViewById(R.id.header);
        categoryImage = findViewById(R.id.categoryImage);
        searchEditText = findViewById(R.id.searchEditText);
        progressBar = findViewById(R.id.progressBar);

        ShowLoading(true);
    }

    private void setupUI(String categoryName) {
        if (categoryImage != null) {
            categoryImage.setImageResource(com.example.fileviewer.Adapters.CategoryAdapter.CategoryViewHolder.getIconResource(categoryName));
        }
        if (tvHeader != null) {
            tvHeader.setText(categoryName);
        }
    }

    private void setupSearch() {
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            performDocumentSearch();
            return true;
        });

        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                currentSearchQuery = s.toString().trim();

                handler.removeCallbacks(searchRunnable);
                handler.postDelayed(searchRunnable, 500);
            }
        });
    }

    private Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            filterDocuments();
        }
    };

    private void performDocumentSearch() {
        currentSearchQuery = searchEditText.getText().toString().trim();
        filterDocuments();

        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }

    private void filterDocuments() {
        List<Document> filteredDocuments = new ArrayList<>();

        if (currentSearchQuery.isEmpty()) {
            filteredDocuments.addAll(allDocuments);
        } else {
            String query = currentSearchQuery.toLowerCase();
            for (Document document : allDocuments) {
                if (document.title != null && document.title.toLowerCase().contains(query)) {
                    filteredDocuments.add(document);
                }
            }
        }

        documentAdapter.updateData(filteredDocuments);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewDocuments.setLayoutManager(layoutManager);

        documentAdapter = new DocumentAdapter(documentList, allCategories, allLevels,
                new DocumentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Document document) {
                        if (document.sections != null && document.sections.size() > 1) {
                            openDocument(document);
                        } else if (document.sections != null && document.sections.size() == 1) {
                            Section section = document.sections.get(0);
                            if (section.title != null && section.title.equals("content")) {
                                openPdf(document);
                            } else {
                                openDocument(document);
                            }
                        } else {
                            openDocument(document);
                        }
                    }
                });
        recyclerViewDocuments.setAdapter(documentAdapter);
    }

    private void loadAllData() {
        if (isLoading) return;

        isLoading = true;
        ShowLoading(true);

        apiService.getAllCategories(new APIService.CategoriesListener() {
            @Override
            public void onSuccess(List<Category> categories) {
                runOnUiThread(() -> {
                    allCategories.clear();
                    allCategories.addAll(categories);
                    loadDocuments();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    loadDocuments();
                });
            }
        });
    }

    private void loadDocuments() {
        if (categoryId == -1) {
            loadDocumentsForAllLevels(categoryName);
        } else {
            loadDocumentsByCategoryIdDirect(categoryId);
        }
    }
    private void loadDocumentsForAllLevels(String categoryName) {
        List<Integer> allCategoryIds = new ArrayList<>();

        for (Category category : allCategories) {
            if (category.name.equals(categoryName)) {
                allCategoryIds.add(category.id);
            }
        }

        if (allCategoryIds.isEmpty()) {
            runOnUiThread(() -> {
                ShowLoading(false);
                isLoading = false;
            });
            return;
        }

        loadDocumentsForMultipleCategoriesWithErrorHandling(allCategoryIds);
    }

    private void loadDocumentsForMultipleCategoriesWithErrorHandling(List<Integer> categoryIds) {
        final List<Document> allDocumentsList = new ArrayList<>();
        final int totalCategories = categoryIds.size();
        final int[] currentIndex = {0};

        loadNextCategorySequentially(categoryIds, currentIndex, allDocumentsList, totalCategories);
    }

    private void loadNextCategorySequentially(List<Integer> categoryIds, int[] currentIndex,
                                              List<Document> allDocumentsList, int totalCategories) {
        if (currentIndex[0] >= totalCategories) {
            runOnUiThread(() -> {
                allDocuments.clear();
                allDocuments.addAll(allDocumentsList);
                filterDocuments();

                handler.postDelayed(() -> {
                    ShowLoading(false);
                    isLoading = false;
                }, 300);
            });
            return;
        }

        int categoryId = categoryIds.get(currentIndex[0]);
        apiService.getDocumentsByCategory(categoryId, new APIService.DocumentsListener() {
            @Override
            public void onSuccess(List<Document> documents) {
                allDocumentsList.addAll(documents);
                currentIndex[0]++;
                loadNextCategorySequentially(categoryIds, currentIndex, allDocumentsList, totalCategories);
            }

            @Override
            public void onError(String error) {
                currentIndex[0]++;
                loadNextCategorySequentially(categoryIds, currentIndex, allDocumentsList, totalCategories);
            }
        });
    }

    private void loadDocumentsByCategoryIdDirect(int categoryId) {
        apiService.getDocumentsByCategory(categoryId, new APIService.DocumentsListener() {
            @Override
            public void onSuccess(List<Document> documents) {
                runOnUiThread(() -> {
                    allDocuments.clear();
                    allDocuments.addAll(documents);
                    filterDocuments();

                    handler.postDelayed(() -> {
                        ShowLoading(false);
                        isLoading = false;
                    }, 300);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(CategoryDocumentsActivity.this,
                            "Ошибка загрузки документов: " + error, Toast.LENGTH_LONG).show();

                    ShowLoading(false);
                    isLoading = false;
                });
            }
        });
    }

    public void openDocument(Document document) {
        Intent intent = new Intent(CategoryDocumentsActivity.this, DocumentViewActivity.class);
        intent.putExtra("document_id", document.id);
        startActivity(intent);
    }

    public void openPdf(Document document) {
        try {
            String pdfUrl = APIService.getPdfUrl() + document.id + ".pdf";

            androidx.browser.customtabs.CustomTabsIntent customTabsIntent =
                    new androidx.browser.customtabs.CustomTabsIntent.Builder()
                            .setShowTitle(true)
                            .build();

            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.launchUrl(this, android.net.Uri.parse(pdfUrl));

        } catch (Exception e) {
            try {
                String pdfUrl = APIService.getPdfUrl() + document.id + ".pdf";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(pdfUrl));
                startActivity(browserIntent);
            } catch (Exception ex) {}
        }
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

    private void ShowLoading(boolean show) {
        runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
            if (recyclerViewDocuments != null) {
                recyclerViewDocuments.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(searchRunnable);
        isLoading = false;
    }
}