package com.example.ihsan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class c_ViewAccount extends AppCompatActivity {
    TextView tvUserName,tvCharityName,tvCharityNumber,tvEmail,tvPhoneNumber,tvCharityAddress;
    Button bModify;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__view_account);


        bModify=findViewById(R.id.modify);

        tvUserName=findViewById(R.id.userName);
        tvCharityName=findViewById(R.id.cahrity);
        tvCharityNumber=findViewById(R.id.number);
        tvEmail=findViewById(R.id.eemail);
        tvPhoneNumber=findViewById(R.id.phone);
        tvCharityAddress=findViewById(R.id.address);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userName = fAuth.getCurrentUser().getUid();

        //Get Database values
        DocumentReference documentReference = fStore.collection("Charities").document(userName);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                tvUserName.setText(documentSnapshot.getString("email"));
                tvCharityName.setText(documentSnapshot.getString("charityName"));
                tvCharityNumber.setText(documentSnapshot.getString("charityNumber"));
                tvCharityAddress.setText(documentSnapshot.getString("charityAddress"));
                tvEmail.setText(documentSnapshot.getString("email"));
                tvPhoneNumber.setText(documentSnapshot.getString("phoneNumber"));
            }
        });


        bModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(c_ViewAccount.this, c_UpdateAccount.class));// It changes if we add the database

            }
        });
    }
}






