package be.pxl.denmax.poopchasers.View.ToiletList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import be.pxl.denmax.poopchasers.Model.ToiletAndDistance;
import be.pxl.denmax.poopchasers.R;
import be.pxl.denmax.poopchasers.View.ToiletDetail.ToiletDetailAdapter;

/**
 * Created by dennis on 10.11.17.
 */

public class ToiletListAdapter extends ArrayAdapter<ToiletListActivity.ToiletInfo> implements View.OnClickListener{

    private ArrayList<ToiletListActivity.ToiletInfo> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView nameTxt;
        TextView distanceTxt;
        TextView meterTxt;
    }

    public ToiletListAdapter(ArrayList<ToiletListActivity.ToiletInfo> data, Context context) {
        super(context, R.layout.comment_row_item, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View view) {
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToiletListActivity.ToiletInfo tuple = getItem(position);
        ToiletListAdapter.ViewHolder viewHolder;

        final View result;

        if( convertView == null ) {
            viewHolder = new ToiletListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.toiletlist_row_item, parent, false);

            viewHolder.nameTxt = convertView.findViewById(R.id.name);
            viewHolder.distanceTxt = convertView.findViewById(R.id.distance);
            viewHolder.meterTxt = convertView.findViewById(R.id.meterTxt);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ToiletListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.nameTxt.setText(tuple.name);

        if(tuple.distance < 1000) {
            viewHolder.distanceTxt.setText(String.valueOf(Math.round(tuple.distance)));
            viewHolder.meterTxt.setText(" meter");
        } else {
            float distance = Math.round(tuple.distance/100) / 10f; // to get one decimal precision
            viewHolder.distanceTxt.setText(String.valueOf(distance));
            viewHolder.meterTxt.setText(" kilometer");
        }

        return result;
    }
}
