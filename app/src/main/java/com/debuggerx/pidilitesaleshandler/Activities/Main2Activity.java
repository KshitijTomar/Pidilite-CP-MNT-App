package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.debuggerx.pidilitesaleshandler.R;
import com.debuggerx.pidilitesaleshandler.Working.DestroyingCache;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String val="Yellow";
        getSharedPreferences("ThemeSelect", Context.MODE_PRIVATE).edit().putString("ThemeCode", val).apply();
        ThemeChanger();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }

    private void ThemeChanger() {
        String ThemeCode = getSharedPreferences("ThemeSelect", Context.MODE_PRIVATE).getString("ThemeCode","Yellow");
        switch(ThemeCode){
            case "Blue":
                this.setTheme(R.style.AppTheme);
                break;
            case "Yellow":
                this.setTheme(R.style.YellowTheme);
                break;
            default:
                this.setTheme(R.style.YellowTheme);
        }

    }

    //declaration for all OnClick functions
    public void sellItemsActi(View view){
        Intent user_intent = new Intent(Main2Activity.this,SearchDistributorActivity.class);
        startActivity(user_intent);
    }

    public void sellHistoryActi(View view){
        Intent user_intent = new Intent(Main2Activity.this,SellHistoryActivity.class);
        startActivity(user_intent);
    }

    public void userDetailsActi(View view){
        Intent user_intent = new Intent(Main2Activity.this,UserDetailsActivity.class);
        startActivity(user_intent);
    }

    public void InfoActi(View view){
        Intent user_intent = new Intent(Main2Activity.this,InfoActivity.class);
        startActivity(user_intent);
    }

    public void SettingsActi(View view){
        Intent user_intent = new Intent(Main2Activity.this,SettingsActivity.class);
        startActivity(user_intent);
    }




    //Alert to Close On Back Pressed
    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setMessage("Do you want to quit?");
        builder.setCancelable(true);

        //Positive Button For Interface
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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




    //Code To Clear Cache Memory
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DestroyingCache destroyingCache = new DestroyingCache();
    }


}
