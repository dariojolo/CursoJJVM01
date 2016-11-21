package com.dariojolo.pruebadeintents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private TextView txtSecond;
    private Button btnSecond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Activar flecha ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtSecond = (TextView)findViewById(R.id.txtSecond);
        btnSecond = (Button)findViewById(R.id.btnSecond);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("Mensaje") != null){
            String mensaje = bundle.getString("Mensaje");
            txtSecond.setText(mensaje);
        }else{
            Toast.makeText(this,"Mensaje vacio",Toast.LENGTH_LONG).show();
        }
        btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this,ActivityThird.class);
                startActivity(intent);
            }
        });
    }
}
