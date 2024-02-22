package csc133;

abstract class slGoLBoardLive extends slGoLBoard{

    private static slGoLBoard board;
    private int my_count = 0;

    slGoLBoardLive(int numRows, int numCols){
        super(numRows, numCols);
        countLiveTwoDegreeNeighbors(5,5);
    }

    int countLiveTwoDegreeNeighbors(int row, int col){
        for (row = 0; row < board.NUM_ROWS; row++){
            for (col = 0; col < board.NUM_COLS; col++) {
                if (board.liveCellArray[row][col] == true) {
                    check(row, col);
                }
            }
        }

        return my_count;
    }

    int updateNextCellArray(){
        //implement logic
        return 0;
    }

    private int check(int row, int col){
        //directly right
        if (board.liveCellArray[row + 1][col] == true)
            my_count++;
        //up and right
        else if (board.liveCellArray[row + 1][col + 1] == true)
            my_count++;
        //directly above
        else if (board.liveCellArray[row][col + 1] == true)
            my_count++;
        //directly left
        else if (board.liveCellArray[row - 1][col] == true)
            my_count++;
        //up and left
        else if (board.liveCellArray[row - 1][col + 1] == true)
            my_count++;
        //directly below
        else if (board.liveCellArray[row][col - 1] == true)
            my_count++;
        //down and left
        else if (board.liveCellArray[row - 1][col - 1] == true)
            my_count++;
        //down and right
        else if (board.liveCellArray[row + 1][col - 1] == true)
            my_count++;

        System.out.println("My count = " + my_count);

        return my_count;
    }



}
