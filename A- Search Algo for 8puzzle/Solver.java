import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by dshen on 3/26/2017.
 */

public class Solver {

    private final class SearchNode{
        private SearchNode prevNode;
        private int moves;
        private Board board;
        private int priority;
        public SearchNode(Board board, int moves, SearchNode previous){
            this.board = board;
            this.moves = moves;
            this.prevNode = previous;
        }
        public int hValue(){
            return board.manhattan();
        }
        public int fValue(){
            return hValue() + moves;
        }

    }
    private final class NodeComparator implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b){
            return a.fValue() - b.fValue();
        }
    }
    private final boolean isSolvable;
    private final SearchNode finalNode;
    /**
     * find a solution to the initial board (using the A* algorithm)
     * @param initial
     */
    public Solver(Board initial){

        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>(new NodeComparator());
        MinPQ<SearchNode> minTwinPQ = new MinPQ<SearchNode>(new NodeComparator());
        SearchNode curtMain = new SearchNode(initial, 0, null);
        minPQ.insert(curtMain);
        SearchNode curtTwin = new SearchNode(initial.twin(), 0, null);
        minTwinPQ.insert(curtTwin);
        boolean found = true;
        while(true){
            curtMain = minPQ.delMin();
            if(curtMain.board.isGoal()){
                break;
            }
            curtTwin = minTwinPQ.delMin();
            if(curtTwin.board.isGoal()){
                // break when the twin board reaches goal
                found = false;
                break;
            }

            for(Board neighbor : curtMain.board.neighbors()){
                if(curtMain.prevNode != null && neighbor.equals(curtMain.prevNode.board)){
                    continue;
                }
                SearchNode newNode = new SearchNode(neighbor, curtMain.moves + 1, curtMain);
                minPQ.insert(newNode);
            }

            for(Board neighbor : curtTwin.board.neighbors()){
                if(curtTwin.prevNode != null && neighbor.equals(curtTwin.prevNode.board)){
                    continue;
                }
                SearchNode newNode = new SearchNode(neighbor, curtTwin.moves + 1, curtTwin);
                minTwinPQ.insert(newNode);
            }
        }

        isSolvable = found;
        finalNode = isSolvable ? curtMain : null;
    }

    /**
     * is the initial board solvable?
     * @return
     */
    public boolean isSolvable(){
        return isSolvable;
    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable
     * @return
     */
    public int moves(){
        if(!isSolvable){
            return -1;
        }
        return finalNode.moves;
    }
    /**
     * sequence of boards in a shortest solution; null if unsolvable
     * @return
     */
    public Iterable<Board> solution(){
        if(!isSolvable){
            return null;
        }
        SearchNode tempCurt = finalNode;
        Stack<Board> stack = new Stack<>();
        while(tempCurt != null){
            stack.push(tempCurt.board);
            tempCurt = tempCurt.prevNode;
        }
        return stack;
    }

    /**
     * solve a slider puzzle
     * @param args
     */
    public static void main(String[] args){

        String fileName = args[0];
        In in = new In(fileName);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
