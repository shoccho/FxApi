package org.example.apiCaller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiCaller {
    public static ApiResponse callApi(String urlString, String method) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(method.toUpperCase());

        int responseCode = connection.getResponseCode();
        String inputLine;
        StringBuilder response = new StringBuilder();
        System.out.println("Response Code: " + responseCode);
        BufferedReader in;
        if(responseCode>299){
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }else {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return new ApiResponse(responseCode, response.toString());
    }
}
