package dto;

import lombok.Data;

@Data
public class Todo{
    public int userId;
    public int id;
    public String title;
    public boolean completed;
}
