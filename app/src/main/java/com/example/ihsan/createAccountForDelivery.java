package com.example.ihsan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createAccountForDelivery extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button bSignUp;
    EditText etUserName,etFristName,etFamilyName,etIDNumber,etEmail,etPhoneNumber,etPassword,etPasswordConfir;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userName;
    String fristName;
    String familyName;
    String idNumber;
    String email;
    String phoneNumber;
    String password;
    String passwordConfir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_for_delivery);

        bSignUp=findViewById(R.id.beneficiary);
        etUserName=findViewById(R.id.userName);
        etFristName=findViewById(R.id.charityName);
        etFamilyName=findViewById(R.id.familyName);
        etIDNumber=findViewById(R.id.idNumber);
        etEmail=findViewById(R.id.email);
        etPhoneNumber=findViewById(R.id.phoneNumber);
        etPassword=findViewById(R.id.password);
        etPasswordConfir=findViewById(R.id.passwordConfir);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

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

                if (TextUtils.isEmpty(password)){
                    etPassword.setError("الرجاء إدخال كلمة المرور.");
                    return; }//password

                if (password.length()<6){
                    etPassword.setError("يجب أن تتكون كلمة المرور من 6 أحرف أو أكثر.");
                    return; }//password length

                if(!password.equals(passwordConfir)){
                    Toast.makeText(createAccountForDelivery.this,"يجب عليك كتابة كلمة المرور بشكل صحيح" ,Toast.LENGTH_LONG).show();}
                //   else { } //Using the firebase to knowledge that it is entered correctly or not


                // Create account in firebase & save account information

                fAuth.createUserWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(createAccountForDelivery.this, "تم إنشاء الحساب.", Toast.LENGTH_SHORT).show();
                           userName = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("DeliveryVolunteers").document(userName);
                            Map<String, Object> user = new HashMap<>();
                            user.put("userName", userName);
                            user.put("firstName", fristName);
                            user.put("familyName", familyName);
                            user.put("idNumber", idNumber);
                            user.put("email", email);
                            user.put("phoneNumber", phoneNumber);
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
                            startActivity(new Intent(getApplicationContext(),NavaigationDelivery.class));
                        } else {
                            Toast.makeText(createAccountForDelivery.this, "حصل خطأ!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });







    }
}