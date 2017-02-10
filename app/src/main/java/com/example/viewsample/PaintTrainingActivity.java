package com.example.viewsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.viewsample.widget.Wheel;

public class PaintTrainingActivity extends AppCompatActivity {

    private Wheel mWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_exersize);
        mWheel = (Wheel) findViewById(R.id.wheel);
    }

    public void translation(View view){
        mWheel.setTranslationX(mWheel.getTranslationX() + 10);
        mWheel.setTranslationZ(mWheel.getTranslationZ() + 10);
    }
}
