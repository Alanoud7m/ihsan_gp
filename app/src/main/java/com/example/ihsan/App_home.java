package com.example.ihsan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class App_home extends AppCompatActivity {

    Button tvBeneficiary,tvCharity,tvDelivery;
    ImageView admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_home);

        tvBeneficiary=findViewById(R.id.beneficiary);
        tvCharity=findViewById(R.id.cahrity);
        tvDelivery=findViewById(R.id.delivery);
        admin = findViewById(R.id.admin);

        //According to the type of the user, open its login page.

        tvBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(App_home.this, loginB.class));
            }
        });

        tvCharity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(App_home.this, loginC.class));
            }
        });


        tvDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(App_home.this, loginD.class));
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(App_home.this, loginAdmin.class));
            }
        });


    } }