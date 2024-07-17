package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.OverScroller;
import android.widget.Scroller;

import androidx.annotation.Nullable;

public class ScrollActivity extends Activity {
    private static final String TAG = "ScrollActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MyButton button = findViewById(R.id.button);
        button.computeScroll();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.startScroll();

            }
        });


    }


}
