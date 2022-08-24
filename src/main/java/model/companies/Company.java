package model.companies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class Company {
    private long id;
    private String companyName;
    private String residence;
}
