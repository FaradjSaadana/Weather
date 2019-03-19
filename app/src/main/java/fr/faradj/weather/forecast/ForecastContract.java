package fr.faradj.weather.forecast;

import fr.faradj.weather.data.CurrentWeather;

public interface ForecastContract {
    interface View {
        void showLoading(boolean pEnable);
        void showTodayForecast(CurrentWeather pData);
        void showError(String pMessage);
    }

    interface Presenter {
        void requestForecast(String pLatitude, String pLongitude);
        void requestForecast();
    }

}
