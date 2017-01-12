package pacman.controllers.optimization;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

import static com.sun.tools.doclint.Entity.copy;

/**
 * Created by jay on 10/24/16.
 */
public class HillClimberPacman extends Controller<Constants.MOVE> {

    Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghosts;
    private final Random rand = new Random();

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {

        NodeTree tree = new NodeTree(20);

        boolean isLocalMaxima = false;

        Node maxima = tree.getHeadNode();

        while ((!isLocalMaxima) && System.currentTimeMillis()+1 < timeDue){

            ArrayList<Node> neighbors = maxima.getNeighbors();
            if (neighbors == null) {
                break;
            }

            Game nextState = maxima.getGameState().copy();
            Constants.MOVE move = Constants.MOVE.values()[rand.nextInt(5)];

            for (int j = 0; j < 4; j++) {

                nextState.advanceGame(move, ghosts.getMove(nextState.copy(), timeDue));
            }

            int randomIndex = rand.nextInt(5);
            Node temp = neighbors.get(randomIndex);

            if (maxima.getGameState().getScore() < temp.getGameState().getScore()) {

                maxima = temp;
            }

        }


        return maxima.getMove();
    }

}

class Node {

    private Game game;
    private ArrayList<Node> neighbors;
    private Node previous;
    private Constants.MOVE move;

    public Node(Constants.MOVE move, Node previous) {
        this.move = move;
        this.previous = previous;
    }

    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

    public Game getGameState() {
        return game;
    }

    public Constants.MOVE getMove() {
        return move;
    }

    public void setMove(Constants.MOVE move) {
        this.move = move;
    }


}

class NodeTree {

    private  Node headNode;
    private ArrayList<Node> currentNodes = new ArrayList<Node>();
    private ArrayList<Node> nextNodes = new ArrayList<Node>();
    Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghosts;
    private long timeDue;

    public NodeTree (int depth){

        headNode = new Node(Constants.MOVE.LEFT, null );
        currentNodes.add(headNode);

        for (Node node : currentNodes){

            Constants.MOVE[] possibleMoves = node.getGameState().getPossibleMoves(node.getGameState().getPacmanCurrentNodeIndex(),
                    node.getGameState().getPacmanLastMoveMade());
            for (Constants.MOVE move: possibleMoves) {
                Game nextState = node.getGameState().copy();
                for (int j = 0; j < depth; j++) {
                    nextState.advanceGame(move, ghosts.getMove(nextState.copy(), timeDue));
                }

                Node nextNode = new Node(move, node);
                ArrayList<Node> neighbors = new ArrayList<Node>(4);

                neighbors.add(nextNode);
            }

            currentNodes = nextNodes;
            nextNodes = new ArrayList<>();

        }
    }

    public Node getHeadNode() {
        return headNode;
    }
}

