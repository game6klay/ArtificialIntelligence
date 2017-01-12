package pacman.controllers.informed;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.controllers.uninformed.Node;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by jay on 10/24/16.
 */
public class AStarPacman extends Controller<Constants.MOVE> {

    private final Queue<Node> tree = new PriorityQueue<>(new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {
            return Integer.compare(evaluateGameState(o1), evaluateGameState(o2));
        }
    });
    private final Controller<EnumMap<Constants.GHOST, Constants.MOVE>> ghosts = new StarterGhosts();


    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {

        Node bestNode = new Node(game.copy(), null, null, 0);
        tree.add(bestNode);

        while (System.currentTimeMillis()+1 < timeDue) {
            if (tree.isEmpty()) {
                break;
            }
            Node node = tree.poll();
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
                tree.offer(new Node(nextState, node, move, node.getDepth()+1));
            }
            System.out.println();
        }
        tree.clear();
        return bestNode.getFirstMove();
    }


    private int evaluateGameState(Node node) {
        Game tempGame = node.getGame();
        int[] activePills = tempGame.getActivePillsIndices();
        int[] activePowerPills = tempGame.getActivePowerPillsIndices();
        int numPills = activePills.length;
        int numPowerPills = activePowerPills.length;
        int totalPills = numPills + numPowerPills;
        int[] allPills = new int[totalPills];
        int nearestNodeDist = Integer.MAX_VALUE;

        for (int i = 0; i < numPills; i++) {
            allPills[i] = activePills[i];
        }

        for (int i = 0; i < numPills; i++) {

            int dist = tempGame
                    .getManhattanDistance(i,
                            tempGame.getPacmanCurrentNodeIndex());
            if (dist < nearestNodeDist) {
                nearestNodeDist = dist;
            }
        }
        return nearestNodeDist;
        }

    }

