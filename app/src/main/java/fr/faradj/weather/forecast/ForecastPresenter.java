package fr.faradj.weather.forecast;

import android.support.annotation.NonNull;
import android.util.Log;

import fr.faradj.weather.data.CurrentWeather;
import fr.faradj.weather.data.ForecastModel;

public class ForecastPresenter implements ForecastContract.Presenter {
    private static final int ID = 42;
    @NonNull
    private
    ForecastContract.View mView;
    @NonNull
    private
    ForecastModel mModel;
    @NonNull
    private ForecastModel.Callback<CurrentWeather> mCallback;

    ForecastPresenter(@NonNull ForecastContract.View pView, @NonNull ForecastModel pModel) {
        mView = pView;
        mModel = pModel;
        mCallback = new ForecastModel.Callback<CurrentWeather>() {
            @Override
            public void onResponse(CurrentWeather pResponse) {
                mView.showLoading(false);
                mView.showTodayForecast(pResponse);
                mModel.saveLocation(ID, String.valueOf(pResponse.getLatitude()), String.valueOf(pResponse.getLongitude()));
            }

            @Override
            public void onFailure(String pMessage, Throwable pThrowable) {
                Log.e("FAF", "showError: " + pMessage, pThrowable);
                mView.showLoading(false);
                mView.showError(pMessage);
            }
        };
    }

    @Override
    public void requestForecast(@NonNull String pLatitude, @NonNull String pLongitude) {
        mView.showLoading(true);
        mModel.getCurrentWeather(pLatitude, pLongitude, mCallback);

    }

    @Override
    public void requestForecast() {
        mView.showLoading(true);
        String[] latLng = mModel.loadLocation(ID);
        mModel.getCurrentWeather(latLng[0], latLng[1], mCallback);
    }
}
