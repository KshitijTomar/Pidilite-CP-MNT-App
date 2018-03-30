package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.debuggerx.pidilitesaleshandler.R;

import static com.debuggerx.pidilitesaleshandler.R.id.NewEditText;

public class SettingsActivity extends AppCompatActivity {

    TextView emailtextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        emailtextview= (TextView) findViewById(R.id.EmailidtextView);
        emailtextview.setText(getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("Email", "Email-ID"));
    }

    public void DialogBoxEmail(View v){
        LayoutInflater inflater = LayoutInflater.from(SettingsActivity.this);
        View subView = inflater.inflate(R.layout.layout_edittext,  null);
        final EditText etNewEmail_ID=(EditText) subView.findViewById(NewEditText);
        String mailid = getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("Email", "Email-ID");
        if(mailid.equals("Email-ID")) {
            etNewEmail_ID.setHint("Ëmail-ID");
        }else{
            etNewEmail_ID.setText(mailid);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Email-ID");
        builder.setMessage("Send the saved CSV file to the Email-ID");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (etNewEmail_ID.getText().toString().isEmpty()) {       //checks if empty
                    dialog.cancel();
                    Toast.makeText(SettingsActivity.this, "Nothing Saved", Toast.LENGTH_SHORT).show();
                } else {
                    getSharedPreferences("Settings", Context.MODE_PRIVATE).edit().putString("Email", etNewEmail_ID.getText().toString()).apply();
                    emailtextview= (TextView) findViewById(R.id.EmailidtextView);
                    emailtextview.setText(getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("Email", "Email-ID"));
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        //alertDialog.show();
    }


    public void IntentToPermissionSettings(View v){
        Toast.makeText(this, "Select \"Permissions\" and\nEnable Storage", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public void FeedbackEmail(View v){
        LayoutInflater inflater = LayoutInflater.from(SettingsActivity.this);
        View subView = inflater.inflate(R.layout.layout_edittext,  null);
        final EditText feedback=(EditText) subView.findViewById(NewEditText);
        feedback.setHint("Enter you Feedback");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Feedback");
        builder.setMessage("Send your Feedback via Email");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (feedback.getText().toString().isEmpty()) {       //checks if empty
                    dialog.cancel();
                    Toast.makeText(SettingsActivity.this, "No Feedback Sent", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    String usrname= sharedPref.getString("username","Name");
                    ShareFeedbackViaEmail(feedback.getText().toString() , usrname);
                    Toast.makeText(SettingsActivity.this, "Please press the Send button", Toast.LENGTH_SHORT).show();
                    Toast.makeText(SettingsActivity.this, "Thank you for your Feedback :)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        //alertDialog.show();
    }

    private void ShareFeedbackViaEmail(String Feedback, String usrname) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            String message=Feedback;
            intent.putExtra(Intent.EXTRA_SUBJECT, "Pidilte CP-MNT Feedback: "+usrname);
            intent.putExtra(Intent.EXTRA_TEXT, "This is the FeedBack sent by : " + usrname + ".\n\n\n" + message);
            String email_id_to_sent_to = "kshtjtomar40@gmail.com";
            intent.setData(Uri.parse("mailto:" + email_id_to_sent_to));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        } catch(Exception e)  {
            System.out.println("Exception raises during sending mail"+e);
        }
    }

    public void Update(View v){
        Intent browserIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/6C3JeH"));
        startActivity(browserIntent);
    }

}
