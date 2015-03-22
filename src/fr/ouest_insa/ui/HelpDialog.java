package fr.ouest_insa.ui;

import fr.ouest_insa.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class HelpDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ouest INSA");
        builder.setMessage(R.string.help_dialog_text);
        builder.setPositiveButton("Ok", null);
        return builder.create();
    }
}