package pacman.controllers.uninformed;

import pacman.game.Constants;
import pacman.game.Game;

import static pacman.game.Constants.MOVE;

/**
 * Created by jay on 10/23/16.
 */
public class Node {
    private final Game game;
    private final Node parent;
    private MOVE move;
    private int depth;


    public Node(Game game, Node parent, MOVE move, int depth) {
        this.game = game;
        this.parent = parent;
        this.move = move;
        this.depth = depth;
    }

    public Game getGame() {
        return game;
    }
    public Node getParent() {
        return parent;
    }



    public Constants.MOVE getFirstMove() {
        Node node = this;
        if(node.getParent() == null) {
            return MOVE.NEUTRAL;
        }
        while (node.getParent().getParent() != null) {
            node = node.getParent();
        }
        return node.move;
    }


    public MOVE getMove() {
        return move;
    }

    public void setMove(MOVE move) {
        this.move = move;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
