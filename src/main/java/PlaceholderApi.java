import service.TodosService;
import service.UserService;

import static properties.Constants.userJson;

public class PlaceholderApi {
    public static void main(String[] args) throws Exception {
        UserService userService = new UserService();
        TodosService todosService = new TodosService();
        //task1
        System.out.println("userService.createUser(userJson) = " + userService.createUser(userJson));
        System.out.println("userService.updateUser(\"1\", userJson) = " + userService.updateUser("1", userJson));
        System.out.println("userService.deleteUser(\"1\") = " + userService.deleteUser("1"));
        System.out.println("userService.getAllUsers() = " + userService.getAllUsers());
        System.out.println("userService.getUserById(\"1\") = " + userService.getUserById("1"));
        System.out.println("userService.getUserByUsername(\"Bret\") = " + userService.getUserByUsername("Bret"));
        System.out.println("=".repeat(100));
        //task2
        userService.saveCommentsOfLastPostToFile("1");
        System.out.println("=".repeat(100));
        //task3
        System.out.println("Open tasks for user 1:");
        todosService.getOpenTasksByUserId("1");


    }
}
