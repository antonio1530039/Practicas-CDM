package com.example.antonio.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private EditText num1;
    private EditText num2;
    private Button btn;
    private TextView res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        num1 = (EditText) findViewById(R.id.n1);
        num2 = (EditText) findViewById(R.id.n2);
        res = (TextView) findViewById(R.id.resultado);
        btn = (Button) findViewById(R.id.boton);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n1 = Integer.parseInt(num1.getText().toString());
                int n2 = Integer.parseInt(num2.getText().toString());
                int resul = n1 + n2;
                res.setText("Resultado: " + resul);
            }
        });
    }
}
