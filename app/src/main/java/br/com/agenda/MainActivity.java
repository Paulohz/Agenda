package br.com.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnFone, btnSobre, btnEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnFone = (Button)findViewById(R.id.btnFone);
        btnSobre = (Button)findViewById(R.id.btnSobre);
        btnEmail = (Button)findViewById(R.id.btnEmail);

        btnFone.setOnClickListener(this);
        btnSobre.setOnClickListener(this);
        btnEmail.setOnClickListener(this);

    }

    public void onClick(View v){
        Class classe = null;
        switch(v.getId()){
           case R.id.btnEmail: classe = email.class; break;
           case R.id.btnSobre: classe = sobre.class; break;
           case R.id.btnFone: classe = fone.class; break;
        }
        Intent i = new Intent(getApplicationContext(),classe);
        startActivity(i);
    }

}
