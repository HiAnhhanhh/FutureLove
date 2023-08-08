package com.thinkdiffai.futurelove.view.fragment.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment {

    private DeleteDialogListener listener;

    public void setListener(DeleteDialogListener listener) {
        this.listener = listener;
    }

    public interface DeleteDialogListener {
        void onConfirmDelete();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Comment")
                .setMessage("Are you sure to delete this comment?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onConfirmDelete();
                        }
                    }
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }
}
