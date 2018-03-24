package com.vinicius.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vinicius.whatsapp.R;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference referenceFireBase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //referenceFireBase.child("pontos").setValue(100);

    }
}
