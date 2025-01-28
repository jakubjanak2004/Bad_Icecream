package Model.LevelManagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

// TODO: make files handling more robust. Handle corrupted formats, missing files and folders, ...
/**
 * This class is for management of levels and is communicating with the game as well as levels folder structure.
 */
public class levelFacade {
    private static final URL LEVELS_FOLDER_PATH = levelFacade.class.getClassLoader().getResource("levels");
    private static final URL SCORE_FOLDER_PATH = levelFacade.class.getClassLoader().getResource("levelsScore");
    Logger logger = Logger.getLogger(levelFacade.class.getName());
    private List<File> allLevelsFiles = new ArrayList<>();
    private List<Level> allLevels = new ArrayList<>();

    public levelFacade() {
        checkFoldersPresence();
        readAllFilesFromDirectory();
        createLevels();
    }

    // TODO: implement checking that the folders do exist
    private void checkFoldersPresence() {
    }

    /**
     * This method will read all levels from a directory Levels.
     */
    public void readAllFilesFromDirectory() {
        try (Stream<Path> paths = Files.list(Paths.get(LEVELS_FOLDER_PATH.toURI()))) {
            allLevelsFiles = paths
                    .filter(path -> !Files.isDirectory(path))
                    .filter(path -> path.toString().endsWith(".csv"))
                    .map(Path::toFile)
                    .peek(file -> logger.warning("Currently reading: " + file.getAbsolutePath()))
                    .filter(File::exists)
                    .sorted(Comparator.naturalOrder())
                    .toList();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void createLevels() {
        for (int i = 0; i < allLevelsFiles.size(); i++) {
            allLevels.add(new Level(getLevelBoard(allLevelsFiles.get(i).getAbsolutePath()), i));
        }
    }

    private int[][] getLevelBoard(String filePath) {
        return Arrays.stream(readAllLinesFromFile(filePath))
                .map(line -> Arrays.stream(line.trim().split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray())
                .toArray(int[][]::new);
    }

    public void setScoreOfLevel(int levelNum, boolean status) {
        try {
            writeToFile(Paths.get(SCORE_FOLDER_PATH.toURI()) + "/" + "level_" + levelNum + ".csv", status + "");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getScoreOfLevel(int levelNum) {
        try {
            return Arrays.stream(readAllLinesFromFile(Paths.get(SCORE_FOLDER_PATH.toURI()) + "/" + "level_" + levelNum + ".csv"))
                    .map(String::trim)
                    .anyMatch(line -> line.contains("true"));

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] readAllLinesFromFile(String filePath) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            return reader.lines()
                    .toArray(String[]::new);
        } catch (IOException e) {
            logger.warning("Could not read file with exception " + e);
            return new String[0];
        }
    }

    private void writeToFile(String filePath, String content) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + filePath, e);
        }
    }


    // getters and setters for level manager
    public List<Level> getAllLevels() {
        return allLevels;
    }
}