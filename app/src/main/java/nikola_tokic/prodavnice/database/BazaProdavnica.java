package nikola_tokic.prodavnice.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import nikola_tokic.prodavnice.entity.Prodavnica;

/**
 * Created by Nikola on 22.10.2016!
 */

public class BazaProdavnica extends SQLiteOpenHelper {
    private static final int VERZIJA_BAZE = 25;
    private static final String IME_BAZE = "Baza_prodavnica.db";

    private static final String IME_TABELE = "Prodavnice";
    private static final String ID = "id";
    private static final String RB = "rb";
    private static final String COUNTRY_ID = "COUNTRY_ID";
    private static final String STATUS = "STATUS";
    private static final String ACC_TYPE = "ACC_TYPE";
    private static final String REVIEW_NUM = "REVIEW_NUM";
    private static final String SCORE = "SCORE";
    private static final String LON = "LONGITUDE";
    private static final String LAT = "LATITUDE";
    private static final String WORKING = "WORKING";
    private static final String COUNTRY = "COUNTRY";
    private static final String IMG = "IMG";
    private static final String NAME = "NAME";
    private static final String ADDRESS = "ADDRESS";
    private static final String CITY = "CITY";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String WEBSITE = "WEBSITE";
    private static final String MON = "MON";
    private static final String TUE = "TUE";
    private static final String WED = "WED";
    private static final String THU = "THU";
    private static final String FRI = "FRI";
    private static final String SAT = "SAT";
    private static final String SUN = "SUN";
    private static final String TAG = "BAZA";

    private static BazaProdavnica bzr;

    public static BazaProdavnica getBzr(Context c){
        if(bzr == null)
            bzr = new BazaProdavnica(c);
        return bzr;
    }

    private BazaProdavnica(Context context) {
        super(context, IME_BAZE, null, VERZIJA_BAZE);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE "+ IME_TABELE + "(" + ID + " INTEGER PRIMARY KEY,"
                + COUNTRY_ID + " INTEGER,"+ RB + " INTEGER,"+ STATUS + " INTEGER,"+ ACC_TYPE + " INTEGER,"+ REVIEW_NUM +
                " INTEGER,"+ SCORE + " INTEGER,"+ LON + " REAL,"+ LAT + " REAL," + WORKING + " INTEGER,"+
                COUNTRY + " TEXT," + IMG + " TEXT," +NAME + " TEXT," +ADDRESS + " TEXT," + CITY +
                " TEXT," +DESCRIPTION + " TEXT," +WEBSITE + " TEXT," +MON + " TEXT," +TUE + " TEXT,"
                +WED + " TEXT," +THU + " TEXT," +FRI + " TEXT," +SAT + " TEXT," +SUN + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IME_TABELE);
        onCreate(sqLiteDatabase);
    }

    public void addRow(Prodavnica prodavnica){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ID, prodavnica.getId());
        values.put(RB, prodavnica.getRB());
        values.put(COUNTRY_ID, prodavnica.getCountryId());
        values.put(STATUS, prodavnica.getStatus());
        values.put(ACC_TYPE, prodavnica.getAccountType());
        values.put(REVIEW_NUM, prodavnica.getReviewNum());
        values.put(SCORE, prodavnica.getScore());
        values.put(LON, prodavnica.getLongitude());
        values.put(LAT, prodavnica.getLatitude());
        values.put(WORKING, 1);
        values.put(COUNTRY, prodavnica.getCountryName());
        values.put(IMG, prodavnica.getPlaceImgUrl());
        values.put(NAME, prodavnica.getName());
        values.put(ADDRESS, prodavnica.getAddress());
        values.put(CITY, prodavnica.getCity());
        values.put(DESCRIPTION, prodavnica.getDescription());
        values.put(WEBSITE, prodavnica.getWebSite());
        values.put(MON, prodavnica.getWorkingHours().get("mon"));
        values.put(TUE, prodavnica.getWorkingHours().get("tue") );
        values.put(WED, prodavnica.getWorkingHours().get("wed") );
        values.put(THU, prodavnica.getWorkingHours().get("thu") );
        values.put(FRI, prodavnica.getWorkingHours().get("fri") );
        values.put(SAT, prodavnica.getWorkingHours().get("sat") );
        values.put(SUN, prodavnica.getWorkingHours().get("sun") );
        database.insert(IME_TABELE, null, values);
        Log.d(TAG, "ubačeno u bazu: "+prodavnica.getName());
    }

    public ArrayList<Prodavnica> getProdavnice(){
        SQLiteDatabase database = getWritableDatabase();
        ArrayList<Prodavnica> prodavnice = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM "+ IME_TABELE, null );
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount() ; i++){
            Prodavnica prodavnica = new Prodavnica();
            prodavnica.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            prodavnica.setRB(cursor.getInt(cursor.getColumnIndex(RB)));
            prodavnica.setCountryId(cursor.getInt(cursor.getColumnIndex(COUNTRY_ID)));
            prodavnica.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
            prodavnica.setAccountType(cursor.getInt(cursor.getColumnIndex(ACC_TYPE)));
            prodavnica.setReviewNum(cursor.getInt(cursor.getColumnIndex(REVIEW_NUM)));
            prodavnica.setScore(cursor.getInt(cursor.getColumnIndex(SCORE)));
            prodavnica.setLongitude(cursor.getDouble(cursor.getColumnIndex(LON)));
            prodavnica.setLatitude(cursor.getDouble(cursor.getColumnIndex(LAT)));
            prodavnica.setWorking(cursor.getInt(cursor.getColumnIndex(WORKING)) == 1);
            prodavnica.setCountryName(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            prodavnica.setPlaceImgUrl(cursor.getString(cursor.getColumnIndex(IMG)));
            prodavnica.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            prodavnica.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
            prodavnica.setCity(cursor.getString(cursor.getColumnIndex(CITY)));
            prodavnica.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
            prodavnica.setWebSite(cursor.getString(cursor.getColumnIndex(WEBSITE)));
            HashMap<String, String> map = new HashMap<>();
            map.put("mon",cursor.getString(cursor.getColumnIndex(MON)));
            map.put("tue",cursor.getString(cursor.getColumnIndex(TUE)));
            map.put("wed",cursor.getString(cursor.getColumnIndex(WED)));
            map.put("thu",cursor.getString(cursor.getColumnIndex(THU)));
            map.put("fri",cursor.getString(cursor.getColumnIndex(FRI)));
            map.put("sat",cursor.getString(cursor.getColumnIndex(SAT)));
            map.put("sun",cursor.getString(cursor.getColumnIndex(SUN)));
            prodavnica.setWorkingHours(map);
            prodavnice.add(prodavnica);
            cursor.moveToNext();
        }

        cursor.close();
        return  prodavnice;
    }


}
