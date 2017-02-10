package com.example.viewsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.viewsample.widget.DragArea;
import com.example.viewsample.widget.DraggerView;

public class MainActivity extends AppCompatActivity {

    private DraggerView mDragBar;
    private DragArea mDragArea;

    private TextView mTextInfo;

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextInfo = (TextView) findViewById(R.id.txt_info);

        mDragBar = (DraggerView) findViewById(R.id.drag_view);
        mDragBar.setDragListener(new DraggerView.DragListener() {
            @Override
            public void onDragged(int startProgress, int endProgress) {
                Log.d(TAG, "startProgress:" + startProgress +" endProgress:" + endProgress);
            }

            @Override
            public void onDragStarted() {

            }
        });

        mDragArea = (DragArea) findViewById(R.id.drag_area);
        mDragArea.setDragAreaListener(new DragArea.DragAreaListener() {
            @Override
            public void onDragProgress(int progress) {
                mTextInfo.setText("current progress:" + progress);
            }
        });

    }
}
