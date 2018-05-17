package com.example.marcos.cpfl;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.marcos.cpfl.Connection.Constants;
import com.example.marcos.cpfl.Connection.RequestHandler;
import com.example.marcos.cpfl.Models.User;
import com.example.marcos.cpfl.SharedPreferences.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginCPF, editTextLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()){

            if (SharedPrefManager.getInstance(this).getUser().getUser_type() == 0){
                startActivity(new Intent(this, Gerente.class));
            }

            if (SharedPrefManager.getInstance(this).getUser().getUser_type() == 1){
                startActivity(new Intent(this, Usuario.class));
            }

            if (SharedPrefManager.getInstance(this).getUser().getUser_type() == 2){
                startActivity(new Intent(this, Medidor.class));
            }

            finish();
            return;
        }

        editTextLoginCPF = findViewById(R.id.txtCPF);
        editTextLoginPassword = findViewById(R.id.txtPassword);

    }

    public void userLogin(View view){

        final String cpf = editTextLoginCPF.getText().toString().trim();
        final String password = editTextLoginPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")){

                        User user = new User(obj.getInt("user_id"), obj.getString("cpf"),
                                obj.getInt("user_type"), obj.getString("username")
                        );

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        if (obj.getInt("user_type") == 0){
                            startActivity(new Intent(getApplicationContext(), Gerente.class));
                        }

                        if (obj.getInt("user_type") == 1){
                            startActivity(new Intent(getApplicationContext(), Usuario.class));
                        }

                        if (obj.getInt("user_type") == 2){
                            startActivity(new Intent(getApplicationContext(), Medidor.class));
                        }

                        finish();

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

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
                params.put("cpf", cpf);
                params.put("password", password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void gotoRegister(View view){
        Intent intent = new Intent(getApplicationContext(), CadastroUsuario.class);
        startActivity(intent);
    }
}
