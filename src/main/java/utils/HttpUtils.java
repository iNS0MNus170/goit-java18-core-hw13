package utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
    public static String sendRequest(HttpUriRequest request, boolean returnEntity) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpResponse response = httpClient.execute(request);
            if (returnEntity) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            } else {
                return Integer.toString(response.getStatusLine().getStatusCode());
            }
        }
    }
}
