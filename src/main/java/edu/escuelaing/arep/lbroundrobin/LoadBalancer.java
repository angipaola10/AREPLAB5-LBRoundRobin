package edu.escuelaing.arep.lbroundrobin;

import edu.escuelaing.arep.lbroundrobin.httpclient.LoadBalancerClient;
import com.mashape.unirest.http.HttpResponse;

import static spark.Spark.*;

/**
 * The main class of Load balancer Round Robin application.
 */
public class LoadBalancer
{
    /**
     * The entry point of the Load Balancer application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        port(getPort());
        staticFileLocation("/");
        LoadBalancerClient lbClient = new LoadBalancerClient( new String[]{"http://172.17.0.1:35001", "http://172.17.0.1:35002", "http://172.17.0.1:35003"});
        get("/", (req, res) -> {
            res.redirect("/index.html");
            res.status(200);
            return null;
        });
        post("/messages", (req, res) -> {
            HttpResponse<String> lbResponse = lbClient.postMessage(req.body());
            res.status(lbResponse.getStatus());
            return lbResponse.getBody();
        });
        get("/messages", (req, res) -> {
            HttpResponse<String> lbResponse = lbClient.getMessages();
            res.status(lbResponse.getStatus());
            return lbResponse.getBody();
        });

    }

    /**
     * Gets port to running Load Balancer application.
     *
     * @return the port
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
