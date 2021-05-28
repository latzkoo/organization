package hu.latzkoo.organization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 912;

    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;
    private Button loginBtnButton;
    private Button createAccountBtnButton;
    private RelativeLayout loaderLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginBtnButton = findViewById(R.id.loginBtn);
        createAccountBtnButton = findViewById(R.id.createAccountBtn);
        loaderLogin = findViewById(R.id.loaderLogin);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void login(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        loginBtnButton.startAnimation(animation);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.trim().isEmpty()) {
            if (!emailEditText.hasFocus()) {
                emailEditText.requestFocus();
                Helper.showKeyboard(this);
            }
        }
        else if (password.trim().isEmpty()) {
            passwordEditText.requestFocus();
        }
        else {
            loaderLogin.setVisibility(View.VISIBLE);
            loginBtnButton.setEnabled(false);
            createAccountBtnButton.setEnabled(false);

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                loaderLogin.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    SharedPreference.setLoggedIn(getApplicationContext(), true);

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("SECRET_KEY", SECRET_KEY);

                    startActivity(intent);
                    LoginActivity.this.finish();
                }
                else {
                    loginBtnButton.setEnabled(true);
                    createAccountBtnButton.setEnabled(true);

                    Toast.makeText(this, R.string.wrongEmailOrPassword, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void createAccount(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        createAccountBtnButton.startAnimation(animation);

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}