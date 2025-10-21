package co.edu.eci.MathService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MathServiceController {

    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }

    

    @GetMapping(value="/linearsearch", produces= MediaType.APPLICATION_JSON_VALUE)
    public String linearSearch(@RequestParam("list") String listCsv,
                               @RequestParam("value") String value) {
        String[] items = splitCsv(listCsv);
        int index = -1;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(value)) {
                index = i;
                break;
            }
        }
        return jsonResponse("linearSearch", listCsv, value, index);
    }

    @GetMapping(value="/binarysearch", produces= MediaType.APPLICATION_JSON_VALUE)
    public String binarySearch(@RequestParam("list") String listCsv,
                               @RequestParam("value") String value) {
        String[] items = splitCsv(listCsv);
        int idx = binarySearchRecursive(items, value, 0, items.length - 1);
        if (idx > 0) {
            while (idx - 1 >= 0 && items[idx - 1].equals(value)) {
                idx--;
            }
        }
        return jsonResponse("binarySearch", listCsv, value, idx);
    }

    private int binarySearchRecursive(String[] a, String target, int lo, int hi) {
        if (lo > hi) return -1;
        int mid = lo + (hi - lo) / 2;
        int cmp = a[mid].compareTo(target);
        if (cmp == 0) return mid;
        if (cmp > 0) return binarySearchRecursive(a, target, lo, mid - 1);
        return binarySearchRecursive(a, target, mid + 1, hi);
    }

    private String[] splitCsv(String csv) {
        if (csv == null || csv.isEmpty()) return new String[0];
        String[] raw = csv.split(",");
        for (int i = 0; i < raw.length; i++) {
            raw[i] = raw[i].trim();
        }
        return raw;
    }

    private String jsonResponse(String op, String inputList, String value, int outputIndex) {
        return String.format(
            "{\"operation\":\"%s\",\"inputlist\":\"%s\",\"value\":\"%s\",\"output\":%d}",
            escape(op), escape(inputList), escape(value), outputIndex
        );
    }

    private String escape(String s) {
        return s.replace("\"", "\\\"");
    }
}
