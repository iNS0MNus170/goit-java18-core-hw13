package service;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static properties.Constants.BASE_URL;
import static utils.HttpUtils.sendRequest;

public class TodosService {
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
