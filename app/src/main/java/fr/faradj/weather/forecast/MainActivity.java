package fr.faradj.weather.forecast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import fr.faradj.weather.R;
import fr.faradj.weather.data.CurrentWeather;
import fr.faradj.weather.data.ForecastModel;
import fr.faradj.weather.data.ForecastRepository;

public class MainActivity extends AppCompatActivity implements ForecastContract.View {
    private static final int REQUEST_CODE_LOCATION = 42;
    ForecastContract.Presenter mPresenter;
    TextView mCityNameView;
    ImageView mImageView;
    TextView mConditionTextView;
    TextView mTemperatureView;
    ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    private AutocompleteSupportFragment mAutocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        mCityNameView = findViewById(R.id.city_name);
        mImageView = findViewById(R.id.imageView);
        mConditionTextView = findViewById(R.id.text_condition);
        mTemperatureView = findViewById(R.id.text_temperature);
        mProgressBar = findViewById(R.id.progressBar);
        mFab = findViewById(R.id.fab);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        ForecastModel forecastModel = new ForecastRepository(sharedPreferences);
        //TODO injection
        mPresenter = new ForecastPresenter(this, forecastModel);
        mPresenter.requestForecast();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
                }
                else {
                    getCurrentPositionWeather();
                }
            }
        });
        mAutocompleteFragment.setTypeFilter(TypeFilter.CITIES);
        mAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        mAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (place.getLatLng() != null) {
                    mPresenter.requestForecast(String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude));
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("FAF", "An error occurred: " + status);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentPositionWeather();
            } else {
                Toast.makeText(this, getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO Move to Model
    @SuppressLint("MissingPermission")
    private void getCurrentPositionWeather() {
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mPresenter.requestForecast(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                locationManager.removeUpdates(this);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        String locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
        locationManager.getLastKnownLocation(locationProvider);
    }

    @Override
    public void showLoading(boolean pEnable) {
        mCityNameView.setVisibility(!pEnable ? View.VISIBLE : View.INVISIBLE);
        mImageView.setVisibility(!pEnable ? View.VISIBLE : View.INVISIBLE);
        mConditionTextView.setVisibility(!pEnable ? View.VISIBLE : View.INVISIBLE);
        mTemperatureView.setVisibility(!pEnable ? View.VISIBLE : View.INVISIBLE);
        mFab.setEnabled(!pEnable);
        mAutocompleteFragment.a.setVisibility(!pEnable ? View.VISIBLE : View.INVISIBLE);
        mAutocompleteFragment.setText("");
        mProgressBar.setVisibility(pEnable ? View.VISIBLE : View.INVISIBLE);
    }

    //TODO Apply Day/Night variation
    @Override
    public void showTodayForecast(CurrentWeather pData) {
        mCityNameView.setText(pData.getLocation());
        switch (pData.getCode()) {
            case 0:    //tornado
                mImageView.setImageResource(R.drawable.ic_wi_tornado);
                break;
            case 1:    //tropical storm
                mImageView.setImageResource(R.drawable.ic_wi_day_storm_showers);
                break;
            case 2:    //hurricane
                mImageView.setImageResource(R.drawable.ic_wi_hurricane);
                break;
            case 3:    //severe thunderstorms
            case 4:    //thunderstorms
                mImageView.setImageResource(R.drawable.ic_wi_day_thunderstorm);
                break;
            case 5:    //mixed rain and snow
                mImageView.setImageResource(R.drawable.ic_wi_day_snow);
                break;
            case 6:    //mixed rain and sleet
                mImageView.setImageResource(R.drawable.ic_wi_sleet);
                break;
            case 7:    //mixed snow and sleet
            case 8:    //freezing drizzle
            case 9:    //drizzle
            case 10:    //freezing rain
            case 11:    //showers
            case 12:    //showers
                mImageView.setImageResource(R.drawable.ic_wi_day_showers);
                break;
            case 13:    //snow flurries
            case 14:    //light snow showers
            case 15:    //blowing snow
            case 16:    //snow
                mImageView.setImageResource(R.drawable.ic_wi_snow);
                break;
            case 17:    //hail
            case 18:    //sleet
                mImageView.setImageResource(R.drawable.ic_wi_sleet);
                break;
            case 19:    //dust
                mImageView.setImageResource(R.drawable.ic_wi_dust);
                break;
            case 20:    //foggy
            case 21:    //haze
                mImageView.setImageResource(R.drawable.ic_wi_day_haze);
                break;
            case 22:    //smoky
            case 23:    //blustery
            case 24:    //windy
                mImageView.setImageResource(R.drawable.ic_wi_windy);
                break;
            case 25:    //cold
            case 26:    //cloudy
                mImageView.setImageResource(R.drawable.ic_wi_cloudy);
                break;
            case 27:    //mostly cloudy (night)
                mImageView.setImageResource(R.drawable.ic_wi_night_alt_cloudy_high);
                break;
            case 28:    //mostly cloudy (day)
                mImageView.setImageResource(R.drawable.ic_wi_day_cloudy_high);
                break;
            case 29:    //partly cloudy (night)
                mImageView.setImageResource(R.drawable.ic_wi_night_alt_partly_cloudy);
                break;
            case 30:    //partly cloudy (day)
                mImageView.setImageResource(R.drawable.ic_wi_cloudy);
                break;
            case 31:    //clear (night)
            case 32:    //sunny
                mImageView.setImageResource(R.drawable.ic_wi_day_sunny);
                break;
            case 33:    //fair (night)
            case 34:    //fair (day)
            case 35:    //mixed rain and hail
            case 36:    //hot
                mImageView.setImageResource(R.drawable.ic_wi_hot);
                break;
            case 37:    //isolated thunderstorms
            case 38:    //scattered thunderstorms
            case 39:    //scattered thunderstorms
            case 40:    //scattered showers
            case 41:    //heavy snow
            case 42:    //scattered snow showers
            case 43:    //heavy snow
            case 44:    //partly cloudy
            case 45:    //thundershowers
            case 46:    //snow showers
            case 47:    //isolated thundershowers
            case 3200:  //not available
            default:
                break;
        }
        mTemperatureView.setText(String.valueOf(pData.getTemperature()) + " " + getString(R.string.Celcius));
        mConditionTextView.setText(pData.getText());
    }

    @Override
    public void showError(String pMessage) {
    }
}
