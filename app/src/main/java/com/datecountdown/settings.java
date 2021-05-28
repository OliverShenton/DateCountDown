package com.datecountdown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settingscontainer, new settingsFragment())
                .commit();

    }

    public void ClickBackSettings(View view){
        Intent intent2 = new Intent(settings.this,MainActivity.class);
        startActivity(intent2);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(settings.this, MainActivity.class));
        finish();

    }
}