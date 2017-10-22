package be.pxl.denmax.poopchasers.View.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import be.pxl.denmax.poopchasers.Model.Toilet;
import be.pxl.denmax.poopchasers.Model.ToiletTag;
import be.pxl.denmax.poopchasers.Model.ToiletTags;
import be.pxl.denmax.poopchasers.R;
import be.pxl.denmax.poopchasers.Repo.ToiletFactory;
import be.pxl.denmax.poopchasers.Repo.ToiletRepository;

/**
 * Created by dennis on 22.10.17.
 */

public class AddToiletDialog extends DialogFragment {
    private AddToiletListener listener;
    private LatLng latLng;
    private ArrayList<ToiletTag> toiletTags;

    public interface AddToiletListener {
        public void onPositiveAddToiletClick(Toilet toilet);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            latLng = (LatLng) getArguments().getParcelable("latLng");
        } else {
            throw new NullPointerException("No latLng found.");
        }

        toiletTags = new ArrayList<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (AddToiletListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement AddToiletListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_add_toilet, null);

        builder.setView(view)
                .setPositiveButton(R.string.addToilet, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onPositiveAddToiletClick(getToilet(view));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        view.findViewById(R.id.manButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTagClick(ToiletTag.MENS, view);
            }
        });
        view.findViewById(R.id.womanButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTagClick(ToiletTag.WOMENS, view);
            }
        });
        view.findViewById(R.id.unisexButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTagClick(ToiletTag.UNISEX, view);
            }
        });
        view.findViewById(R.id.babyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTagClick(ToiletTag.BABIES, view);
            }
        });
        view.findViewById(R.id.wheelchairButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTagClick(ToiletTag.ACCESSIBLE, view);
            }
        });
        view.findViewById(R.id.freeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTagClick(ToiletTag.FREE, view);
            }
        });

        return builder.create();
    }

    private void onTagClick(ToiletTag tag, View view){
        if(toiletTags.contains(tag)){
            toiletTags.remove(tag);
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        } else {
            toiletTags.add(tag);
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        }
    }

    private Toilet getToilet(View view) {
        String name = ((EditText) view.findViewById(R.id.toiletNameText)).getText().toString();
        ToiletTags tags = new ToiletTags(toiletTags.toArray(new ToiletTag[0]));

        Toilet toilet = new ToiletFactory().setLatlng(latLng).setName(name).setTags(tags).getToilet();

        return toilet;
    }

}
