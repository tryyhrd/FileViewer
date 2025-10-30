package com.example.fileviewer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerViewGosts;
    private GOSTAdapter gostAdapter;
    private List<GOST> gostList = new ArrayList<GOST>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_page);

        dbHelper = new DatabaseHelper(this);
        recyclerViewGosts = (RecyclerView) findViewById(R.id.gostEducation);

        setupRecyclerView();
        loadGostsFromDatabase();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewGosts.setLayoutManager(layoutManager);

        gostAdapter = new GOSTAdapter(gostList, new GOSTAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GOST gost) {
                showShortGostInfo(gost);
            }
            @Override
            public void onItemLongClick(GOST gost) {
                showDetailedGostInfo(gost);
            }
        });

        recyclerViewGosts.setAdapter(gostAdapter);
    }

    private void loadGostsFromDatabase() {
        List<GOST> loadedGosts = dbHelper.getAllGosts();
        gostAdapter.updateData(loadedGosts);

        if (loadedGosts.isEmpty()) {
            Toast.makeText(this, "ГОСТы не найдены", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Загружено ГОСТов: " + loadedGosts.size(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showShortGostInfo(GOST gost) {
        String info = gost.code + "\n" + gost.title;
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();
    }

    private void showDetailedGostInfo(GOST gost) {
        String fullInfo = "Номер: " + gost.number + "\n\n" +
                "Код ГОСТа: " + gost.code + "\n\n" +
                "Название: " + gost.title + "\n\n" +
                "Описание: " + gost.description;

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Подробная информация о ГОСТе");
        builder.setMessage(fullInfo);
        builder.setPositiveButton("Закрыть", null);

        builder.setNeutralButton("Поделиться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareGostInfo(gost);
            }
        });

        builder.show();
    }

    private void shareGostInfo(GOST gost) {
        String shareText = gost.number + ". " + gost.code + "\n" +
                gost.title + "\n" +
                gost.description;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Информация о ГОСТе");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Поделиться информацией о ГОСТе"));
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}