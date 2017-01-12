package supervised;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jay on 10/6/16.
 */
public class Perceptron {

    public static final int PERIOD = 300;

    public static String trainingDataPath;
    public static String testingDataPath;
    public double[] attributes = new double[9];

    Path trainingPath = Paths.get("/Data Files/votes-train.csv");
    Path testingPath = Paths.get("/Data Files/votes-test.csv");


    public double theta;
    public double rate;
    public static TrainDataSet trainDataSet;

    private Random random;

    public Perceptron(String trainingDataPath) throws IOException {
        this.trainingDataPath = trainingPath.toString();
        this.testingDataPath = testingPath.toString();

        CSVReader reader = null;
        List<String[]> list;
        reader = new CSVReader(new FileReader(trainingDataPath));
        list = reader.readAll();

        trainDataSet = new TrainDataSet((ArrayList<String[]>) list);

        this.theta = 0.3d;
        this.rate = 0.2d;

        initiateAttributes(attributes);
    }

    private void initiateAttributes(double[] attritubutes) {

        int i;
        for (i = attritubutes.length -1; i >0; i--) {
            attritubutes[i] = random.nextDouble();
        }
    }

    public static void main(String[] args) throws IOException {

        Perceptron percept = new Perceptron(trainingDataPath);

        percept.train();

        CSVReader reader = null;
        List<String[]> list;
        reader = new CSVReader(new FileReader(testingDataPath));
        list = reader.readAll();

        percept.perceptAndPredict(list);

    }

    private void perceptAndPredict(List<String[]> list) {

        int counter =0;
        int success = 0;
        for (String[] string : list) {

            County test = trainDataSet.convert(string);

            int actual = test.votedFor ,forecast;
            int i;
            double total = 0;
            for (i = attributes.length -1; i >0; i--) {

                total += attributes[i]*test.values[i];
            }

            if (total >= theta ) {
                forecast = 1;
            }

            else {forecast = 0;}

            if (actual == forecast) {
                success++;

            }

            else {counter++;}

        }
    }

    private void train() {

        int iter =0 ;
        int err = -1;

        while (err != 0 || iter > PERIOD ) {

            err =0 ;
            for (County county : trainDataSet) {

                int forecast;
                int errorByIteration;
                int i;
                double total = 0;
                for (i = attributes.length -1; i >0; i--) {

                    total += attributes[i]*county.values[i];
                }

                if (total >= theta ) {
                    forecast = 1;
                }

                else{
                    forecast = 0;}

                errorByIteration = county.votedFor - forecast;
                int j;
                for (j = attributes.length -1 ; j >0; j--) {
                    attributes[i] += rate * errorByIteration * county.values[i];
                }
                err += Math.pow(errorByIteration,2);


            }

            iter ++;


        }

    }


}


