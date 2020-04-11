package com.olexyn.ensync.ui;


import com.olexyn.ensync.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/***
 * Controller class for JavaFX. Contains the application logic.
 */
public class Controller implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        Button newCollectionButton = new Button("New Collection");
        newCollectionButton.setId("newCollectionButton");
        newCollectionButton.setOnAction(event -> { this.newCollection();});



        TextField addDirectoryTextField = new TextField();
        addDirectoryTextField.setId("addDirectoryTextField");

        Button addDirectoryButton = new Button("Add Directory");
        addDirectoryButton.setId("addDirectoryButton");
        addDirectoryButton.setOnAction(event -> { this.addDirectory();});

        gridPane.add(addDirectoryTextField, 0, 0);
        gridPane.add(new Text(""), 1, 0);
        gridPane.add(new Text(""), 2, 0);
        gridPane.add(new Text(""), 3, 0);
        gridPane.add(addDirectoryButton, 4, 0);


    }


    private void newCollection(){
        /*
            TODO copy old init here, redraw.
         */
    }



    @FXML
    protected GridPane gridPane;






    /**
     *
     */
    private void addDirectory() {
        Window stage = gridPane.getScene().getWindow();

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory.");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File dir = directoryChooser.showDialog(stage);

        if (dir != null) {
            TextField directoryPathTextField = new TextField();
            directoryPathTextField.setText(dir.getAbsolutePath());
            directoryPathTextField.setDisable(true);

            // TODO for now there is only one SyncMap "test".
            Main.SYNC.get("test").addDirectory(dir.getAbsolutePath());

            TextField directoryStateTextField = new TextField();
            directoryStateTextField.setText("STATE");
            directoryStateTextField.setStyle("-fx-text-fill: green");
            directoryStateTextField.setDisable(true);

            Button removeDirectoryButton = new Button("Remove");
            removeDirectoryButton.setId("removeDirectoryButton-" + dir.getAbsolutePath());
            removeDirectoryButton.setOnAction(event -> { this.removeDirectory(removeDirectoryButton.getId());});


            List<Node> nodeList = new ArrayList<>(gridPane.getChildren());
            insertRow(nodeList, directoryPathTextField, directoryStateTextField, removeDirectoryButton);
            redraw(gridPane, nodeList);
        }


    }

    /**
     * Find the addDirectoryTextField.
     * Insert the cells of the new row starting from the last cell.
     * This pushes the addDirectoryTextField forward.
     */
    private void insertRow(List<Node> nodeList, TextField path , TextField state, Button button){
        for (Node node : nodeList) {

            if (node.getId() != null && node.getId().equals("addDirectoryTextField")) {
                int i = nodeList.indexOf(node);

                nodeList.add(i, button);
                nodeList.add(i, new Text(""));
                nodeList.add(i, state);
                nodeList.add(i, new Text(""));
                nodeList.add(i, path);
                break;
            }
        }
    }



    /**
     * Clear the gridPane and redraw it with contents of nodeList.
     */
    private void redraw(GridPane gridPane, List<Node> nodeList){
        gridPane.getChildren().clear();

        int col = 0;
        int row = 0;

        for (Node node : nodeList) {

            gridPane.add(node, col, row);
            col++;
            if (nodeList.indexOf(node) % 5 == 4) {
                row++;
                col = 0;
            }

        }
    }


    private void removeDirectory(String id) {


        List<Node> nodeList = new ArrayList<>(gridPane.getChildren());

        //TODO fix ConcurrentModificationException. This will possibly resolve further errors.

        for (Node node : nodeList) {

            if (node.getId() != null && node.getId().equals(id)) {
                int i = nodeList.indexOf(node) - 5;
                for (int j = 0; j < 5; j++) {
                    nodeList.remove(i);
                }
                break;
            }
        }

        gridPane.getChildren().clear();

        int col = 0;
        int row = 0;

        for (Node node : nodeList) {

            gridPane.add(node, col, row);
            col++;
            if (nodeList.indexOf(node) % 5 == 4) {
                row++;
                col = 0;
            }

        }


        String path = id.replace("removeButton", "");
        while(true){
            if (Main.flowThread.getState().equals(Thread.State.TIMED_WAITING)){
                try {
                    Main.flowThread.wait();
                } catch (InterruptedException e){
                    Main.SYNC.get("test").removeDirectory(path);
                    Main.flowThread.notify();
                    break;
                }
            }
        }


    }


}
