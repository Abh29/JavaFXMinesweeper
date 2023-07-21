package com.example.javafxminnergame.Models;

import java.util.ArrayList;
import java.util.List;

public class Cell {



    private final boolean safe ;
    private final int bombCount ;
    private boolean revealed = false;
    private boolean marked = false;

    private int x ;
    private int y ;

    public List<ClickedEvent> listeners ;
    public List<GameOverEvent> gameOverEventListeners ;

    private boolean canFireEvent  = true ;



    Cell(int x , int y , boolean safe , int bombCount){
        this(safe , bombCount);
        this.x = x ;
        this.y = y ;
    }

    Cell(boolean safe , int bombCount){
        this.safe = safe ;
        this.bombCount = safe ? bombCount : -1 ;
        listeners = new ArrayList<>();
        gameOverEventListeners = new ArrayList<>();
    }

    public void reveal(){
        revealed = true ;
      //  marked = true ;


        if (!safe)
            fireGameOverEvent(false);
    }

    public void click(){

         if(!isMarked())
            fireClickedEvent();
    }

    public boolean toggleMark(){
        marked = !marked;

        return marked;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void addClickedEventListener(ClickedEvent listener){
        listeners.add(listener);
    }

    public void addGameOverEventListeners(List<GameOverEvent> listeners) {gameOverEventListeners = listeners;}

    public void fireClickedEvent(){

        if (canFireEvent)
        for (ClickedEvent listener : listeners) {
            listener.clicked();
        }
        
    }

    public void fireGameOverEvent(boolean success){
        for (GameOverEvent listener : gameOverEventListeners) {
            listener.gameOver(success);
        }
    }

    public boolean isMarked(){
        return marked;
    }

    public int getBombCount() {
        return bombCount;
    }

    public boolean isSafe() {
        return safe;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCanFireEvent(boolean canFireEvent) {
        this.canFireEvent = canFireEvent;
    }
}
