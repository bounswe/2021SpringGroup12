package com.group12.beabee.network.mocking;

import java.io.IOException;

import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class MockCall<T> implements Call<T> {

    @Override
    public Response<T> execute() throws IOException {
        return null;
    }

    @Override
    public abstract void enqueue(Callback<T> callback);

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call<T> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }

    @Override
    public Timeout timeout() {
        return null;
    }
}
