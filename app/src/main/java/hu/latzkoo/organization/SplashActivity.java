package hu.latzkoo.organization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 912;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            Intent intent;

            if (!SharedPreference.getLoggedStatus(getApplicationContext())) {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("SECRET_KEY", SECRET_KEY);
            }

            startActivity(intent);
            SplashActivity.this.finish();
        }, 1500);
    }

}
