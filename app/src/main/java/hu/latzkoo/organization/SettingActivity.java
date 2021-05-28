package hu.latzkoo.organization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingActivity extends AppCompatActivity {

    private EditText newPasswordEditText;
    private EditText newPasswordConfirmationEditText;
    private Button updatePasswordButton;
    private RelativeLayout loaderSetting;

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        newPasswordConfirmationEditText = findViewById(R.id.newPasswordConfirmationEditText);
        updatePasswordButton = findViewById(R.id.updatePasswordBtn);
        loaderSetting = findViewById(R.id.loaderSetting);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_back, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuBack:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updatePassword(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        updatePasswordButton.startAnimation(animation);

        String newPassword = newPasswordEditText.getText().toString();
        String newPasswordConfirmation = newPasswordConfirmationEditText.getText().toString();

        if (newPassword.equals("")) {
            if (!newPasswordEditText.hasFocus()) {
                newPasswordEditText.requestFocus();
                Helper.showKeyboard(this);
            }
        }
        else if (newPasswordConfirmation.equals("")) {
            newPasswordConfirmationEditText.requestFocus();
        }
        else if (newPassword.length() < 6 || newPasswordConfirmation.length() < 6) {
            Toast.makeText(this, R.string.passwordMinLength, Toast.LENGTH_LONG).show();
        }
        else if (!newPasswordConfirmation.equals(newPassword)) {
            newPasswordConfirmationEditText.requestFocus();
            Toast.makeText(this, R.string.passwordsNotMatch, Toast.LENGTH_LONG).show();
        }
        else {
            loaderSetting.setVisibility(View.VISIBLE);
            updatePasswordButton.setEnabled(false);

            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                loaderSetting.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    newPasswordEditText.setText("");
                    newPasswordConfirmationEditText.setText("");
                    newPasswordConfirmationEditText.clearFocus();

                    MessageDialog dialog = new MessageDialog();
                    dialog.show(getSupportFragmentManager(), "ALERT_DIALOG");
                }
                else {
                    Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

                updatePasswordButton.setEnabled(true);
            });
        }
    }
}