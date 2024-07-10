import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonPlaceholderAPI {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/users";

    public static void main(String[] args) throws Exception {
        JsonPlaceholderAPI api = new JsonPlaceholderAPI();
        api.saveCommentsOfLastPostToFile("1");
        System.out.println("Open tasks for user 1:");
        api.getOpenTasksByUserId("1");

        String userJson = "{\"name\":\"Alex Poirier\",\"username\":\"Diamond\",\"email\":\"diamond45@gmail.com\"}";
        System.out.println(api.createUser(userJson));
        System.out.println(api.updateUser("1", userJson));
        System.out.println(api.deleteUser("1"));
        System.out.println(api.getAllUsers());
        System.out.println(api.getUserById("1"));
        System.out.println(api.getUserByUsername("Bret"));
    }

    // 1
    public String getAllUsers() throws Exception {
        return sendRequest(new HttpGet(BASE_URL), true);
    }

    public String getUserById(String id) throws Exception {
        return sendRequest(new HttpGet(BASE_URL + "/" + id), true);
    }

    public String getUserByUsername(String username) throws Exception {
        return sendRequest(new HttpGet(BASE_URL + "?username=" + username), true);
    }

    public String createUser(String userJson) throws Exception {
        HttpPost post = new HttpPost(BASE_URL);
        post.setEntity(new StringEntity(userJson));
        post.setHeader("Content-type", "application/json");
        return sendRequest(post, true);
    }

    public String updateUser(String id, String userJson) throws Exception {
        HttpPut put = new HttpPut(BASE_URL + "/" + id);
        put.setEntity(new StringEntity(userJson));
        put.setHeader("Content-type", "application/json");
        return sendRequest(put, true);
    }

    public String deleteUser(String id) throws Exception {
        return sendRequest(new HttpDelete(BASE_URL + "/" + id), false);
    }

    private String sendRequest(HttpUriRequest request, boolean returnEntity) throws Exception {
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

    //2
    public void saveCommentsOfLastPostToFile(String userId) throws Exception {
        String postsJson = sendRequest(new HttpGet(BASE_URL + "/" + userId + "/posts"), true);

        JSONArray postsArray = new JSONArray(postsJson);
        int maxPostId = postsArray.getJSONObject(postsArray.length() - 1).getInt("id");

        URI commentsUri = new URIBuilder(BASE_URL)
                .setPath("/posts/" + maxPostId + "/comments")
                .build();
        String commentsJson = sendRequest(new HttpGet(commentsUri), true);

        String fileName = "src/main/resources" + "user-" + userId + "-post-" + maxPostId + "-comments.json";
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(commentsJson);
            System.out.println("Comments saved to: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //3
    public void getOpenTasksByUserId(String userId) throws Exception {
        String todosJson = sendRequest(new HttpGet(BASE_URL + "/" + userId + "/todos"), true);
        JSONArray todosArray = new JSONArray(todosJson);

        List<JSONObject> openTasks = todosArray.toList().stream()
                .map(item -> new JSONObject((Map) item))
                .filter(task -> !task.getBoolean("completed"))
                .toList();
        openTasks.forEach(task -> System.out.println(task.toString(4)));
    }
}

