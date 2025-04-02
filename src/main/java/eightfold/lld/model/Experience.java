package eightfold.lld.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Experience {
    private String start;
    private String end;
    private String jobTitle;
    private Organization organization;

}
