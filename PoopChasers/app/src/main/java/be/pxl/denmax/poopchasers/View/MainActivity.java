package be.pxl.denmax.poopchasers.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import be.pxl.denmax.poopchasers.R;
import be.pxl.denmax.poopchasers.Storage.PreferenceStorage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent;
        if(PreferenceStorage.getUsername(this) == null) {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), MapsActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
