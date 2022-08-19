package service.developers;

import lombok.Data;

@Data
public class Developer {
    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private Sex sex;

    public enum Sex {
        male,
        female,
        other
    }
}
