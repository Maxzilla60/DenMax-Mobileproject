package be.pxl.denmax.poopchasers.View.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import be.pxl.denmax.poopchasers.Model.ToiletComment;
import be.pxl.denmax.poopchasers.R;
import be.pxl.denmax.poopchasers.Storage.PreferenceStorage;

/**
 * Created by dennis on 22.10.17.
 */

public class CommentDialog extends DialogFragment{
    private CommentListener listener;
    private int rating;
    ArrayList<ImageView> starImages;

    public interface CommentListener {
        void onPositiveCommentClick(ToiletComment toilet);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        starImages = new ArrayList<>();
        rating = 3;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (CommentListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement CommentListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_add_comment, null);

        builder.setView(view)
                .setPositiveButton(R.string.addComment, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onPositiveCommentClick(getComment(view));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        ImageView star1 = view.findViewById(R.id.star1);
        ImageView star2 = view.findViewById(R.id.star2);
        ImageView star3 = view.findViewById(R.id.star3);
        ImageView star4 = view.findViewById(R.id.star4);
        ImageView star5 = view.findViewById(R.id.star5);


        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStarClick(1, view);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStarClick(2, view);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStarClick(3, view);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStarClick(4, view);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStarClick(5, view);
            }
        });

        starImages.add(star1);
        starImages.add(star2);
        starImages.add(star3);
        starImages.add(star4);
        starImages.add(star5);


        return builder.create();
    }

    private void onStarClick(int rating, View view) {
        this.rating = rating;

        for(int i = 1; i<=rating; i++){
            starImages.get(i-1).setImageResource(R.drawable.starfull);
        }

        for(int i = 5; i>rating; i--){
            starImages.get(i-1).setImageResource(R.drawable.starempty);
        }
    }

    private ToiletComment getComment(View view) {
        String content = ((TextView) view.findViewById(R.id.commentText)).getText().toString();
        String username = PreferenceStorage.getUsername(getContext());

        return new ToiletComment(content, username, rating);
    }
}
