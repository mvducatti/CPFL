package com.example.marcos.cpfl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.marcos.cpfl.SharedPreferences.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Medidor extends AppCompatActivity {

    private Spinner spinnerMonth, spinnerYear;
    private TextView tvBandeira, tvkWh, tvImposto, tvtotal;
    private String verde = "Verde", amarelo = "Amarela", vermelho = "Vermelha";
    private Button calcular, gerarFatura;
    private EditText tietCPF, tietConsumo;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medidor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);

        calcular = findViewById(R.id.btnCalcular);
        calcular.setEnabled(false);
        calcular.setBackgroundColor(Color.parseColor("#808080"));

        gerarFatura = findViewById(R.id.btnGerarFatura);
        gerarFatura.setEnabled(false);
        gerarFatura.setBackgroundColor(Color.parseColor("#808080"));

        tietCPF = findViewById(R.id.tietCPF);
        tietConsumo = findViewById(R.id.tietConsumo);

        tvBandeira = findViewById(R.id.txtBandeiraAtual);
        tvkWh = findViewById(R.id.txtkwhAtual);
        tvImposto = findViewById(R.id.txtImpostoAtual);
        tvtotal = findViewById(R.id.txtTotal);

        gerarFatura.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                gerarGatura();
            }
        });

        loadTaxes();

    }

    //inflate menu so it can be shown
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gerente_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
                            tvImposto.setText(percentagem);

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

    public void CalcularFatura (View view){

        if (tietConsumo.length() <= 0){
            tietConsumo.setError("Digite um valor");
            Snackbar.make(findViewById(android.R.id.content), "Digite o valor do consumo",Snackbar.LENGTH_LONG).show();
        }else{
            final float consumo = Float.parseFloat(tietConsumo.getText().toString().trim());

            final float kwh = Float.parseFloat(tvkWh.getText().toString().trim());
            final float imposto = Float.parseFloat(tvImposto.getText().toString().trim());
            final String bandeira = tvBandeira.getText().toString().trim();

            float flagValue = 0;

            if (bandeira.equals("Verde")){
                flagValue = 2;
            } else if (bandeira.equals("Amarela")){
                flagValue = 8;
            } else if (bandeira.equals("Vermelha")){
                flagValue = 15;
            }

            final float total = (consumo * kwh) * (1.0f + (imposto / 100.0f)) * (1.0f + flagValue / 100.0f);

            tvtotal.setText(String.format("%.2f", total));

            Snackbar.make(findViewById(android.R.id.content), String.format("%.2f", total),Snackbar.LENGTH_LONG).show();

            gerarFatura.setEnabled(true);
            gerarFatura.setBackgroundColor(Color.parseColor("#FF082D6C"));
        }
    }

    public void checkUser(View view){

        final String cpf = tietCPF.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CHECK_USER, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")){

                        Snackbar.make(findViewById(android.R.id.content),obj.getString("message"),Snackbar.LENGTH_LONG).show();
                        user_id = obj.getInt("user_id");
                        System.out.println(user_id);

                        calcular.setEnabled(true);
                        calcular.setBackgroundColor(Color.parseColor("#FF082D6C"));
                        tietConsumo.requestFocus();

                    }else{
                        tietCPF.setError(obj.getString("message"));
//                        Snackbar.make(findViewById(android.R.id.content),obj.getString("message"),Snackbar.LENGTH_LONG).show();
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
                params.put("cpf", cpf);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void logout(){
        SharedPrefManager.getInstance(this).logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void gerarGatura(){

        final int year = Integer.parseInt(spinnerYear.getSelectedItem().toString());
        final int month = Integer.parseInt(spinnerMonth.getSelectedItem().toString());
        final float consumo = Float.parseFloat(tvtotal.getText().toString().trim());
        final int id = user_id;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_FATURA,
                new com.android.volley.Response.Listener<String>(){
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")){

                                Snackbar.make(findViewById(android.R.id.content),obj.getString("message"),Snackbar.LENGTH_LONG).show();

                                tietCPF.setText("");
                                tietCPF.requestFocus();
                                tietConsumo.setText("");
                                tvtotal.setText("");

                                calcular.setEnabled(false);
                                calcular.setBackgroundColor(Color.parseColor("#808080"));

                                gerarFatura.setEnabled(false);
                                gerarFatura.setBackgroundColor(Color.parseColor("#808080"));

                            }else{
                                Snackbar.make(findViewById(android.R.id.content),obj.getString("message"),Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Erro 1: " + e, Toast.LENGTH_LONG).show();

                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro 2: " + error, Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("year", String.valueOf(year));
                params.put("month", String.valueOf(month));
                params.put("consumo", String.valueOf(consumo));
                params.put("user_id", String.valueOf(id));
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
