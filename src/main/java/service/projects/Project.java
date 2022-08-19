package service.projects;

import lombok.Data;

@Data
public class Project {
    private long id;
    private String projectName;
    private String type;
    private String description;
}
