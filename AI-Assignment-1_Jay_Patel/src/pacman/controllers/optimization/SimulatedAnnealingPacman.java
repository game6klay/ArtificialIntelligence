package pacman.controllers.optimization;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

/**
 * Created by jay on 10/25/16.
 */
public class SimulatedAnnealingPacman extends Controller<Constants.MOVE> {

    private static final int SEQUENCE_LENGTH = 30;
    private final Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghosts = new StarterGhosts();
    private final long initialTemperature = 50000;
    private final double coolingRate = 0.002d;

    private final Random rand = new Random();




    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        long temp = initialTemperature;
        ActionSequence bestInstance = ActionSequence.generate(game.copy(),30);


        while (System.currentTimeMillis()+1 < timeDue) {
            ActionSequence someInstance = new ActionSequence(game.copy(),30,timeDue);
            int randomIndex = rand.nextInt(SEQUENCE_LENGTH);
            Constants.MOVE randomAction = someInstance.getAction(randomIndex);

            Constants.MOVE move = Constants.MOVE.values()[rand.nextInt(5)];

            if (move.equals(randomAction)){
                continue;
            }
            someInstance = someInstance.changeAction(move,randomIndex);

            int bestScore = bestInstance.bestScore();
            int someScore = someInstance.bestScore();

            if (bestScore<someScore){

                bestInstance = someInstance.changeGameState(game.copy());
                bestScore = someScore;
            }

            temp = (long) (temp - (temp*coolingRate));



        }


        return bestInstance.getAction(rand.nextInt(2));
    }



}

class ActionSequence {

    private static final int SEQUENCE_LENGTH = 30;
    private final Game initialState;
    private final int sequenceLength;
    private final int score;
    private final long timeDue;
    private final Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghosts = new StarterGhosts();
    private final ArrayList<Constants.MOVE> sequence;
    private final Random rand = new Random();


    public ActionSequence(Game initialState, int sequenceLength, long timeDue) {

        this.initialState = initialState;
        this.sequenceLength = SEQUENCE_LENGTH;
        this.timeDue = timeDue;
        this.sequence = new ArrayList<>(sequenceLength);
        this.score = bestScore();

    }

    public int bestScore() {

        Game nextState = initialState.copy();
        int i = 0;
        while (i<sequence.size()) {
            nextState.advanceGame(sequence.get(i), ghosts.getMove(nextState, timeDue));
            i++;
        }
        return nextState.getScore();
    }


    public static ActionSequence generate(Game initialState, int sequenceLength) {
        return generate(initialState, sequenceLength);
    }

    public Constants.MOVE getAction(int i) {
        return sequence.get(i);
    }

    public ActionSequence changeAction(Constants.MOVE move, int i) {
        ActionSequence actionSequence = new ActionSequence(initialState, sequenceLength, this.timeDue);
        actionSequence.sequence.set(i,move);
        return actionSequence;
    }


    public ActionSequence changeGameState(Game game) {

        return new ActionSequence(game, sequenceLength, timeDue);
    }
}
