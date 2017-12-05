package com.labconco.freezone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/*
TODO: IP Enter Screen, Start animation, UI, Background run, Graphing, Translations, Unit Conversions, add round icons, Settings, Look into making the bakcground task a service, figure out how to run setup
 */

public class MainActivity extends AppCompatActivity {
  private Boolean firstRun = true; //used for setup change to false eventually
    private FreezeDryer freeZone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide(); //hide action bar and handle possible exception
        } catch (NullPointerException e) {
            Log.d("Null Pointer", "onCreate: Encountered Null Pointer in MainActivity");
            e.printStackTrace();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //remove notification bar

        if (firstRun){
            Log.d("Debug", "Running firstRun setup");
            if (freeZone == null) { //if freezezone does not have values create it
                Log.d("Debug", "Setup executing");
                freeZone = new FreezeDryer("12.43.13.50");
                freeZone.startRepeatingTask(); //does the background updating
                firstRun = false;
            }
        }
        setContentView(R.layout.activity_main); //////////////////////View Setter
    }

    public String callGetTime(){
        return freeZone.getTimeRemaining();
    }
}
