package com.olexyn.ensync.ui;


import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/***
 * Controller class for JavaFX. Contains the application logic.
 */
public class Controller {




    // Delete Duplicates
    // ----------------------------------------------------------------------------------------------------------------

    @FXML
    protected Text loadDirState;

    @FXML
    protected Text calcMd5State;

    @FXML
    protected Text sortFileState;

    @FXML
    protected Text findDuplicateState;

    @FXML
    protected Text delDuplicateState;

    @FXML
    protected Text fileNrCount;

    @FXML
    protected Text doubleNrCount;

    @FXML
    protected TextField directoryField;

    @FXML
    protected void openDir() {
        Window stage = loadDirState.getScene().getWindow();


        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory.");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File dir = directoryChooser.showDialog(stage);
        if (dir != null) {
            directoryField.setText(dir.getAbsolutePath());
        }
    }

    @FXML
    protected void loadDuplicateDir() {

        Task<Void> loadDirTask = new Task<Void>() {
            @Override
            public Void call() {

                loadDirState.setText("__");
                calcMd5State.setText("__");
                sortFileState.setText("__");
                findDuplicateState.setText("__");
                delDuplicateState.setText("__");
                fileNrCount.setText("__");
                doubleNrCount.setText("__");



                Path path = Paths.get(directoryField.getText());

                if (!Files.isDirectory(path)) {
                    loadDirState.setFill(Color.RED);
                    loadDirState.setText("ERROR.");

                } else {



                    loadDirState.setFill(Color.GREEN);
                    loadDirState.setText("OK.");


                    calcMd5State.setFill(Color.GREEN);
                    calcMd5State.setText("OK.");


                    sortFileState.setFill(Color.GREEN);
                    sortFileState.setText("OK.");


                    findDuplicateState.setFill(Color.GREEN);
                    findDuplicateState.setText("OK.");


                }
                return null;
            }
        };
        new Thread(loadDirTask).start();
    }


    @FXML
    protected void deleteDuplicates() {

        Task<Void> delDuplicateTask = new Task<Void>() {
            @Override
            public Void call() {


                delDuplicateState.setFill(Color.GREEN);
                delDuplicateState.setText("OK.");
                return null;
            }
        };
        new Thread(delDuplicateTask).start();
    }

    @FXML
    protected void loadBaseFiles() {
    }


    // Retrieve Sub-Files
    // ----------------------------------------------------------------------------------------------------------------

    @FXML
    protected Text loadPdfState;

    @FXML
    protected Text splitPdfState;

    @FXML
    protected Text baseFileCount;

    @FXML
    protected Text subFileCount;

    @FXML
    protected void loadBaseDir() {
        Task<Void> loadDirTask = new Task<Void>() {
            @Override
            public Void call() {

                loadPdfState.setText("__");
                splitPdfState.setText("__");
                baseFileCount.setText("__");
                subFileCount.setText("__");

                Path path = Paths.get(directoryField.getText());

                if (!Files.isDirectory(path)) {
                    loadPdfState.setFill(Color.RED);
                    loadPdfState.setText("ERROR.");
                } else {


                    loadPdfState.setFill(Color.GREEN);
                    loadPdfState.setText("OK.");

                }
                return null;
            }
        };
        new Thread(loadDirTask).start();
    }

    @FXML
    protected void splitPdf() {

        Task<Void> splitPdfTask = new Task<Void>() {
            @Override
            public Void call() {


                splitPdfState.setFill(Color.GREEN);
                splitPdfState.setText("OK.");

                return null;
            }
        };
        new Thread(splitPdfTask).start();
    }

}
