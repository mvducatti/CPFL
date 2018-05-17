package com.example.marcos.cpfl;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.marcos.cpfl.Adapter.FaturaAdapter;
import com.example.marcos.cpfl.Connection.Constants;
import com.example.marcos.cpfl.Models.Faturas;
import com.example.marcos.cpfl.Models.User;
import com.example.marcos.cpfl.SharedPreferences.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Usuario extends AppCompatActivity {

    ArrayList<Faturas> faturaArrayList;
    private LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        recyclerView = findViewById(R.id.recylerView);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        //Invert the view of the list
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        faturaArrayList = new ArrayList<>();

        loadFaturas();
    }

    private void loadFaturas() {

        final User user = sharedPrefManager.getInstance(this).getUser();
        int id = user.getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constants.URL_GET_FATURAS + "?user_id=" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting the string to json array object
                            JSONArray jsonArray = response.getJSONArray("faturas");

                            //traversing through all the object
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting product object from json array
                                JSONObject jsnews = jsonArray.getJSONObject(i);
                                //adding the product to product list
                                try {

                                    int month = jsnews.getInt("month");
                                    int year = jsnews.getInt("year");
                                    float consume = Float.parseFloat(jsnews.getString("consume"));

                                    faturaArrayList.add(new Faturas(month, year, consume));

                                } catch (JSONException e) {
                                    Snackbar.make(findViewById(android.R.id.content),"Erro: " + e,Snackbar.LENGTH_LONG).show();
                                }
                            }

                            //creating adapter object and setting it to recyclerview
                            FaturaAdapter adapter = new FaturaAdapter(Usuario.this, faturaArrayList);
                            recyclerView.setAdapter(adapter);

                            /* ------ SETTING OUR ADAPTER TO OUR ONCLICKLISTERNER ---------*/
//                            adapter.setOnClickListener(Usuario.this);

                        } catch (JSONException e) {
                            Snackbar.make(findViewById(android.R.id.content),"Parou aqui: " + e,Snackbar.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(findViewById(android.R.id.content),"Ainda não existem faturas na sua conta",Snackbar.LENGTH_LONG).show();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(request);
    }

    public void logout(View view){
        SharedPrefManager.getInstance(this).logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
