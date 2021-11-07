package com.group12.beabee.network;

import android.app.Application;
import android.content.res.Resources;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.network.mocking.MockService;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeABeeService {

    public static ServiceAPI serviceAPI;
    private final static boolean isMock = true;

    public static void InitNetworking() {
        if (isMock) {
            serviceAPI = new MockService();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BeABeeApplication.getAppContext().getString(R.string.base_url_dev))
//                    .baseUrl("https://api.agify.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient())
                    .build();

            serviceAPI = retrofit.create(ServiceAPI.class);
        }
    }

    private static OkHttpClient okHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                // Do anything with response here
                //if we ant to grab a specific cookie or something..


                return response;
            }
        });

        builder.cookieJar(new JavaNetCookieJar(cookieManager));
        builder.addInterceptor(interceptor);

        return builder.build();

    }


}
