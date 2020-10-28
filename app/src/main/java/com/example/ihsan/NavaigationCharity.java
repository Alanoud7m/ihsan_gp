package com.example.ihsan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class NavaigationCharity extends AppCompatActivity  {
    MenuItem miLogout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navaigation_charity);

        Button addItemBtn= findViewById(R.id.addItemBtn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),CharityAddItemActivity.class));
            }
        });

        Button viewItemsBtn= findViewById(R.id.viewItemsBtn);
        viewItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),CharityItemsActivity.class));
            }
        });

        miLogout=findViewById(R.id.logout);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




//Navigation coding start
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle= new ActionBarDrawerToggle(NavaigationCharity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView=findViewById(R.id.navigation_view);
        View navView=navigationView.inflateHeaderView(R.layout.navigation_header_c);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelected(menuItem);
                return false;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void UserMenuSelected(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case  R.id.home:
                break;

            case  R.id.account:
                startActivity(new Intent(NavaigationCharity.this, c_ViewAccount.class));

                break;

            case  R.id.check:
                break;

            case  R.id.call:
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("تواصل معنا ... ");
                alertDialogBuilder.setMessage("ihsangpksu@gmail.com");
                // alertDialogBuilder.setCancelable(false);


                alertDialogBuilder.setNegativeButton("أغلاق", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(NavaigationCharity.this,"إلغاء الأمر",Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alertDialog=alertDialogBuilder.create();
                alertDialog.show();;
                break;

            case  R.id.logout:
                AlertDialog.Builder alertDialogBuilder1=new AlertDialog.Builder(this);
                alertDialogBuilder1.setTitle("رسالة !");
                alertDialogBuilder1.setMessage("هل أنت متأكد أنك تريد تسجيل الخروج ؟");
                alertDialogBuilder1.setCancelable(false);

                alertDialogBuilder1.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

                alertDialogBuilder1.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(NavaigationCharity.this,"إلغاء الأمر",Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alertDialog1=alertDialogBuilder1.create();
                alertDialog1.show();


        }


    }
}
