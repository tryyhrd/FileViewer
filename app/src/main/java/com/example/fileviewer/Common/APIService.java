package com.example.fileviewer.Common;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fileviewer.Models.Category;
import com.example.fileviewer.Models.Document;
import com.example.fileviewer.Models.Level;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

public class APIService {
    private static final String TAG = "ApiService";
    private static final String BASE_URL = "http://10.0.2.2:5068/";
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

    public interface LevelsListener {
        void onSuccess(List<Level> levels);
        void onError(String error);
    }

    public void getAllDocuments(final DocumentsListener listener) {
        String url = BASE_URL + "api/Documents/Read";

        Log.d(TAG, "Requesting documents from: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Documents response received, length: " + response.length());
                        try {
                            Type listType = new TypeToken<List<Document>>(){}.getType();
                            List<Document> documents = gson.fromJson(response.toString(), listType);
                            listener.onSuccess(documents);
                        } catch (Exception e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
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
                        Log.e(TAG, "Volley error: " + errorMsg);
                        listener.onError("Ошибка загрузки документов: " + errorMsg);
                    }
                }
        );
        requestQueue.add(request);
    }

    public void getAllCategories(final CategoriesListener listener) {
        String url = BASE_URL + "api/Categories/Read";

        Log.d(TAG, "Requesting categories from: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Categories response received, length: " + response.length());
                        try {
                            Type listType = new TypeToken<List<Category>>(){}.getType();
                            List<Category> categories = gson.fromJson(response.toString(), listType);
                            listener.onSuccess(categories);
                        } catch (Exception e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
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

    public void getAllLevels(final LevelsListener listener) {
        String url = BASE_URL + "levels";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Type listType = new TypeToken<List<Level>>(){}.getType();
                            List<Level> levels = gson.fromJson(response.toString(), listType);
                            listener.onSuccess(levels);
                        } catch (Exception e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            listener.onError("Ошибка парсинга уровней");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        listener.onError("Ошибка загрузки уровней: " + error.getMessage());
                    }
                }
        );
        requestQueue.add(request);
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
