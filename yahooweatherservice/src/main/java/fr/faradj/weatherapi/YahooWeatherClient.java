package fr.faradj.weatherapi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;

import java.io.IOException;
import java.net.URISyntaxException;

import fr.faradj.weatherapi.pojo.ForecastResult;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YahooWeatherClient {
    private YahooWeatherApi mApi;

    public interface Callback {
        void onResponse(@NonNull ForecastResult pResult);
        void onFailure(@Nullable String pMessage, @Nullable Throwable pThrowable);
    }

    public void getForecast(String pLocation, final Callback pCallback) {
        getClient().getForecast(pLocation).enqueue(new retrofit2.Callback<ForecastResult>() {
            @Override
            public void onResponse(@NonNull Call<ForecastResult> call, @NonNull retrofit2.Response<ForecastResult> response) {
                if (response.body() != null) {
                    pCallback.onResponse(response.body());
                } else {
                    pCallback.onFailure(response.message(), null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastResult> call, @NonNull Throwable t) {
                pCallback.onFailure(t.getMessage(), t);
            }
        });
    }

    public void getForecast(@NonNull String pLatitude,@NonNull String pLongitude, @NonNull final Callback pCallback) {
        getClient().getForecast(pLatitude, pLongitude).enqueue(new retrofit2.Callback<ForecastResult>() {
            @Override
            public void onResponse(@NonNull Call<ForecastResult> call, @NonNull retrofit2.Response<ForecastResult> response) {
                if (response.body() != null) {
                    pCallback.onResponse(response.body());
                } else {
                    pCallback.onFailure(response.message(), null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastResult> call, @NonNull Throwable t) {
                pCallback.onFailure(t.getMessage(), t);
            }
        });
    }

    @NonNull
    private YahooWeatherApi getClient() {
        if (mApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(YahooWeatherApi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getAuthorizedClient())
                    .build();
            mApi = retrofit.create(YahooWeatherApi.class);
        }
        return mApi;
    }

    @NonNull
    private OkHttpClient getAuthorizedClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        OAuthConsumer consumer = new OAuthConsumer(null, BuildConfig.YAHOO_CONSUMER_KEY, BuildConfig.YAHOO_CONSUMER_SECRET, null);
                        consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
                        OAuthAccessor accessor = new OAuthAccessor(consumer);
                        try {
                            OAuthMessage oAuthMessage = accessor.newRequestMessage(OAuthMessage.GET, chain.request().url().toString(), null);
                            String authorization = oAuthMessage.getAuthorizationHeader(null);
                            Request request = chain.request().newBuilder()
                                    .addHeader("Authorization", authorization)
                                    .addHeader("Yahoo-App-Id", BuildConfig.YAHOO_APP_ID)
                                    .addHeader("Content-Type", "application/json")
                                    .build();
                            return chain.proceed(request);
                        } catch (OAuthException pE) {
                            pE.printStackTrace();
                        } catch (URISyntaxException pE) {
                            pE.printStackTrace();
                        }
                        return chain.proceed(chain.request());
                    }
                }).build();
    }


}
