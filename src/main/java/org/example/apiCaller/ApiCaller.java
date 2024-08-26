package org.example.apiCaller;

import okhttp3.*;
import org.example.state.State;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class ApiCaller {
    private final State state;
    private final OkHttpClient client;

    public ApiCaller(State state) {
        this.state = state;
        client = new OkHttpClient();
    }

    public ApiResponse callApi() throws IOException {
        String url = this.state.getUrl();
        String method = this.state.getMethod().toLowerCase();

        if (method.equals("get")) {
            return executeGetRequest(url);
        } else if (method.equals("post")) {
            return executePostRequest(url);
        } else {
            throw new UnsupportedOperationException("Unsupported method: " + method);
        }
    }

    private ApiResponse executeGetRequest(String url) throws IOException {
        String queryString = buildQueryString(this.state.getState("queries"));
        Request request = new Request.Builder()
                .url(url + "?" + queryString)
                .build();

        return executeRequest(request);
    }

    private ApiResponse executePostRequest(String url) throws IOException {
        String bodyJson = buildBodyJson(this.state.getState("body"));
        RequestBody requestBody = RequestBody.create(
                bodyJson, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        return executeRequest(request);
    }

    private String buildQueryString(JSONObject queries) {
        StringBuilder queryString = new StringBuilder();
        Iterator<String> keys = queries.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject child = new JSONObject(queries.getString(key));
            String childKey = child.keys().next();
            String value = child.getString(childKey);

            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append(childKey).append("=").append(value);
        }

        return queryString.toString();
    }

    private String buildBodyJson(JSONObject body) {

        Iterator<String> keys = body.keys();
        StringBuilder bodyJson = new StringBuilder();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = body.getString(key);
            if (!bodyJson.isEmpty()) {
                bodyJson.append(",");
            }
            bodyJson.append(value);
        }
        return bodyJson.toString();
    }

    private ApiResponse executeRequest(Request request) throws IOException {
        System.out.println("Request URL: " + request.url());
        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return new ApiResponse(response.code(), response.body().string());
            } else {
                return new ApiResponse(response.code(), "");
            }
        }
    }
}
