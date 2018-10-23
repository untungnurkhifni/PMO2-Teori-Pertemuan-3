package com.example.zul_mizini.xmlparser2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String URL =
            "https://kelompok2.netlify.com/rss.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPage();
    }

    public void Parse_Online(View v){
        super.onStart();
        loadPage();
    }

    public void btoffline (View v){
        Intent i = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(i);
    }
    private void loadPage() {
        new DownloadXmlTask().execute(URL);
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return getResources().getString(R.string.xml_error);
            }
        }
        @Override
        protected void onPostExecute(String result) {
            setContentView(R.layout.activity_main);
            // Displays the HTML string in the UI via a WebView
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.loadData(result, "text/html", null);
        }
    }


    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        XmlParserclasstambahan rssXmlParser = new XmlParserclasstambahan();
        List<XmlParserclasstambahan.Entry> entries = null;
        String url = null;
        StringBuilder htmlString = new StringBuilder();
        try {
            stream = downloadUrl(urlString);
            entries = rssXmlParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        //ArrayList<String> mylist = new ArrayList<String>();

        for (XmlParserclasstambahan.Entry entry : entries) {
            htmlString.append("<p><a href='");
            htmlString.append(entry.url);
            htmlString.append("'>" + entry.title + "</a></p>");

            /* mylist.add(entry.url); //this adds an element to the list. */

        }
        // return mylist.toString();
        return htmlString.toString();
    }
    private InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }
    // Displays an error if the app is unable to load content.

}
