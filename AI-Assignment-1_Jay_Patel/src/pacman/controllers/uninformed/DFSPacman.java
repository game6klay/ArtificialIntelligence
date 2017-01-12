package pacman.controllers.uninformed;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.Stack;

/**
 * Created by jay on 10/23/16.
 */
public class DFSPacman extends Controller<Constants.MOVE> {

    private final Stack<Node> visited = new Stack<>();
    private final StarterGhosts ghosts = new StarterGhosts();

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {

        Node bestNode = new Node(game.copy(), null, null, 0);
        visited.add(bestNode);

        while (System.currentTimeMillis()+1 < timeDue) {
            if (visited.isEmpty()) {
                break;
            }
            Node node = visited.pop();

            if (bestNode.getGame().getScore() < node.getGame().getScore()) {
                bestNode = node;
            }
            Constants.MOVE[] possibleMoves = node.getGame().getPossibleMoves(node.getGame().getPacmanCurrentNodeIndex(),
                    node.getGame().getPacmanLastMoveMade());
            for (Constants.MOVE move: possibleMoves) {
                Game nextState = node.getGame().copy();
                for (int j = 0; j < 4; j++) {
                    nextState.advanceGame(move, ghosts.getMove(nextState.copy(), timeDue));
                }
                visited.push(new Node(nextState, node, move, node.getDepth()+1));
            }
            System.out.println();
        }
        visited.clear();
        return bestNode.getFirstMove();
    }
}
