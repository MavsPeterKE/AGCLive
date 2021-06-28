package com.room.arcadelive.view.activity;

import android.os.Bundle;

import com.room.arcadelive.R;

import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class HomeActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

}

