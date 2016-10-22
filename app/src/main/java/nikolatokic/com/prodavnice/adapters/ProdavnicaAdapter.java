package nikolatokic.com.prodavnice.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import nikolatokic.com.prodavnice.MainActivity;
import nikolatokic.com.prodavnice.ProdavnicaActivity;
import nikolatokic.com.prodavnice.R;
import nikolatokic.com.prodavnice.entity.Prodavnica;

/**
 * Created by Nikola on 22.10.2016!
 */

public class ProdavnicaAdapter extends ArrayAdapter<Prodavnica> {

    private List<Prodavnica> prodavnice;

    private Context context;
    public ProdavnicaAdapter(Context context, int resource, List<Prodavnica> objects) {
        super(context, resource, objects);
        this.prodavnice = objects;
        this.context = context;
    }

    private class ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView title, description;
        private LinearLayout lLayout;
        private Prodavnica prodavnica;
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ProdavnicaActivity.class);
            intent.putExtra("prodavnica" , prodavnica);
            intent.putExtra("vreme", prodavnica.getWorkingHours());
            context.startActivity(intent);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final Prodavnica prodavnica = prodavnice.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.prodavnica_row, parent, false);
            viewHolder.lLayout = (LinearLayout) convertView.findViewById(R.id.row_txt_layout);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.row_img);
            viewHolder.title = (TextView) viewHolder.lLayout.findViewById(R.id.row_naslov);
            viewHolder.description = (TextView) viewHolder.lLayout.findViewById(R.id.row_opis);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        setDrawableFromSD(context,prodavnica,viewHolder.image);
        viewHolder.title.setText(String.valueOf(prodavnica.getCity() + ", " + prodavnica.getCountryName()));
        String desc = (String.valueOf(prodavnica.getDescription().
                substring(0, Math.min(prodavnica.getDescription().length(), 200)))+"...");
        viewHolder.description.setText(desc);
        viewHolder.prodavnica = prodavnica;

        convertView.setOnClickListener(viewHolder);
        return convertView;
    }

    public static void setDrawableFromSD(Context context, Prodavnica prodavnica, ImageView imageView){
        String type = prodavnica.getPlaceImgUrl().substring(prodavnica.getPlaceImgUrl().length() - 4).equals(".png")?".png":".jpg";
        String filename = prodavnica.getId() + type;
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        Picasso.with(context).load(new File(baseDir + File.separator + filename)).into(imageView);
    }
}

