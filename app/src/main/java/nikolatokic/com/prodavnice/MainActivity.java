package nikolatokic.com.prodavnice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nikolatokic.com.prodavnice.adapters.ProdavnicaAdapter;
import nikolatokic.com.prodavnice.database.BazaProdavnica;
import nikolatokic.com.prodavnice.entity.Prodavnica;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "_DEBUG";
    private BazaProdavnica baza;
    private ListView listViewProdavnica;
    private Context context;
    public static String imgsUrl = "http://n551jk.com/demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        baza = BazaProdavnica.getBzr(context);

        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        listViewProdavnica = (ListView) findViewById(R.id.listview_prodavnice);
        listViewProdavnica.setAdapter(new ProdavnicaAdapter(context,R.layout.prodavnica_row, baza.getProdavnice()));

        //new DownloadFileFromURL().execute("http://n551jk.com/demo/10.jpg");
        if(baza.getProdavnice().size()<1)
        /*POKREĆE PREUZIMANJE PODATAKA*/connectionCheck("http://n551jk.com/demo/places.json");
    }

    public void connectionCheck(String url) {

        ConnectivityManager cmg = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cmg.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(DEBUG_TAG, "connection OK");
            new DownloadTask().execute(url);
        }else
            Log.d(DEBUG_TAG, "connection ERROR" );
//
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Url Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {

        }

        public List<Prodavnica> readJsonStream(InputStream in) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            try {
                return readProdavnicaArray(reader);
            }
            finally {

                reader.close();
            }
        }

        public List<Prodavnica> readProdavnicaArray(JsonReader reader) throws IOException {
            List<Prodavnica> listaProdavnica = new ArrayList<Prodavnica>();

            reader.beginArray();
            int rb = 1;
            while (reader.hasNext()){
                listaProdavnica.add(readProdavnica(reader,rb));
                rb++;
            }

            reader.endArray();
            return listaProdavnica;
            }

        public Prodavnica readProdavnica(JsonReader reader, int rb) throws IOException {
            Prodavnica prodavnica = new Prodavnica();
            prodavnica.setRB(rb);
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("id")) {
                    prodavnica.setId(reader.nextInt());
                } else if (name.equals("name")) {
                    prodavnica.setName(reader.nextString());
                } else if (name.equals("address"))
                    prodavnica.setAddress(reader.nextString());
                else if (name.equals("city"))
                    prodavnica.setCity(reader.nextString());
                else if (name.equals("description"))
                    prodavnica.setDescription(reader.nextString());
                else if (name.equals("countryId"))
                    prodavnica.setCountryId(reader.nextInt());
                else if (name.equals("webSite"))
                    prodavnica.setWebSite(reader.nextString());
                else if (name.equals("status"))
                    prodavnica.setStatus(reader.nextInt());
                else if (name.equals("accountType"))
                    prodavnica.setAccountType(reader.nextInt());
                else if (name.equals("longitude"))
                    prodavnica.setLongitude(reader.nextDouble());
                else if (name.equals("latitude"))
                    prodavnica.setLatitude(reader.nextDouble());
                else if (name.equals("working"))
                    prodavnica.setWorking(reader.nextBoolean());
                else if (name.equals("workingHour"))
                    prodavnica.setWorkingHours(readWorkingHours(reader));
                else if (name.equals("country"))
                    prodavnica.setCountryName(readCountry(reader));
                else if (name.equals("reviewNum"))
                    prodavnica.setReviewNum(reader.nextInt());
                else if (name.equals("score"))
                    prodavnica.setScore(reader.nextInt());
                else if (name.equals("placeImgUrl")) {
                    prodavnica.setPlaceImgUrl(reader.nextString());
                    new DownloadFileFromURL().execute(prodavnica.getPlaceImgUrl(),String.valueOf(prodavnica.getId()));
                }
                else
                    reader.skipValue();


            }
            reader.endObject();
            baza.addRow(prodavnica);
            return prodavnica;
        }

        private HashMap<String, String> readWorkingHours(JsonReader reader) throws IOException {
            HashMap<String, String> workingHours = new HashMap<>();

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("mon"))
                    workingHours.put("mon",reader.nextString());
                else if (name.equals("tue"))
                    workingHours.put("tue",reader.nextString());
                else if (name.equals("wed"))
                    workingHours.put("wed",reader.nextString());
                else if (name.equals("thu"))
                    workingHours.put("thu",reader.nextString());
                else if (name.equals("fri"))
                    workingHours.put("fri",reader.nextString());
                else if (name.equals("sat"))
                    workingHours.put("sat",reader.nextString());
                else if (name.equals("sun"))
                    workingHours.put("sun",reader.nextString());
                else
                    reader.skipValue();

            }
            reader.endObject();
            return workingHours;
        }

        private String readCountry(JsonReader reader)throws IOException {
            String country = "";
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("name"))
                    country = reader.nextString();
            }
            reader.endObject();
            return country;

        }

        private String downloadUrl(String myurl) throws IOException {
            InputStream inputStream = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(150000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int response = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response is: " + response);
                inputStream = conn.getInputStream();

                return readIt(inputStream);

            } finally {
                if (inputStream != null)
                    inputStream.close();
            }

        }

        private String readIt(InputStream stream) throws IOException {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            StringBuilder total = new StringBuilder();
            String line;
            readJsonStream(stream);

            while ((line = r.readLine()) != null)
                total.append(line).append('\n');

            return total.toString();


        }


    }
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();

                System.out.println("Downloading");
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file

                String type = f_url[0].substring(f_url[0].length()-4);
                OutputStream output = new FileOutputStream(root+"/"+f_url[1]+type);
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return f_url[0];
        }



        /**
         * After completing background task
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            if(file_url.contains("10"))
                listViewProdavnica.setAdapter(new ProdavnicaAdapter(context,R.layout.prodavnica_row, baza.getProdavnice()));
            System.out.println("Downloaded " + file_url);
        }

    }
}

