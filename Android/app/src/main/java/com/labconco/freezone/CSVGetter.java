package com.labconco.freezone;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
/**
 * Created by James Holdcroft
 */
public class CSVGetter{
    private String CSVName = "";
    private String freezeDryerIP = "";
    private URL CSVURL = null;
    private HttpURLConnection connection = null;
    private InputStream input = null;
    private OutputStream output = null;
    private Context appContext = null;

    public CSVGetter (String name, String ip, Context context) {
        this.CSVName = name;
        this.freezeDryerIP = ip;
        this.appContext = context;
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
        protected String doInBackground(String... donkey) {
            try {
                CSVURL = new URL("http://" + freezeDryerIP + "/dir/" + CSVName + ".csv");
                try {
                    connection = (HttpURLConnection) CSVURL.openConnection();
                    connection.connect();

                    input = connection.getInputStream(); //Download
                    output = new FileOutputStream(new File(appContext.getCacheDir() + "/" + CSVName));
                    byte data[] = new byte[2048];
                    int count;
                    while ((count = input.read(data)) != -1) {
                        output.write(data, 0, count);
                    }
                } catch (IOException IOE) {
                    Log.d("Debug", "IOException in CSVGetter\n" + IOE.getMessage());
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }

                    if (connection != null)
                        connection.disconnect();
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