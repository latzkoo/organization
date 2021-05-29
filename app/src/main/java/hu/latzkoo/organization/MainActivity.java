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
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
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

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference departmentsRef = firestore.collection("organizations");

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
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.organization_details);

        int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.90);
        dialog.getWindow().setLayout(width, dialog.getWindow().getAttributes().height);

        TextView organizationNameTextView = dialog.findViewById(R.id.organizationNameTextView);
        TextView organizationAddressTextView = dialog.findViewById(R.id.organizationAddressTextView);

        organizationNameTextView.setText(organization.getName());
        organizationAddressTextView.setText(organization.getAddress());

        dialog.show();
    }
}