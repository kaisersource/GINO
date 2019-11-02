package com.example.emanuele.gino;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.emanuele.gino.R;

public class Splash extends Activity {

        private static int SPLASH_TIME_OUT = 4000;
        protected ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        pb=(ProgressBar)findViewById(R.id.pb);
        pb.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        //intent per lo splash screen, mediante lambda espressione
        new Handler().postDelayed(new Runnable(){
        @Override
        public void run(){
            Intent homeIntent = new Intent(Splash.this,HomeActivity.class);
             startActivity(homeIntent);
            finish();
        }
    },SPLASH_TIME_OUT);

    }




}
