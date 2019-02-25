package com.example.marce.projeto7udacity.Activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.marce.projeto7udacity.Adapter.AdapterFragments;
import com.example.marce.projeto7udacity.Adapter.BookCursorAdapter;

import com.example.marce.projeto7udacity.ConnectionDB.ConnectionDbHelper;
import com.example.marce.projeto7udacity.R;

public class TelaPrincipalActivity extends AppCompatActivity{

    ConnectionDbHelper dbHelper = new ConnectionDbHelper(this);
    BookCursorAdapter mCursorAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);


        TabLayout tabLayoutMain = findViewById(R.id.tabLayoutMain);
        ViewPager viewPagerMain = findViewById(R.id.viewPagerMain);


        viewPagerMain.setAdapter(new AdapterFragments(getSupportFragmentManager(),getResources().getStringArray(R.array.pageNames)));

        tabLayoutMain.setupWithViewPager(viewPagerMain);

        tabLayoutMain.setElevation(10);

        tabLayoutMain.getTabAt(0).setIcon(R.drawable.tab1_icon);
        tabLayoutMain.getTabAt(1).setIcon(R.drawable.sale);
        tabLayoutMain.getTabAt(2).setIcon(R.drawable.tab1_icon);


    }
}
