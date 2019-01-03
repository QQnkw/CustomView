package com.nkw.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nkw.customview.R;

public class VyWoQuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vy_wo_qu);
       /* VyHexagonImageView viewById = findViewById(R.id.iv);
        viewById.setOnClickHexagonListener(new VyHexagonImageView.OnClickHexagonListener() {
            @Override
            public void clickHexagon(View view) {
                Toast.makeText(VyWoQuActivity.this,"被点击了",Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
