package com.pl.maksimum.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.pl.maksimum.util.Alerts;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class MenuController implements Initializable {

	@FXML
	private MenuItem infoMenuItem;
	@FXML
	private MenuItem closeMenuItem;
	@FXML
	private MenuItem settingsMenuItems;

	final private String VER = "v1.5.0";
        
    ////////////////////////////////////////////////////////////////
    //                                                            //
    //                       initialize                           //
    //                                                            //
    ////////////////////////////////////////////////////////////////       
        
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// Menu Info
		infoMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Alerts.alertInfo(
						"Informacja",
						"PDF Fusion " + VER + "\nStworzona na potrzeby MAKSIMUM Sp. z o.o. HOLDING S.K.A.",
						"Autor: Mateusz Dąbek\nKontakt e-mail: mateusz.dabek@opal2.pl"	+ "\nTelefon: 601 459 758"
				);
			}
		});

		// Menu Close
		closeMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Stop App z menu");
				System.exit(0);
			}

		});

		// Menu Ustawienia
		settingsMenuItems.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Parent menuWin;
		        try {
		            menuWin = FXMLLoader.load(getClass().getResource("/view/Settings.fxml"));
		            Stage stage = new Stage();
		            stage.setTitle("Ustawienia");
		            stage.setScene(new Scene(menuWin, 400, 325));
		            stage.setResizable(false);
		            stage.centerOnScreen();
		            stage.show();
		        }
		        catch (IOException e) {
		            e.printStackTrace();
		        }

			}
		});
	}
}