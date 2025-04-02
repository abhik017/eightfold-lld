package eightfold.lld.service.parsers;

import eightfold.lld.model.Candidate;
import eightfold.lld.model.Education;
import eightfold.lld.model.Experience;
import eightfold.lld.model.Organization;
import eightfold.lld.model.School;
import eightfold.lld.model.Skill;
import eightfold.lld.service.parsers.util.CommonParserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Schema2Parser implements ISchemaParser {

    public static final String SCHEMA_NAME = "Schema2";

    @Override
    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    @Override
    public Candidate transform(String candidateId, JsonNode jsonNode) {
        return Candidate.builder()
                .id(candidateId)
                .fullName(jsonNode.get("full_name").asText())
                .experience(createExperienceList(jsonNode.get("experience")))
                .education(createEducationList(jsonNode.get("education")))
                .skills(createSkillsList(jsonNode.get("skills")))
                .build();
    }

    private List<Experience> createExperienceList(JsonNode experienceNode) {
        List<Experience> experiences = new ArrayList<>();
        for (JsonNode node : experienceNode) {
            experiences.add(createExperience(node));
        }
        return experiences;
    }

    private List<Education> createEducationList(JsonNode educationNode) {
        List<Education> educations = new ArrayList<>();
        for (JsonNode node : educationNode) {
            educations.add(createEducation(node));
        }
        return educations;
    }

    private Education createEducation(JsonNode educationNode) {
        JsonNode specializationNode = Objects.nonNull(educationNode.get("specialization")) ? educationNode.get("specialization") : educationNode.get("primary_subject");
        JsonNode otherSubjects = Objects.nonNull(educationNode.get("other_subjects")) ? educationNode.get("other_subjects") : educationNode.get("ancillary_subjects");
        return Education.builder()
                .type(educationNode.get("type").asText())
                .school(createSchool(educationNode.get("school")))
                .specialization(specializationNode.asText())
                .otherSubjects(CommonParserUtil.createOtherSubjects(otherSubjects))
                .build();
    }

    private School createSchool(JsonNode schoolNode) {
        return School.builder()
                .name(schoolNode.get("name").asText())
                .country(schoolNode.get("country").asText())
                .state(schoolNode.get("state").asText())
                .build();
    }

    private Experience createExperience(JsonNode experienceNode) {
        return Experience.builder()
                .start(experienceNode.get("start").asText())
                .jobTitle(experienceNode.get("job_title").asText())
                .end(experienceNode.get("end").asText())
                .organization(createOrganization(experienceNode.get("organization")))
                .build();
    }

    private Organization createOrganization(JsonNode organizationNode) {
        return Organization.builder()
                .name(organizationNode.get("name").asText())
                .country(organizationNode.get("country").asText())
                .state(organizationNode.get("state").asText())
                .city(organizationNode.get("city").asText())
                .build();
    }

    private List<Skill> createSkillsList(JsonNode skillsNode) {
        List<Skill> skills = new ArrayList<>();
        for (JsonNode node : skillsNode) {
            String category = node.get("category").asText();
            for (JsonNode skill : node.get("values")) {
                skills.add(Skill.builder().name(skill.asText()).category(category).build());
            }
        }
        return skills;
    }
}
