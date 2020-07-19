package com.calltaxi.calltaxi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.calltaxi.calltaxi.Database.PreferenceHelper;
import com.calltaxi.calltaxi.R;

public class DashboardActivity extends AppCompatActivity {

    CardView driveBook, cabBook, profile, logout;
    TextView userName;
    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        preferenceHelper = new PreferenceHelper(this);

        driveBook = findViewById(R.id.cv_b1);
        cabBook = findViewById(R.id.cv_b2);
        profile = findViewById(R.id.cv_b3);
        logout = findViewById(R.id.cv_b4);
        userName = findViewById(R.id.tv_dash_userName);

        userName.setText(preferenceHelper.getUsername());

        //Driver Book Page Calling Method...
        driveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent driverBook = new Intent(getApplicationContext(),DriverActivity.class);
                startActivity(driverBook);
            }
        });


        //Logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceHelper.putIsLogin(false);
                Intent intent = new Intent(DashboardActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                DashboardActivity.this.finish();
            }
        });
    }
}
