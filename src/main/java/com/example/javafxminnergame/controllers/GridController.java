package com.example.javafxminnergame.controllers;

import com.example.javafxminnergame.Models.Cell;
import com.example.javafxminnergame.Models.Game;
import com.example.javafxminnergame.Models.GameBoard;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;


public class GridController {

    @FXML
    private GridPane grid_cells_container;

    @FXML
    private VBox container_vBox;

    @FXML
    private BorderPane root_border_pane;

    private GameBoard board ;

    private boolean mark = false ;






    @FXML
    public void initialize(){


        Game game = new Game("src/main/java/com/example/javafxminnergame/Data/grid_1.txt");
        try {
            game.instantiate();
        }catch (IOException e){

            System.err.println("the files was not found !");
            System.exit(-1);

        }


        board = game.getBoard();
        board.addGameOverEventListener(this::gameOverSequence);


        int[][] arr = board.getGridArray();



        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {

                int finalI = i;
                int finalJ = j;


             board.getNode(i , j ).addClickedEventListener(() -> {


                 List<Cell> cells = board.getNeighbors(finalI , finalJ);
                 Cell cell = board.getNode(finalI , finalJ);


                     for (Cell n :cells) {
                            if (n.isRevealed()){
                                JFXButton button = (JFXButton) grid_cells_container.getChildren().filtered( child -> {
                                            if(child.getId().equals("btn_"+n.getX()+"_"+n.getY()))
                                                return true;

                                            return false;
                                        }).get(0);
                                button.setText(switch (n.getBombCount()){
                                            case -1 -> "#" ;
                                            default -> String.valueOf(n.getBombCount());
                                        });
                            }
                     }


             });

             JFXButton jfxButton = new JFXButton();
             jfxButton.setText("*");
             jfxButton.setId("btn_"+i+"_"+j);

                jfxButton.setOnAction(new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent actionEvent) {

                     Cell cell = board.getNode(finalI , finalJ);

                     if (!mark){
                         if (!cell.isRevealed() && cell.isMarked())
                             return;
                         setClick_btn(actionEvent , arr[finalI][finalJ]);
                         cell.click();
                     }else if (!cell.isRevealed()){

                         if(board.toggleMarked(finalI , finalJ)){
                             setClick_btn(actionEvent , -2);
                         }else
                             setClick_btn(actionEvent , -3);

                     }

                 }
             });


             jfxButton.getStyleClass().add("grid-cell");
             jfxButton.setMaxSize(25 , 25 );



             grid_cells_container.add(jfxButton , j , i);

            }
        }

        grid_cells_container.getRowConstraints().forEach(e -> e.setMaxHeight(20));
        grid_cells_container.getColumnConstraints().forEach(e-> e.setMaxWidth(20));

        grid_cells_container.setAlignment(Pos.CENTER);





    }

    @FXML
    public void setClick_btn(ActionEvent e , int bombCount){

        JFXButton control = (JFXButton) e.getSource() ;

        String s = switch (bombCount){
            case -3 -> "*" ;
            case -2 -> "p";
            case -1 ->  "#";
            default -> String.valueOf(bombCount);

        };

        control.setText(s);




        /*System.out.println(grid_cells_container.getChildren().size());

        for (Node n : grid_cells_container.getChildren()) {
            System.out.println(n.getId());

        }*/


      //  System.out.println(root_border_pane.getWidth());



    }

    @FXML
    void toggleMark_RevealClick(ActionEvent event) {
        this.mark = !mark;
    }

    private void gameOverSequence(boolean success){
        System.err.println("game over !");

        if (success){
            System.out.println("You Won !");
            return;
        }

        System.out.println("You Lost !");

        for (Node btn : grid_cells_container.getChildren() ) {

            btn = (JFXButton) btn ;
            String[] split  = btn.getId().split("_");
            int x = Integer.parseInt(split[1]);
            int y = Integer.parseInt(split[2]);

            Cell cell = board.getNode(x ,y );

            ((JFXButton) btn).setText(switch (cell.getBombCount()){
                case -1 -> "#" ;
                default -> String.valueOf(cell.getBombCount());
            });

            ((JFXButton) btn).disarm();
        }
    }




}
