package com.labconco.freezone;

//Connection Imports
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.MalformedURLException;

//JSON imports
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//Misc imports
import android.os.Handler;
import android.util.Log; //favorite import
/**
 * Created by James Holdcroft
 */
public class FreezeDryer {
    private String inClassGlobalIP = ""; //get String to make global
    private String URLContent; //This is grabbed directly from the url
    private JsonParser jsonParser = new JsonParser();
    private final static int INTERVAL = 60000; //Background task interval
    private URL apiURL;
    private URL csvURL;
    private String unparsedCSVHTML;
    private Handler backgroundPullHandler = new Handler();
    private StringBuffer buffer;


    Runnable backgroundPullTask = new Runnable() {
        @Override
        public void run() {
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
        return sensors[1];
    }

    //Used to get specific sensor values from the sensor array, since the JSON is not valid in this section we must interpret it manually. WORKING
    protected String getSensorValue(String Sensor) {
        String sensors = getSensors();
        if (sensors != null){
            if (sensors.contains(Sensor + "=")) {
                String currentCharacter = "";
                int marker = sensors.indexOf(Sensor + "=") + Sensor.length() + 1; //go to the start of the sensor value
                int valueEnd = marker; //used to mark the ending value
                while (!currentCharacter.equals(",") && !currentCharacter.equals("}")) { //search for the index of the end
                    valueEnd++;
                    currentCharacter = sensors.charAt(valueEnd) + "";
                }
                return sensors.substring(marker, valueEnd);
            }
    }
        return "---";

    }

    //Gets specific values from the JSON cluster
    private String getValue(String value){
        if (URLContent != null) { //don't check null content
            JsonElement convertFromURL;
            JsonObject convertedJSON;
            convertFromURL = jsonParser.parse(URLContent); //retrieves specified JsonValue
            if (convertFromURL.isJsonObject()) {
                convertedJSON = convertFromURL.getAsJsonObject();
                return convertedJSON.get(value).getAsString();
            }
        }
        return "---";
    }

    //Used to get a list of avaliable CSV files;
    protected ArrayList<String> getCSVList(){
        ArrayList<String> csvNames = new ArrayList<String>();

        if (buffer == null) {
            Log.d("Debug", "buffer is blank");
        } else {
            for (int index = buffer.indexOf("/dir/");
                 index >= 0;
                 index = buffer.indexOf("/dir/", index + 1)) {
                int indexStart = index + 5;
                int indexEnd = indexStart;
                String charHold = "";
                while (!charHold.equalsIgnoreCase(".")) {
                    indexEnd++;
                    charHold = buffer.charAt(indexEnd) + "";
                }
                csvNames.add(buffer.substring(indexStart, indexEnd));
            }
        }
        return csvNames;
    }

    private class backgroundJSONPull extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.d("Debug", "async task ran");
            try {
                apiURL = new URL("http://" + inClassGlobalIP + "/dump");
                csvURL = new URL("http://" + inClassGlobalIP);
                InputStream is = csvURL.openStream();
                int ptr = 0;
                buffer = new StringBuffer();
                while ((ptr = is.read()) != -1) { //gets the html
                    buffer.append((char)ptr);
                }
                Scanner contentGetter = new Scanner(apiURL.openStream());
                URLContent = contentGetter.nextLine();
            } catch (MalformedURLException Malformed) {
                Log.e("Connection", "Malformed URL Exception in FreezeDryer class");
                Malformed.printStackTrace();
            } catch (IOException IOE) {
                Log.e("Connection", "IOException in FreezeDryer class");
                Log.d("Debug", "IO Exception occured\n" +IOE.getStackTrace());
            }
            return "---";
        }
    }
}


