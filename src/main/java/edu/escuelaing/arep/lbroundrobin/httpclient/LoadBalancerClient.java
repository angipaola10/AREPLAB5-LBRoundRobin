package edu.escuelaing.arep.lbroundrobin.httpclient;

import java.io.IOException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class LoadBalancerClient {

    private String[] serversUrl;
    private Integer currentServerIndex = 0;


    public LoadBalancerClient(String[] serversUrl){
        this.serversUrl = serversUrl;
    }

    public HttpResponse<String> postMessage(String message) throws UnirestException {
        String route = getServer() + "/messages";
        HttpResponse<String> response = Unirest.post(route).body(message).asString();
        return response;
    }

    public HttpResponse<String> getMessages() throws UnirestException {
        String route = getServer() + "/messages";
        HttpResponse<String> response = Unirest.get(route).asString();
        return response;
    }

    private String getServer(){
        String server;
        synchronized (currentServerIndex) {
            server = serversUrl[currentServerIndex];
            currentServerIndex += 1;
            if (currentServerIndex == serversUrl.length - 1) currentServerIndex = 0;
        }
        return server;
    }
}
