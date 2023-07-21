package com.example.javafxminnergame.Models;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class GameBoard  {

    private Cell[][] board ;
    private int[][] gridArray;
    private int sizeX ;
    private int sizeY ;
    private int bombCount;
    private List<GameOverEvent> gameOverEventListeners ;
    private int markedCellsCount  = 0 ;
    private int revealedCellsCount = 0 ;
    private int boardSafeCellsCount;



    private GameBoard(int[][] boardArray){

        gameOverEventListeners = new ArrayList<>();

        gridArray = boardArray ;

        sizeX = boardArray.length;
        sizeY = boardArray[0].length;

        this.board  = new Cell[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {

                Cell cell = new Cell(i ,j ,boardArray[i][j] != -1 , boardArray[i][j]);
                int finalI = i;
                int finalJ = j;
                cell.addClickedEventListener( () -> {

                    boolean fired  = false;

                    if (!cell.isRevealed()){
                        revealedCellsCount ++ ;
                        cell.reveal();
                    }

                    else if ( getSumOfNeighborsMarks(finalI , finalJ) == cell.getBombCount()){
                        for (Cell n : getNeighbors(finalI , finalJ)) {
                            if (!n.isMarked() && !n.isRevealed()){
                                fired = true;
                                n.reveal();
                                revealedCellsCount ++ ;
                            }
                        }
                    }

                    if (fired)
                        cell.setCanFireEvent(false);

                    if (revealedCellsCount == boardSafeCellsCount)
                        cell.fireGameOverEvent(true);

                });

                cell.addGameOverEventListeners(gameOverEventListeners);

                board[i][j] = cell;

            }

        }

    }

    private GameBoard(int[][] boardArray , int bombCount){
        this(boardArray);
        this.bombCount = bombCount;
        boardSafeCellsCount = sizeX * sizeY - bombCount;
    }

    public static GameBoard getInstanceFromFile(File file) throws IOException {

        // this file must contain a map of the board



        RandomAccessFile reader = new RandomAccessFile(file , "rws");

        String line = reader.readLine();  // read the head of the file

        String[] head = line.split(";");

        int x = Integer.parseInt(head[0]);
        int y = Integer.parseInt(head[1]);
        int bombCount = Integer.parseInt(head[2]);



        int[][] arr = new int[x][y];
        int i = 0 ;

        line = reader.readLine();

        while (line != null){
            arr[i] = line.chars().map(operand -> {

                return switch (operand){
                    case '*' -> -1 ;
                    default ->  Integer.parseInt(String.valueOf((char)operand));
                };

            }).toArray() ;
            line = reader.readLine();
            i++;
        }

        return new GameBoard(arr , bombCount) ;
    }

    public List<Cell> getNeighbors(int x , int y ){

        List<Cell> out = new ArrayList<>(9);
        if (x + 1 < sizeX)
            out.add(board[x+1][y]);
        if (x - 1 >= 0)
            out.add(board[x-1][y]);
        if (y + 1 < sizeY)
            out.add(board[x][y + 1]);
        if (y - 1 >= 0)
            out.add(board[x][y -1]);
        if (x + 1 < sizeX && y + 1 < sizeY)
            out.add(board[x+1][y+1]);
        if (x + 1 < sizeX && y - 1 >= 0)
            out.add(board[x+1][y-1]);
        if (x - 1 >= 0 && y + 1 < sizeY)
            out.add(board[x-1][y + 1]);
        if (x - 1 >= 0 && y - 1 >=0)
            out.add(board[x-1][y -1]);


        return out;

    }

    public Cell getNode(int x , int y ){
        return board[x][y];
    }

    public int getSumOfNeighborsMarks(int x , int y ){

        List<Cell> neighbors = getNeighbors(x, y);
        int count = 0 ;

        for (Cell cell : neighbors) {
            count += cell.isMarked() ? 1 : 0 ;
        }

        return count;

    }

    public void addGameOverEventListener(GameOverEvent listener){
        gameOverEventListeners.add(listener);
    }

    public void addClickedEventListenerForEachCell(ClickedEvent listener){
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                board[i][j].addClickedEventListener(listener);
            }
        }
    }

    public void reveal(int x , int y ){
        revealedCellsCount++;
        board[x][y].reveal();
    }

    public boolean toggleMarked(int x , int y) {
        boolean out = false ;
       if (board[x][y].toggleMark()){
           markedCellsCount++ ;
           out = true;

       }
       else
           markedCellsCount--;
       
       return out ;

    }

    public int getBombCount() {
        return bombCount;
    }

    public int[][] getGridArray() {
        return gridArray;
    }

    public int getMarkedCellsCount() {
        return markedCellsCount;
    }

    public int getRevealedCellsCount() {
        return revealedCellsCount;
    }

    public int getBoardSafeCellsCount() {
        return boardSafeCellsCount;
    }
}
