package fr.faradj.weather.data;

public interface ForecastModel {
    String[] loadLocation(int pId);

    void saveLocation(int pId, String pLatitude, String pLongitude);

    interface Callback<T> {
        void onResponse(T pResponse);
        void onFailure(String pMessage, Throwable pThrowable);
    }
    void getCurrentWeather(String pLatitude, String pLongitude, Callback<CurrentWeather> pCallback);
}
