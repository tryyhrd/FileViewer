package com.example.fileviewer;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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
    private static final String BASE_URL = "https://localhost:7214/";
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
        String url = BASE_URL + "documents";

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
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            listener.onError("Ошибка парсинга документов");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        listener.onError("Ошибка загрузки документов: " + error.getMessage());
                    }
                }
        );
        requestQueue.add(request);
    }

    public void getAllCategories(final CategoriesListener listener) {
        String url = BASE_URL + "categories";

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
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            listener.onError("Ошибка парсинга категорий");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        listener.onError("Ошибка загрузки категорий: " + error.getMessage());
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
