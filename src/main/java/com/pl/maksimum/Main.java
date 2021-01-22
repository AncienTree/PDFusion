package com.pl.maksimum;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Start App");

        final String appName = "PDF Fusion";
        try {
            Image image = new Image("file:pdf.png");
            Parent parent = (Parent) FXMLLoader.load(getClass().getResource(
                    "/view/MainPanel.fxml"));
            Scene scene = new Scene(parent);

            //Ustawienia Stage'a
            primaryStage.setTitle(appName);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.getIcons().add(image);
            primaryStage.initStyle(StageStyle.UTILITY);
            
            primaryStage.show();
            
            // Przycisk zamykania wyłącza cały program i wszystkie okna
            primaryStage.setOnCloseRequest(x -> {
                Platform.exit();
                System.exit(0);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Stop App");
    }
}
