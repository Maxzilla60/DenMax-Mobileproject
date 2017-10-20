package be.pxl.denmax.poopchasers.View.ToiletDetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.pxl.denmax.poopchasers.Model.ToiletComment;
import be.pxl.denmax.poopchasers.R;

/**
 * Created by dennis on 20.10.17.
 */

public class CustomAdapter extends ArrayAdapter<ToiletComment> implements View.OnClickListener {

    private ArrayList<ToiletComment> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView nameTxt;
        ImageView profilepic;
        TextView commentTxt;
        TextView morelessTxt;
        List<ImageView> starImages;
        boolean expanded;
    }

    public CustomAdapter(ArrayList<ToiletComment> data, Context context) {
        super(context, R.layout.comment_row_item, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.moreless:
                moreless(view);
                break;
        }
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToiletComment toiletComment = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if( convertView == null ) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.comment_row_item, parent, false);

            viewHolder.nameTxt = (TextView) convertView.findViewById(R.id.name);
            viewHolder.profilepic = (ImageView) convertView.findViewById(R.id.profile_picture);
            viewHolder.starImages = new ArrayList<>();
            viewHolder.starImages.add((ImageView) convertView.findViewById(R.id.star1));
            viewHolder.starImages.add((ImageView) convertView.findViewById(R.id.star2));
            viewHolder.starImages.add((ImageView) convertView.findViewById(R.id.star3));
            viewHolder.starImages.add((ImageView) convertView.findViewById(R.id.star4));
            viewHolder.starImages.add((ImageView) convertView.findViewById(R.id.star5));
            viewHolder.commentTxt = (TextView) convertView.findViewById(R.id.comment);
            viewHolder.morelessTxt = (TextView) convertView.findViewById(R.id.moreless);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.nameTxt.setText(toiletComment.getUsername());
        setStars(viewHolder, toiletComment);
        setComment(viewHolder, toiletComment);
        viewHolder.morelessTxt.setOnClickListener(this);
        viewHolder.morelessTxt.setTag(viewHolder);

        return convertView;
    }

    private void setComment(ViewHolder viewHolder, ToiletComment toiletComment) {
        String content = toiletComment.getContent();
        viewHolder.commentTxt.setText(content);
        viewHolder.expanded = false;

        if (content.length() <= 200) {
            viewHolder.morelessTxt.setVisibility(View.GONE);
        } else {
            viewHolder.commentTxt.setMaxLines(4);
            viewHolder.morelessTxt.setText("Show more");
            viewHolder.morelessTxt.setVisibility(View.VISIBLE);
        }
    }

    private void setStars(ViewHolder viewHolder, ToiletComment tc){
        for (int i = 0; i < tc.getRating(); i++) {
            viewHolder.starImages.get(i).setVisibility(View.VISIBLE);
        }
        for (int i = 5; i > tc.getRating(); i--) {
            viewHolder.starImages.get(i-1).setVisibility(View.INVISIBLE);
        }
    }

    private void moreless(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder.expanded){
            viewHolder.morelessTxt.setText("Show more");
            viewHolder.commentTxt.setMaxLines(4);
            viewHolder.expanded = false;
        } else {
            viewHolder.morelessTxt.setText("Show less");
            viewHolder.commentTxt.setMaxLines(Integer.MAX_VALUE);
            viewHolder.expanded = true;
        }
    }


}
