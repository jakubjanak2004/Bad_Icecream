package LevelManagement;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// -1 ->  player
// 0  ->  regular block
// 1  ->  ice
// 2  ->  STUPID monster
// 3  ->  CLEVER monster
// 4  ->  fruit

public class LevelManager {
    private final String LEVELS_FOLDER_PATH = "/Users/jakubjanak/Desktop/SIT/S2/PJV/BadIcecream/src/main/java/LevelManagement/Levels";
    private final String SCORE_FOLDER_PATH = "/Users/jakubjanak/Desktop/SIT/S2/PJV/BadIcecream/src/main/java/LevelManagement/LevelsScore";
    private final ArrayList<File> allLevelsFiles = new ArrayList<>();
    private final ArrayList<Level> allLevels = new ArrayList<>();

    public ArrayList<Level> getAllLevels() {
        return allLevels;
    }

    public LevelManager() {
        readAllFromDir();
        createLevels();
    }

    public void readAllFromDir() {
        Set<String> fileSet = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LEVELS_FOLDER_PATH))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path) && path.toString().endsWith(".csv")) {
                    File file = new File(path.toString());
                    if (file.exists()) {
                        allLevelsFiles.add(file);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        allLevelsFiles.sort(Comparator.naturalOrder());
    }

    public void createLevels() {
        int count = 0;
        for (File file : allLevelsFiles) {
            allLevels.add(new Level(getLevelBoard(file.getAbsolutePath()), count));
            count++;
        }
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
                .map(l -> l.stream().toArray(String[]::new))
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
        File file = new File(SCORE_FOLDER_PATH + "/" + "level_" + levelNum + ".csv");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bw = new BufferedWriter(fw);
        try {
            bw.write(status + "");
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getScoreOfLevel(int levelNum) {
        File file = new File(SCORE_FOLDER_PATH + "/" + "level_" + levelNum + ".csv");
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}