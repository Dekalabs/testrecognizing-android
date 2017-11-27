package me.ivmg.dekavoice.service;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.ivmg.dekavoice.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    private static Api instance = null;

    String baseUrl = "https://api.wit.ai/";

    public NetworkClient(final String apiKey) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            builder.addInterceptor(loggingInterceptor);
        }

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization",
                                "Bearer " + apiKey)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        instance = retrofit.create(Api.class);
    }

    public void init(@NonNull String apiKey) {
        new NetworkClient(apiKey);
    }

    public static Api getInstance() {
        return instance;
    }
}
