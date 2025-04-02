package eightfold.lld.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Organization {
    private String name;
    private String country;
    private String state;
    private String city;
}
