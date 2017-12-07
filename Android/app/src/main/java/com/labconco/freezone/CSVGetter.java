package com.labconco.freezone;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Created by James Holdcroft
 *
 */
public class CSVGetter{
    private String CSVName = "";
    private String freezeDryerIP = "";
    private URL CSVURL = null;
    private HttpURLConnection connection = null;

    public CSVGetter (String name, String ip) {
        this.CSVName = name;
        this.freezeDryerIP = ip;
    }

    public void getterStart(){
        subCSVGetter cvg = new subCSVGetter();
        cvg.execute();
    }


    public class subCSVGetter extends AsyncTask<String, String, String> {
        public subCSVGetter() {

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Debug", "Starting CSVDownload task");

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                CSVURL = new URL("http://" + freezeDryerIP + "/dir/" + CSVName + ".csv");
                try {
                    connection = (HttpURLConnection) CSVURL.openConnection();
                    connection.connect();
                } catch (IOException IOE) {
                    Log.d("Debug", "IOException in CSVGetter");
                }
            } catch (MalformedURLException malURL) {
                Log.d("Debug", "MalformedURL in CSV getter");
                malURL.printStackTrace();
            }
            return "---";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Debug", "CSVDownload task ended");
        }
    }
}