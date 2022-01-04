package com.group12.beabee.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.group12.beabee.R;

import java.io.InputStream;

import butterknife.OnClick;

public class StringDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_display);
        TextView textView = (TextView) findViewById(R.id.tv_text);
        findViewById(R.id.iv_cancel).setOnClickListener(v -> OnCancelClicked());
        Bundle bundle = getIntent().getExtras();
        int value = -1;
        if (bundle != null)
            value = bundle.getInt("key");

        int resId;
        if (value == 0)
            resId = R.raw.terms;
        else {
            resId = R.raw.privacypolicy;
        }
        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(resId);
            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            textView.setText(new String(b));
        } catch (Exception e) {
            textView.setText("Error: can't show terms.");
        }
    }

    public void OnCancelClicked() {
        finish();
    }
}