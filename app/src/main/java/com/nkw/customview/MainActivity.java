package com.nkw.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nkw.customview.view.TabLayoutVY;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayoutVY tabLayoutVY = findViewById(R.id.tablayoutVy);
        tabLayoutVY.addSelectorListener(new TabLayoutVY.TabClickedListener() {
            @Override
            public void onTabClicked(int clickedResID) {
                Log.d("NKW",clickedResID+"");
            }
        });
    }
}
