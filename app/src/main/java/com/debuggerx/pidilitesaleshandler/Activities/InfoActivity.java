package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.debuggerx.pidilitesaleshandler.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    public void gotoDebuggerX(View view){
        Intent browserIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=DebuggerX%20inc.&hl=en"));
        startActivity(browserIntent);
    }
}
