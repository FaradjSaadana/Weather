package fr.faradj.weather;

import android.app.Application;
import com.google.android.libraries.places.api.Places;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Places.initialize(getApplicationContext(), BuildConfig.PLACE_API_KEY);
    }
}
