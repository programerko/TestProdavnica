package nikola_tokic.prodavnice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nikola_tokic.prodavnice.adapters.ProdavnicaAdapter;
import nikola_tokic.prodavnice.database.BazaProdavnica;
import nikola_tokic.prodavnice.entity.Prodavnica;
import nikolatokic.nikola_tokic.prodavnice.R;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "_DEBUG";
    private BazaProdavnica baza;
    private ListView listViewProdavnica;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        baza = BazaProdavnica.getBzr(context);

        listViewProdavnica = (ListView) findViewById(R.id.listview_prodavnice);
        listViewProdavnica.setAdapter(new ProdavnicaAdapter(context,R.layout.prodavnica_row, baza.getProdavnice()));

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

    }

    @SuppressWarnings({"ThrowFromFinallyBlock", "TryFinallyCanBeTryWithResources"})
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

        List<Prodavnica> readJsonStream(InputStream in) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

            try {
                return readProdavnicaArray(reader);
            }
            finally {
                reader.close();
            }
        }

        List<Prodavnica> readProdavnicaArray(JsonReader reader) throws IOException {
            List<Prodavnica> listaProdavnica = new ArrayList<>();

            reader.beginArray();
            int rb = 1;
            while (reader.hasNext()){
                listaProdavnica.add(readProdavnica(reader,rb));
                rb++;
            }

            reader.endArray();
            return listaProdavnica;
            }

        Prodavnica readProdavnica(JsonReader reader, int rb) throws IOException {
            Prodavnica prodavnica = new Prodavnica();
            prodavnica.setRB(rb);
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "id":
                        prodavnica.setId(reader.nextInt());
                        break;
                    case "name":
                        prodavnica.setName(reader.nextString());
                        break;
                    case "address":
                        prodavnica.setAddress(reader.nextString());
                        break;
                    case "city":
                        prodavnica.setCity(reader.nextString());
                        break;
                    case "description":
                        prodavnica.setDescription(reader.nextString());
                        break;
                    case "countryId":
                        prodavnica.setCountryId(reader.nextInt());
                        break;
                    case "webSite":
                        prodavnica.setWebSite(reader.nextString());
                        break;
                    case "status":
                        prodavnica.setStatus(reader.nextInt());
                        break;
                    case "accountType":
                        prodavnica.setAccountType(reader.nextInt());
                        break;
                    case "longitude":
                        prodavnica.setLongitude(reader.nextDouble());
                        break;
                    case "latitude":
                        prodavnica.setLatitude(reader.nextDouble());
                        break;
                    case "working":
                        prodavnica.setWorking(reader.nextBoolean());
                        break;
                    case "workingHour":
                        prodavnica.setWorkingHours(readWorkingHours(reader));
                        break;
                    case "country":
                        prodavnica.setCountryName(readCountry(reader));
                        break;
                    case "reviewNum":
                        prodavnica.setReviewNum(reader.nextInt());
                        break;
                    case "score":
                        prodavnica.setScore(reader.nextInt());
                        break;
                    case "placeImgUrl":
                        prodavnica.setPlaceImgUrl(reader.nextString());
                        new DownloadImgTask().execute(prodavnica.getPlaceImgUrl(), String.valueOf(prodavnica.getId()));
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
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
                switch (name) {
                    case "mon":
                        workingHours.put("mon", reader.nextString());
                        break;
                    case "tue":
                        workingHours.put("tue", reader.nextString());
                        break;
                    case "wed":
                        workingHours.put("wed", reader.nextString());
                        break;
                    case "thu":
                        workingHours.put("thu", reader.nextString());
                        break;
                    case "fri":
                        workingHours.put("fri", reader.nextString());
                        break;
                    case "sat":
                        workingHours.put("sat", reader.nextString());
                        break;
                    case "sun":
                        workingHours.put("sun", reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }

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
                    //noinspection ThrowFromFinallyBlock
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
    class DownloadImgTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                // Output stream to write file
                String type = f_url[0].substring(f_url[0].length()-4);
                OutputStream output = new FileOutputStream(root+"/"+f_url[1]+type);
                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1)
                    output.write(data, 0, count);

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

        @Override
        protected void onPostExecute(String file_url) {
            if(file_url.contains("10"))//ne baš najpametniji način
                listViewProdavnica.setAdapter(new ProdavnicaAdapter(context,R.layout.prodavnica_row, baza.getProdavnice()));
            System.out.println("Downloaded " + file_url);
        }

    }
}

