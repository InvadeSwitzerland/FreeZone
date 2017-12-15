package com.labconco.freezone;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


/*
TODO: Settings, Start animation, UI and gestures, Graphing, Translations, push notifications
 */

public class MainActivity extends AppCompatActivity {
    private String dryerIP = "";
    private FreezeDryer freeZone;
    private Handler updateHandler = new Handler();
    private savedPreferenceManager preferenceManager;
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextOfApplication = getApplicationContext();
        try {
            this.getSupportActionBar().hide(); //hide action bar and handle possible exception
            Log.d("Debug", "actionbar hide success");
        } catch (NullPointerException e) {
            Log.d("Null Pointer", "onCreate: Encountered Null Pointer in MainActivity");
            e.printStackTrace();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //remove notification bar
        setContentView(R.layout.activity_main); // Sets the content view MAKE SURE THIS LINE DOESN'T GET DELETED


        //SETUP
        preferenceManager = new savedPreferenceManager(this.getApplicationContext()); //Get preferences
        //preferenceManager.store("firstRun", "Empty"); //TODO: REMOVE INJECTOR
        if (preferenceManager.pull("firstRun").equalsIgnoreCase("Empty")){
            Log.d("Debug", "Running setup");
            showDialog();
            preferenceManager.store("firstRun", "False"); //store so setup isn't needed anymore
            preferenceManager.store("temperature_unit", "°C"); //store defaults
            preferenceManager.store("pressure_unit", "mBar");
        } else {
            dryerIP = preferenceManager.pull("dryer_ip");
            Log.d("Debug", "Setup not required\nConnected to dryer: " + dryerIP);
            freeZone = new FreezeDryer(dryerIP);
            updateHandler.postDelayed(valueUpdates, 5000); //Delay 5 sec to let values fetch
        }
    }

    protected void updateTemperature(String value){
        String temperatureText = value + preferenceManager.pull("temperature_unit");
        TextView temperatureView = findViewById(R.id.temperature_view);
        temperatureView.setText(temperatureText);
    }

    protected void updateVacuum(String value){
        String vacuumText;

        if (value.equalsIgnoreCase("high") || value.equalsIgnoreCase("low")){
            vacuumText = value;
        } else {
            vacuumText = value + preferenceManager.pull("pressure_unit");
            startTask();
        }

        TextView vacuumView = findViewById(R.id.vacuum_view);
        vacuumView.setText(vacuumText);
    }

    public void showDialog(){
        FragmentManager manager = getFragmentManager();
        ip_fragment frag = new ip_fragment();
        frag.show(manager, "ipDiag");
    }

    public void startTask(){
        freeZone = new FreezeDryer(dryerIP);
        freeZone.startRepeatingTask(); //does the background updating
    }

    //Used to pass context
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    Runnable valueUpdates = new Runnable() {
        @Override
        public void run() {
            updateTemperature(freeZone.getSensorValue("39"));
            updateVacuum(freeZone.getvacuumLevel()); //gets vacuum level which is high or low
            Log.d("Debug", "Updating Display");
            updateHandler.postDelayed(valueUpdates, 60000);
        }
    };
}
