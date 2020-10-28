package com.example.ihsan;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

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

public class housingImageScreen extends AppCompatActivity {
    ImageView housingImage;
    StorageReference storageReference;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housing_image_screen);

        housingImage=findViewById(R.id.housingPic);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userName = fAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();

        //Get user's documents from the database from folder BeneficiariesDocs

        DocumentReference documentReference = fStore.collection("Beneficiaries").document(userName);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                StorageReference viewHousingRef = storageReference.child("BeneficiariesDocs/" + documentSnapshot.getString("email") + "/housing.jpg");
                viewHousingRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getApplicationContext()).load(uri).into(housingImage);
                    }
                });

            }
    });
    }
}