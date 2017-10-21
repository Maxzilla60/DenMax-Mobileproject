package be.pxl.denmax.poopchasers.View.ToiletDetail;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import be.pxl.denmax.poopchasers.Exceptions.ToiletLocationIDNotFoundException;
import be.pxl.denmax.poopchasers.Model.Toilet;
import be.pxl.denmax.poopchasers.Model.ToiletComment;
import be.pxl.denmax.poopchasers.Model.ToiletTag;
import be.pxl.denmax.poopchasers.R;
import be.pxl.denmax.poopchasers.Repo.ToiletRepository;

public class ToiletDetailActivity extends AppCompatActivity {

    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_detail);

        int id = getIntent().getIntExtra("id", 0);

        try {
            Toilet toilet = ToiletRepository.getToiletLocationByID(id);
            setToiletName(toilet);
            setLocationName(toilet);
            setStars(toilet);
            setTags(toilet);
            setComments(toilet.getComments());


        } catch (ToiletLocationIDNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setComments(ArrayList<ToiletComment> comments) {
        adapter = new CustomAdapter(comments, getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.comments);

        listView.setAdapter(adapter);
    }

    private void setToiletName(Toilet toilet){
        TextView toiletName = (TextView) findViewById(R.id.toiletNameTextView);
        toiletName.setText(toilet.getName());
    }

    private void setLocationName(Toilet toilet){
        try {
            TextView locationName = (TextView) findViewById(R.id.locationNameTextView);

            // Create a geocoder to get the address associated with the coordinates
            Geocoder gc = new Geocoder(this, Locale.getDefault());
            LatLng latLng = toilet.getLatLng();
            List<Address> addressList = gc.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if(addressList.size() > 0) {
                StringBuilder sb = new StringBuilder();

                String addrLine;
                int i = 0;
                while ((addrLine = addressList.get(0).getAddressLine(i)) != null){ // Build the complete address
                    sb.append(addrLine.toString());
                    i++;
                }
                locationName.setText(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setStars(Toilet toilet) {
        int rating = toilet.getRating();

        // create basic layout for every starimage
        int starWidth = (int) getResources().getDimension(R.dimen.star_width);
        int starHeigth = (int) getResources().getDimension(R.dimen.star_heigth);
        int starMargin = (int) getResources().getDimension(R.dimen.star_margin);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(starWidth, starHeigth);
        layout.setMargins(starMargin, 0, starMargin, 0);

        LinearLayout starLayout = (LinearLayout) findViewById(R.id.starLayout);

        // add full stars
        for(int i = 0; i<rating; i++) {
            ImageView starImage = new ImageView(this);
            starImage.setLayoutParams(layout);
            starImage.setImageResource(R.drawable.starfull);

            starLayout.addView(starImage);
        }
        // add empty stars
        for(int i = 5; i>rating; i--) {
            ImageView starImage = new ImageView(this);
            starImage.setLayoutParams(layout);
            starImage.setImageResource(R.drawable.starempty);

            starLayout.addView(starImage);
        }
    }

    private void setTags(Toilet toilet){
        List<ToiletTag> toiletTags = toilet.getTagsAsList();

        if(toiletTags.contains(ToiletTag.ACCESSIBLE)){
            findViewById(R.id.wheelchairImageView).setVisibility(View.VISIBLE);
        }
        if(toiletTags.contains(ToiletTag.BABIES)){
            findViewById(R.id.babyImageView).setVisibility(View.VISIBLE);
        }
        if(toiletTags.contains(ToiletTag.FREE)){
            findViewById(R.id.freeImageView).setVisibility(View.VISIBLE);
        }
        if(toiletTags.contains(ToiletTag.MENS)){
            findViewById(R.id.manImageView).setVisibility(View.VISIBLE);
        }
        if(toiletTags.contains(ToiletTag.UNISEX)){
            findViewById(R.id.unisexImageView).setVisibility(View.VISIBLE);
        }
        if(toiletTags.contains(ToiletTag.WOMENS)){
            findViewById(R.id.womanImageView).setVisibility(View.VISIBLE);
        }
    }
}
