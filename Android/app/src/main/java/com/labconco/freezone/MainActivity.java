package com.labconco.freezone;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/*
TODO: IP Enter Screen, Start animation, UI and gestures, Graphing, Translations, Unit Conversions, add round icons, Settings, figure out how to run setup, push notifications
 */

public class MainActivity extends AppCompatActivity {
    private Boolean firstRun = true; //used for setup change to false eventually
    private String dryerIP = "12.43.13.50";
    private FreezeDryer freeZone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide(); //hide action bar and handle possible exception
            Log.d("Debug", "actionbar hide success");
        } catch (NullPointerException e) {
            Log.d("Null Pointer", "onCreate: Encountered Null Pointer in MainActivity");
            e.printStackTrace();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //remove notification bar

        if (firstRun){
            Log.d("Debug", "Running firstRun setup");
            if (freeZone == null) { //if freeze zone does not have values create it
                Log.d("Debug", "Setup executing");
                freeZone = new FreezeDryer(dryerIP);
                freeZone.startRepeatingTask(); //does the background updating
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {Log.d("Debug", "Main delay has been interrupted.");} //Delaying to let the background task run real quick
                firstRun = false;
            }
        }

        System.out.println(freeZone.getCSVList());
        showDialog();
        setContentView(R.layout.activity_main); // Sets the content view MAKE SURE THIS LINE DOESN'T GET DELETED
    }

    public void showDialog(){
        FragmentManager manager = getFragmentManager();
        ip_fragment frag = new ip_fragment();
        frag.show(manager, "ipDiag");
    }

    public void setDryerIP(String ip){
        this.dryerIP = ip;
    }
}
