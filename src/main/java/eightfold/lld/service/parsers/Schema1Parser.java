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

@Component
@RequiredArgsConstructor
public class Schema1Parser implements ISchemaParser {

    public static final String SCHEMA_NAME = "Schema1";

    @Override
    public String getSchemaName() {
        return SCHEMA_NAME;
    }

    @Override
    public Candidate transform(String candidateId, JsonNode jsonNode) {
        return Candidate.builder()
                .id(candidateId)
                .fullName(jsonNode.get("first_name").asText() + " " + jsonNode.get("last_name").asText())
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
        return Education.builder()
                .type(educationNode.get("type").asText())
                .school(createSchool(educationNode.get("school").asText(), educationNode.get("country").asText(), educationNode.get("state").asText()))
                .specialization(educationNode.get("primary_subject").asText())
                .otherSubjects(CommonParserUtil.createOtherSubjects(educationNode.get("other_subjects")))
                .build();
    }

    private School createSchool(String schoolName, String country, String state) {
        return School.builder()
                .name(schoolName)
                .country(country)
                .state(state)
                .build();
    }

    private Experience createExperience(JsonNode experienceNode) {
        return Experience.builder()
                .start(experienceNode.get("from").asText())
                .jobTitle(experienceNode.get("title").asText())
                .end(experienceNode.get("to").asText())
                .organization(createOrganization(experienceNode.get("company").asText(), experienceNode.get("country").asText(),
                        experienceNode.get("state").asText(), experienceNode.get("city").asText()))
                .build();
    }

    private Organization createOrganization(String name, String country, String state, String city) {
        return Organization.builder()
                .name(name)
                .country(country)
                .state(state)
                .city(city)
                .build();
    }

    private Skill createSkill(JsonNode skillNode) {
        return Skill.builder()
                .name(skillNode.asText())
                .build();
    }

    private List<Skill> createSkillsList(JsonNode skillsNode) {
        List<Skill> skills = new ArrayList<>();
        for (JsonNode node : skillsNode) {
            skills.add(createSkill(node));
        }
        return skills;
    }
}
