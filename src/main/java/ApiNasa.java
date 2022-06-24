import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ApiNasa {

    final static String URL = "https://api.nasa.gov/planetary/apod?api_key=UksQjv3TwlzNTUJqeYVJjPlrtT3Sa5wjkCfF4S4x";

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet(URL);
        CloseableHttpResponse response = httpClient.execute(request);

        NasaResponse myResponse = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
        });
        HttpGet request2 = new HttpGet(myResponse.getUrl());
        CloseableHttpResponse response2 = httpClient.execute(request2);
        File nasaFile = new File(myResponse.getUrl().split("/")[myResponse.getUrl().split("/").length - 1]);
        try (FileOutputStream fos = new FileOutputStream(nasaFile)) {
            fos.write(response2.getEntity().getContent().readAllBytes());
        } catch (IOException e) {
            e.getMessage();
        }

    }
}
