package com.example.buscacep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public EditText edtCep;
    public TextView logradouro;
    public TextView bairro;
    public TextView localidade;
    public TextView uf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCep = findViewById(R.id.edtCep);
        logradouro = findViewById(R.id.logradouro);
        bairro = findViewById(R.id.bairro);
        localidade = findViewById(R.id.localidade);
        uf = findViewById(R.id.uf);
    }

    public void onBuscar(View view) {
        final String cep = edtCep.getText().toString();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                buscarCep(cep);
            }
        });
        t.start();
    }

    public void buscarCep(String cep) {
        try {
            String retornoJson = efetuarRequisicao(cep);
            JSONObject obj = new JSONObject(retornoJson);
            logradouro.setText(obj.getString("logradouro"));
            bairro.setText(obj.getString("bairro"));
            localidade.setText(obj.getString("localidade"));
            uf.setText(obj.getString("uf"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String efetuarRequisicao(String cep) throws IOException {
        HttpURLConnection conexao = null;
        String retornoJson = null;
        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/json");
            conexao = (HttpURLConnection) url.openConnection();
            InputStream is = new BufferedInputStream(conexao.getInputStream());
            retornoJson = lerStream(is);
        } finally {
            if (conexao != null) {
                conexao.disconnect();
            }
        }
        return retornoJson;
    }

    private String lerStream(InputStream is) throws IOException {
        String retornoJson = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String linha = null;
            while ((linha = reader.readLine()) != null) {
                builder.append(linha + "\n");
            }
            retornoJson = builder.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return retornoJson;
    }
}