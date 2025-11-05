package com.example.fileviewer.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Adapters.CategoryAdapter;
import com.example.fileviewer.Common.APIService;
import com.example.fileviewer.Models.Category;
import com.example.fileviewer.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        setupRecyclerView();
        loadCategoriesFromAPI();
    }
    private void initComponents() {
        apiService = new APIService(this);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
    }
    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });

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
        intent.putExtra("category_id", category.id);
        intent.putExtra("category_name", category.name);
        startActivity(intent);
    }

    private List<Category> getUniqueCategories(List<Category> categories) {
        List<Category> uniqueCategories = new ArrayList<>();
        Set<String> categoryNames = new HashSet<>();

        for (Category category : categories) {
            if (!categoryNames.contains(category.name)) {
                categoryNames.add(category.name);
                uniqueCategories.add(category);
            }
        }
        return uniqueCategories;
    }
    private void loadCategoriesFromAPI() {
        apiService.getAllCategories(new APIService.CategoriesListener() {
            @Override
            public void onSuccess(List<Category> categories) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Category> uniqueCategories = getUniqueCategories(categories);
                        categoryList.clear();
                        categoryList.addAll(uniqueCategories);

                        categoryAdapter.updateData(uniqueCategories);

                        Toast.makeText(MainActivity.this,
                                "Загружено категорий: " + uniqueCategories.size(),
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
        super.onDestroy();
    }
}