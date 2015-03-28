package fr.ouest_insa.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import fr.ouest_insa.R;

public class HelpDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	TextView msg = new TextView(getActivity());
    	msg.setText(R.string.help_dialog_text);
    	msg.setGravity(Gravity.CENTER);
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ouest INSA");
        //builder.setMessage(R.string.help_dialog_text);
        builder.setView(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", null);
        return builder.create();
    }
}