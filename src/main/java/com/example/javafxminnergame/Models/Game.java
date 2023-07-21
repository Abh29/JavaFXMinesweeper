package com.example.javafxminnergame.Models;

import java.io.File;
import java.io.IOException;

public class Game {
    private String filePath ;
    private GameBoard board;

    public Game(String filePath){
        this.filePath = filePath;
    }
    public void instantiate() throws IOException {
        File file = new File(filePath);
        board = GameBoard.getInstanceFromFile(file);

    }

    public GameBoard getBoard() {
        return board;
    }
}
