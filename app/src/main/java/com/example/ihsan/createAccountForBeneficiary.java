package com.example.ihsan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class createAccountForBeneficiary extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button bSignUp, bhousingUpload, bIncomeUpload;
    EditText etUserName,etFristName,etFamilyName,etIDNumber,etEmail,etPhoneNumber,etPassword,etPasswordConfir,etHomeAddress,etCharityName;
    ImageView housingImage, incomeImage;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    String userName;
    String fristName;
    String familyName;
    String idNumber;
    String email;
    String phoneNumber;
    String password;
    String passwordConfir;
    String homeAddress;
    String charityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_for_beneficiary);

        bSignUp=findViewById(R.id.beneficiary);
        etUserName=findViewById(R.id.userName);
        etFristName=findViewById(R.id.fristName);
        etFamilyName=findViewById(R.id.familyName);
        etIDNumber=findViewById(R.id.idNumber);
        etEmail=findViewById(R.id.email);
        etPhoneNumber=findViewById(R.id.phoneNumber);
        etPassword=findViewById(R.id.password);
        etPasswordConfir=findViewById(R.id.passwordConfir);
        etHomeAddress=findViewById(R.id.homeAddress);
        etCharityName=findViewById(R.id.charityName);

        bhousingUpload = findViewById(R.id.housing);
        bIncomeUpload = findViewById(R.id.income);

        housingImage = findViewById(R.id.housingPic);
        incomeImage = findViewById(R.id.incomePic);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //Beneficiary's documents storage
        StorageReference viewHousingRef = storageReference.child("BeneficiariesDocs/"+fAuth.getCurrentUser().getUid()+"/housing.jpg");
        viewHousingRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getApplicationContext()).load(uri).into(housingImage);
            }
        });

        StorageReference viewIncomeRef = storageReference.child("BeneficiariesDocs/"+fAuth.getCurrentUser().getUid()+"/income.jpg");
        viewIncomeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getApplicationContext()).load(uri).into(incomeImage);
            }
        });


            //Save values to the database
                bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 userName=etUserName.getText().toString().trim();
                 fristName=etFristName.getText().toString().trim();
                 familyName=etFamilyName.getText().toString().trim();
                 idNumber=etIDNumber.getText().toString().trim();
                 email=etEmail.getText().toString().trim();
                 phoneNumber=etPhoneNumber.getText().toString().trim();
                 password=etPassword.getText().toString().trim();
                 passwordConfir=etPasswordConfir.getText().toString().trim();
                 homeAddress=etHomeAddress.getText().toString().trim();
                 charityName=etCharityName.getText().toString().trim();

                String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

                if (userName.isEmpty()||userName.length()<2){
                    etUserName.setError("الرجاء إدخال اسم المستخدم.");
                    return; }//userName

                if (TextUtils.isEmpty(fristName)){
                    etFristName.setError("الرجاء إدخال الاسم الاول.");
                    return; }//fristName

                if (TextUtils.isEmpty(familyName)){
                    etFamilyName.setError("الرجاء إدخال اسم العائلة.");
                    return; }//familyName

                if (idNumber.isEmpty()||idNumber.length()!=10){
                    etIDNumber.setError("الرجاء إدخال رقم الهوية الوطنية أو رقم الاقامة.");
                    return; }//idNumber

                if (TextUtils.isEmpty(email)){
                    etEmail.setError("الرجاء إدخال البريد الالكتروني.");
                    return; }//email
                else if (!email.matches(checkEmail)) {
                    etEmail.setError("البريد الالكتروني خاطئ!");
                    return ;
                }//email

                if (phoneNumber.isEmpty()||phoneNumber.length()!=10){
                    etPhoneNumber.setError("الرجاء إدخال رقم الجوال.");
                    return; }//PhoneNumber

                if (TextUtils.isEmpty(homeAddress)){
                    etHomeAddress.setError("الرجاء إدخال عنوان المنزل.");
                    return; }//homeAddress

                if (TextUtils.isEmpty(charityName)){
                    etCharityName.setError("الرجاء إدخال اسم المؤسسة الخيرية التابع لها.");
                    return; }//charityName

                if (TextUtils.isEmpty(password)){
                    etPassword.setError("الرجاء إدخال كلمة المرور.");
                    return; }//password

                if (password.length()<6){
                    etPassword.setError("يجب أن تتكون كلمة المرور من 6 أحرف أو أكثر.");
                    return; }//password length

                if(!password.equals(passwordConfir)){
                    Toast.makeText(createAccountForBeneficiary.this,"يجب عليك كتابة كلمة المرور بشكل صحيح" ,Toast.LENGTH_LONG).show();}
                //   else { } //Using the firebase to knowledge that it is entered correctly or not

                fAuth.createUserWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(createAccountForBeneficiary.this, "تم إنشاء الحساب.", Toast.LENGTH_SHORT).show();
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
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for " + userName);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),NavaigationActivity.class));
                        } else {
                            Toast.makeText(createAccountForBeneficiary.this, "حصل خطأ!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

                //upload pictures from gallery

        bhousingUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });

        bIncomeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 2000);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

        final StorageReference fileRef = storageReference.child("BeneficiariesDocs/" + etEmail.getText().toString() + "/housing.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(createAccountForBeneficiary.this, "تم رفع الملف بنجاح.", Toast.LENGTH_SHORT).show();
             //   Picasso.with(getApplicationContext()).load(uri).into(housingImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(createAccountForBeneficiary.this, "حصل خطأ! لم يتم رفع الملف.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadIncomeImageToFirebase(Uri imageUri) {
        // upload image to Firebase Storage
        final StorageReference fileRef = storageReference.child("BeneficiariesDocs/" + etEmail.getText().toString() + "/income.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(createAccountForBeneficiary.this, "تم رفع الملف بنجاح.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(createAccountForBeneficiary.this, "حصل خطأ! لم يتم رفع الملف.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
