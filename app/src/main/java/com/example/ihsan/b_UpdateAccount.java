package com.example.ihsan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class b_UpdateAccount extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button bCancel,bSave, bNewPassword, housingButton, incomeButton;
    EditText etName,etFrist,etFamily,etID,etEemail,etPhone,etAddress,etCharity;
    ImageView housingImage, incomeImage;

    //Database attributes
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    String userName;
    String fristName;
    String familyName;
    String idNumber;
    String email;
    String phoneNumber;
    String homeAddress;
    String charityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b__update_account);


        bCancel = findViewById(R.id.cancel);
        bSave = findViewById(R.id.save);
        bNewPassword = findViewById(R.id.newPassword);
        housingButton = findViewById(R.id.housingButton);
        incomeButton = findViewById(R.id.incomeButton);

        etName = findViewById(R.id.userName);
        etFrist = findViewById(R.id.first);
        etFamily = findViewById(R.id.family);
        etID = findViewById(R.id.id);
        etEemail = findViewById(R.id.eemail);
        etPhone = findViewById(R.id.phone);
        etAddress = findViewById(R.id.addess);
        etCharity = findViewById(R.id.cahrity);

        housingImage = findViewById(R.id.housingPic);
        incomeImage = findViewById(R.id.incomePic);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userName = fAuth.getCurrentUser().getUid();


        //Get Database values
        DocumentReference documentReference = fStore.collection("Beneficiaries").document(userName);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                etName.setText(documentSnapshot.getString("email"));
                etFrist.setText(documentSnapshot.getString("firstName"));
                etFamily.setText(documentSnapshot.getString("familyName"));
                etID.setText(documentSnapshot.getString("idNumber"));
                etEemail.setText(documentSnapshot.getString("email"));
                etPhone.setText(documentSnapshot.getString("phoneNumber"));
                etAddress.setText(documentSnapshot.getString("homeAddress"));
                etCharity.setText(documentSnapshot.getString("charityName"));

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
                startActivity(new Intent(b_UpdateAccount.this, housingImageScreen.class));
            }
        });

        incomeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(b_UpdateAccount.this, incomeImageScreen.class));
            }
        });


        //Save new (updated) values to the database
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 userName=etName.getText().toString().trim();
                 fristName=etFrist.getText().toString().trim();
                 familyName=etFamily.getText().toString().trim();
                 idNumber=etID.getText().toString().trim();
                 email=etEemail.getText().toString().trim();
                 phoneNumber=etPhone.getText().toString().trim();
                 homeAddress=etAddress.getText().toString().trim();
                 charityName=etCharity.getText().toString().trim();

                String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

                if (userName.isEmpty()||userName.length()<2){
                    etName.setError("الرجاء إدخال اسم المستخدم.");
                    return; }//userName

                if (TextUtils.isEmpty(fristName)){
                    etFrist.setError("الرجاء إدخال الاسم الاول.");
                    return; }//fristName

                if (TextUtils.isEmpty(familyName)){
                    etFamily.setError("الرجاء إدخال اسم العائلة.");
                    return; }//familyName

                if (idNumber.isEmpty()||idNumber.length()!=10){
                    etID.setError("الرجاء إدخال رقم الهوية الوطنية أو رقم الاقامة.");
                    return; }//idNumber

                if (TextUtils.isEmpty(email)){
                    etEemail.setError("الرجاء إدخال البريد الالكتروني.");
                    return; }//email
                else if (!email.matches(checkEmail)) {
                    etEemail.setError("البريد الالكتروني خاطئ!");
                    return ;
                }//email

                if (phoneNumber.isEmpty()||phoneNumber.length()!=10){
                    etPhone.setError("الرجاء إدخال رقم الجوال.");
                    return; }//PhoneNumber

                if (TextUtils.isEmpty(homeAddress)){
                    etAddress.setError("الرجاء إدخال عنوان المنزل.");
                    return; }//homeAddress

                if (TextUtils.isEmpty(charityName)){
                    etCharity.setError("الرجاء إدخال اسم المؤسسة الخيرية التابع لها.");
                    return; }//charityName


                userName = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Beneficiaries").document(userName);
                Map<String, Object> user = new HashMap<>();
                user.put("userName", userName);
                user.put("firstName", fristName);
                user.put("familyName", familyName);
                user.put("idNumber", idNumber);
                user.put("email", email);
                user.put("phoneNumber", phoneNumber);
                user.put("homeAddress", homeAddress);
                user.put("charityName", charityName);


                Toast.makeText(b_UpdateAccount.this, "تم تحديث معلومات الحساب..", Toast.LENGTH_SHORT).show();
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: user Profile is updated for " + userName);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });


            }
        });

        //Change Password
        bNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetUserName = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("إعادة تعيين كلمة المرور؟");
                passwordResetDialog.setMessage("أدخل بريدك الإلكتروني لتلقي رابط إعادة التعيين.");
                passwordResetDialog.setView(resetUserName);

                passwordResetDialog.setPositiveButton("إرسال", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //extract the email and send reset link
                        String mail = resetUserName.getText().toString();
                        // use firebase
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(b_UpdateAccount.this, "تم إرسال رابط إعادة التعيين إلى بريدك الالكتروني", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(b_UpdateAccount.this, "حصل خطأ! لم يتم إرسال رابط إعادة التعيين." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //close the dialog

                    }
                });

                passwordResetDialog.create().show();


            }
        });

        //upload pictures from gallery

        housingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });

        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 2000);

            }
        });


        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(b_UpdateAccount.this, b_ViewAccount.class));// It changes if we add the database

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                housingImage.setImageURI(imageUri);

                uploadHousingImageToFirebase(imageUri);

            }
        }
        if (requestCode == 2000) {
            if(resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                incomeImage.setImageURI(imageUri);

                uploadIncomeImageToFirebase(imageUri);

            }
        }

    }

    //Upload pictures to the database in folder BeneficiariesDocs

    private void uploadHousingImageToFirebase(Uri imageUri) {
        // upload image to Firebase Storage
        StorageReference fileRef = storageReference.child("BeneficiariesDocs/" + etEemail.getText().toString() + "/housing.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(b_UpdateAccount.this, "تم رفع الملف بنجاح.", Toast.LENGTH_SHORT).show();
                //   Picasso.with(getApplicationContext()).load(uri).into(housingImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(b_UpdateAccount.this, "حصل خطأ! لم يتم رفع الملف.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadIncomeImageToFirebase(Uri imageUri) {
        // upload image to Firebase Storage
        StorageReference fileRef = storageReference.child("BeneficiariesDocs/" + etEemail.getText().toString() + "/income.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(b_UpdateAccount.this, "تم رفع الملف بنجاح.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(b_UpdateAccount.this, "حصل خطأ! لم يتم رفع الملف.", Toast.LENGTH_SHORT).show();
            }
        });

    }








}