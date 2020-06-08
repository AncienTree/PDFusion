package com.pl.maksimum.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;


public class ChoiceOfConsentController implements Initializable {

	// JavaFx kontrolery
	@FXML
	private Label checkBoxInfoLabel;
	@FXML
	private CheckBox takZgodaCheckBox;
	@FXML
	private CheckBox nieZgodaCheckBox;
	
        // Zmienne
	public boolean isSelectedYes;
        
	// Gettery & Settery
	public CheckBox getTakZgodaCheckBox() {
		return takZgodaCheckBox;
	}

	public CheckBox getNieZgodaCheckBox() {
		return nieZgodaCheckBox;
	}

	// Pozostałe metody
	public void setTakZgodaCheckBox(boolean b) {
		takZgodaCheckBox.setSelected(b);
	}

	public void setNieZgodaCheckBox(boolean b) {
		nieZgodaCheckBox.setSelected(b);
	}
	
    ////////////////////////////////////////////////////////////////
    //                                                            //
    //                       initialize                           //
    //                                                            //
    ////////////////////////////////////////////////////////////////
        
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		takZgodaCheckBox.setSelected(false);
		nieZgodaCheckBox.setSelected(false);
		
		takZgodaCheckBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				setNieZgodaCheckBox(false);
				checkBoxInfoLabel.setText("Wybrano oświadczenie \"TAK\"");
				isSelectedYes = true;
			}
			
		});
		nieZgodaCheckBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				setTakZgodaCheckBox(false);
				checkBoxInfoLabel.setText("Wybrano oświadczenie \"NIE\"");
				isSelectedYes = false;
			}
		});
	}

}
