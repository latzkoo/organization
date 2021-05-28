package hu.latzkoo.organization;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import hu.latzkoo.organization.model.Organization;

public class OrganizationAdapter extends FirestoreRecyclerAdapter<Organization, OrganizationAdapter.OrganizationHolder> {

    public OrganizationAdapter(@NonNull FirestoreRecyclerOptions<Organization> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrganizationAdapter.OrganizationHolder holder, int position, @NonNull Organization model) {
        holder.nameTextView.setText(model.getName());
        holder.addressTextView.setText(model.getAddress());
    }

    @NonNull
    @Override
    public OrganizationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new OrganizationHolder(view);
    }

    class OrganizationHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView addressTextView;

        public OrganizationHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.itemNameTextView);
            addressTextView = itemView.findViewById(R.id.itemAddressTextView);
        }
    }

}
