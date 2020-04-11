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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/***
 * Controller class for JavaFX. Contains the application logic.
 */
public class Controller implements Initializable {


    final static int COLUMN_COUNT = 5; // How many columns should the GridPane have? Adjust if necessary.
    final static Text SPACE = new Text(""); // Placeholder
    int collection_count = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Text end = new Text("end");
        end.setId("end");

        Button newCollectionButton = new Button("New Collection");
        newCollectionButton.setId("newCollectionButton");
        newCollectionButton.setOnAction(event -> { this.newCollection();});

        gridPane.add(end, 0, 0);

        List<Node> nodeList = new ArrayList<>(gridPane.getChildren());

        List<Node> payload = Arrays.asList(new Text(""), new Text(""), new Text(""), new Text(""), newCollectionButton);


        insertPayload(nodeList, payload, "", 0);
        redraw(gridPane, nodeList);


    }






    private void newCollection() {

        String collectionName = "name" + collection_count++;


        TextField collectionStateTextField = new TextField();
        collectionStateTextField.setText("STATE");
        collectionStateTextField.setStyle("-fx-text-fill: green");
        collectionStateTextField.setDisable(true);
        collectionStateTextField.setId("collectionStateTextField-" + collectionName);

        Button removeCollectionButton = new Button("Remove Collection");
        removeCollectionButton.setId("removeCollectionButton-" + collectionName);
        removeCollectionButton.setOnAction(event -> { this.removeCollection(collectionName);});

        TextField addDirectoryTextField = new TextField();
        addDirectoryTextField.setId("addDirectoryTextField-" + collectionName);

        Button addDirectoryButton = new Button("Add Directory");
        addDirectoryButton.setId("addDirectoryButton-" + collectionName);
        addDirectoryButton.setOnAction(event -> { this.addDirectory(collectionName);});


        List<Node> nodeList = new ArrayList<>(gridPane.getChildren());

        List<Node> payload = new ArrayList<>();
        payload.addAll(Arrays.asList(new Text(""), new Text(""), collectionStateTextField, new Text(""), removeCollectionButton));
        payload.addAll(Arrays.asList(addDirectoryTextField, new Text(""), new Text(""), new Text(""), addDirectoryButton));
        insertPayload(nodeList, payload, "newCollectionButton", -4);
        redraw(gridPane, nodeList);
    }


    @FXML
    protected GridPane gridPane;

    private void removeCollection(String collectionName) {
        List<Node> nodeList = new ArrayList<>(gridPane.getChildren());


        here : for (Node node : nodeList) {

            if (node.getId() != null && node.getId().equals("removeCollectionButton-" + collectionName)) {
                int i = nodeList.indexOf(node) - 4;
                while (i<nodeList.size()) {
                    nodeList.remove(i);


                if (nodeList.get(i).getId() != null &&  nodeList.get(i).getId().equals("addDirectoryButton-" + collectionName)){
                    nodeList.remove(i);
                    break here;
                }
                }

            }
        }

        redraw(gridPane,nodeList);
    }


    private List<Node> fillBlanks() {
        List<Node> out = new ArrayList<>();


        return out;

    }


    /**
     *
     */
    private void addDirectory(String collectionName) {
        // TODO throw error if other collection already contains absollutepath
        Window stage = gridPane.getScene().getWindow();

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory.");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File dir = directoryChooser.showDialog(stage);

        if (dir != null) {
            TextField directoryPathTextField = new TextField();
            directoryPathTextField.setText(dir.getAbsolutePath());
            directoryPathTextField.setDisable(true);
            directoryPathTextField.setId("directoryPathTextField-" + dir.getAbsolutePath());

            // TODO for now there is only one SyncMap "test".
            // Main.SYNC.get("test").addDirectory(dir.getAbsolutePath());

            TextField directoryStateTextField = new TextField();
            directoryStateTextField.setText("STATE");
            directoryStateTextField.setStyle("-fx-text-fill: green");
            directoryStateTextField.setDisable(true);
            directoryStateTextField.setId("directoryStateTextField-" + dir.getAbsolutePath());

            Button removeDirectoryButton = new Button("Remove");
            removeDirectoryButton.setId("removeDirectoryButton-" + dir.getAbsolutePath());
            removeDirectoryButton.setOnAction(event -> { this.removeDirectory(removeDirectoryButton.getId());});


            List<Node> nodeList = new ArrayList<>(gridPane.getChildren());
            List<Node> payload = Arrays.asList(directoryPathTextField, new Text(""), directoryStateTextField, new Text(""), removeDirectoryButton);
            insertPayload(nodeList, payload, "addDirectoryTextField-" + collectionName, 0);
            redraw(gridPane, nodeList);
        }


    }


    /**
     * Find the Node with @param id.
     * Insert the contents of the @param payload starting from the last.
     * This pushes the Node with @param id forward.
     */
    private void insertPayload(List<Node> nodeList, List<Node> payload, String id, int offset) {
        for (Node node : nodeList) {

            if (node.getId() != null && node.getId().equals(id)) {
                int i = nodeList.indexOf(node) + offset;

                for (int j = payload.size() - 1; j >= 0; j--) {
                    nodeList.add(i, payload.get(j));
                }
                break;
            }
        }
    }


    /**
     * Clear the gridPane and redraw it with contents of nodeList.
     */
    private void redraw(GridPane gridPane, List<Node> nodeList) {
        gridPane.getChildren().clear();

        int col = 0;
        int row = 0;

        for (Node node : nodeList) {
            if ((nodeList.indexOf(node) + 0) % COLUMN_COUNT == 0) {
                row++;
                col = 0;
            }
            gridPane.add(node, col, row);
            col++;


        }
    }


    private void removeDirectory(String id) {


        List<Node> nodeList = new ArrayList<>(gridPane.getChildren());

        //TODO fix ConcurrentModificationException. This will possibly resolve further errors.

        for (Node node : nodeList) {

            if (node.getId() != null && node.getId().equals(id)) {
                int i = nodeList.indexOf(node) - 4;
                for (int j = 0; j < 5; j++) {
                    nodeList.remove(i);
                }
                break;
            }
        }

        redraw(gridPane,nodeList);

        /*
        String path = id.replace("removeButton", "");
        while (true) {
            if (Main.flowThread.getState().equals(Thread.State.TIMED_WAITING)) {
                try {
                    Main.flowThread.wait();
                } catch (InterruptedException e) {
                    Main.SYNC.get("test").removeDirectory(path);
                    Main.flowThread.notify();
                    break;
                }
            }
        }

         */


    }


}

 class Placeholder extends Text{
    static int count =0;

    Placeholder(String label, String name){

        super(label);
        this.setId("placeholder-"+name+"-"+count++);

    }

}
