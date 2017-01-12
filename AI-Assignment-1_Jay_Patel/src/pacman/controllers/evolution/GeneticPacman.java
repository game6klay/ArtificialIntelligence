package pacman.controllers.evolution;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Created by jay on 10/25/16.
 */
public class GeneticPacman extends Controller<Constants.MOVE> {


    private int numGenerations = 10;
    private int populationSize = 50;
    private PriorityQueue<Person> population = new PriorityQueue<>();
    private PriorityQueue<Person> survivedPop = new PriorityQueue<>();
    private final int length = 10;
    private ArrayList<Person> selectedReproducers = new ArrayList<>();
    private ArrayList<Person> unfitPopulation = new ArrayList<>();
    private Random rand = new Random();
    private ArrayList<Constants.MOVE> actionSequence= new ArrayList<>();
    private final Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghosts = new StarterGhosts();
    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {

        for (int i = 0; i < populationSize; i++) {
            Person temp = new Person(game.copy(),generateSequence(length), 0);
            population.add(temp);
        }

        for (int i = 0; i < numGenerations; i++) {

            for (Person person : population) {
                Game currentState = game.copy();
                person.setGame(currentState);
                ArrayList<Constants.MOVE> actionSeq = person.getSequence();
                executeSequence(actionSeq, currentState);
                person.setFitness(currentState.getScore());
                survivedPop.add(person);

            }

            population.clear();

            int newPopulation = 0;

            selectPopulation(populationSize);

            while(newPopulation < populationSize){

                int indexFemale = rand.nextInt(selectedReproducers.size());
                int indexMale = rand.nextInt(selectedReproducers.size());

                if (indexFemale == indexMale) {
                    indexMale = rand.nextInt(selectedReproducers.size());
                }

                Person female = selectedReproducers.get(indexFemale);
                Person male = selectedReproducers.get(indexMale);

                ArrayList<Constants.MOVE> femaleSequence = female.getSequence();
                ArrayList<Constants.MOVE> maleSequence = male.getSequence();

                ArrayList<Constants.MOVE> newSequence = crossoverSequence(femaleSequence, maleSequence);

                Person child = new Person (game.copy(), newSequence, 0);
                newPopulation++;
            }

            selectedReproducers.clear();
            unfitPopulation.clear();

        }
        return population.remove().getSequence().remove(0);
    }

    private ArrayList<Constants.MOVE> crossoverSequence(ArrayList<Constants.MOVE> femaleSequence, ArrayList<Constants.MOVE> maleSequence) {

        ArrayList<Constants.MOVE> sequence = new ArrayList<>();

        for (int i = 0; i < femaleSequence.size(); i++) {
            // Flip a coin to see who gets to pass on their genese
            int parent = rand.nextInt(1);
            if (parent == 0) {
                sequence.add(femaleSequence.get(i));
            } else {
                sequence.add(maleSequence.get(i));
            }
        }
        return sequence;

    }

    private void selectPopulation(int newPopulation) {


        for (int j = 0; j < populationSize / 3; j++) {
            Person temp = survivedPop.remove();
            population.add(temp);
            selectedReproducers.add(temp);
            unfitPopulation.remove(temp);
            newPopulation++;
        }
    }

    private ArrayList<Constants.MOVE> generateSequence(int length) {

        for (int i = 0; i < length; i++) {
            Constants.MOVE move = Constants.MOVE.values()[rand.nextInt(5)];
            actionSequence.add(move);
        }
        return actionSequence;
    }
    private void executeSequence(ArrayList<Constants.MOVE> actionSeq, Game currentState) {

        for (Constants.MOVE move : actionSeq) {
            for (int j = 0; j < 4; j++) {
                currentState.advanceGame(move, ghosts.getMove(currentState.copy(), -1));
            }
        }
    }


}



