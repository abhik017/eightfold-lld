package eightfold.lld.service.parsers.util;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonParserUtil {

    public static List<String> createOtherSubjects(JsonNode otherSubjectsNode) {
        List<String> otherSubjects = new ArrayList<>();
        if (otherSubjectsNode == null || otherSubjectsNode.isNull()) {
            return otherSubjects;
        }
        for (JsonNode node : otherSubjectsNode) {
            otherSubjects.add(node.asText());
        }
        return otherSubjects;
    }

}
