package com.labconco.freezone;

//Connection Imports
import android.os.AsyncTask;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Scanner;
import java.net.MalformedURLException;

//JSON imports
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//Misc imports
import android.os.Handler;
import android.view.View;
import android.util.Log;
import android.widget.*;
/**
 * Created by James Holdcroft
 * TODO:
 */
public class FreezeDryer {
    private String inClassGlobalIP = ""; //get String to make global
    private String URLContent; //This is grabbed directly from the url
    private JsonParser jsonParser = new JsonParser();
    private final static int INTERVAL = 1000 * 60; //Background task interval
    private URL apiURL;
    private Handler backgroundPullHandler = new Handler();
    private DataStore freezeDatabase;

    Runnable backgroundPullTask = new Runnable() {
        @Override
        public void run() {
            Log.d("Debug", "Runnable has executed");
            backgroundJSONPull JSONPull = new backgroundJSONPull();
            JSONPull.execute();
            backgroundPullHandler.postDelayed(backgroundPullTask, INTERVAL);
        }
    };

    public void startRepeatingTask(){
        backgroundPullTask.run();
    }

    void stopRepeatingTask(){
        backgroundPullHandler.removeCallbacks(backgroundPullTask);
    }

    public FreezeDryer(String ip) {
        inClassGlobalIP = ip;
        freezeDatabase = new DataStore(null, null, null, 1);
    }


    protected void connect(){
        backgroundJSONPull JSONPull = new backgroundJSONPull();
        JSONPull.execute();
    }
    protected String getIP(){
        return inClassGlobalIP;
    }
    protected String getTime(){
        return getValue("time");
    }
    protected String getDate(){
        return getValue("date");
    }
    protected String getvacuumLevel(){
        return getValue("vacuumLevel");
    }
    protected String getTimeRemaining(){
        return getValue("timeRemaining");
    }
    protected String getSensors(){
        return getValue("Sensors");
    }

    //Matches the sensor identity with an array index to make it easier to find the sensors name
    protected String identifySensor(int sensor){
        String[] sensors = {"unknown", "Lexsol Temp Sensor", "Shelf Temp Probe", "Shelf Sample Probe", "Collector Temp Sensor", "Lexsol Temp Sensor", "Temp Probe 1", "Temp Probe 2", "Temp Probe 3", "Shelf 1 Temp Probe", "Shelf 2 Temp Probe", "Shelf 3 Temp Probe", "Shelf 4 Temp Probe", "Shelf 5 Temp Probe", "Shelf 1 Sample Probe", "Shelf 2 Sample Probe", "Shelf 3 Sample Probe", "Shelf 4 Sample Probe", "Shelf 5 Sample Probe", "Shelf 1 Temp Probe", "Shelf 2 Temp Probe", "Shelf 3 Temp Probe", "Shelf 4 Temp Probe", "Shelf 5 Temp Probe", "Shelf 1 Sample Probe", "Shelf 2 Sample Probe", "Shelf 3 Sample Probe", "Shelf 4 Sample Probe", "Shelf 5 Sample Probe", "Tray 1 Temp Probe", "Tray 2 Temp Probe", "Tray 3 Temp Probe", "Tray 4 Temp Probe", "Tray 5 Temp Probe", "Tray 1 Sample Probe", "Tray 2 Sample Probe", "Tray 3 Sample Probe", "Tray 4 Sammple Probe", "Tray 5 Sample Probe", "Collector Temp Sensor", "Mini-Chamber Temp", "Shell Freezer Temp", "Triad Home Screen", "Reserved", "Reserved", "Reserved", "Reserved", "Reserved", "Reserved", "Reserved", "System Vacuum Sensor", "System Vacuum Sensor", "System Vacuum Sensor", "Vacuum Sample Sensor 1", "Vacuum Sample Sensor 2", "Vacuum Sample Sensor 3","Vacuum Sample Sensor 4", "Vacuum Sample Sensor 5", "Vacuum Sample Sensor 6",};
        return sensors[sensor];
    }

    //Used to get specific sensor values from the sensor array, since the JSON is not valid in this section we must interpret it manually. WORKING
    protected String getSensorValue(String Sensor){
        String sensors = getSensors();
        if (sensors.contains(Sensor + "=")){
            String currentCharacter = "";
            int marker = sensors.indexOf(Sensor + "=") + Sensor.length() + 1; //go to the start of the sensor value
            int valueEnd = marker; //used to mark the ending value
            while (!currentCharacter.equals(",") && !currentCharacter.equals("}")){ //search for the index of the end
                valueEnd++;
                currentCharacter = sensors.charAt(valueEnd) + "";
            }
            return sensors.substring(marker, valueEnd);
        }
        return "---";

    }

    //Gets specific values from the JSON cluster
    private String getValue(String value){
        Log.d("Debug", "Running getValue");
        JsonElement convertFromURL;
        JsonObject convertedJSON;
        convertFromURL = jsonParser.parse(URLContent); //retrieves specified JsonValue
        if (convertFromURL.isJsonObject()){
            convertedJSON = convertFromURL.getAsJsonObject();
            return convertedJSON.get(value).getAsString();
        }
        return null;
    }

    private class backgroundJSONPull extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.d("Debug", "async task ran");
            Scanner URLContentGrabber;
            try {
                apiURL = new URL("http://" + inClassGlobalIP + "/dump");
                Scanner contentGetter = new Scanner(apiURL.openStream());
                URLContent = contentGetter.nextLine();
            } catch (MalformedURLException Malformed) {
                Log.e("Connection", "Malformed URL Exception in FreezeDryer class");
                Malformed.printStackTrace();
            } catch (IOException IOE) {
                Log.e("Connection", "IOException in FreezeDryer class");
                System.out.println("IO Exception occurred");
                IOE.printStackTrace();
            }
            //freezeDatabase.insertData("test", "test", "Here we would want to get sensor 39");
            System.out.println("The current Collector Temp is : " + getSensorValue("39"));
            return null;
        }
    }
}


