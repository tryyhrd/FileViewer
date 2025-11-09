package com.example.fileviewer.Common;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fileviewer.Models.Category;
import com.example.fileviewer.Models.Document;
import com.example.fileviewer.Models.Level;
import com.example.fileviewer.Models.Section;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class APIService {
    public static final String BASE_URL = "http://192.168.0.104:5068";
    private RequestQueue requestQueue;
    private Gson gson;

    public APIService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    }

    public interface DocumentsListener {
        void onSuccess(List<Document> documents);
        void onError(String error);
    }

    public interface CategoriesListener {
        void onSuccess(List<Category> categories);
        void onError(String error);
    }

    public interface DocumentListener {
        void onSuccess(Document document);
        void onError(String error);
    }

    public interface LevelsListener {
        void onSuccess(List<Level> levels);
        void onError(String error);
    }

    public interface LevelsWithCategoriesListener {
        void onSuccess(List<Level> levelsWithCategories);
        void onError(String error);
    }

    public interface SectionsListener {
        void onSuccess(List<Section> sections);
        void onError(String error);
    }

    public void getAllLevelsWithCategories(final LevelsWithCategoriesListener listener) {
        String url = BASE_URL + "/api/DocumentCategories/Read";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Level>>(){}.getType();
                        List<Level> levelsWithCategories = gson.fromJson(response.toString(), listType);
                        listener.onSuccess(levelsWithCategories);
                    } catch (Exception e) {
                        listener.onError("Ошибка парсинга уровней с категориями");
                    }
                },
                error -> listener.onError(getErrorMessage(error))
        );
        requestQueue.add(request);
    }

    public void getAllLevels(final LevelsListener listener) {
        String url = BASE_URL + "/api/Levels/Read";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Level>>(){}.getType();
                        List<Level> levels = gson.fromJson(response.toString(), listType);
                        listener.onSuccess(levels);
                    } catch (Exception e) {
                        listener.onError("Ошибка парсинга уровней");
                    }
                },
                error -> listener.onError(getErrorMessage(error))
        );
        requestQueue.add(request);
    }

    public static String getPdfUrl(){
        return BASE_URL + "/api/documents/";
    }

    public void getDocumentById(int documentId, final DocumentListener listener) {
        String url = BASE_URL + "/api/Documents/ReadId?id=" + documentId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        Document document = gson.fromJson(response.toString(), Document.class);
                        listener.onSuccess(document);
                    } catch (Exception e) {
                        listener.onError("Ошибка парсинга документа");
                    }
                },
                error -> listener.onError(getErrorMessage(error))
        );
        requestQueue.add(request);
    }

    public void getAllDocuments(final DocumentsListener listener) {
        String url = BASE_URL + "/api/Documents/Read";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Document>>(){}.getType();
                        List<Document> documents = gson.fromJson(response.toString(), listType);
                        listener.onSuccess(documents);
                    } catch (Exception e) {
                        listener.onError("Ошибка парсинга документов");
                    }
                },
                error -> listener.onError(getErrorMessage(error))
        );
        requestQueue.add(request);
    }

    public void getAllCategories(final CategoriesListener listener) {
        String url = BASE_URL + "/api/Categories/Read";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Category>>(){}.getType();
                        List<Category> categories = gson.fromJson(response.toString(), listType);
                        listener.onSuccess(categories);
                    } catch (Exception e) {
                        listener.onError("Ошибка парсинга категорий");
                    }
                },
                error -> listener.onError(getErrorMessage(error))
        );
        requestQueue.add(request);
    }

    public void getDocumentsByCategory(int categoryId, final DocumentsListener listener) {
        String url = BASE_URL + "/api/Documents/ByCategory/" + categoryId;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Document>>(){}.getType();
                        List<Document> documents = gson.fromJson(response.toString(), listType);
                        listener.onSuccess(documents);
                    } catch (Exception e) {
                        listener.onError("Ошибка парсинга документов категории");
                    }
                },
                error -> listener.onError("Ошибка загрузки документов категории: " + getErrorMessage(error))
        );
        requestQueue.add(request);
    }

    public void getDocumentsByCategoryWithRetry(int categoryId, final DocumentsListener listener, int maxRetries) {
        getDocumentsByCategoryWithRetry(categoryId, listener, maxRetries, 0);
    }

    private void getDocumentsByCategoryWithRetry(int categoryId, final DocumentsListener listener, int maxRetries, int currentRetry) {
        getDocumentsByCategory(categoryId, new DocumentsListener() {
            @Override
            public void onSuccess(List<Document> documents) {
                listener.onSuccess(documents);
            }

            @Override
            public void onError(String error) {
                if (currentRetry < maxRetries) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    getDocumentsByCategoryWithRetry(categoryId, listener, maxRetries, currentRetry + 1);
                } else {
                    listener.onError(error + " (после " + maxRetries + " попыток)");
                }
            }
        });
    }

    public void getDocumentSections(int documentId, final SectionsListener listener) {
        String url = BASE_URL + "/api/Documents/ReadId?id=" + documentId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        List<Section> sections = parseSectionsFromDocument(response);
                        listener.onSuccess(sections);
                    } catch (Exception e) {
                        listener.onError("Ошибка парсинга разделов");
                    }
                },
                error -> listener.onError(getErrorMessage(error))
        );
        requestQueue.add(request);
    }

    private List<Section> parseSectionsFromDocument(JSONObject json) throws JSONException {
        List<Section> sections = new ArrayList<>();

        if (json.has("sections") && !json.isNull("sections")) {
            JSONArray sectionsArray = json.getJSONArray("sections");

            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject sectionJson = sectionsArray.getJSONObject(i);
                Section section = new Section();

                if (sectionJson.has("id")) section.id = sectionJson.getInt("id");
                section.title = sectionJson.optString("title", "");
                section.content = sectionJson.optString("content", "");
                section.order = sectionJson.optInt("order_index", i);
                if (sectionJson.has("document_id")) section.document_id = sectionJson.getInt("document_id");

                sections.add(section);
            }
        }

        return sections;
    }

    private String getErrorMessage(VolleyError error) {
        if (error.networkResponse != null) {
            return "HTTP " + error.networkResponse.statusCode;
        }
        return "Сетевая ошибка";
    }
}