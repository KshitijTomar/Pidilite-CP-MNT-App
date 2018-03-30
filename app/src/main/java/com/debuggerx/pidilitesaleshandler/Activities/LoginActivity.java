package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.debuggerx.pidilitesaleshandler.R;


public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    //elements declared
    EditText etname;
    EditText etid;
    EditText etcontact;
    EditText etemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //objects declaration
        etname = (EditText) findViewById(R.id.name_login_text);
        etid=(EditText) findViewById(R.id.id_login_text);
        etcontact=(EditText) findViewById(R.id.contact_login_text);
        etemail=(EditText) findViewById(R.id.email_login_text);


    }


    // this is the function that will be called if the button is pressed
    public void registerInfo(View view) {
        //check for contact number(should ahve 10 number digits)
        if ((etcontact.getText().toString().length() == 10) && (etcontact.getText().toString().matches("\\d+"))) {
            //concats all edit text in one to check if comaa is present
            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String CommaDetector;
            CommaDetector=etname.getText().toString()+etid.getText().toString()+etcontact.getText().toString()+etemail.getText().toString();
            if(CommaDetector.contains(",")){                //checks for a comma in the edittexts
                Toast.makeText(this, "Do not use Comma(,)", Toast.LENGTH_LONG).show();
            }else{
                //variables are stored in phone memory
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username", etname.getText().toString());
                editor.putString("userid", etid.getText().toString());
                editor.putString("usercontact", etcontact.getText().toString());
                editor.putString("useremail", etemail.getText().toString());
                editor.apply();

                //A tost is displayed, that the data is saved
                Toast.makeText(LoginActivity.this, "Saved!", Toast.LENGTH_LONG).show();

                //Set the value of isFirstRun to false, so that Login doesn't run again
                getSharedPreferences("launchInfo", Context.MODE_PRIVATE).edit().putString("isfirstrun", "false").apply();

                //Intent leads to MainActivity
                Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                startActivity(intent);
                finish();
            }
        }else{
            Toast.makeText(this, "Enter a valid Contact number", Toast.LENGTH_SHORT).show();
            etcontact.setText("");
        }
    }



}
