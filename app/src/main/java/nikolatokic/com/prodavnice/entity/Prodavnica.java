package nikolatokic.com.prodavnice.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Prodavnica implements Parcelable{

    private int id;
    private int countryId;
    private int status;
    private int accountType;
    private int reviewNum;
    private int score;
    private int RB;
    private double longitude, latitude;
    private boolean working;
    private String countryName;
    private String placeImgUrl;
    private String name;
    private String address;
    private String city;
    private String description;
    private String webSite;
    private String promotion;
    private HashMap<String, String> workingHours;

    public Prodavnica(int accountType, String address, String city, int countryId, String countryName,
                      String description, int id, long latitude, long longitude, String name,
                      String placeImgUrl, String promotion, int reviewNum, int score, int status,
                      String webSite, boolean working, HashMap<String, String> workingHours) {
        this.accountType = accountType;
        this.address = address;
        this.city = city;
        this.countryId = countryId;
        this.countryName = countryName;
        this.description = description;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.placeImgUrl = placeImgUrl;
        this.promotion = promotion;
        this.reviewNum = reviewNum;
        this.score = score;
        this.status = status;
        this.webSite = webSite;
        this.working = working;
        this.workingHours = workingHours;
    }

    public Prodavnica() {

    }

    protected Prodavnica(Parcel in) {
        id = in.readInt();
        countryId = in.readInt();
        status = in.readInt();
        accountType = in.readInt();
        reviewNum = in.readInt();
        score = in.readInt();
        RB = in.readInt();
        longitude = in.readDouble();
        latitude = in.readDouble();
        working = in.readByte() != 0;
        countryName = in.readString();
        placeImgUrl = in.readString();
        name = in.readString();
        address = in.readString();
        city = in.readString();
        description = in.readString();
        webSite = in.readString();
        promotion = in.readString();

    }

    public static final Creator<Prodavnica> CREATOR = new Creator<Prodavnica>() {
        @Override
        public Prodavnica createFromParcel(Parcel in) {
            return new Prodavnica(in);
        }

        @Override
        public Prodavnica[] newArray(int size) {
            return new Prodavnica[size];
        }
    };

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceImgUrl() {
        return placeImgUrl;
    }

    public void setPlaceImgUrl(String placeImgUrl) {
        this.placeImgUrl = placeImgUrl;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public int getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(int reviewNum) {
        this.reviewNum = reviewNum;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public HashMap<String, String> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(HashMap<String, String> workingHours) {
        this.workingHours = workingHours;
    }

    public int getRB() {
        return RB;
    }

    public void setRB(int RB) {
        this.RB = RB;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(countryId);
        dest.writeInt(status);
        dest.writeInt(accountType);
        dest.writeInt(reviewNum);
        dest.writeInt(score);
        dest.writeInt(RB);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeByte((byte) (working ? 1 : 0));
        dest.writeString(countryName);
        dest.writeString(placeImgUrl);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(description);
        dest.writeString(webSite);
        dest.writeString(promotion);

    }
}
