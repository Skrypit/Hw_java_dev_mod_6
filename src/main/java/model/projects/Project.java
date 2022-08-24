package model.projects;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Project {
    private long id;
    private String projectName;
    private String type;
    private String description;
    private LocalDate creationDate;

}
