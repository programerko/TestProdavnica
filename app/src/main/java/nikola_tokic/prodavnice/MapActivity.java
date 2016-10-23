package nikola_tokic.prodavnice;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import nikola_tokic.prodavnice.adapters.ProdavnicaAdapter;
import nikola_tokic.prodavnice.database.BazaProdavnica;
import nikola_tokic.prodavnice.entity.Prodavnica;
import nikolatokic.nikola_tokic.prodavnice.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private List<Prodavnica>prodavnicaList = new ArrayList<>();
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context = MapActivity.this;
        prodavnicaList = BazaProdavnica.getBzr(context).getProdavnice();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //custom adapter za prikazivanje informacija markera
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file map_info layout
                View v = getLayoutInflater().inflate(R.layout.map_info, null);

                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();

                Prodavnica prodavnica = null;
                for (Prodavnica p: prodavnicaList) {
                    if(latLng.latitude == p.getLatitude()) {
                        prodavnica = p;
                        break;
                    }
                }

                TextView tvtitle = (TextView) v.findViewById(R.id.map_title);
                TextView tvsnippet = (TextView) v.findViewById(R.id.map_snippet);
                ImageView iVProdavnica = (ImageView) v.findViewById(R.id.map_image);

                tvtitle.setText(prodavnica.getName());
                tvsnippet.setText(prodavnica.getAddress()+ ", \n"+ prodavnica.getCity());
                ProdavnicaAdapter.setDrawableFromSD(context,prodavnica,iVProdavnica);
                // Returning the view containing InfoWindow contents
                return v;

            }
        });

        Prodavnica prodavnica = getIntent().getParcelableExtra("prodavnica");

        //dodavanje markera
        for (Prodavnica p: prodavnicaList ) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(p.getLatitude(), p.getLongitude())));
            if(p.getId() == prodavnica.getId())
                marker.showInfoWindow();
        }
        //pozicioniranje kamere i podešavanje zoom-a
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(prodavnica.getLatitude(),prodavnica.getLongitude())));
        mMap.setMinZoomPreference(13f);
        mMap.setMaxZoomPreference(40f);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }



}
