package eightfold.lld.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class Education {
    private String type;
    private School school;
    private String specialization;
    private List<String> otherSubjects;
    private List<String> ancillarySubjects;
}
