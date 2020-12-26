package com.example.assignment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView Recycler_View;
    private fetchdataAdapter Adapter;
    private ArrayList<fetchdata> Fetch_data;

    RequestQueue requestQueue;

    String URL = "https://developers.zomato.com/api/v2.1/search?cuisines=Indian";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Method to bind the views with their respective ID's
        Recycler_View = findViewById(R.id.recyclerView);
        Recycler_View.setHasFixedSize(true);
        Recycler_View.setLayoutManager(new LinearLayoutManager(this));
        Fetch_data = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Method to fetch all the hotels
        searchRestaurants();
    }


    public void searchRestaurants() {
        //code to show the loading icon when data is being retrieved
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("restaurants");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject res = jsonArray.getJSONObject(i);
                        JSONObject res1 = res.getJSONObject("restaurant");
                        JSONObject res2 = res1.getJSONObject("R");
                        String temp = String.valueOf(res2.get("res_id"));
                        String restaurantNames = String.valueOf(res1.get("name"));
                        String cuisinesNames = String.valueOf(res1.get("cuisines"));

                        fetchdata fetchdata = new fetchdata(
                                restaurantNames + "",
                                cuisinesNames + ""
                        );
                        Fetch_data.add(fetchdata);
                    }
                    Adapter = new fetchdataAdapter(Fetch_data, MainActivity.this);
                    Recycler_View.setAdapter(Adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myApp", "Error is" + error);
            }
        }) {

            //This code is to send headers with API
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", "d4061a9dccbd1748ab1247595363067d");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}
