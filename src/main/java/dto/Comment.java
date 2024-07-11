package dto;

import lombok.Data;

@Data
public class Comment{
    public int postId;
    public int id;
    public String name;
    public String email;
    public String body;
}
