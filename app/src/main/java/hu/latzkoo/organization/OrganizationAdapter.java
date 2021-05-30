package hu.latzkoo.organization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import hu.latzkoo.organization.model.Organization;

public class OrganizationAdapter extends FirestoreRecyclerAdapter<Organization, OrganizationAdapter.OrganizationHolder> {

    private Context context;
    private OnItemClickListener listener;
    private NotificationHandler notificationHandler;

    public OrganizationAdapter(@NonNull FirestoreRecyclerOptions<Organization> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrganizationAdapter.OrganizationHolder holder,
                                    int position, @NonNull Organization model) {
        holder.nameTextView.setText(model.getName());
        holder.addressTextView.setText(model.getAddress());
        holder.identifierTextView.setText("ID: " + model.getIdentifier());
    }

    @NonNull
    @Override
    public OrganizationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        notificationHandler = new NotificationHandler(context);

        View view = LayoutInflater.from(context).inflate(R.layout.organization_item, parent, false);
        return new OrganizationHolder(view);
    }

    public void deleteItem(int position) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        Organization organization = documentSnapshot.toObject(Organization.class);
        getSnapshots().getSnapshot(position).getReference().delete();
        notificationHandler.send(context.getString(R.string.notificationDeleted) + ": " + organization.getName());
    }

    class OrganizationHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView addressTextView;
        private TextView identifierTextView;

        public OrganizationHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.itemNameTextView);
            addressTextView = itemView.findViewById(R.id.itemAddressTextView);
            identifierTextView = itemView.findViewById(R.id.itemIdentifierTextView);

            itemView.setOnClickListener(v -> {
                int position = getLayoutPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
