package com.pl.maksimum.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.pl.maksimum.util.Alerts;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileAAController implements Initializable {

    // JavaFx kontrolery
    @FXML
    private TextField plikAADirTextField;
    @FXML
    private Button selectAAButton;

    // Zmienne
    private File plik;
    private String fileName;
    
    // Pozostałe metody
    public File getPlik() {
        return plik;
    }

    public void setPlik(File plik) {
        this.plik = plik;
    }

    public TextField getPlikAADirTextField() {
        return plikAADirTextField;
    }

    public void setPlikAADirTextField(String plikAADirTextField) {
        this.plikAADirTextField.setText(plikAADirTextField);
    }

    public String setFileName(String fileName) {
        this.fileName = fileName;
        return this.fileName;
    }

    private void getFileName(File file, String string) {
        string = file.getName();
        setFileName(string);
    }
    
    ////////////////////////////////////////////////////////////////
    //                                                            //
    //                       initialize                           //
    //                                                            //
    ////////////////////////////////////////////////////////////////
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Tooltip do przycisku utwórz
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Dodaj wygenerowany plik AA z Rainbow");
        selectAAButton.setTooltip(tooltip);

        selectAAButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // Wywołanie okna przelgądania plików
                    FileChooser fcAA = new FileChooser();
                    fcAA.setTitle("Wybierz wygenerowany plik AA");
                    fcAA.getExtensionFilters().addAll(new ExtensionFilter("Plik PDF", "*.pdf"));

                    setPlik(fcAA.showOpenDialog(new Stage()));
                    setPlikAADirTextField(getPlik().getPath());
                    getFileName(plik, fileName);

                } catch (NullPointerException e) {
                    Alerts.alertWarning("Nie wybrano pliku!", null, "Proszę o wybranie z dysku pliku AA. \nPlik AA nie został wczytany.");

                } catch (Exception e) {
                    System.err.println(".............................................");
                    System.err.println("Nastąpił wyjątek przy wczytywaniu pliku AA.");
                    System.err.println(".............................................");
                    e.printStackTrace();
                };
            }

        });

        plikAADirTextField.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard drag = event.getDragboard();
                boolean success = false;

                if (drag.hasFiles()) {
                    success = true;
                    String pathFileAA = null;
                    event.acceptTransferModes(TransferMode.ANY);

                    for (File file : drag.getFiles()) {
                        pathFileAA = file.getAbsolutePath();
                        System.out.println(pathFileAA);
                        plikAADirTextField.setText(pathFileAA);
                        plik = new File(pathFileAA);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }

        });
    }

}
