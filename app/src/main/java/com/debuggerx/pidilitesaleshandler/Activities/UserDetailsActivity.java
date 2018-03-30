package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.debuggerx.pidilitesaleshandler.R;

public class UserDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        //Creating views
        TextView name_tt =(TextView) findViewById(R.id.name_txt);
        TextView id_tt =(TextView) findViewById(R.id.id_txt);
        TextView contact_tt =(TextView) findViewById(R.id.contact_txt);
        TextView email_tt =(TextView) findViewById(R.id.email_txt);

        //Calling for shared preferences
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String usrname= sharedPref.getString("username","Name");
        String usrid= sharedPref.getString("userid","ID");
        String usrcontact= sharedPref.getString("usercontact","Contact");
        String usremail= sharedPref.getString("useremail","E-mail");

        //Setting txs as per user input in LoginActivity
        name_tt.setText(usrname);
        id_tt.setText(usrid);
        contact_tt.setText(usrcontact);
        email_tt.setText(usremail);
    }

    public void toLoginActivity(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailsActivity.this);
        builder.setMessage("Do you want to edit User Details?");
        builder.setCancelable(true);

        //Positive Button For Interface
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intenttologin = new Intent(UserDetailsActivity.this, Login2Activity.class);
                startActivity(intenttologin);
                finish();
            }
        });

        //Negative button for interface
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
