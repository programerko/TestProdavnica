package nikolatokic.com.prodavnice;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import nikolatokic.com.prodavnice.adapters.ProdavnicaAdapter;
import nikolatokic.com.prodavnice.entity.Prodavnica;

public class ProdavnicaActivity extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodavnica);
        context = ProdavnicaActivity.this;

        Prodavnica prodavnica = getIntent().getParcelableExtra("prodavnica");
        prodavnica.setWorkingHours((HashMap<String, String>) getIntent().getSerializableExtra("vreme"));
        String location = prodavnica.getAddress()+ ", "+prodavnica.getCity()+", "+prodavnica.getCountryName();

        ProdavnicaAdapter.setDrawableFromSD(context,prodavnica,(ImageView)findViewById(R.id.prodavnica_img));
        ((TextView)findViewById(R.id.prodavnica_title_tv)).setText(prodavnica.getName());
        ((TextView)findViewById(R.id.prodavnica_desc_tv)).setText(prodavnica.getDescription());
        ((TextView)findViewById(R.id.prodavnica_location_tv)).setText(location);
        ((TextView)findViewById(R.id.prodavnica_workhours_tv)).setText(prodavnica.getWorkingHours().toString());
    }
}
