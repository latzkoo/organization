package hu.latzkoo.organization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import hu.latzkoo.organization.model.Organization;

public class MainActivity extends AppCompatActivity implements OrganizationAdapter.OnItemClickListener {
    private static final int SECRET_KEY = 912;

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private OrganizationAdapter adapter;

    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private CollectionReference departmentsRef = fireStore.collection("organizations");
    private DocumentSnapshot currentItem;
    private Dialog dialogShow;
    private Dialog dialogEdit;

    TextView editIdentifierTextView;
    TextView editNameTextView;
    TextView editAliasTextView;
    TextView editAddressTextView;
    TextView editContactTextView;
    TextView editTelecomTextView;
    TextView editEndpointTextView;
    Spinner editTypeSpinner;
    Switch editActiveSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if (secret_key != SECRET_KEY || user == null) {
            finish();
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        recyclerView.setAdapter(adapter);

        initList();
    }

    private void initList() {
        Query query = departmentsRef.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Organization> options = new FirestoreRecyclerOptions.Builder<Organization>()
                .setQuery(query, Organization.class)
                .build();

        adapter = new OrganizationAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getLayoutPosition());
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            Organization organization = documentSnapshot.toObject(Organization.class);
            if (organization != null) {
                this.currentItem = documentSnapshot;
                showOrganization(organization);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.menuSettings:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuLogout:
                SharedPreference.setLoggedIn(getApplicationContext(), false);

                FirebaseAuth.getInstance().signOut();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createDepartment(View view) {
        Intent intent = new Intent(this, FormActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

    }

    void showOrganization(Organization organization) {
        dialogShow = new Dialog(MainActivity.this);
        dialogShow.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialogShow.setCancelable(true);
        dialogShow.setContentView(R.layout.organization_details);

        int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.90);
        dialogShow.getWindow().setLayout(width, dialogShow.getWindow().getAttributes().height);

        TextView organizationIdentifierTextView = dialogShow.findViewById(R.id.organizationIdentifierTextView);
        TextView organizationNameTextView = dialogShow.findViewById(R.id.organizationNameTextView);
        TextView organizationAliasTextView = dialogShow.findViewById(R.id.organizationAliasTextView);
        TextView organizationAddressTextView = dialogShow.findViewById(R.id.organizationAddressTextView);
        TextView organizationContactTextView = dialogShow.findViewById(R.id.organizationContactTextView);
        TextView organizationTelecomTextView = dialogShow.findViewById(R.id.organizationTelecomTextView);
        TextView organizationEndpointTextView = dialogShow.findViewById(R.id.organizationEndpointTextView);
        TextView organizationTypeTextView = dialogShow.findViewById(R.id.organizationTypeTextView);

        organizationIdentifierTextView.setText(Integer.toString(organization.getIdentifier()));
        organizationNameTextView.setText(organization.getName());
        organizationAliasTextView.setText(organization.getAlias());
        organizationAddressTextView.setText(organization.getAddress());
        organizationContactTextView.setText(organization.getContact());
        organizationTelecomTextView.setText(organization.getTelecom());
        organizationEndpointTextView.setText(organization.getEndpoint());
        organizationTypeTextView.setText(organization.getType());

        if (organization.isActive()) {
            TextView organizationActiveTextView = dialogShow.findViewById(R.id.organizationActiveTextView);
            organizationActiveTextView.setText(R.string.active);
        }

        dialogShow.show();
    }

    public void editOrganization(View view) {
        dialogEdit = new Dialog(MainActivity.this);
        dialogEdit.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialogEdit.setCancelable(true);
        dialogEdit.setContentView(R.layout.organization_edit);

        int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.90);
        dialogEdit.getWindow().setLayout(width, dialogEdit.getWindow().getAttributes().height);

        Organization organization = currentItem.toObject(Organization.class);

        editIdentifierTextView = dialogEdit.findViewById(R.id.editIdentifierTextView);
        editNameTextView = dialogEdit.findViewById(R.id.editNameTextView);
        editAliasTextView = dialogEdit.findViewById(R.id.editAliasTextView);
        editAddressTextView = dialogEdit.findViewById(R.id.editAddressTextView);
        editContactTextView = dialogEdit.findViewById(R.id.editContactTextView);
        editTelecomTextView = dialogEdit.findViewById(R.id.editTelecomTextView);
        editEndpointTextView = dialogEdit.findViewById(R.id.editEndpointTextView);
        editTypeSpinner = dialogEdit.findViewById(R.id.editTypeSpinner);
        editActiveSwitch = dialogEdit.findViewById(R.id.editActiveSwitch);

        editIdentifierTextView.setText(Integer.toString(organization.getIdentifier()));
        editNameTextView.setText(organization.getName());
        editAliasTextView.setText(organization.getAlias());
        editAddressTextView.setText(organization.getAddress());
        editContactTextView.setText(organization.getContact());
        editTelecomTextView.setText(organization.getTelecom());
        editEndpointTextView.setText(organization.getEndpoint());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.departmentTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTypeSpinner.setAdapter(adapter);
        editTypeSpinner.setSelection(organization.getType().equals("Clinic") ? 1 : 0);
        editActiveSwitch.setChecked(organization.isActive());

        dialogShow.hide();
        dialogEdit.show();
    }

    public void closeEditModal(View view) {
        dialogEdit.hide();
    }

    public void updateOrganization(View view) {
        String id = editIdentifierTextView.getText().toString();
        String name = editNameTextView.getText().toString();

        if (id.trim().isEmpty()) {
            if (!editIdentifierTextView.hasFocus()) {
                editIdentifierTextView.requestFocus();
                Helper.showKeyboard(this);
            }
            return;
        }
        else if (name.trim().isEmpty()) {
            if (!editNameTextView.hasFocus()) {
                editNameTextView.requestFocus();
                Helper.showKeyboard(this);
            }
            return;
        }

        int identifier = Integer.parseInt(id);
        String alias = editAliasTextView.getText().toString();
        String address = editAddressTextView.getText().toString();
        String contact = editContactTextView.getText().toString();
        String telecom = editTelecomTextView.getText().toString();
        String endpoint = editEndpointTextView.getText().toString();
        String type = editTypeSpinner.getSelectedItem().toString();
        boolean active = editActiveSwitch.isChecked();

        DocumentReference reference = FirebaseFirestore.getInstance().collection("organizations")
                .document(currentItem.getId());

        reference.update("identifier", identifier);
        reference.update("name", name);
        reference.update("alias", alias);
        reference.update("address", address);
        reference.update("contact", contact);
        reference.update("telecom", telecom);
        reference.update("endpoint", endpoint);
        reference.update("type", type);
        reference.update("active", active);

        Toast.makeText(this, R.string.departmentUpdated, Toast.LENGTH_SHORT).show();

        dialogEdit.hide();
    }
}