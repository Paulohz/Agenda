package br.com.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class email extends AppCompatActivity {
    private ArrayList<Contato> contatos = new ArrayList<Contato>();
    private ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        lerJSON();

        final ArrayAdapter<Contato> myadapter = new ArrayAdapter<Contato>(
                getApplicationContext(),
                R.layout.item_list,
                R.id.listContatos,
                contatos);

        ListView lista = (ListView)findViewById(R.id.listContatos);
        lista.setAdapter(myadapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        ""+myadapter.getItem(position),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void lerJSON() {  //este método atende o evento de click no botão Funcionários
        if (checkInternetConection()){
            progressDialog = ProgressDialog.show(this, "", "Baixando dados");
            new DownloadJson().execute("http://mfpledon.com.br/listadecontatos.json");
        } else{
            Toast.makeText(getApplicationContext(),"Sem conexão. Verifique.",Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkInternetConection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void mostrarJSONReceita(String strjson){
        //recebe uma String com os dados do JSON
        String data = "";
        Contato contato = null;
        contatos = new ArrayList<Contato>();
        try {
            JSONObject objRaiz = new JSONObject(strjson);
            JSONArray jsonArray = objRaiz.optJSONArray("listacontatos");
            JSONObject jsonObject = null;
            //percorre o vetor de funcionarios e pega o nome para imprimir
            for(int i=0; i < jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);
                contato = new Contato();
                contato.setId(jsonObject.optString("id"));
                contato.setNomecontato(jsonObject.optString("nomecontato"));
                contato.setEmail(jsonObject.optString("email"));
                contato.setEndereco(jsonObject.optString("endereco"));
                contato.setGenero(jsonObject.optString("genero"));
                contato.setCelular(jsonObject.optString("celular"));

                jsonObject = null;
            }
           // ((TextView)findViewById(R.id.dados)).setText(data);
            progressDialog.dismiss();
        } catch (JSONException e) {}
    }

    private class DownloadJson extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // params é um vetor onde params[0] é a URL
            try {
                return downloadJSON(params[0]);
            } catch (IOException e) {
                return "URL inválido";
            }
        }

        // onPostExecute exibe o resultado do AsyncTask
        @Override
        protected void onPostExecute(String result) {
            mostrarJSONReceita(result);
        }

        private String downloadJSON(String myurl) throws IOException {
            InputStream is = null;
            String respostaHttp = "";
            HttpURLConnection conn = null;
            InputStream in = null;
            ByteArrayOutputStream bos = null;
            try {
                URL u = new URL(myurl);
                conn = (HttpURLConnection) u.openConnection();
                conn.setConnectTimeout(7000); // 7 segundos de timeout
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                in = conn.getInputStream();
                bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                }
                respostaHttp = bos.toString("UTF-8");
                return respostaHttp;
            } catch (Exception ex) {
                return "URL inválido ou estouro de memória ou...: \n" + ex.getMessage() + "\nmyurl: " + myurl;
            } finally {
                if (in != null) in.close();
            }
        }


    }
}
