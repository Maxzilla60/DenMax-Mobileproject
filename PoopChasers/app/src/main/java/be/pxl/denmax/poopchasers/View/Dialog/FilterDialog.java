package be.pxl.denmax.poopchasers.View.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;

import java.util.ArrayList;

import be.pxl.denmax.poopchasers.Model.ToiletTag;
import be.pxl.denmax.poopchasers.R;

/**
 * Created by dennis on 21.10.17.
 */

public class FilterDialog extends DialogFragment {
    ArrayList<ToiletTag> filterTags;
    FilterDialogListener listener;

    public interface FilterDialogListener {
        public void onPositiveFilterDialogClick(ArrayList<ToiletTag> filterTags);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            filterTags = (ArrayList<ToiletTag>) getArguments().getSerializable("filter");
        } else {
            filterTags = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (FilterDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement FilterDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_filter, null);

        builder.setView(view)
                .setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onPositiveFilterDialogClick(getToiletTags(view));
                    }
                })
                .setNegativeButton(R.string.cancel, null);

        if(filterTags != null){
            initFilters(view);
        }

        return builder.create();
    }

    private ArrayList<ToiletTag> getToiletTags(View view){
        ArrayList<ToiletTag> toiletTags = new ArrayList<>();

        if( ((Switch) view.findViewById(R.id.manSwitch)).isChecked() ){
            toiletTags.add(ToiletTag.MENS);
        }
        if( ((Switch) view.findViewById(R.id.womanSwitch)).isChecked() ){
            toiletTags.add(ToiletTag.WOMENS);
        }
        if( ((Switch) view.findViewById(R.id.unisexSwitch)).isChecked() ){
            toiletTags.add(ToiletTag.UNISEX);
        }
        if( ((Switch) view.findViewById(R.id.babySwitch)).isChecked() ){
            toiletTags.add(ToiletTag.BABIES);
        }
        if( ((Switch) view.findViewById(R.id.wheelchairSwitch)).isChecked() ){
            toiletTags.add(ToiletTag.ACCESSIBLE);
        }
        if( ((Switch) view.findViewById(R.id.freeSwitch)).isChecked() ){
            toiletTags.add(ToiletTag.FREE);
        }


        return toiletTags;
    }

    private void initFilters(View view) {
        for (ToiletTag tag: filterTags) {
            switch (tag){
                case MENS:
                    ((Switch) view.findViewById(R.id.manSwitch)).setChecked(true);
                    break;
                case UNISEX:
                    ((Switch) view.findViewById(R.id.unisexSwitch)).setChecked(true);
                    break;
                case WOMENS:
                    ((Switch) view.findViewById(R.id.womanSwitch)).setChecked(true);
                    break;
                case ACCESSIBLE:
                    ((Switch) view.findViewById(R.id.wheelchairSwitch)).setChecked(true);
                    break;
                case BABIES:
                    ((Switch) view.findViewById(R.id.babySwitch)).setChecked(true);
                    break;
                case FREE:
                    ((Switch) view.findViewById(R.id.freeSwitch)).setChecked(true);
                    break;

            }
        }
    }
}
