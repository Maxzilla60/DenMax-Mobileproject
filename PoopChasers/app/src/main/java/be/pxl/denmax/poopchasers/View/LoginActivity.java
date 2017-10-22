package be.pxl.denmax.poopchasers.View;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import be.pxl.denmax.poopchasers.Model.ToiletTag;
import be.pxl.denmax.poopchasers.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // set button behavior
        final Button login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login(){
        EditText nameEdit = (EditText) findViewById(R.id.nameEditText);
        EditText passEdit = (EditText) findViewById(R.id.passEditText);

        String username = nameEdit.getText().toString();
        String password = passEdit.getText().toString();

        Toast.makeText(getBaseContext(), username + " - " + password, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(intent);

        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id1")
                .setShortLabel("Add Toilet")
                .setLongLabel("Add a toilet to the map")
                .setIntents(new Intent[]{
                        new Intent(getBaseContext(), LoginActivity.class).setAction(""),
                        new Intent(getBaseContext(), MapsActivity.class).setAction("ADD_TOILET")
                })
                .build();

        shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));

        // TODO: login
    }
}
