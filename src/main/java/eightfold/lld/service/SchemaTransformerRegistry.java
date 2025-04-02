package eightfold.lld.service;

import eightfold.lld.model.Candidate;
import eightfold.lld.service.parsers.ISchemaParser;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchemaTransformerRegistry {
    private static final HashMap<String, ISchemaParser> registry = new HashMap<>();

    @Autowired
    public SchemaTransformerRegistry(List<ISchemaParser> transformers) {
        for (ISchemaParser transformer : transformers) {
            registry.put(transformer.getSchemaName(), transformer);
        }

    }

    public static Candidate transform(String schema, String candidateId, JsonNode jsonNode) {
        ISchemaParser transformer = registry.get(schema);
        if (transformer != null) {
            log.info("Parsing schema: {} for candidate: {}", schema, candidateId);
            return transformer.transform(candidateId, jsonNode);
        }
        throw new IllegalArgumentException("Unknown schema: " + schema);
    }

}
