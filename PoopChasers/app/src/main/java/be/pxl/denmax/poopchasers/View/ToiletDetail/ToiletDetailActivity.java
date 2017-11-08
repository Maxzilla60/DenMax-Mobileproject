package be.pxl.denmax.poopchasers.View.ToiletDetail;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
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
import be.pxl.denmax.poopchasers.View.Dialog.CommentDialog;


public class ToiletDetailActivity extends AppCompatActivity implements
        CommentDialog.CommentListener,
        ToiletRepository.ToiletUpdateListener,
        ToiletRepository.ToiletCommentUpdateListener{
    private Toilet toilet;
    private ArrayList<ImageView> starImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_detail);

        int id = getIntent().getIntExtra("id", 0);
        starImages = new ArrayList<>();

        starImages.add((ImageView) findViewById(R.id.star1));
        starImages.add((ImageView) findViewById(R.id.star2));
        starImages.add((ImageView) findViewById(R.id.star3));
        starImages.add((ImageView) findViewById(R.id.star4));
        starImages.add((ImageView) findViewById(R.id.star5));


        ToiletRepository.getToiletLocationByID(this, Volley.newRequestQueue(this), id);
        ToiletRepository.getToiletCommentsById(this, Volley.newRequestQueue(this), id);
    }

    private void setAddComment() {
        findViewById(R.id.addCommentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDialog dialog = new CommentDialog();
                dialog.show(getFragmentManager(), "");
            }
        });
    }

    private void setDirections() {
        final Toilet t = toilet;

        findViewById(R.id.directionsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng = t.getLatLng();

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latLng.latitude+","+latLng.longitude+"&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                // Check if intent can be resolved
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(getBaseContext(), "Could not find Google Maps application", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setComments(ArrayList<ToiletComment> comments) {
        CustomAdapter adapter = new CustomAdapter(comments, getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.comments);

        listView.setAdapter(adapter);
    }

    private void setToiletName(){
        TextView toiletName = (TextView) findViewById(R.id.toiletNameTextView);
        toiletName.setText(toilet.getName());
    }

    private void setLocationName(){
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
                    sb.append(addrLine);
                    i++;
                }
                locationName.setText(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setStars() {
        int rating = toilet.getRating();

        for(int i = 1; i<=rating; i++){
            starImages.get(i-1).setImageResource(R.drawable.starfull);
        }

        for(int i = 5; i>rating; i--){
            starImages.get(i-1).setImageResource(R.drawable.starempty);
        }
    }

    private void setTags(){
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

    @Override
    public void onPositiveCommentClick(ToiletComment comment) {
        try {
            ToiletRepository.addCommentToToiletLocation(Volley.newRequestQueue(this), comment, toilet.getId());
            ToiletRepository.getToiletCommentsById(this, Volley.newRequestQueue(this), toilet.getId());
            setStars();
        } catch (ToiletLocationIDNotFoundException e) {
            Toast.makeText(getBaseContext(), "Could not add comment", Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }

    @Override
    public void onToiletUpdate(List<Toilet> toilets) {
        Log.i("test", "inOnToiletUpdate");
        if(toilets.size() > 0){
            Log.i("test", "size > 0");
            this.toilet = toilets.get(0);
            setToiletName();
            setLocationName();
            setStars();
            setTags();
            setDirections();
        } else {
            this.finish();
        }
    }

    @Override
    public void onToiletCommentUpdate(ArrayList<ToiletComment> toiletComments) {
        Log.i("test", "inOnToiletCommentUpdate");
        setAddComment();
        setComments(toiletComments);
    }
}
