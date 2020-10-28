package com.example.ihsan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class d_UpdateAccount extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button bCancel,bSave, bNewPassword;
    EditText etName,etFrist,etFamily,etID,etEemail,etPhone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d__update_account);

        bCancel = findViewById(R.id.cancel);
        bSave = findViewById(R.id.save);
        bNewPassword = findViewById(R.id.newPassword);

        etName = findViewById(R.id.userName);
        etFrist = findViewById(R.id.cahrity);
        etFamily = findViewById(R.id.family);
        etID = findViewById(R.id.id);
        etEemail = findViewById(R.id.eemail);
        etPhone = findViewById(R.id.phone);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userName = fAuth.getCurrentUser().getUid();

        //Get Database values
        DocumentReference documentReference = fStore.collection("DeliveryVolunteers").document(userName);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                etName.setText(documentSnapshot.getString("email"));
                etFrist.setText(documentSnapshot.getString("firstName"));
                etFamily.setText(documentSnapshot.getString("familyName"));
                etID.setText(documentSnapshot.getString("idNumber"));
                etEemail.setText(documentSnapshot.getString("email"));
                etPhone.setText(documentSnapshot.getString("phoneNumber"));
            }
        });


        //Save new (updated) values to the database
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=etName.getText().toString().trim();
                String fristName=etFrist.getText().toString().trim();
                String familyName=etFamily.getText().toString().trim();
                String idNumber=etID.getText().toString().trim();
                String email=etEemail.getText().toString().trim();
                String phoneNumber=etPhone.getText().toString().trim();


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


                userName = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("DeliveryVolunteers").document(userName);
                Map<String, Object> user = new HashMap<>();
                user.put("userName", userName);
                user.put("firstName", fristName);
                user.put("familyName", familyName);
                user.put("idNumber", idNumber);
                user.put("email", email);
                user.put("phoneNumber", phoneNumber);


                Toast.makeText(d_UpdateAccount.this, "تم تحديث معلومات الحساب..", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(d_UpdateAccount.this, "تم إرسال رابط إعادة التعيين إلى بريدك الالكتروني", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(d_UpdateAccount.this, "حصل خطأ! لم يتم إرسال رابط إعادة التعيين." + e.getMessage(), Toast.LENGTH_SHORT).show();
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

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(d_UpdateAccount.this, d_ViewAccount.class));// It changes if we add the database

            }
        });


    }}