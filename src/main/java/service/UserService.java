package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.User;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static properties.Constants.BASE_URL;
import static utils.HttpUtils.sendRequest;

public class UserService {
    private ObjectMapper objectMapper = new ObjectMapper();

    public List<User> getAllUsers() throws Exception {
        String jsonResponse = sendRequest(new HttpGet(BASE_URL), true);
        return objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });
    }

    public User getUserById(String id) throws Exception {
        String jsonResponse = sendRequest(new HttpGet(BASE_URL + "/" + id), true);
        return objectMapper.readValue(jsonResponse, User.class);
    }

    public Optional<User> getUserByUsername(String username) throws Exception {
        String jsonResponse = sendRequest(new HttpGet(BASE_URL + "?username=" + username), true);
        User[] users = objectMapper.readValue(jsonResponse, User[].class);
        return users.length > 0 ? Optional.of(users[0]) : Optional.empty();
    }

    public User createUser(String userJson) throws Exception {
        HttpPost post = new HttpPost(BASE_URL);
        post.setEntity(new StringEntity(userJson));
        post.setHeader("Content-type", "application/json");
        String jsonResponse = sendRequest(post, true);
        return objectMapper.readValue(jsonResponse, User.class);
    }

    public User updateUser(String id, String userJson) throws Exception {
        HttpPut put = new HttpPut(BASE_URL + "/" + id);
        put.setEntity(new StringEntity(userJson));
        put.setHeader("Content-type", "application/json");
        String jsonResponse = sendRequest(put, true);
        return objectMapper.readValue(jsonResponse, User.class);
    }

    public boolean deleteUser(String id) throws Exception {
        sendRequest(new HttpDelete(BASE_URL + "/" + id), false);
        return true;
    }

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
}


