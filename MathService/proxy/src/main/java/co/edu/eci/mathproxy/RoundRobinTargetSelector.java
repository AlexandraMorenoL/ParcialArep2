
package co.edu.eci.mathproxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RoundRobinTargetSelector {

    private final List<String> targets = new ArrayList<>();
    private final AtomicInteger idx = new AtomicInteger(0);

    public RoundRobinTargetSelector(@Value("${MATH_TARGETS:}") String targetsEnv) {
        if (targetsEnv == null || targetsEnv.trim().isEmpty()) {
            targets.add("http://localhost:8081");
        } else {
            String[] parts = targetsEnv.split(",");
            for (String p : parts) {
                String t = p.trim();
                if (t.isEmpty()) continue;
                try {
                    new URL(t);
                    targets.add(t);
                } catch (MalformedURLException e) {
                    // ignore invalid entry
                }
            }
            if (targets.isEmpty()) {
                targets.add("http://localhost:8081");
            }
        }
    }

    public String next() {
        int i = Math.abs(idx.getAndIncrement());
        return targets.get(i % targets.size());
    }

    public List<String> all() {
        return new ArrayList<>(targets);
    }
}
