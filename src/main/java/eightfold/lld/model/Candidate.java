package eightfold.lld.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class Candidate {
    private String id;
    private String fullName;
    private List<Education> education;
    private List<Experience> experience;
    private List<Skill> skills;
}
