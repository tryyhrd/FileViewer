package com.example.fileviewer.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Adapters.CategoryAdapter;
import com.example.fileviewer.Common.APIService;
import com.example.fileviewer.Models.Category;
import com.example.fileviewer.Models.Document;
import com.example.fileviewer.Models.DocumentCategory;
import com.example.fileviewer.Models.Level;
import com.example.fileviewer.Models.Section;
import com.example.fileviewer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<Level> levelList = new ArrayList<>();
    private APIService apiService;
    private Spinner levelSpinner;
    private android.widget.EditText searchEditText;
    private int selectedLevelId = -1;
    private String currentSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        setupLevelSpinner();
        setupSearch();
        setupRecyclerView();
        loadLevelsWithCategories();
    }

    private void initComponents() {
        apiService = new APIService(this);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        levelSpinner = findViewById(R.id.levelSpinner);
        searchEditText = findViewById(R.id.searchEditText);
    }

    private void loadLevelsWithCategories() {
        apiService.getAllLevelsWithCategories(new APIService.LevelsWithCategoriesListener() {
            @Override
            public void onSuccess(List<Level> levels) {
                runOnUiThread(() -> {
                    levelList.clear();
                    levelList.addAll(levels);
                    setupLevelSpinnerData();
                    loadAllDocumentsForFiltering();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                    setupLevelSpinnerData();
                    loadAllDocumentsForFiltering();
                });
            }
        });
    }

    private void loadAllDocumentsForFiltering() {
        apiService.getAllDocuments(new APIService.DocumentsListener() {
            @Override
            public void onSuccess(List<Document> documents) {
                runOnUiThread(() -> {
                    List<Document> documentsWithSections = filterDocumentsWithSections(documents);
                    updateCategoryDocumentCounts(documentsWithSections);
                    applyFilters();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    applyFilters();
                });
            }
        });
    }

    private List<Document> filterDocumentsWithSections(List<Document> documents) {
        List<Document> filtered = new ArrayList<>();
        if (documents != null) {
            for (Document document : documents) {
                if (!document.sections.isEmpty()) {
                    filtered.add(document);
                }
            }
        }
        return filtered;
    }

    private void updateCategoryDocumentCounts(List<Document> documentsWithSections) {
        Map<Integer, Integer> categoryDocCount = new HashMap<>();

        for (Document doc : documentsWithSections) {
            if (doc.document_categories != null && !doc.document_categories.isEmpty()) {
                for (DocumentCategory docCat : doc.document_categories) {
                    int categoryId = docCat.category_id;
                    categoryDocCount.put(categoryId, categoryDocCount.getOrDefault(categoryId, 0) + 1);
                }
            }
        }

        for (Level level : levelList) {
            if (level.categories != null) {
                for (Category category : level.categories) {
                    int actualCount = categoryDocCount.getOrDefault(category.id, 0);
                    category.countDocumentCategory = actualCount;
                    }
            }
        }
    }

    private void setupLevelSpinnerData() {
        List<String> levelNames = new ArrayList<>();
        levelNames.add("Все уровни");

        for (Level level : levelList) {
            levelNames.add(level.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, levelNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(adapter);
    }

    private void setupLevelSpinner() {
        List<String> tempLevels = new ArrayList<>();
        tempLevels.add("Все уровни");
        ArrayAdapter<String> tempAdapter = new ArrayAdapter<>(this,
                R.layout.item_spinner, tempLevels);
        tempAdapter.setDropDownViewResource(R.layout.item_spinner_drop);
        levelSpinner.setAdapter(tempAdapter);

        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedLevelId = -1;
                } else {
                    Level selectedLevel = levelList.get(position - 1);
                    selectedLevelId = selectedLevel.id;
                }
                applyFilters();

                if (view != null) {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLevelId = -1;
                applyFilters();
            }
        });
    }

    private void applyFilters() {
        List<Category> filteredCategories = new ArrayList<>();

        if (selectedLevelId == -1) {
            Map<String, Category> uniqueCategories = new HashMap<>();

            for (Level level : levelList) {
                if (level.categories != null && !level.categories.isEmpty()) {
                    for (Category category : level.categories) {
                        if (category.countDocumentCategory > 0) {
                            if (!uniqueCategories.containsKey(category.name)) {
                                Category allLevelsCategory = new Category();
                                allLevelsCategory.id = -1;
                                allLevelsCategory.name = category.name;
                                allLevelsCategory.level_id = -1;
                                allLevelsCategory.countDocumentCategory = calculateTotalDocumentsForCategory(category.name);
                                uniqueCategories.put(category.name, allLevelsCategory);
                            }
                        }
                    }
                }
            }
            filteredCategories.addAll(uniqueCategories.values());

        } else {
            for (Level level : levelList) {
                if (level.id == selectedLevelId) {
                    if (level.categories != null && !level.categories.isEmpty()) {
                        for (Category category : level.categories) {
                            if (category.countDocumentCategory > 0) {
                                filteredCategories.add(category);
                            }
                        }
                    }
                    break;
                }
            }
        }

        if (!currentSearchQuery.isEmpty()) {
            String query = currentSearchQuery.toLowerCase();
            List<Category> searchFiltered = new ArrayList<>();
            for (Category category : filteredCategories) {
                if (category.name.toLowerCase().contains(query)) {
                    searchFiltered.add(category);
                }
            }
            filteredCategories = searchFiltered;
        }

        updateCategoryList(filteredCategories);
    }

    private int calculateTotalDocumentsForCategory(String categoryName) {
        int totalCount = 0;
        for (Level level : levelList) {
            if (level.categories != null) {
                for (Category category : level.categories) {
                    if (category.name.equals(categoryName)) {
                        totalCount += category.countDocumentCategory;
                    }
                }
            }
        }
        return totalCount;
    }

    private void updateCategoryList(List<Category> categories) {
        categoryList.clear();
        categoryList.addAll(categories);
        categoryAdapter.updateData(categoryList);
    }

    private void setupSearch() {
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            performSearch();
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
                applyFilters();
            }
        });
    }

    private void performSearch() {
        currentSearchQuery = searchEditText.getText().toString().trim();
        applyFilters();
        android.view.inputmethod.InputMethodManager imm =
                (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerViewCategories.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(categoryList, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                openCategoryDocuments(category);
            }
        });
        recyclerViewCategories.setAdapter(categoryAdapter);
    }

    private void openCategoryDocuments(Category category) {
        Intent intent = new Intent(MainActivity.this, CategoryDocumentsActivity.class);

        intent.putExtra("category_name", category.name);
        intent.putExtra("category_id", category.id);
        intent.putExtra("selected_level_id", selectedLevelId);

        intent.putExtra("all_levels", new ArrayList<>(levelList));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}