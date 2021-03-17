package edu.escuelaing.arep.lbroundrobin.httpclient;

import java.io.IOException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * This class represents a implementation of Load balancer Round Robin as Http client.
 */
public class LoadBalancerClient {

    private String[] serversUrl;
    private Integer currentServerIndex = 0;


    /**
     * Instantiates a new Load balancer.
     *
     * @param serversUrl the log servers url list to execute requests
     */
    public LoadBalancerClient(String[] serversUrl){
        this.serversUrl = serversUrl;
    }

    /**
     * Post message and return http response with this message in the body.
     *
     * @param message the message
     * @return the http response
     * @throws UnirestException the unirest exception
     */
    public HttpResponse<String> postMessage(String message) throws UnirestException {
        String route = getServer() + "/messages";
        HttpResponse<String> response = Unirest.post(route).body(message).asString();
        return response;
    }

    /**
     * Gets messages registered.
     *
     * @return the messages
     * @throws UnirestException the unirest exception
     */
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
