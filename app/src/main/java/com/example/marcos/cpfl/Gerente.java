package com.example.marcos.cpfl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.marcos.cpfl.Connection.Constants;
import com.example.marcos.cpfl.Connection.RequestHandler;
import com.example.marcos.cpfl.Models.User;
import com.example.marcos.cpfl.SharedPreferences.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Gerente extends AppCompatActivity {

    private Spinner spinner;
    private TextView tvBandeira, tvkWh, tvImposto;
    private String verde = "Verde", amarelo = "Amarela", vermelho = "Vermelha";
    private EditText tietKwh, tietPorcentagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerente);

//        Toolbar myToolbar = findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

        spinner = findViewById(R.id.spinnerBandeiras);

        tietKwh = findViewById(R.id.tietKwh);
        tietPorcentagem = findViewById(R.id.tietPorcentagem);

        tvBandeira = findViewById(R.id.tvBandeira);
        tvkWh = findViewById(R.id.tvkWh);
        tvImposto = findViewById(R.id.tvImposto);



        loadTaxes();
    }

    private void loadTaxes() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constants.URL_SHOW_TAXES, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting the string to json array object
                            JSONArray jsonArray = response.getJSONArray("taxas");

                                JSONObject jsnews = jsonArray.getJSONObject(0);

                                String flag_color = jsnews.getString("flag_color");
                                String kwh = jsnews.getString("kwh");
                                String percentagem = jsnews.getString("percentage");

                                if (flag_color.equals("0")){
                                    tvBandeira.setText(verde);
                                } if (flag_color.equals("1")){
                                    tvBandeira.setText(amarelo);
                                }else if (flag_color.equals("2")){
                                tvBandeira.setText(vermelho);
                            }

                                tvkWh.setText(kwh);
                                tvImposto.setText(percentagem + "%");

                        } catch (JSONException e) {
                            Snackbar.make(findViewById(android.R.id.content),"Algo deu errado: " + e,Snackbar.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(findViewById(android.R.id.content),"Algo deu errado: " + error + "",Snackbar.LENGTH_LONG).show();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(request);
    }


    public void updateFlag(View view) {


        final int bandeira = spinner.getSelectedItemPosition();
        final String kwh = tietKwh.getText().toString().trim();
        final String percentage = tietPorcentagem.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPDATE_TAXES, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")){

                        Snackbar.make(findViewById(android.R.id.content),obj.getString("message"),Snackbar.LENGTH_LONG).show();

                        tietKwh.setText("");
                        tietPorcentagem.setText("");
                        spinner.setSelection(0);

                        loadTaxes();

                    }else{
                        Snackbar.make(findViewById(android.R.id.content),obj.getString("message"),Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("flag_color", String.valueOf(bandeira));
                params.put("kwh", kwh);
                params.put("percentage", percentage);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void logout(View view){
        SharedPrefManager.getInstance(this).logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_setting:
                logout(null);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
