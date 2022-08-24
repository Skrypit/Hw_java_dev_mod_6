package model.projects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Setter
@Getter
public class ProjectFormat {
    private LocalDate creationDate;
    private String projectName;
    private int totalDevelopers;
}