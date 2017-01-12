package supervised;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;


/**
 * Created by jay on 10/6/16.
 */
public class TrainDataSet implements Iterable<County> {

    List<County> counties = new ArrayList<>(5000);

    double[] maxValues = new double[9];
    double[] minValues = new double[9];

    public TrainDataSet (ArrayList<String[]> counties) {

        initiateArray(maxValues,minValues);

        for (String[] county:counties) {
            County c1 = convert(county);
            this.counties.add(c1);
        }

    }

    public County convert(String[] county) {

        int votedFor;

        double[] fields = new double[9];
        try {
            votedFor = Integer.parseInt(county[0]);
        } catch (NumberFormatException nfe) {
            return null;
        }
        for (int i = 1; i < county.length; i++) {
            double val = Integer.parseInt(county[0]);
            fields[i - 1] = (val - minValues[i - 1]) / (maxValues[i - 1] - minValues[i - 1]);
        }

        return new County(votedFor,fields);

    }



    public void initiateArray(double[] maxValues, double[] minValues){

        for (int i =0;i<9;i++) {

            maxValues[i] = Double.MAX_VALUE;
            minValues[i] = Double.MIN_VALUE;

        }

    }



    @Override
    public Iterator<County> iterator() {
        return counties.iterator();
    }

}


