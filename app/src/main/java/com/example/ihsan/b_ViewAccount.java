package com.example.ihsan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import javax.annotation.Nullable;

public class b_ViewAccount extends AppCompatActivity {
    TextView tvUserName,tvFristName,tvFamilyName,tvIdNumber,tvEmail,tvPhoneNumber,tvHomeAddress,tvCharityName;
    ImageView housingImage, incomeImage;
    Button bModify;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b__view_account);
        bModify=findViewById(R.id.modify);

        tvUserName=findViewById(R.id.userName);
        tvFristName=findViewById(R.id.first);
        tvFamilyName=findViewById(R.id.family);
        tvIdNumber=findViewById(R.id.id);
        tvEmail=findViewById(R.id.eemail);
        tvPhoneNumber=findViewById(R.id.phone);
        tvHomeAddress=findViewById(R.id.address);
        tvCharityName=findViewById(R.id.cahrity);
        housingImage=findViewById(R.id.housingPic);
        incomeImage=findViewById(R.id.incomePic);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userName = fAuth.getCurrentUser().getUid();

        //Get Database values
        DocumentReference documentReference = fStore.collection("Beneficiaries").document(userName);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                tvUserName.setText(documentSnapshot.getString("email"));
                tvFristName.setText(documentSnapshot.getString("firstName"));
                tvFamilyName.setText(documentSnapshot.getString("familyName"));
                tvIdNumber.setText(documentSnapshot.getString("idNumber"));
                tvEmail.setText(documentSnapshot.getString("email"));
                tvPhoneNumber.setText(documentSnapshot.getString("phoneNumber"));
                tvHomeAddress.setText(documentSnapshot.getString("homeAddress"));
                tvCharityName.setText(documentSnapshot.getString("charityName"));

                StorageReference viewIncomeRef = storageReference.child("BeneficiariesDocs/" + documentSnapshot.getString("email") + "/income.jpg");
                viewIncomeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getApplicationContext()).load(uri).into(incomeImage);
                    }
                });

                StorageReference viewHousingRef = storageReference.child("BeneficiariesDocs/" + documentSnapshot.getString("email") + "/housing.jpg");
                viewHousingRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getApplicationContext()).load(uri).into(housingImage);
                    }
                });

            }
        });

        housingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(b_ViewAccount.this, housingImageScreen.class));
            }
        });

        incomeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(b_ViewAccount.this, incomeImageScreen.class));
            }
        });



        bModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(b_ViewAccount.this, b_UpdateAccount.class));

            }
        });
    }
}