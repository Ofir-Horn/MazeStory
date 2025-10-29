package Server;

import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.ISearchingAlgorithm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configurations {
    private static Configurations instance = null;
    private static Properties properties;
    private int threadPoolSize;

    private Configurations() {
        loadProperties();
        loadThreadPoolSize();
    }

    private void loadProperties() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadThreadPoolSize() {
        String threadPoolSizeValue = properties.getProperty("threadPoolSize");
        if (threadPoolSizeValue != null) {
            try {
                threadPoolSize = Integer.parseInt(threadPoolSizeValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public static Configurations getInstance() {
        if (instance == null) {
            instance = new Configurations();
        }
        return instance;
    }

    public IMazeGenerator getMazeAlgo() {
        String mazeAlgoValue = properties.getProperty("mazeGeneratingAlgorithm");
        if (mazeAlgoValue.equals("EmptyMazeGenerator")) {
            return new EmptyMazeGenerator();
        }
        if (mazeAlgoValue.equals("SimpleMazeGenerator")) {
            return new SimpleMazeGenerator();
        } else {
            return new MyMazeGenerator();
        }
    }

    public static ISearchingAlgorithm getSearchAlgo() {
        String searchAlgoValue = properties.getProperty("mazeSearchingAlgorithm");
        if (searchAlgoValue.equals("BreadthFirstSearch")) {
            return new BreadthFirstSearch();
        }
        if (searchAlgoValue.equals("DepthFirstSearch")) {
            return new DepthFirstSearch();
        } else {
            return new BestFirstSearch();
        }
    }

    public int getTPSize() {
        return threadPoolSize;
    }
}
