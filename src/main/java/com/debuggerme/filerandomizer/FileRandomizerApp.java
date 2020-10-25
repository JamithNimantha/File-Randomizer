package com.debuggerme.filerandomizer;


import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FileRandomizerApp extends Application {

    public static void main(String[] args) {
        LauncherImpl.launchApplication(FileRandomizerApp.class, AppPreLoader.class,args);
    }

    public void start(Stage primaryStage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));
        Parent parent = FXMLLoader.load(this.getClass().getResource("/com/debuggerme/filerandomizer/view/Main.fxml"));
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/debuggerme/filerandomizer/assets/logo.png")));
        Scene temp = new Scene(parent);
        primaryStage.setScene(temp);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("File Randomizer Tool v 1.0 By DebuggerMe");
        primaryStage.setResizable(false);

        primaryStage.show();
    }
}
