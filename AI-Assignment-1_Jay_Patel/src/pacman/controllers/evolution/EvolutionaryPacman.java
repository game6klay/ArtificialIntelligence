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
public class EvolutionaryPacman extends Controller<Constants.MOVE> {

    private int numGenerations = 10;
    private int populationSize = 30;
    private PriorityQueue<Person> population = new PriorityQueue<>();
    private PriorityQueue<Person> survivedPop = new PriorityQueue<>();
    private final int length = 10;
    private ArrayList<Person> mutated = new ArrayList<>();
    private Random rand = new Random();
    private ArrayList<Constants.MOVE> actionSequence= new ArrayList<>();
    private final Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghosts = new StarterGhosts();

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {

        for (int i = 0; i < populationSize; i++) {
            Person temp = new Person(game.copy(),getRandomSequence(length), 0);
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

                int index = rand.nextInt(mutated.size());
                Person willMutate = mutated.get(index);
                ArrayList<Constants.MOVE> oldSeq = willMutate.getSequence();
                ArrayList<Constants.MOVE> mutatedSeq = mutateSequence(oldSeq);
                // Create new mutated candidate and add to population
                Person mutated = new Person(game.copy(), mutatedSeq, 0);
                population.add(mutated);
                newPopulation++;
            }
            mutated.clear();
        }
        return population.remove().getSequence().remove(0);
        }

    private ArrayList<Constants.MOVE> mutateSequence(ArrayList<Constants.MOVE> oldSequence) {

        ArrayList<Constants.MOVE> seq = new ArrayList<>();
            for (int i = 0; i < oldSequence.size(); i++) {

                    Constants.MOVE newMove = Constants.MOVE.values()[rand.nextInt(5)];
                    seq.add(newMove);
                }
                return seq;

            }



    private void selectPopulation(int newPopulation) {

        for (int j = 0; j < populationSize / 3; j++) {
            Person temp = survivedPop.remove();
            population.add(temp);
            mutated.add(temp);
            newPopulation++;
        }

    }

    private ArrayList<Constants.MOVE> getRandomSequence(int length) {

        for (int i = 0; i < length; i++) {
            Constants.MOVE move = Constants.MOVE.values()[rand.nextInt(5)];
            actionSequence.add(move);
        }
        return actionSequence;
    }

    private void executeSequence(ArrayList<Constants.MOVE> actionSeq, Game currentState){

        for (Constants.MOVE move : actionSeq) {
            for (int j = 0; j < 4; j++) {
                currentState.advanceGame(move, ghosts.getMove(currentState.copy(), -1));
            }
        }
    }
}
