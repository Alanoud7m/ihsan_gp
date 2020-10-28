package com.example.ihsan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginAdmin extends AppCompatActivity {
    EditText etUserName,etPassword;
    Button bLogin;
    TextView tvForgot;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        etUserName=findViewById(R.id.editTextTextPersonName);
        etPassword=findViewById(R.id.password);
        bLogin=findViewById(R.id.beneficiary);
        tvForgot=findViewById(R.id.forgot);

        fAuth = FirebaseAuth.getInstance();

        bLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userN=etUserName.getText().toString().trim();
                String Passw=etPassword.getText().toString().trim();

                String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

                if (TextUtils.isEmpty(userN)){
                    etUserName.setError("الرجاء إدخال البريد الالكتروني. ");
                    return;
                }//email

                if (!(userN.matches(checkEmail))){
                    etUserName.setError("الرجاء إدخال البريد الالكتروني بشكل صحيح. ");
                    return;
                }//email

                if (TextUtils.isEmpty(Passw)){
                    etPassword.setError("الرجاء إدخال كلمة المرور. ");
                    return;
                }//end if
                if (Passw.length()<6){
                    etPassword.setError("يجب أن تتكون كلمة المرور من 6 أحرف أو أكثر");
                    return;
                }


                //User Authentication

                fAuth.signInWithEmailAndPassword(userN,Passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(loginAdmin.this, "تم تسجيل الدخول بنجاح.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(loginAdmin.this, NavaigationAdmin.class));
                        } else {
                            Toast.makeText(loginAdmin.this, "حصل خطأ! اسم المستخدم أو كلمة المرور خاطئة"/* + task.getException().getMessage()*/, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        //Reset Password

        tvForgot.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(loginAdmin.this, "تم إرسال رابط إعادة التعيين إلى بريدك الالكتروني", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(loginAdmin.this, "حصل خطأ! لم يتم إرسال رابط إعادة التعيين." + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    }}

