package test.practice.Test.Practice.services;

import java.io.Reader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import test.practice.Test.Practice.models.Currency;
import test.practice.Test.Practice.repositories.CurrencyRepo;


@Service
public class CurrencyServices {

    private static final String URL = "https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD,SGD,EUR,MYR,GBP";

    @Value("${API_KEY}")
    private String key;

    @Autowired
    private CurrencyRepo currencyRepo;

    public List<Currency> getCurrency(String ctype) {

        // Check if we have the weather cached
        Optional<String> opt = currencyRepo.get(ctype);
        String payload;

        System.out.printf(">>> Currency type: %s\n", ctype);

        // Check if the box is empty
        if (opt.isEmpty()) {

            System.out.println("Getting currency from CryptoCompare");

            try {
                // Create the url with query string
                String url = UriComponentsBuilder.fromUriString(URL)
                    .queryParam("q", URLEncoder.encode(ctype, "UTF-8"))
                    .queryParam("appid", key)
                    .toUriString();

                // Create the GET request, GET url
                RequestEntity<Void> req = RequestEntity.get(url).build();

                // Make the call to OpenWeatherMap
                RestTemplate template = new RestTemplate();
                ResponseEntity<String> resp;

                // Throws an exception if status code not in between 200 - 399
                resp = template.exchange(req, String.class);

                // Get the payload and do something with it
                payload = resp.getBody();
                System.out.println("payload: " + payload);

                currencyRepo.save(ctype, payload);
            } catch (Exception ex) {
                System.err.printf("Error: %s\n", ex.getMessage());
                return Collections.emptyList();
            }
        } else {
            // Retrieve the value for the box
            payload = opt.get();
            System.out.printf(">>>> cache: %s\n", payload);
        }

        // Convert payload to JsonObject
        // Convert the String to a Reader
        Reader strReader = new StringReader(payload);
        // Create a JsonReader from Reader
        JsonReader jsonReader = Json.createReader(strReader);
        // Read the payload as Json object
        JsonObject currencyResult = jsonReader.readObject();
        JsonArray ctypes = currencyResult.getJsonArray("currency");
        List<Currency> list = new LinkedList<>();
        for (int i = 0; i < ctypes.size(); i++) {
            // weather[0]
            JsonObject jo = ctypes.getJsonObject(i);
            list.add(Currency.create(jo));
        }
        return list;
    }

}
