package fr.faradj.weatherapi;

import fr.faradj.weatherapi.pojo.ForecastResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface YahooWeatherApi {
    String URL = "https://weather-ydn-yql.media.yahoo.com/";

    @GET("forecastrss?format=json&u=c")
    Call<ForecastResult> getForecast(@Query("location") String pLocation);

    @GET("forecastrss?format=json&u=c")
    Call<ForecastResult> getForecast(@Query("lat") String pLatitude, @Query("lon") String pLongitude);
}
