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
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class APIService {
    private static final String TAG = "ApiService";
    private static final String BASE_URL = "https://your-domain.com/api/";
    private RequestQueue requestQueue;
    private Gson gson;

    public APIService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        gson = new Gson();
    }

    public interface GostListListener {
        void onSuccess(List<GOST> gosts);
        void onError(String error);
    }

    public void getAllGosts(final GostListListener listener) {
        String url = BASE_URL + "gosts.php";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<GOST> gostList = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                GOST gost = gson.fromJson(jsonObject.toString(), GOST.class);
                                gostList.add(gost);
                            }

                            listener.onSuccess(gostList);

                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            listener.onError("Ошибка парсинга данных");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        listener.onError("Ошибка загрузки данных: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(request);
    }

    public void searchGosts(String query, final GostListListener listener) {
        String url = BASE_URL + "search_gosts.php?q=" + query;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<GOST> gostList = gson.fromJson(response.toString(),
                                    new TypeToken<List<GOST>>(){}.getType());
                            listener.onSuccess(gostList);
                        } catch (Exception e) {
                            listener.onError("Ошибка парсинга данных");
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
