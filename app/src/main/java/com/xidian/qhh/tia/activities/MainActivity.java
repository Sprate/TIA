package com.xidian.qhh.tia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xidian.qhh.tia.R;
import com.xidian.qhh.tia.tia.Parameters;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button entry_data=(Button)findViewById(R.id.entry_button);
        Button test_data=(Button)findViewById(R.id.test_button);
        Button exit_button=(Button) findViewById(R.id.Exit_button);

        entry_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DemoActivity.class);
                startActivity(intent);
                Parameters.setState(Parameters.entry_state);
            }
        });
        test_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DemoActivity.class);
                startActivity(intent);
                Parameters.setState(Parameters.test_state);
            }
        });
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
