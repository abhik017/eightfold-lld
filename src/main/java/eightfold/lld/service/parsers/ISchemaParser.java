package eightfold.lld.service.parsers;

import eightfold.lld.model.Candidate;
import com.fasterxml.jackson.databind.JsonNode;

public interface ISchemaParser {

    String getSchemaName();

    Candidate transform(String candidateId, JsonNode jsonNode);

}
