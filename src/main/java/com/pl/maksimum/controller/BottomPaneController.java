package com.pl.maksimum.controller;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.pl.maksimum.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class BottomPaneController implements Initializable {

    // JavaFx kontrolery
    @FXML
    private TextField umowaDirTextField;
    @FXML
    private Button selectUmowaButton;
    @FXML
    private Button createButton;

    // Zmienne
    private File plik;
    private String fileName;

    // Gettery & Settery
    public File getPlik() {
        return plik;
    }

    public void setPlik(File plik) {
        this.plik = plik;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private void getFileName(File file, String string) {
        string = file.getName();
        setFileName(string);
    }

    public String date() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(currentDate);
    }

    ////////////////////////////////////////////////////////////////
    //                                                            //
    //                       initialize                           //
    //                                                            //
    ////////////////////////////////////////////////////////////////
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // Tooltip do przycisku z wyborem umowy
        Tooltip tooltip = new Tooltip("Wybierz plik z odpowiednim regulaminem");
        selectUmowaButton.setTooltip(tooltip);

        // Tooltip do przycisku utwórz
        Tooltip tooltip2 = new Tooltip("Utwórz gotowy plik z umowę");
        createButton.setTooltip(tooltip2);

        // Wybór umowy
        selectUmowaButton.setOnAction(event -> {
            // Wywołanie okna przelgądania plików
            try {
                FileChooser umowaFC = new FileChooser();
                umowaFC.setTitle("Wybierz plik z regulaminem");
                umowaFC.getExtensionFilters().addAll(new ExtensionFilter("Plik PDF", "*.pdf"));
                setPlik(umowaFC.showOpenDialog(new Stage()));
                umowaDirTextField.setText(plik.getPath());
                getFileName(plik, fileName);
            } catch (NullPointerException e) {
                Alerts.alertWarning("Nie wybrano pliku!", null, "Proszę o wybranie z dysku regulaminu. \nRegulamin nie został wczytany.");
            } catch (Exception e) {
                System.err.println("...........................................................");
                System.err.println("Nastąpił wyjątek przy wczytywaniu pliku regulamin z dysku.");
                System.err.println("...........................................................");
                e.printStackTrace();
            }
        });
        
        // Funkcja Drag&Drop dla wyboru pliku z regulaminami.
        umowaDirTextField.setOnDragOver(event -> {
            Dragboard drag = event.getDragboard();
            boolean success = false;

            if (drag.hasFiles()) {
                success = true;
                String pathFileAA = null;
                event.acceptTransferModes(TransferMode.ANY);

                for (File file : drag.getFiles()) {
                    pathFileAA = file.getAbsolutePath();
//                    System.out.println(pathFileAA);
                    umowaDirTextField.setText(pathFileAA);
                    plik = new File(pathFileAA);
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

}
