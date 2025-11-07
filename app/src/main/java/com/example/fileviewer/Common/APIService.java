package com.example.fileviewer.Common;

import android.content.Context;
import android.text.Html;
import android.util.Log;
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
    private static final String TAG = "ApiService";
    public static final String BASE_URL = "http://10.0.2.2:5068";
    private RequestQueue requestQueue;
    private Gson gson;

    public APIService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
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

    public static String getPdfUrl(){
        return BASE_URL + "/api/documents/";
    }


    public void getDocumentById(int documentId, final DocumentListener listener) {
        String url = BASE_URL + "/api/Documents/ReadId?id=" + documentId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Document by ID response received");
                        try {
                            Document document = gson.fromJson(response.toString(), Document.class);
                            listener.onSuccess(document);
                        } catch (Exception e) {
                            listener.onError("Ошибка парсинга документа: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg = "Network error";
                        if (error.networkResponse != null) {
                            errorMsg = "HTTP " + error.networkResponse.statusCode;
                        }
                        Log.e(TAG, "Volley error: " + errorMsg);
                        listener.onError("Ошибка загрузки документа: " + errorMsg);
                    }
                }
        );
        requestQueue.add(request);
    }

    public void getAllDocuments(final DocumentsListener listener) {
        String url = BASE_URL + "/api/Documents/Read";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Type listType = new TypeToken<List<Document>>(){}.getType();
                            List<Document> documents = gson.fromJson(response.toString(), listType);
                            listener.onSuccess(documents);
                        } catch (Exception e) {
                            listener.onError("Ошибка парсинга документов: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg = "Network error";
                        if (error.networkResponse != null) {
                            errorMsg = "HTTP " + error.networkResponse.statusCode;
                        }
                        listener.onError("Ошибка загрузки документов: " + errorMsg);
                    }
                }
        );
        requestQueue.add(request);
    }

    public void getAllCategories(final CategoriesListener listener) {
        String url = BASE_URL + "/api/Categories/Read";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Type listType = new TypeToken<List<Category>>(){}.getType();
                            List<Category> categories = gson.fromJson(response.toString(), listType);
                            listener.onSuccess(categories);
                        } catch (Exception e) {
                            listener.onError("Ошибка парсинга категорий: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg = "Network error";
                        if (error.networkResponse != null) {
                            errorMsg = "HTTP " + error.networkResponse.statusCode;
                        }
                        Log.e(TAG, "Volley error: " + errorMsg);
                        listener.onError("Ошибка загрузки категорий: " + errorMsg);
                    }
                }
        );
        requestQueue.add(request);
    }

    public void getDocumentsByCategory(int categoryId, final DocumentsListener listener) {
        String url = BASE_URL + "/api/Documents/ByCategory/" + categoryId;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Documents by category response received");
                        try {
                            Type listType = new TypeToken<List<Document>>(){}.getType();
                            List<Document> documents = gson.fromJson(response.toString(), listType);
                            listener.onSuccess(documents);
                        } catch (Exception e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            listener.onError("Ошибка парсинга документов категории: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg = "Network error";
                        if (error.networkResponse != null) {
                            errorMsg = "HTTP " + error.networkResponse.statusCode;
                        }
                        Log.e(TAG, "Volley error: " + errorMsg);
                        listener.onError("Ошибка загрузки документов категории: " + errorMsg);
                    }
                }
        );
        requestQueue.add(request);
    }

    public void getDocumentSections(int documentId, SectionsListener listener) {
        String url = BASE_URL + "/api/Documents/ReadId?id=" + documentId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Section> sections = parseSectionsFromDocument(response);
                            listener.onSuccess(sections);
                        } catch (Exception e) {
                            listener.onError("Ошибка парсинга секций: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Ошибка сети: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(request);
    }

    private List<Section> parseSectionsFromDocument(JSONObject json) throws JSONException {
        List<Section> sections = new ArrayList<>();

        Log.d("APIService", "Parsing sections from document response");

        if (json.has("sections") && !json.isNull("sections")) {
            JSONArray sectionsArray = json.getJSONArray("sections");
            Log.d("APIService", "Found " + sectionsArray.length() + " sections");

            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject sectionJson = sectionsArray.getJSONObject(i);
                Section section = new Section();

                if (sectionJson.has("id")) {
                    section.id = sectionJson.getInt("id");
                }
                if (sectionJson.has("title")) {
                    section.title = sectionJson.getString("title");
                } else {
                    section.title = "";
                }
                if (sectionJson.has("content")) {
                    section.content = sectionJson.getString("content");
                    Log.d("APIService", "Section content: " + section.content);
                } else {
                    section.content = "";
                }
                if (sectionJson.has("order_index")) {
                    section.order = sectionJson.getInt("order_index");
                } else {
                    section.order = i;
                }
                if (sectionJson.has("document_id")) {
                    section.document_id = sectionJson.getInt("document_id");
                }

                sections.add(section);
            }
        } else {
            Log.d("APIService", "No sections found in document");
        }

        return sections;
    }

    public interface SectionsListener {
        void onSuccess(List<Section> sections);
        void onError(String error);
    }

    public interface PdfUrlListener {
        void onSuccess(String pdfUrl);
        void onError(String error);
    }

    public void searchDocuments(String query, final DocumentsListener listener) {
        String url = BASE_URL + "documents/search?query=" + query;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Type listType = new TypeToken<List<Document>>(){}.getType();
                            List<Document> documents = gson.fromJson(response.toString(), listType);
                            listener.onSuccess(documents);
                        } catch (Exception e) {
                            listener.onError("Ошибка парсинга данных поиска");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Ошибка поиска: " + error.getMessage());
                    }
                }
        );
        requestQueue.add(request);
    }
}