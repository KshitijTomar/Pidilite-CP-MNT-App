package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.debuggerx.pidilitesaleshandler.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class SellHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_history);
        TextView SellHistoryText = (TextView) findViewById(R.id.SellHistoryText);
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String usrname= sharedPref.getString("username","Name");

        SellHistoryText.setText(Readfromsd("/PidiliteSH/Database" , "DB-"+usrname+".txt"));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sell_history, menu);
        return true;// Return true to display menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.Share_email) {
            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String usrname= sharedPref.getString("username","Name");
            ShareViaEmail("/PidiliteSH/Database" , "DB-"+usrname+".csv", usrname);
        }
        if (id == R.id.Settings) {
            Intent user_intent = new Intent(SellHistoryActivity.this,SettingsActivity.class);
            startActivity(user_intent);
            return true;
        }
        if (id == R.id.Clear_History) {
            TextView SellHistoryText = (TextView) findViewById(R.id.SellHistoryText);
            if(SellHistoryText.getText().toString().matches("No file Exists. Save some data first.")){
                Toast.makeText(this, "No file Exists.\nSave some data first.", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                Clear_Sell_History();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void Clear_Sell_History() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SellHistoryActivity.this);
        builder.setMessage("Do you want to Clear the Sell History?\n\n(Clearing History will delete the existing CSV file)");
        builder.setCancelable(true);

        //Positive Button For Interface
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String usrname= sharedPref.getString("username","Name");
                boolean D1 = DeleteFromSD("","DB-"+usrname+".csv");
                boolean D2 = DeleteFromSD("","DB-"+usrname+".txt");
                if (!D1||!D2){
                    Toast.makeText(SellHistoryActivity.this, "Error: History cannot be cleared.", Toast.LENGTH_SHORT).show();
                }
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
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }

    private boolean DeleteFromSD(String Folder_name, String file_name) {
        File file =new File(Environment.getExternalStorageDirectory(),file_name);
        try {
            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false ;
        }
    }

    private void ShareViaEmail(String Folder_name, String file_name, String usrname) {
        File file =new File(Environment.getExternalStorageDirectory(),file_name);
        if (file.exists()){
            try {
                File Root= Environment.getExternalStorageDirectory();

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                String message="This data of PidiliteSalesHandler is sent by "+usrname+".";
                intent.putExtra(Intent.EXTRA_SUBJECT, "Pidilte CP-MNT : "+usrname);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse( "file://"+Root+"/"+file_name));
                intent.putExtra(Intent.EXTRA_TEXT, message);
                String email_id_to_sent_to = getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("Email", "");
                intent.setData(Uri.parse("mailto:" + email_id_to_sent_to));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            } catch(Exception e)  {
                System.out.println("is exception raises during sending mail"+e);
            }
        }else{
            Toast.makeText(this, "No file Exists.\nSave some data first.", Toast.LENGTH_SHORT).show();
        }
    }


    public String Readfromsd(String Folder_name, String file_name){
        File file =new File(Environment.getExternalStorageDirectory(),file_name);
        String Message;
        try {
            FileInputStream fileinputstream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileinputstream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((Message=bufferReader.readLine())!=null){
                stringBuffer.append(Message + "\n");
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "No file Exists. Save some data first." ;
        } catch (IOException e) {
            e.printStackTrace();
            return "No file Exists. Save some data first." ;
        }
    }


    public void SaveToSD (String data_to_be_saved, String Folder_name, String file_name){
        String state;
        state= Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            File Root= Environment.getExternalStorageDirectory();
            File file =new File(Root,file_name);
            String Message=data_to_be_saved;
            try{
                FileWriter fileOutputStream= new FileWriter(file);
                fileOutputStream.write(Message);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Data not saved (FileNotFoundException)", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Data not saved (IOException)", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Sd card not Found", Toast.LENGTH_SHORT).show();
        }
    }


}
