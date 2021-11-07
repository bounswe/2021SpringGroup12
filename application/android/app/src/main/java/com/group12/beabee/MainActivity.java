package com.group12.beabee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.group12.beabee.network.BeABeeService;
import com.group12.beabee.network.ServiceAPI;

public class MainActivity extends AppCompatActivity {

    private ServiceAPI serviceAPI;
    private TextView tvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMain = findViewById(R.id.tv_main);
        serviceAPI = BeABeeService.serviceAPI;

    }
}