package com.example.emanuele.gino;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shawnlin.numberpicker.NumberPicker;

public class HomeActivity extends Activity {

    private Button prosegui;
    private TextView testohome;
    private String roni_size;
    private int choose;
    private RadioGroup homegroup;
    private RadioButton choosesize3, choosesize4;
    private int a;
    protected View homelayout;
    private ImageView iv;
    private NumberPicker numberPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);
        verifyStoragePermissions(this);
        //libreria glide per far partire la gif
        iv = (ImageView) findViewById(R.id.gifgraph);
        Glide.with(this).load(R.drawable.gifgraph).into(iv);


        homelayout = (View) findViewById(R.id.homelayout);
        // size = (EditText) findViewById(R.id.size);
        numberPicker = (NumberPicker) findViewById(R.id.number_picker);
        testohome = (TextView) findViewById(R.id.testohome);

        //Gradiente di background inutilizzato per via della gif
       /* GradientDrawable Ghomelayout = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFFACDBC9, 0xFFF8B195});
        Ghomelayout.setCornerRadius(0f);
        homelayout.setBackground(Ghomelayout);*/

        //gradiente bottone "Prosegui"
        final GradientDrawable shapeButton = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFFF05053, 0xFFF69D3D});
        shapeButton.setShape(GradientDrawable.OVAL);
        shapeButton.setCornerRadii(new float[]{8, 8, 8, 8, 0, 0, 0, 0});
        shapeButton.setStroke(3, Color.parseColor("#040009"));


        prosegui = (Button) findViewById(R.id.prosegui);
        prosegui.setBackground(shapeButton);

        prosegui.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {

                    a = numberPicker.getValue();

                    if (a > 10)
                        Toast.makeText(getApplicationContext(), "inserisci un numero più piccolo", Toast.LENGTH_LONG).show();
                    else {
                        Intent size_three_intent = new Intent(getApplicationContext(), MainActivity.class); // <----- START "SEARCH" ACTIVITY
                        size_three_intent.putExtra("size", a);
                        startActivityForResult(size_three_intent, 0);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "inserisci un numero", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }


    }
}




