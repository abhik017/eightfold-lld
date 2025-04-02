package eightfold.lld.service;

import eightfold.lld.model.Candidate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateStorageService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String OUTPUT_DIRECTORY = "./output";

    public Path getOutputDirectoryAbsolutePath() {
        return Paths.get("").toAbsolutePath().resolve(OUTPUT_DIRECTORY);
    }

    public void storeCandidate(Candidate candidate) {
        try {
            String customerName = candidate.getId().split("_")[0];
            String fileName = candidate.getId().split("_")[1] + ".json";
            Path outputDir = Paths.get(getOutputDirectoryAbsolutePath().toString(), customerName);
            if (!Files.exists(outputDir)) {
                log.info("Creating directory: " + outputDir);
                Files.createDirectories(outputDir);
            }
            File outputFile = outputDir.resolve(fileName).toFile();
            log.info("Storing candidate file: " + outputFile.getAbsolutePath());
            objectMapper.writeValue(outputFile, candidate);
        } catch (IOException e) {
            log.error("Error encountered while storing candidate: " + e.getMessage());
        }
    }

    public void removeCandidate(String candidateId) {
        try {
            String customerName = candidateId.split("_")[0];
            String candidate = candidateId.split("_")[1];
            Path directory = Paths.get(getOutputDirectoryAbsolutePath().toString(), customerName);
            if (Files.exists(directory)) {
                File outputFile = directory.resolve(candidate + ".json").toFile();
                if (outputFile.exists()) {
                    log.info("Hard deleting candidate file: " + outputFile.getAbsolutePath());
                    Files.delete(outputFile.toPath());
                }
            }
        } catch (IOException e) {
            log.error("Error encountered while removing candidate: " + e.getMessage());
        }
    }
}