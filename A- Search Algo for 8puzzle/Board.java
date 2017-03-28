public class Board {
    private final int[][] block;
    private final int mDist;
    private final int n;
    private final int blankInt;
    /**
     * construct a board from an n-by-n array of blocks
     * @param blocks
     */
    public Board(int[][] blocks){
        int tempMDist = 0;
        int tempBlankR = 0;
        int tempBlankC = 0;
        n = blocks.length;
        block = new int[n][n];
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                this.block[i][j] = blocks[i][j];
                if(blocks[i][j] == 0){
                    tempBlankR = i;
                    tempBlankC = j;
                }
                if((blocks[i][j] != 0) && rc2Int(i, j) != blocks[i][j]){
                    tempMDist += Math.abs(i - int2R(blocks[i][j])) + Math.abs(j - int2C(blocks[i][j]));
                }
            }
        }
        mDist = tempMDist;
        blankInt = rc2Int(tempBlankR, tempBlankC);
    }

    private int rc2Int(int r, int c){
        return n*r + c + 1;
    }

    private int int2R(int x){
        return (x-1)/n;
    }

    private int int2C(int x){
        return (x-1)%n;
    }
    /**
     * board dimension n
     * @return
     */
    public int dimension(){
        return n;
    }

    /**
     * number of blocks out of place
     * @return
     */
    public int hamming(){
        int tempHDist = 0;
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if((block[i][j] != 0) && rc2Int(i, j) != block[i][j]){
                    tempHDist++;
                }
            }
        }
        return tempHDist;
    }

    /**
     * sum of Manhattan distances between blocks and goal
     * @return
     */
    public int manhattan(){
        return mDist;
    }

    /**
     * is this board the goal board?
     * @return
     */
    public boolean isGoal(){
        return mDist == 0;
    }

    /**
     * a board that is obtained by exchanging any pair of blocks
     * @return
     */
    public Board twin(){
        int start = 1;
        int r1 = int2R(start);
        int c1 = int2C(start);
        if(this.block[r1][c1] == 0){
            start++;
            r1 = int2R(start);
            c1 = int2C(start);
        }
        start++;
        int r2 = int2R(start);
        int c2 = int2C(start);
        if(this.block[r2][c2] == 0){
            start++;
            r2 = int2R(start);
            c2 = int2C(start);
        }
        int[][] twinBlock = new int[n][n];
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                twinBlock[i][j] = this.block[i][j];
            }
        }
        exchange2Blocks(twinBlock, r1, c1, r2, c2);
        Board twinBoard = new Board(twinBlock);
        return twinBoard;
    }

    private void exchange2Blocks(int[][] block, int r1, int c1, int r2, int c2){
        int temp = block[r1][c1];
        block[r1][c1] = block[r2][c2];
        block[r2][c2] = temp;
    }

    /**
     * does this board equal y?
     * @param y
     * @return
     */
    public boolean equals(Object y){
        if(y == this){
            return true;
        }
        if(y == null){
            return false;
        }
        if(y.getClass() != this.getClass()){
            return false;
        }
        Board yBoard = (Board)y;
        if(yBoard.dimension() != this.dimension()){
            return false;
        }
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(yBoard.block[i][j] != this.block[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * all neighboring boards
     * @return
     */
    public Iterable<Board> neighbors(){
        int[] dr = {0,0,1,-1};
        int[] dc = {1,-1,0,0};
        Stack<Board> stack = new Stack<>();
        for(int k = 0; k<4; k++){
            int newBlankR = int2R(blankInt) + dr[k];
            int newBlankC = int2C(blankInt) + dc[k];
            if(inBounds(newBlankR, newBlankC)){
                int[][] neighborBlock = copyBlock();
                exchange2Blocks(neighborBlock, int2R(blankInt), int2C(blankInt), newBlankR, newBlankC);
                stack.push(new Board(neighborBlock));
            }
        }
        return stack;
    }
    private int[][] copyBlock() {
        int[][] blockCopy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blockCopy[i][j] = this.block[i][j];
            }
        }
        return blockCopy;
    }

    private boolean inBounds(int r, int c){
        return r >= 0 && r < n && c >= 0 && c < n;
    }

    /**
     * string representation of this board
     * @return
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", this.block[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}
