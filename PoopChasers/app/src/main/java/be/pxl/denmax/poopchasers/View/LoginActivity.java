package be.pxl.denmax.poopchasers.View;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import be.pxl.denmax.poopchasers.Model.ToiletTag;
import be.pxl.denmax.poopchasers.R;
import be.pxl.denmax.poopchasers.Storage.PreferenceStorage;

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

        final Button skip = (Button) findViewById(R.id.skipButton);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipLogin();
            }
        });

    }

    private void login(){
        EditText nameEdit = (EditText) findViewById(R.id.nameEditText);
        EditText passEdit = (EditText) findViewById(R.id.passEditText);

        String username = nameEdit.getText().toString();
        String password = passEdit.getText().toString();

        PreferenceStorage.setUserName(this, username);

        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(intent);
        finish();
    }

    private void skipLogin(){
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
