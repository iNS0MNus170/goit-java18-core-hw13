package dto;

import lombok.Data;

@Data
public class Post {
    public int userId;
    public int id;
    public String title;
    public String body;
}
