package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.debuggerx.pidilitesaleshandler.R;

public class Login2Activity extends AppCompatActivity {

    //elements declared
    EditText etname;
    EditText etid;
    EditText etcontact;
    EditText etemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        etname = (EditText) findViewById(R.id.name_login_text);
        etid = (EditText) findViewById(R.id.id_login_text);
        etcontact = (EditText) findViewById(R.id.contact_login_text);
        etemail = (EditText) findViewById(R.id.email_login_text);

        //Calling for shared preferences
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String usrname = sharedPref.getString("username", "Name");
        String usrid = sharedPref.getString("userid", "ID");
        String usrcontact = sharedPref.getString("usercontact", "Contact");
        String usremail = sharedPref.getString("useremail", "E-mail");

        //Setting txs as per user input in LoginActivity
        etname.setText(usrname);
        etid.setText(usrid);
        etcontact.setText(usrcontact);
        etemail.setText(usremail);
    }

    // this is the function that will be called if the button is pressed
    public void registerInfo(View view) {

        if ((etcontact.getText().toString().length() == 10) && (etcontact.getText().toString().matches("\\d+"))) {
            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String CommaDetector;
            CommaDetector=etname.getText().toString()+etid.getText().toString()+etcontact.getText().toString()+etemail.getText().toString();
            if(CommaDetector.contains(",")){
                Toast.makeText(this, "Do not use Comma(,)", Toast.LENGTH_LONG).show();
            }else{
                //here the variables are stored in phone memory
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username", etname.getText().toString());
                editor.putString("userid", etid.getText().toString());
                editor.putString("usercontact", etcontact.getText().toString());
                editor.putString("useremail", etemail.getText().toString());
                editor.apply();

                //A toast is displayed, that the data is saved
                Toast.makeText(Login2Activity.this, "Changes Saved", Toast.LENGTH_LONG).show();

                finish();
            }
        }else{
            Toast.makeText(this, "Enter a valid Contact number", Toast.LENGTH_SHORT).show();
            etcontact.setText("");
        }
    }
}
