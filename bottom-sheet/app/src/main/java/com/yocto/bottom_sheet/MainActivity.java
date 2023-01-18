package com.yocto.bottom_sheet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.image_button_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                demo();
            }
        });
    }

    public void demo() {
        DemoBottomDialogFragment demoBottomDialogFragment = DemoBottomDialogFragment.newInstance();

        demoBottomDialogFragment.show(getSupportFragmentManager(), "demoBottomDialogFragment");
    }
}