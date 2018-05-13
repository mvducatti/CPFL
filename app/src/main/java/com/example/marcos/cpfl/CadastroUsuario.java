package com.example.marcos.cpfl;

import android.annotation.SuppressLint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.marcos.cpfl.Connection.Constants;
import com.example.marcos.cpfl.Connection.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CadastroUsuario extends AppCompatActivity {

    private EditText editTextCPF, editTextNome, editTextSenha, editTextRua,
            editTextNumero, editTextBairro, editTextCidade;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        editTextCPF = findViewById(R.id.txtCPF);
        editTextNome = findViewById(R.id.txtNome);
        editTextSenha = findViewById(R.id.txtSenha);
        editTextRua = findViewById(R.id.txtRua);
        editTextNumero = findViewById(R.id.txtNumero);
        editTextBairro = findViewById(R.id.txtBairro);
        editTextCidade = findViewById(R.id.txtCidade);
        spinner = findViewById(R.id.spinner);

    }

    public void registerUser(View view) {


        final String cpf = editTextCPF.getText().toString().trim();
        final String nome = editTextNome.getText().toString().trim();
        final String senha = editTextSenha.getText().toString().trim();
        final String rua = editTextRua.getText().toString().trim();
        final String numero = editTextNumero.getText().toString().trim();
        final String bairro = editTextBairro.getText().toString().trim();
        final String cidade = editTextCidade.getText().toString().trim();
        final String estado = spinner.getSelectedItem().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                new com.android.volley.Response.Listener<String>(){
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")){

                                Snackbar.make(findViewById(android.R.id.content),obj.getString("message"),Snackbar.LENGTH_LONG).show();

                                editTextCPF.setText("");
                                editTextNome.setText("");
                                editTextRua.setText("");
                                editTextSenha.setText("");
                                editTextNumero.setText("");
                                editTextBairro.setText("");
                                editTextCidade.setText("");
                                spinner.setSelection(0);

                            }else{
                                Snackbar.make(findViewById(android.R.id.content),obj.getString("message"),Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Erro: " + e,
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro: " + error,
                        Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cpf", cpf);
                params.put("username", nome);
                params.put("password", senha);
                params.put("street", rua);
                params.put("house_number", numero);
                params.put("neighborhood", bairro);
                params.put("city", cidade);
                params.put("state", estado);
                params.put("user_type", String.valueOf(1));
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

}
