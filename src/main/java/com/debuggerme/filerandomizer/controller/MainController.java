package com.debuggerme.filerandomizer.controller;

import com.debuggerme.filerandomizer.util.FilenameComparator;
import com.debuggerme.filerandomizer.util.Randomizer;
import com.debuggerme.filerandomizer.util.WindowsExplorerStringComparator;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

/**
 * @author Jamith Nimantha
 */
public class MainController implements Initializable {

    @FXML
    private Button btnFolder;

    @FXML
    private TextField txtFolder;

    @FXML
    private Button btnStart;

    @FXML
    Parent root;

    @FXML
    private Label lblNumOfArticles;

    @FXML
    private ImageView imgLoader;

    @FXML
    private Label lblProgress;

    private String dirPath;

    @FXML
    private TextField txtNumberOfSet;

    @FXML
    private TextField txtNumberPerSet;

    private Randomizer randomizer;

    @FXML
    void btnFolderOnAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) MainController.this.root.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            dirPath = selectedDirectory.getAbsolutePath();
            txtFolder.setText(dirPath);
            lblNumOfArticles.setText("Found " + getFiles(selectedDirectory).length + " Files");
            lblNumOfArticles.setVisible(true);
        }
    }

    private File[] getFiles(File dir) {
        File[] files = dir.listFiles(File::isFile);
        Arrays.sort(Objects.requireNonNull(files), Comparator.comparing(File::getName, new WindowsExplorerStringComparator()));
        return files;
    }

    @FXML
    void btnStartOnStart(ActionEvent event) {
        loading(true);
        if (dirPath != null) {
            File[] files = getFiles(new File(dirPath));
            if (files.length < 1) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "NO FILES FOUND !",
                        ButtonType.OK,
                        "Make Sure Files Are Exists In The Folder !"
                );
                loading(false);
            } else {
                int numberOfSet = Integer.parseInt(txtNumberOfSet.getText());
                int numberPerSet = Integer.parseInt(txtNumberPerSet.getText());
                for (File file : files) {
                    System.out.println(file.getName());
                }
                if (numberPerSet <= files.length) {
                    randomizeFiles(files, numberOfSet, numberPerSet);
                } else {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "ERROR DETECTED!",
                            ButtonType.OK,
                            "Cannot generate more numbers per set than number of files!"
                    );
                    loading(false);
                }
            }
        } else {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Error! Folder Not Selected",
                    ButtonType.OK,
                    "Please Select a Folder to Continue!"
            );
            loading(false);
        }
    }

    private void randomizeFiles(File[] files, int numberOfSet, int numberPerSet) {
        Task task = new Task<Void>() {
            @Override
            protected Void call() {
                AtomicInteger index = new AtomicInteger(1);
                for (int set = 0; set < numberOfSet; set++) {
                    int[] singleSet = randomizer.generateSingleSet(numberPerSet, 1, files.length);
                    File targetDir = new File(files[0].getParentFile().getAbsolutePath().concat("\\batch " + ( set+1)));
                    int finalSet = set;
                    Task objectTask = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            FileUtils.deleteQuietly(targetDir);
                                for (int i : singleSet) {
                                    FileUtils.copyFileToDirectory(files[i-1], targetDir);
                                    System.out.println(targetDir);
                                    Platform.runLater(() -> lblProgress.setText("Completed " + (finalSet +1) + " out of " + numberOfSet + " Sets"));
                                    System.out.println(i);
                                    System.out.println(files[i-1].getName());
                                }
                            return null;
                        }
                    };
                    Thread thread = new Thread(objectTask);
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Successful!",
                        ButtonType.CLOSE,
                        "Files Randomized Successfully !"
                );
                loading(false);
                return null;
            }
        };
        new Thread(task).start();

    }

    private void showAlert(final Alert.AlertType alertType, final String title, final ButtonType buttonType, final String msg) {
        Platform.runLater(() -> {
            Stage stage = (Stage) MainController.this.root.getScene().getWindow();
            Alert a = new Alert(alertType, title, buttonType);
            a.initOwner(stage);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(msg);
            a.show();
        });
    }

    private void loading(final boolean show) {
        Platform.runLater(() -> {
            imgLoader.setVisible(show);
            if (show){
                unBindBtnStart();
                btnStart.setDisable(show);
            } else {
                bindBtnStart();
            }
            btnFolder.setDisable(show);
        });
    }

    public void initialize(URL location, ResourceBundle resources) {
        lblNumOfArticles.setVisible(false);
        this.randomizer = new Randomizer(this);
        txtNumberOfSet.setTextFormatter(new TextFormatter<>(filter));
        txtNumberPerSet.setTextFormatter(new TextFormatter<>(filter));
        bindBtnStart();

    }

    private void bindBtnStart() {
        btnStart.disableProperty()
                .bind(txtNumberOfSet.textProperty().isEmpty()
                        .or(txtNumberPerSet.textProperty().isEmpty())
                        .or(txtFolder.textProperty().isEmpty())
                );
    }

    private void unBindBtnStart() {
        btnStart.disableProperty()
                .unbind();
    }

    private UnaryOperator<TextFormatter.Change> filter = change -> {
        if (change.getText().matches("[0-9]*"))
            return change;
        return null;
    };

}
