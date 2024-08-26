package org.example.apiCaller;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.state.State;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class ApiCaller {
    private final State state;

    public ApiCaller(State state) {
        this.state = state;
    }

    public ApiResponse callApi() throws IOException {
        String url = this.state.getUrl();
        String method = this.state.getMethod();
        if (method.equalsIgnoreCase("get")) {
            JSONObject queries = this.state.getState("queries");
            StringBuilder queryString = new StringBuilder();
            Iterator<String> keys = queries.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                String value = queries.getString(key);
                if (!queryString.isEmpty()) {
                    queryString.append("&");
                }
                queryString.append(key).append("=").append(value);
            }
            Request request = new Request.Builder()
                    .url(url+"?"+queryString)
                    .build();

            OkHttpClient client = new OkHttpClient();
            System.out.println(request.url());
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                return new ApiResponse(response.code(), response.body().string());
            } catch (IOException e) {
                throw new IOException(e);
            }
        } else {
            throw new Error("UnImplemented" + method);
        }

    }

}
