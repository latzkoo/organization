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
import android.widget.Switch;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import hu.latzkoo.organization.model.Organization;

public class FormActivity extends AppCompatActivity {

    private EditText identifierEditText;
    private EditText nameEditText;
    private EditText aliasEditText;
    private EditText addressEditText;
    private EditText endpointEditText;
    private Switch activeSwitch;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        identifierEditText = findViewById(R.id.identifierEditText);
        nameEditText = findViewById(R.id.nameEditText);
        aliasEditText = findViewById(R.id.aliasEditText);
        endpointEditText = findViewById(R.id.itemEndpointEditText);
        addressEditText = findViewById(R.id.addressEditText);
        activeSwitch = findViewById(R.id.activeSwitch);
        saveBtn = findViewById(R.id.saveBtn);
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

    public void saveOrganization(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        saveBtn.startAnimation(animation);

        int identifier = Integer.parseInt(identifierEditText.getText().toString());
        String name = nameEditText.getText().toString();
        String alias = aliasEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String endpoint = endpointEditText.getText().toString();

        if (identifier == 0) {
            if (!identifierEditText.hasFocus()) {
                identifierEditText.requestFocus();
                Helper.showKeyboard(this);
            }
        }
        else if (name.trim().isEmpty()) {
            if (!nameEditText.hasFocus()) {
                nameEditText.requestFocus();
                Helper.showKeyboard(this);
            }
        }

        CollectionReference organizationRef = FirebaseFirestore.getInstance()
                .collection("organizations");

        Organization organization = new Organization();
        organization.setIdentifier(identifier);
        organization.setName(name);
        organization.setAlias(alias);
        organization.setAddress(address);
        organization.setEndpoint(endpoint);

        organizationRef.add(organization);
        finish();
    }
}