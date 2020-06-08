package com.pl.maksimum.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerts {

	public static void alertInfo(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		if (header == null) {
			alert.setHeaderText(null);
		} else {
			alert.setHeaderText(header);
		}
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public static void alertError(String title, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		if (header == null) {
			alert.setHeaderText(null);
		} else {
			alert.setHeaderText(header);
		}
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public static void alertConfirmation(String title, String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		if (header == null) {
			alert.setHeaderText(null);
		} else {
			alert.setHeaderText(header);
		}
		alert.setContentText(content);
		alert.showAndWait();
	}
	public static void alertWarning(String title, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		if (header == null) {
			alert.setHeaderText(null);
		} else {
			alert.setHeaderText(header);
		}
		alert.setContentText(content);
		alert.showAndWait();
	}
	
}
