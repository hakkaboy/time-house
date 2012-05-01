package com.hakka.pluto.example;

import android.app.ListActivity;
import android.os.Bundle;

public class PlutoActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}