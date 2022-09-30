package pe.edu.galaxy.training.parqueaderov1.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Api {

    public static HttpHeaders obtenerHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", Constantes.TYPETOKEN + " " + Constantes.TOKEN);
        headers.set("Content-Type", "application/json");
        return headers;
    }
    public static ResponseEntity<String> fetchApi(String url, HttpMethod httpMethod, HttpHeaders headers) {
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
        return response;
    }
}
