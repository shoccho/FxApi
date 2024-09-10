package com.github.shoccho.apiCaller;

import okhttp3.*;
import com.github.shoccho.model.Parameter;
import com.github.shoccho.model.ResponseData;
import com.github.shoccho.state.State;

import java.io.IOException;
import java.util.ArrayList;

public class ApiCaller {
    private final State state;
    private final OkHttpClient client;

    public ApiCaller(State state) {
        this.state = state;
        client = new OkHttpClient();
    }

    public ResponseData callApi() throws IOException {
        String url = this.state.getUrl();
        String method = this.state.getMethod().toLowerCase();

        return switch (method) {
            case "get" -> executeGetRequest(url);
            case "post" -> executePostRequest(url);
            case "put" -> executePutRequest(url);
            case "delete" -> executeDeleteRequest(url);
            default -> throw new UnsupportedOperationException("Unsupported method: " + method);
        };
    }

    private ResponseData executeGetRequest(String url) throws IOException {
        String queryString = buildQueryString(this.state.getState("queries"));
        Request request = new Request.Builder()
                .url(url + "?" + queryString)
                .headers(getHeaders())
                .build();

        return executeRequest(request);
    }

    private ResponseData executeDeleteRequest(String url) throws IOException {
        String queryString = buildQueryString(this.state.getState("queries"));
        Request request = new Request.Builder()
                .url(url + "?" + queryString)
                .headers(getHeaders())
                .delete()
                .build();

        return executeRequest(request);
    }

    private Headers getHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        ArrayList<Parameter> headersJSON = this.state.getState("headers");

        headersJSON.forEach(header -> {
            headerBuilder.add(header.getKey(), header.getValue());
        });
        return headerBuilder.build();
    }

    private ResponseData executePutRequest(String url) throws IOException {
        String bodyJson = "";
        if (this.state.getBodyType().equals("raw")) {
            bodyJson = this.state.getRawBody();
        } else {
            bodyJson = buildBodyJson(this.state.getState("body"));
        }
        RequestBody requestBody = RequestBody.create(
                bodyJson, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .headers(getHeaders())
                .build();

        return executeRequest(request);
    }

    private ResponseData executePostRequest(String url) throws IOException {
        String bodyJson = "";
        if (this.state.getBodyType().equals("raw")) {
            bodyJson = this.state.getRawBody();
        } else {
            bodyJson = buildBodyJson(this.state.getState("body"));
        }

        RequestBody requestBody = RequestBody.create(
                bodyJson, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(getHeaders())
                .build();

        return executeRequest(request);
    }

    private String buildQueryString(ArrayList<Parameter> queries) {
        StringBuilder queryString = new StringBuilder();
        for (Parameter query : queries) {
            String key = query.getKey();
            String value = query.getValue();

            if (!queryString.isEmpty()) {
                queryString.append("&");
            }
            queryString.append(key).append("=").append(value);
        }

        return queryString.toString();
    }

    private String buildBodyJson(ArrayList<Parameter> body) {

        StringBuilder bodyJson = new StringBuilder();
        for (Parameter parameter : body) {
            String key = parameter.getKey();
            String value = parameter.getValue();
            if (!bodyJson.isEmpty()) {
                bodyJson.append(",");
            }
            bodyJson.append("{\"").append(key).append("\":\"").append(value).append("\"}");
        }
        return "{" + bodyJson.toString() + "}";
    }

    private ResponseData executeRequest(Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return new ResponseData(response.code(), response.body().string());
            } else {
                return new ResponseData(response.code(), "");
            }
        }
    }
}
