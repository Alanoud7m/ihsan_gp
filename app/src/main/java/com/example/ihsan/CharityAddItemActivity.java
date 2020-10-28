package com.example.ihsan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

public class CharityAddItemActivity extends AppCompatActivity {

    EditText itemNumber,itemDesc,itemChName,itemType,itemGender,itemColor,itemSize,itemCount;
    FirebaseFirestore fStore;
    FirebaseStorage storage;
    StorageReference sRef;
    FirebaseAuth fAuth;
    Uri imgUri;
    String charityName;
    CharityItem ch;
    String itemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_add_item);
        itemNumber=(EditText)findViewById(R.id.itemNumber);
        itemCount=(EditText)findViewById(R.id.itemCount);
        itemDesc=(EditText)findViewById(R.id.itemDesc);
        itemChName=(EditText)findViewById(R.id.itemChName);
        itemType=(EditText)findViewById(R.id.itemType);
        itemGender=(EditText)findViewById(R.id.itemGender);
        itemColor=(EditText)findViewById(R.id.itemColor);
        itemSize=(EditText)findViewById(R.id.itemSize);
        Button cancelBtn = (Button)findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(CharityAddItemActivity.this);
                builder.setMessage("هل انت متأكد؟").setPositiveButton("نعم", dialogClickListener)
                        .setNegativeButton("لا", dialogClickListener).show();
            }
        });
        Button addItemBtn =findViewById(R.id.addItemBtn);
        Button addImgBtn =findViewById(R.id.addImgBtn);
        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),0);


            }
        });
        Intent intent = getIntent();
        if(intent.getExtras()!=null)
        {
            itemId=intent.getExtras().getString("itemID");

            TextView titleTxt= (TextView)findViewById(R.id.titleTxt);
            titleTxt.setText("تعديل بيانات القطعة");
            addItemBtn.setText("حفظ التعديلات");
             ch=new CharityItem();
            fStore=FirebaseFirestore.getInstance();
            fStore.collection("CharityItems").document(itemId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    ch= documentSnapshot.toObject(CharityItem.class);
                    itemNumber.setText(ch.number);
                    itemCount.setText(ch.count);
                    itemDesc.setText(ch.description);
                    itemChName.setText(ch.charity);
                    itemType.setText(ch.type);
                    itemGender.setText(ch.gender);
                    itemColor.setText(ch.color);
                    itemSize.setText(ch.size);
                }
            });
            addItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(imgUri!=null)
                    {
                        String randomKey = UUID.randomUUID().toString();
                        storage=FirebaseStorage.getInstance();
                        sRef =storage.getReference().child("CharityItems/"+randomKey);
                        sRef.putFile(imgUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {


                                                ch.number=itemNumber.getText().toString();
                                                ch.count=itemCount.getText().toString();
                                                ch.description=itemDesc.getText().toString();
                                                ch.charity=itemChName.getText().toString();
                                                ch.type=itemType.getText().toString();
                                                ch.gender=itemGender.getText().toString();
                                                ch.color=itemColor.getText().toString();
                                                ch.size=itemSize.getText().toString();
                                                ch.image=uri.toString();
                                                fStore=FirebaseFirestore.getInstance();
                                                fStore.collection("CharityItems").document(itemId).set(ch).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getBaseContext(),"تم تعديل القطعة بنجاح",Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(getBaseContext(),NavaigationCharity.class));
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                    }else
                    {
                        ch.number=itemNumber.getText().toString();
                        ch.count=itemCount.getText().toString();
                        ch.description=itemDesc.getText().toString();
                        ch.charity=itemChName.getText().toString();
                        ch.type=itemType.getText().toString();
                        ch.gender=itemGender.getText().toString();
                        ch.color=itemColor.getText().toString();
                        ch.size=itemSize.getText().toString();
                        fStore=FirebaseFirestore.getInstance();
                        fStore.collection("CharityItems").document(itemId).set(ch).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getBaseContext(),"تم تعديل القطعة بنجاح",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getBaseContext(),NavaigationCharity.class));
                            }
                        });
                    }


                }
            });
        }else
        {
            fAuth=FirebaseAuth.getInstance();
            FirebaseUser user= fAuth.getCurrentUser();
            String userId=user.getUid();
            charityName="";
            fStore=FirebaseFirestore.getInstance();
            fStore.collection("Charities").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    charityName= documentSnapshot.get("charityName").toString();

                    itemChName.setText(charityName);
                }
            });



            addItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String randomKey = UUID.randomUUID().toString();
                    storage=FirebaseStorage.getInstance();
                    sRef =storage.getReference().child("CharityItems/"+randomKey);
                    if(itemNumber.getText().toString().isEmpty()||
                            itemCount.getText().toString().isEmpty()||
                            itemDesc.getText().toString().isEmpty()||
                            itemChName.getText().toString().isEmpty()||
                            itemType.getText().toString().isEmpty()||
                            itemGender.getText().toString().isEmpty()||
                            itemColor.getText().toString().isEmpty()||
                            itemSize.getText().toString().isEmpty())
                    {
                        Toast.makeText(getBaseContext(),"الرجاء ملء جميع الحقول",Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(imgUri==null)
                    {
                        Toast.makeText(getBaseContext(),"الرجاء اختيار صورة القطعة",Toast.LENGTH_LONG).show();
                        return;
                    }
                    sRef.putFile(imgUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            CharityItem charityItem = new CharityItem();
                                            charityItem.number=itemNumber.getText().toString();
                                            charityItem.count=itemCount.getText().toString();
                                            charityItem.description=itemDesc.getText().toString();
                                            charityItem.charity=itemChName.getText().toString();
                                            charityItem.type=itemType.getText().toString();
                                            charityItem.gender=itemGender.getText().toString();
                                            charityItem.color=itemColor.getText().toString();
                                            charityItem.size=itemSize.getText().toString();
                                            charityItem.image=uri.toString();
                                            fStore=FirebaseFirestore.getInstance();
                                            fStore.collection("CharityItems").add(charityItem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(getBaseContext(),"تم اضافة القطعة بنجاح",Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(getBaseContext(),NavaigationCharity.class));
                                                }
                                            });
                                        }
                                    });
                                }
                            });

                }
            });

        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0 && resultCode==RESULT_OK && data !=null)
        {
            imgUri=data.getData();
        }
    }
}