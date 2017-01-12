package supervised;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import com.opencsv.CSVReader;

/**
 * Created by jay on 10/6/16.
 */

public class Knn {


    public  String trainingDataPath;
    public  String testingDataPath;
    public  int K;
    public static TrainDataSet trainDataSet;

    public Knn(String trainingDataPath, String testingDataPath, int K) {
        this.trainingDataPath = Paths.get(trainingDataPath).toString();
        this.testingDataPath = Paths.get(testingDataPath).toString();
        this.K = K;
        CSVReader reader = null;
        List<String[]> list;
        try {
            reader = new CSVReader(new FileReader(trainingDataPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            list = reader.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        trainDataSet = new TrainDataSet((ArrayList<String[]>) list);
    }

    public static void main(String[] args) {

        Knn knn = new Knn("Data Files/votes-train.csv", "Data Files/votes-test.csv", 10);

        knn.test();





    }

    public void test() {

        int correct = 0;
        int total = 0;

        CSVReader reader = null;

        List<String[]> list;


        try {
            reader = new CSVReader(new FileReader(trainingDataPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            list = reader.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String[] string : list) {
            List<County> neighbours = new ArrayList<>();
            County test = trainDataSet.convert(string);
            if (test == null) {
                continue;
            }
            for (County county: trainDataSet) {
                if (county == null) {
                    continue;
                }
                county.distance = county.distanceFrom(test);
                if (neighbours.size()<K) {
                    neighbours.add(county);
                } else {
                    County furthestNeighbour = neighbours.get(0);
                    for (County neighbour : neighbours) {
                        if (neighbour.distance>furthestNeighbour.distance) {
                            furthestNeighbour = neighbour;
                        }
                    }

                    if (county.distance<furthestNeighbour.distance) {
                        neighbours.remove(furthestNeighbour);
                        neighbours.add(county);
                    }
                }


            }

            if (test.votedFor == predict(neighbours)) {
                correct++;
            }

            total++;

        }


        System.out.println("Success rate : " + (correct*100)/total);

    }

    private int predict(List<County> neighbours) {
        int class1 = 0;
        for (County neighbour : neighbours) {
            if (neighbour.votedFor == 1) {
                class1 ++;
            }
        }
        if (class1 > neighbours.size()/2) {
            return 1;
        }
        return 0;
    }


}

class County {

    public int votedFor;
    double[] values;

    public double distance ;

    public County(int votedFor, double[] values) {

        this.votedFor = votedFor;
        this.values = values;

    }

    public double distanceFrom(County other) {
        int i;

        double del = 0;
        for (i = 0; i < this.values.length; i++) {
            del =+ Math.pow(other.values[i] - this.values[i],2);
        }
        del = Math.sqrt(del);
        return del;
    }


}






