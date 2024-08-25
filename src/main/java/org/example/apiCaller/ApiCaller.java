package org.example.apiCaller;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.state.State;

import java.io.IOException;

public class ApiCaller {
    private final State state;

    public ApiCaller(State state) {
        this.state = state;
    }

    public ApiResponse callApi() throws IOException {
        String url = this.state.getUrl();
        String method = this.state.getMethod();
        if (method.equalsIgnoreCase("get")) {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            OkHttpClient client = new OkHttpClient();

            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                return new ApiResponse(response.code(), response.body().string());
            } catch (IOException e) {
                throw new IOException(e);
            }
        } else {
            throw new Error("UnImplemented"+method);
        }

    }

}
