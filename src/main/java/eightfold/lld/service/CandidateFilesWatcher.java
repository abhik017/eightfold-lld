package eightfold.lld.service;

import eightfold.lld.model.Candidate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateFilesWatcher implements CommandLineRunner {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CandidateStorageService candidateStorageService;
    private final String INPUT_DIRECTORY  = "/Users/AbhikMehta/Personal Projects/EightfoldLLD/input";

    @Override
    public void run(String... args) throws Exception {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path inputDir = Paths.get(INPUT_DIRECTORY);
        registerAll(inputDir, watchService);
        initialize();
        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                try {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path filePath = ((Path) key.watchable()).resolve((Path) event.context());
                    if (Files.isDirectory(filePath)) {
                        log.info("Directory modification detected: " + filePath);
                        continue; // Ignore directory events
                    }
                    log.info("File modification detected: " + filePath);
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        processFile(filePath);
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        String candidateId = getCandidateIdFromFileName(filePath);
                        candidateStorageService.removeCandidate(candidateId);
                    }
                } catch (Exception ex) {
                    log.error("Error processing modification event: " + ex.getMessage());
                }
            }
            key.reset();
        }
    }
    
    private void initialize() {
        try {
            log.info("Initializing first time parsing of files in directory: " + INPUT_DIRECTORY);
            Files.walk(Paths.get(INPUT_DIRECTORY))
                .filter(path -> path.toString().endsWith(".json"))
                .forEach(this::processFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerAll(Path start, WatchService watchService) throws IOException {
        Files.walk(start)
                .filter(Files::isDirectory)
                .forEach(path -> {
                    try {
                        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void processFile(Path filePath) {
        try {
            String candidateId = getCandidateIdFromFileName(filePath);
            JsonNode jsonNode = objectMapper.readTree(filePath.toFile());
            String schema = determineSchema(filePath);
            Candidate candidate = SchemaTransformerRegistry.transform(schema, candidateId, jsonNode);
            candidateStorageService.storeCandidate(candidate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String determineSchema(Path filePath) {
        // Implement logic to determine the schema based on the file path
        return filePath.getParent().getParent().getFileName().toString(); // Example
    }

    private String getCandidateIdFromFileName(Path filePath) {
        return (filePath.getParent().getFileName() + "_" + filePath.getFileName().toString()).replaceAll("\\.json$", "");
    }
}
