package com.group12.beabee.network;

import android.content.Intent;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.network.mocking.MockService;
import com.group12.beabee.views.LoginActivity;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeABeeService {

    public static ServiceAPI serviceAPI;
    public final static String BASE_URL = "http://18.117.95.170:8085/";
    public final static String BASE_URL_DEV = "http://3.144.201.198:8085/";
    public final static String BASE_URL_LOCAL = "http://192.168.1.205:8085/v2/";
    private final static boolean isMock = false;

    public static void InitNetworking() {
        if (isMock) {
            serviceAPI = new MockService();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_LOCAL)
//                    .baseUrl(BASE_URL)
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

        builder.addInterceptor(new HeaderInterceptor());
        builder.addInterceptor(new AuthErrorInterceptor());
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

    public static class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            if (BeABeeApplication.AuthToken != null)
                request = request.newBuilder()
                        .addHeader("Authorization", "Bearer " + BeABeeApplication.AuthToken)
                        .build();

            Response response = chain.proceed(request);
            return response;
        }
    }

    public static class AuthErrorInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(request);

            if (response.code() == 401) {
                BeABeeApplication.AuthToken = null;
                BeABeeApplication.userId = -1;
                Intent intent = new Intent(BeABeeApplication.getAppContext(), LoginActivity.class);
                intent.putExtra("Error", "403");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                BeABeeApplication.getAppContext().startActivity(intent);

                return response;
            } else if (response.code() == 403) {

            }

            return response;
        }
    }


}
