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

public class d_ViewAccount extends AppCompatActivity {
    TextView tvUserName,tvFristName,tvFamilyName,tvIdNumber,tvEmail,tvPhoneNumber;
    Button bModify;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d__view_account);

        bModify=findViewById(R.id.modify);

        tvUserName=findViewById(R.id.userName);
        tvFristName=findViewById(R.id.cahrity);
        tvFamilyName=findViewById(R.id.family);
        tvIdNumber=findViewById(R.id.id);
        tvEmail=findViewById(R.id.eemail);
        tvPhoneNumber=findViewById(R.id.phone);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userName = fAuth.getCurrentUser().getUid();

        //get values from the database
        DocumentReference documentReference = fStore.collection("DeliveryVolunteers").document(userName);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                tvUserName.setText(documentSnapshot.getString("email"));
                tvFristName.setText(documentSnapshot.getString("firstName"));
                tvFamilyName.setText(documentSnapshot.getString("familyName"));
                tvIdNumber.setText(documentSnapshot.getString("idNumber"));
                tvEmail.setText(documentSnapshot.getString("email"));
                tvPhoneNumber.setText(documentSnapshot.getString("phoneNumber"));
            }
        });


        bModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(d_ViewAccount.this, d_UpdateAccount.class));// It changes if we add the database

            }
        });
    }
}











