package com.example.marcos.cpfl;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.marcos.cpfl.Connection.Constants;
import com.example.marcos.cpfl.SharedPreferences.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Medidor extends AppCompatActivity {

    private Spinner spinnerMes, spinnerAno;
    private TextView tvBandeira, tvkWh, tvImposto, tvtotal;
    private String verde = "Verde", amarelo = "Amarela", vermelho = "Vermelha";
    private EditText tietCPF, tietConsumo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medidor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinnerMes = findViewById(R.id.spinnerMes);
        spinnerAno = findViewById(R.id.spinnerAno);

        tietCPF = findViewById(R.id.tietCPF);
        tietConsumo = findViewById(R.id.tietConsumo);

        tvBandeira = findViewById(R.id.txtBandeiraAtual);
        tvkWh = findViewById(R.id.txtkwhAtual);
        tvImposto = findViewById(R.id.txtImpostoAtual);
        tvtotal = findViewById(R.id.txtTotal);

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

    public void gerarFatura (View view){

        final float consumo = 200;
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

        final float total = (((consumo * kwh) /100 * imposto) / 100 * flagValue);

//        final float total = Float.parseFloat(tvtotal.getText().toString().trim());

        Snackbar.make(findViewById(android.R.id.content),"" + total,Snackbar.LENGTH_LONG).show();
    }


    public void logout(){
        SharedPrefManager.getInstance(this).logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
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
