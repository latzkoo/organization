
package hu.latzkoo.organization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import hu.latzkoo.organization.model.User;

public class RegisterActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 912;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmationEditText;
    private Button registerBtnButton;
    private RelativeLayout loaderRegister;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmationEditText = findViewById(R.id.passwordConfirmationEditText);
        registerBtnButton = findViewById(R.id.registerBtn);
        loaderRegister = findViewById(R.id.loaderRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public void register(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        registerBtnButton.startAnimation(animation);

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirmation = passwordConfirmationEditText.getText().toString();

        if (name.equals("")) {
            if (!nameEditText.hasFocus()) {
                nameEditText.requestFocus();
                Helper.showKeyboard(this);
            }
        }
        else if (email.equals("")) {
            if (!emailEditText.hasFocus()) {
                emailEditText.requestFocus();
                Helper.showKeyboard(this);
            }
        }
        else if (password.equals("")) {
            if (!passwordEditText.hasFocus()) {
                passwordEditText.requestFocus();
                Helper.showKeyboard(this);
            }
        }
        else if (passwordConfirmation.equals("")) {
            passwordConfirmationEditText.requestFocus();
        }
        else if (password.length() < 6 || passwordConfirmation.length() < 6) {
            Toast.makeText(this, R.string.passwordMinLength, Toast.LENGTH_LONG).show();
        }
        else if (!passwordConfirmation.equals(password)) {
            passwordConfirmationEditText.requestFocus();
            Toast.makeText(this, R.string.passwordsNotMatch, Toast.LENGTH_LONG).show();
        }
        else {
            loaderRegister.setVisibility(View.VISIBLE);
            registerBtnButton.setEnabled(false);

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    User user = new User(name, email);
                    String userId = firebaseAuth.getCurrentUser().getUid();

                    firestore.collection("users")
                            .document(userId)
                            .set(user)
                            .addOnCompleteListener(userTask -> {
                                loaderRegister.setVisibility(View.GONE);

                                if (userTask.isSuccessful()) {
                                    Intent intent = new Intent(this, MainActivity.class);
                                    intent.putExtra("SECRET_KEY", SECRET_KEY);

                                    startActivity(intent);
                                    RegisterActivity.this.finish();
                                }
                                else {
                                    registerBtnButton.setEnabled(true);
                                    Toast.makeText(RegisterActivity.this, userTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else {
                    loaderRegister.setVisibility(View.GONE);
                    registerBtnButton.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}