package hu.latzkoo.organization;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    private EditText contactEditText;
    private EditText telecomEditText;
    private EditText endpointEditText;
    private Switch activeSwitch;
    private Button saveBtn;

    private NotificationHandler notificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        identifierEditText = findViewById(R.id.identifierEditText);
        nameEditText = findViewById(R.id.nameEditText);
        aliasEditText = findViewById(R.id.aliasEditText);
        endpointEditText = findViewById(R.id.endpointEditText);
        addressEditText = findViewById(R.id.addressEditText);
        contactEditText = findViewById(R.id.contactEditText);
        telecomEditText = findViewById(R.id.telecomEditText);
        activeSwitch = findViewById(R.id.activeSwitch);
        saveBtn = findViewById(R.id.saveBtn);

        notificationHandler = new NotificationHandler(this);
    }

    public void saveOrganization(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        saveBtn.startAnimation(animation);

        String id = identifierEditText.getText().toString();
        String name = nameEditText.getText().toString();

        if (id.trim().isEmpty()) {
            if (!identifierEditText.hasFocus()) {
                identifierEditText.requestFocus();
                Helper.showKeyboard(this);
            }
            return;
        }
        else if (name.trim().isEmpty()) {
            if (!nameEditText.hasFocus()) {
                nameEditText.requestFocus();
                Helper.showKeyboard(this);
            }
            return;
        }

        int identifier = Integer.parseInt(id);
        String alias = aliasEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String contact = contactEditText.getText().toString();
        String telecom = telecomEditText.getText().toString();
        String endpoint = endpointEditText.getText().toString();
        boolean active = activeSwitch.isChecked();

        CollectionReference organizationRef = FirebaseFirestore.getInstance().collection("organizations");

        Organization organization = new Organization();
        organization.setIdentifier(identifier);
        organization.setName(name);
        organization.setAlias(alias);
        organization.setAddress(address);
        organization.setContact(contact);
        organization.setTelecom(telecom);
        organization.setEndpoint(endpoint);
        organization.setActive(active);

        organizationRef.add(organization);
        notificationHandler.send(getString(R.string.notificationNewAdded) + ": " + organization.getName());

        finish();
    }
}