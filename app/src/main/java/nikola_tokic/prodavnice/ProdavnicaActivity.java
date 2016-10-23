package nikola_tokic.prodavnice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import nikola_tokic.prodavnice.adapters.ProdavnicaAdapter;
import nikola_tokic.prodavnice.entity.Prodavnica;
import nikolatokic.nikola_tokic.prodavnice.R;

public class ProdavnicaActivity extends AppCompatActivity {

    private Context context;
    private Prodavnica prodavnica;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodavnica);
        context = ProdavnicaActivity.this;

        prodavnica = getIntent().getParcelableExtra("prodavnica");
        prodavnica.setWorkingHours((HashMap<String, String>) getIntent().getSerializableExtra("vreme"));
        String location = prodavnica.getAddress()+ ", "+prodavnica.getCity()+", "+prodavnica.getCountryName();

        ProdavnicaAdapter.setDrawableFromSD(context,prodavnica,(ImageView)findViewById(R.id.prodavnica_img));
        ((TextView)findViewById(R.id.prodavnica_title_tv)).setText(prodavnica.getName());
        ((TextView)findViewById(R.id.prodavnica_desc_tv)).setText(prodavnica.getDescription());
        ((TextView)findViewById(R.id.prodavnica_location_tv)).setText(location);
        ((TextView)findViewById(R.id.prodavnica_workhours_tv)).setText(prodavnica.getHoursStr());
    }

    public void openMap(View v){
        startActivity(new Intent(context, MapActivity.class).putExtra("prodavnica",prodavnica));
    }
}
