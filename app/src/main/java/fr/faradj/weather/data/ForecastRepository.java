package fr.faradj.weather.data;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.faradj.weatherapi.YahooWeatherClient;
import fr.faradj.weatherapi.pojo.Condition;
import fr.faradj.weatherapi.pojo.ForecastResult;

public class ForecastRepository implements ForecastModel {
    private static final String LATITUDE_KEY = "latitude_key";
    private static final String LONGITUDE_KEY = "longitude_key";
    private SharedPreferences mSharedPreferences;

    public ForecastRepository(SharedPreferences pSharedPreferences) {
        mSharedPreferences = pSharedPreferences;
    }

    @Override
    public String[] loadLocation(int pId) {
        String lat = mSharedPreferences.getString(LATITUDE_KEY + pId, String.valueOf(43.7378811f));
        String lon = mSharedPreferences.getString(LONGITUDE_KEY + pId, String.valueOf(7.4083429f));
        return new String[]{lat, lon};
    }

    @Override
    public void saveLocation(int pId, String pLatitude, String pLongitude) {
        mSharedPreferences.edit().putString(LATITUDE_KEY + pId, pLatitude).putString(LONGITUDE_KEY + pId, pLongitude).apply();
    }

    @Override
    public void getCurrentWeather(@NonNull String pLatitude, @NonNull String pLongitude, @NonNull final Callback<CurrentWeather> pCallback) {
        YahooWeatherClient yahooWeatherClient = new YahooWeatherClient();
        yahooWeatherClient.getForecast(pLatitude, pLongitude, new YahooWeatherClient.Callback() {
            @Override
            public void onResponse(@NonNull ForecastResult pResult) {
                Condition condition = pResult.getCurrentObservation().getCondition();
                pCallback.onResponse(new CurrentWeather(pResult.getLocation().getCity(), condition.getText(), condition.getCode(), condition.getTemperature(), pResult.getLocation().getLat(), pResult.getLocation().getLong()));
            }

            @Override
            public void onFailure(@Nullable String pMessage, @Nullable Throwable pThrowable) {
                pCallback.onFailure(pMessage, pThrowable);
            }
        });

    }
}
