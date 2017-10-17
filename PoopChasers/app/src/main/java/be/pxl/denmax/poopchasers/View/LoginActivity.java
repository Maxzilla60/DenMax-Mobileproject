package be.pxl.denmax.poopchasers.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        // TODO
    }
}
