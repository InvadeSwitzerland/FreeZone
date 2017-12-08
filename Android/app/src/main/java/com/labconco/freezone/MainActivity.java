package com.labconco.freezone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/*
TODO: IP Enter Screen, Start animation, UI, Background run, Graphing, Translations, Unit Conversions, add round icons, Settings, Look into making the bakcground task a service, figure out how to run setup
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
                firstRun = false;
            }
        }
        CSVGetter csv = new CSVGetter("BATCH003", dryerIP, this);
        csv.getterStart();

        setContentView(R.layout.activity_main); // Sets the content view MAKE SURE THIS LINE DOESN'T GET DELETED
    }



    public String callGetTime(){
        return freeZone.getTimeRemaining();
    }

}
