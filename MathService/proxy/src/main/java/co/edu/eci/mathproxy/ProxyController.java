
package co.edu.eci.mathproxy;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@RestController
public class ProxyController {

    private final RoundRobinTargetSelector selector;

    public ProxyController(RoundRobinTargetSelector selector) {
        this.selector = selector;
    }

    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/api/targets")
    public String targets() {
        return String.join(",", selector.all());
    }

    @GetMapping(value = "/api/fib", produces = MediaType.TEXT_PLAIN_VALUE)
    public String fib(@RequestParam("n") int n) throws IOException {
        return forward("/api/fib?n=" + n);
    }

    @GetMapping(value = "/api/fact", produces = MediaType.TEXT_PLAIN_VALUE)
    public String fact(@RequestParam("n") int n) throws IOException {
        return forward("/api/fact?n=" + n);
    }

    @GetMapping(value = "/api/isPrime", produces = MediaType.TEXT_PLAIN_VALUE)
    public String isPrime(@RequestParam("n") long n) throws IOException {
        return forward("/api/isPrime?n=" + n);
    }
    
    @GetMapping(value = "/linearsearch", produces = MediaType.APPLICATION_JSON_VALUE)
    public String linearSearch(@RequestParam("list") String listCsv,
                               @RequestParam("value") String value) throws IOException {
        return forward("/linearsearch?list=" + encode(listCsv) + "&value=" + encode(value));
    }

    @GetMapping(value = "/binarysearch", produces = MediaType.APPLICATION_JSON_VALUE)
    public String binarySearch(@RequestParam("list") String listCsv,
                               @RequestParam("value") String value) throws IOException {
        return forward("/binarysearch?list=" + encode(listCsv) + "&value=" + encode(value));
    }

    private String forward(String pathAndQuery) throws IOException {
        String target = selector.next();
        URL url = new URL(target + pathAndQuery);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty(HttpHeaders.USER_AGENT, "Mozilla/5.0");
        con.setConnectTimeout(3000);
        con.setReadTimeout(5000);

        int code = con.getResponseCode();
        BufferedReader reader;
        if (code >= 200 && code < 300) {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        } else {
            reader = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        if (code >= 200 && code < 300) {
            return sb.toString();
        } else {
            return "Upstream error (" + code + "): " + sb;
        }
    }

    private String encode(String s) {
        return s.replace(" ", "%20").replace(",", "%2C");
    }
}
