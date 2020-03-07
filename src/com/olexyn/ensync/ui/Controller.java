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


        TextField directoryField = new TextField();
        directoryField.setId("directoryField");

        Button addDirButton = new Button("Add");
        addDirButton.setId("addDirButton");
        addDirButton.setOnAction(event -> { this.addDir();});

        gridPane.add(directoryField, 0, 0);
        gridPane.add(new Text(""), 1, 0);
        gridPane.add(new Text(""), 2, 0);
        gridPane.add(new Text(""), 3, 0);
        gridPane.add(addDirButton, 4, 0);


    }


    @FXML
    protected GridPane gridPane;


    protected void addDir() {
        Window stage = gridPane.getScene().getWindow();

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory.");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File dir = directoryChooser.showDialog(stage);

        if (dir != null) {
            TextField pathTextField = new TextField();
            pathTextField.setText(dir.getAbsolutePath());
            pathTextField.setDisable(true);

            // TODO for now there is only one SyncMap "test".
            Main.sync.syncMaps.get("test").addDirectory(dir.getAbsolutePath());

            TextField stateTextField = new TextField();
            stateTextField.setText("STATE");
            stateTextField.setStyle("-fx-text-fill: green");
            stateTextField.setDisable(true);

            Button removeButton = new Button("Remove");
            removeButton.setId("removeButton" + dir.getAbsolutePath());
            removeButton.setOnAction(event -> { this.rmDir(removeButton.getId());});


            List<Node> nodeList = new ArrayList<>();

            nodeList.addAll(gridPane.getChildren());


            for (Node node : nodeList) {

                if (node.getId() != null && node.getId().equals("directoryField")) {
                    int i = nodeList.indexOf(node);

                    nodeList.add(i, removeButton);
                    nodeList.add(i, new Text(""));
                    nodeList.add(i, stateTextField);
                    nodeList.add(i, new Text(""));
                    nodeList.add(i, pathTextField);
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


        }


    }


    private void rmDir(String id) {
        String path = id.replace("removeButton", "");

        Main.sync.syncMaps.get("test").removeDirectory(path);

        List<Node> nodeList = new ArrayList<>();

        nodeList.addAll(gridPane.getChildren());


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

    }


}
