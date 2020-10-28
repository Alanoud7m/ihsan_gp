package com.example.ihsan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class CharityItemsActivity extends AppCompatActivity {
ArrayList<CharityItem> charityItems ,filteredList;
RecyclerView recyclerView;
CharityItemAdapter charityItemAdapter;
FirebaseFirestore fireStore;
SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_items);

        recyclerView= findViewById(R.id.recycler);
        charityItems =new ArrayList<CharityItem>();
        filteredList =new ArrayList<CharityItem>();

        getItems();

        searchView =(SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.isEmpty())
                {filteredList.clear();
                    
                    for(CharityItem ch :charityItems)
                    {
                        if(ch.description.contains(s))
                        {
                            filteredList.add(ch);
                        }
                    }
                    charityItemAdapter.notifyDataSetChanged();
                }else
                {
                    filteredList.clear();
                    getItems();
                }


                return false;
            }
        });
    }

    void getItems()
    {
        fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("CharityItems").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                charityItems.clear();
                filteredList.clear();
                for(DocumentSnapshot snapshot :queryDocumentSnapshots)
                {   CharityItem ch1=snapshot.toObject(CharityItem.class);
                    ch1.id=snapshot.getId();
                    charityItems.add(ch1);
                }
                filteredList.addAll(charityItems) ;
                charityItemAdapter = new CharityItemAdapter(getBaseContext(),filteredList);
                charityItemAdapter.setDeleteButtonListener(new CharityItemAdapter.OnDeleteButtonItemClickListener() {
                    @Override
                    public void onDeleteIsClick(View button, final int position) {

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        fireStore = FirebaseFirestore.getInstance();
                                        fireStore.collection("CharityItems").document(charityItems.get(position).id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                charityItemAdapter.notifyDataSetChanged();
                                                Toast.makeText(getBaseContext(),"تم الحذف بنجاح",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CharityItemsActivity.this);
                        builder.setMessage("هل انت متأكد؟").setPositiveButton("نعم", dialogClickListener)
                                .setNegativeButton("لا", dialogClickListener).show();

                    }
                });
                charityItemAdapter.setUpdateButtonListener(new CharityItemAdapter.OnUpdateButtonItemClickListener() {
                    @Override
                    public void onUpdateIsClick(View button, int position) {
                        Intent intent = new Intent(getBaseContext(),CharityAddItemActivity.class);
                        intent.putExtra("itemID",charityItems.get(position).id);
                        startActivity(intent);
                    }
                });

                recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),2));
                recyclerView.setAdapter(charityItemAdapter);
            }
        });
//        CharityItem  charityItem1= new CharityItem("1","قميص احمر بحالة جيدة","قميص احمر","بنيان","قميص","رجالي","احمر","L","https://cf2.s3.souqcdn.com/item/2017/04/26/22/57/99/58/item_XL_22579958_30979612.jpg");
//        CharityItem  charityItem2= new CharityItem("2","تنورة زرقاء بحالة جيدة","تنورة زرقاء","بنيان","تنورة","نسائي","ازرق","S","https://img.shein.com/images/shein.com/201605/1462766963575654117.webp");
//        charityItems =new ArrayList<CharityItem>();
//        charityItems.add(charityItem1);
//        charityItems.add(charityItem2);
//        charityItems.add(charityItem1);
//        charityItems.add(charityItem2);
    }
}