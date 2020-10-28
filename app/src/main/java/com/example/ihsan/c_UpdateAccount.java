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

public class c_UpdateAccount extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button bCancel,bSave, bNewPassword;
    EditText etName,etCharityName,etCharityNumber,etEemail,etPhone,etCharityAddress;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userName;
    String charityName;
    String charityNumber;
    String email;
    String phoneNumber;
    String charityAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__update_account);

        bCancel = findViewById(R.id.cancel);
        bSave = findViewById(R.id.save);
        bNewPassword = findViewById(R.id.newPassword2);

        etName = findViewById(R.id.userName);
        etCharityName = findViewById(R.id.cahrity);
        etCharityNumber = findViewById(R.id.number);
        etEemail = findViewById(R.id.eemail);
        etPhone = findViewById(R.id.phone);
        etCharityAddress = findViewById(R.id.address);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userName = fAuth.getCurrentUser().getUid();

        //Get Database values
        DocumentReference documentReference = fStore.collection("Charities").document(userName);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                etName.setText(documentSnapshot.getString("email"));
                etCharityName.setText(documentSnapshot.getString("charityName"));
                etCharityNumber.setText(documentSnapshot.getString("charityNumber"));
                etCharityAddress.setText(documentSnapshot.getString("charityAddress"));
                etEemail.setText(documentSnapshot.getString("email"));
                etPhone.setText(documentSnapshot.getString("phoneNumber"));
            }
        });

        //Save new (updated) values to the database
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 userName=etName.getText().toString().trim();
                 charityName=etCharityName.getText().toString().trim();
                 charityNumber=etCharityNumber.getText().toString().trim();
                 email=etEemail.getText().toString().trim();
                 phoneNumber=etPhone.getText().toString().trim();
                 charityAddress=etCharityAddress.getText().toString().trim();



                String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";


                if (userName.isEmpty()||userName.length()<2){
                    etName.setError("الرجاء إدخال اسم المستخدم.");
                    return; }//userName

                if (TextUtils.isEmpty(charityName)){
                    etCharityName.setError("الرجاء إدخال اسم المؤسسة الخيرية.");
                    return; }//charityName

                if (TextUtils.isEmpty(charityNumber)){
                    etCharityNumber.setError("الرجاء إدخال رقم ترخيص الجمعية.");
                    return; }//charityName

                if (TextUtils.isEmpty(charityAddress)){
                    etCharityAddress.setError("الرجاء إدخال عنوان المؤسسة الخيرية.");
                    return; }//CharityAddress

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
                DocumentReference documentReference = fStore.collection("Charities").document(userName);
                Map<String, Object> user = new HashMap<>();
                user.put("userName", userName);
                user.put("charityName", charityName);
                user.put("charityNumber", charityNumber);
                user.put("charityAddress", charityAddress);
                user.put("email", email);
                user.put("phoneNumber", phoneNumber);


                Toast.makeText(c_UpdateAccount.this, "تم تحديث معلومات الحساب..", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(c_UpdateAccount.this, "تم إرسال رابط إعادة التعيين إلى بريدك الالكتروني", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(c_UpdateAccount.this, "حصل خطأ! لم يتم إرسال رابط إعادة التعيين." + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(c_UpdateAccount.this, c_ViewAccount.class));// It changes if we add the database

            }
        });


    }}