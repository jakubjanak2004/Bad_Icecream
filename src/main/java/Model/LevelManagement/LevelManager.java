package Model.LevelManagement;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class is for management of levels and is communicating with the game as well as levels folder structure.
 */
public class LevelManager {
    private static final URL LEVELS_FOLDER_PATH = LevelManager.class.getClassLoader().getResource("levels");
    private static final URL SCORE_FOLDER_PATH = LevelManager.class.getClassLoader().getResource("levelsScore");

    private final ArrayList<File> allLevelsFiles = new ArrayList<>();
    private final ArrayList<Level> allLevels = new ArrayList<>();

    Logger logger = Logger.getLogger(LevelManager.class.getName());

    public LevelManager() {
        readAllFromDir();
        createLevels();
    }

    /**
     * This method will read all levels from a directory Levels.
     */

    public void readAllFromDir() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LEVELS_FOLDER_PATH.toURI()))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path) && path.toString().endsWith(".csv")) {
                    File file = new File(path.toString());
                    logger.warning("Currently reading: " + file.getAbsolutePath());
                    if (file.exists()) {
                        allLevelsFiles.add(file);
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        allLevelsFiles.sort(Comparator.naturalOrder());
    }

    /**
     * This method will get all levels from the folder and will create Level objects for them.
     */
    public void createLevels() {
        int count = 0;
        for (File file : allLevelsFiles) {
            allLevels.add(new Level(getLevelBoard(file.getAbsolutePath()), count));
            count++;
        }
    }

    // getters and setters for level manager
    public ArrayList<Level> getAllLevels() {
        return allLevels;
    }

    public int[][] getLevelBoard(String filePath) {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[][] array = records.stream()
                .map(l -> l.toArray(String[]::new))
                .toArray(String[][]::new);

        int[][] returnArray = new int[array.length][array[0].length];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                returnArray[i][j] = Integer.parseInt(array[i][j]);
            }
        }


        return returnArray;
    }

    public void setScoreOfLevel(int levelNum, boolean status) {
        File file;

        try {
            file = new File(Paths.get(SCORE_FOLDER_PATH.toURI()) + "/" + "level_" + levelNum + ".csv");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        BufferedWriter bw;
        FileWriter fw;

        try {
            fw = new FileWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bw = new BufferedWriter(fw);
        try {
            logger.warning("Currently writing: " + status);
            bw.write(status + "");
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getScoreOfLevel(int levelNum) {
        File file;

        try {
            file = new File(Paths.get(SCORE_FOLDER_PATH.toURI()) + "/" + "level_" + levelNum + ".csv");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        BufferedReader bReader = null;

        if (!file.exists()) {
            return false;
        }

        try {
            bReader = new BufferedReader(new FileReader(file));

            String line;

            if ((line = bReader.readLine()) != null) {
                if (line.contains("true")) {
                    return true;
                }
            }
            bReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}