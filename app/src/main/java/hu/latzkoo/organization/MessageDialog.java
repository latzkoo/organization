package hu.latzkoo.organization;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class MessageDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Information")
                .setMessage(R.string.passwordUpdatedSuccessfully)
                .setPositiveButton("OK", (dialog, which) -> {});

        return builder.create();
    }
}
