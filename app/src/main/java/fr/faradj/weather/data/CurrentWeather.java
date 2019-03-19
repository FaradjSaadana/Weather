package fr.faradj.weather.data;

public class CurrentWeather {
    private String mLocation;
    private String mText;
    private Integer mCode;
    private Integer mTemperature;
    private double mLatitude;
    private double mLongitude;

    CurrentWeather(String pLocation, String pText, Integer pCode, Integer pTemperature, double pLatitude, double pLongitude) {
        mLocation = pLocation;
        mText = pText;
        mCode = pCode;
        mTemperature = pTemperature;
        mLatitude = pLatitude;
        mLongitude = pLongitude;
    }

    public String getText() {
        return mText;
    }

    public Integer getCode() {
        return mCode;
    }

    public Integer getTemperature() {
        return mTemperature;
    }

    public String getLocation() {
        return mLocation;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
