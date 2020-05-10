package br.com.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.widget.ListAdapter;
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
import java.util.List;

public class email extends AppCompatActivity {

    private ArrayList<Contato> contatos = new ArrayList<Contato>();
    private ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        lerEmail();


    }


    public void lerEmail() {
        if (checkInternetConection()){
            progressDialog = ProgressDialog.show(this, "", "Obtendo dados");
            new DownloadJson().execute("http://mfpledon.com.br/listadecontatos.json");
            //para aparelhos reais, pode usar o endereço http://mfpledon.com.br/paisesbck.json

        } else{
            Toast.makeText(getApplicationContext(),"Sem conexão. Verifique.",Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("deprecation")
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

    public void mostrarJSONFone(String strjson){
        //((TextView)findViewById(R.id.dados)).setText(strjson);
        //recebe uma String com os dados do JSON
         contatos = new ArrayList<>();
        try {
            JSONObject objRaiz = new JSONObject(strjson);
            JSONArray jsonArray = objRaiz.optJSONArray("listacontatos");
            JSONObject jsonObject = null;
            //percorre o vetor de funcionarios e pega o nome para imprimir
            Contato contato = null;
            for(int i=0; i < jsonArray.length(); i++){
                contato = new Contato();

                jsonObject = jsonArray.getJSONObject(i);
                contato.setId(jsonObject.optString("id"));
                contato.setNomecontato(jsonObject.optString("nomecontato"));
                contato.setEmail(jsonObject.optString("email"));
                contatos.add(contato);
                jsonObject = null;
            }

            CustomListAdapter customAdapter = new CustomListAdapter(
                    this,
                    R.layout.item_list,
                    contatos);

            ListView lista = (ListView) findViewById(R.id.listContatos);
            lista.setAdapter(customAdapter);

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { ((Contato)parent.getAdapter().getItem(position)).getEmail() });
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Projeto Programação Mobile");
                    intent.putExtra(Intent.EXTRA_TEXT, "Projeto enviado, nota 10!");
                    startActivity(Intent.createChooser(intent, ""));
                }
            });
            progressDialog.dismiss();
        } catch (JSONException e) {

        }
        finally { progressDialog.dismiss(); }
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
            mostrarJSONFone(result);
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
