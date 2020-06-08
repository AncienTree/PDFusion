package com.pl.maksimum.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import com.pl.maksimum.util.Alerts;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class SettingsController implements Initializable {

    // JavaFx kontrolery
    @FXML
    private TextField tab1DefaultDirTextField;
    @FXML
    private Button tab1DirButton;
    @FXML
    private CheckBox tab1DefaultDirCheckBox;
    @FXML
    private CheckBox tab1AskDirCheckBox;
    @FXML
    private ListView<?> tab2AAListView;
    @FXML
    private CheckBox tab3NoEmptyPageCheckBox;
    @FXML
    private CheckBox tab3OddEmptyPageCheckBox;
    @FXML
    private CheckBox tab3AlwaysEmptyPageCheckBox;
    @FXML
    private TextField tab5AACountTextField;
    @FXML
    private TextField tab5FileCountTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    // Zmienne
    String currentDir = new File("").getAbsolutePath();
    Properties prop = new Properties();
    
    // Zmienne dla pliku z ustawieniami
    String defaultDirection;
    Boolean checkDefault;
    Boolean checkAskForDirection;
    Boolean nAdd;
    Boolean aOdd;
    Boolean aAlwy;
    Integer numAA;
    Integer numFile;
    //    
    
    // Settery & Gettery
    public void setTab3NoEmptyPageCheckBox(boolean setB) {
        tab3NoEmptyPageCheckBox.setSelected(setB);
    }

    public void setTab3OddEmptyPageCheckBox(boolean setB) {
        tab3OddEmptyPageCheckBox.setSelected(setB);
    }

    public void setTab3AlwaysEmptyPageCheckBox(boolean setB) {
        tab3AlwaysEmptyPageCheckBox.setSelected(setB);
    }

    public void setTab1DefaultDirCheckBox(boolean setB) {
        tab1DefaultDirCheckBox.setSelected(setB);
    }

    public void setTab1AskDirCheckBox(boolean setB) {
        tab1AskDirCheckBox.setSelected(setB);
    }

    // TODO stworzenie menu ustawiń.
    /*
	 * Co potrzebne bedzie w ustawieniach
	 * 
	 * Wybór kolejnosci stron do pliku Stworznie listy kolejności Plik AA: Które
	 * strony AA powinny być dodane
     */

    // Pozostałe metody
    public void loadConfig() {
        try {
            FileInputStream fis = new FileInputStream(currentDir + "\\config.properties");
            prop.load(fis);

            defaultDirection = prop.getProperty("defDir");
            checkDefault = Boolean.valueOf(prop.getProperty("chkDef"));
            checkAskForDirection = Boolean.valueOf(prop.getProperty("chkAsk"));
            nAdd = Boolean.valueOf(prop.getProperty("nAdd"));
            aOdd = Boolean.valueOf(prop.getProperty("aOdd"));
            aAlwy = Boolean.valueOf(prop.getProperty("aAlwy"));
            numAA = Integer.valueOf(prop.getProperty("numAA"));
            numFile = Integer.valueOf(prop.getProperty("numFile"));

            fis.close();
        } catch (FileNotFoundException e) {
            Alerts.alertError("Nie wykryto ustawień", null,
                    "Nie wykryto w katalogu głównym pliku konfiguracyjnego\nconfig.propertis, zostanie utworznoy nowy plik z ustawieniami.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(".....................................................");
            System.err.println("Wykryto wyjątek podczas odczytu pliku z ustawieniami");
            System.err.println(".....................................................");
        }
    }
      
    // Wczytywanie domyślnych ustawień w razie braku pliku z ustawieniami w katalogu głównym
    public void defaultOpt(File config) {
        try {
            FileWriter fw = new FileWriter(config);
            fw.write("defDir=\"\"");
            fw.write("\nchkDef=false");
            fw.write("\nchkAsk=true");
            fw.write("\nnAdd=true");
            fw.write("\naOdd=false");
            fw.write("\naAlwy=false");
            fw.write("\nnumAA=8");
            fw.write("\nnumFile=39");

            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
     
    ////////////////////////////////////////////////////////////////
    //                                                            //
    //                       initialize                           //
    //                                                            //
    //////////////////////////////////////////////////////////////// 
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Test inicjalizacji settingów");
        
        loadConfig();
        tab1DefaultDirTextField.setEditable(false);
        tab1DefaultDirTextField.setFocusTraversable(false);
        
        File loadConfig = new File(currentDir + "\\config.properties");
        if (!loadConfig.exists()) {
            try {
                loadConfig.createNewFile();
                defaultOpt(loadConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Zakładka nr 3 zabezpieczenie przed wyborem kilku opcji checkbox
        tab3NoEmptyPageCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setTab3AlwaysEmptyPageCheckBox(false);
                setTab3OddEmptyPageCheckBox(false);
            }
        });
        tab3AlwaysEmptyPageCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setTab3NoEmptyPageCheckBox(false);
                setTab3OddEmptyPageCheckBox(false);
            }
        });
        tab3OddEmptyPageCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setTab3AlwaysEmptyPageCheckBox(false);
                setTab3NoEmptyPageCheckBox(false);
            }
        });

        // Zakładka 1 zabezpieczenie przed wyborem kilku opcji checkbox
        tab1AskDirCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setTab1DefaultDirCheckBox(false);
            }
        });
        tab1DefaultDirCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setTab1AskDirCheckBox(false);

            }
        });

        // Anuluj button
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
        });

        // Wybór ścieżki
        //TODO zabezpieczenie przed nie wybraniem
        tab1DirButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File dir;
                tab1DefaultDirTextField.setText(null);
                DirectoryChooser dirChooser = new DirectoryChooser();
                dirChooser.setTitle("Wybierz katalog");
                dir = dirChooser.showDialog(new Stage());

                tab1DefaultDirTextField.setText(dir.getAbsolutePath());
            }
        });

    }

}
