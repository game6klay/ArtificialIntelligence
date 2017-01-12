package pacman.controllers.evolution;

import pacman.game.Constants;
import pacman.game.Game;

import java.util.ArrayList;

/**
 * Created by jay on 10/25/16.
 */
public class Person implements Comparable<Person> {


    private Game game;
    private ArrayList<Constants.MOVE> sequence;
    private int fitness;

    public Person(Game game, ArrayList<Constants.MOVE> sequence, int fitness) {
        this.game = game;
        this.sequence = sequence;
        this.fitness = fitness;
    }



    @Override
    public int compareTo(Person o) {
        return (10 * Integer.compare(this.fitness, o.fitness));
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ArrayList<Constants.MOVE> getSequence() {
        return sequence;
    }

    public void setSequence(ArrayList<Constants.MOVE> sequence) {
        this.sequence = sequence;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

}
