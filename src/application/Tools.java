package application;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class Tools {

	public static boolean validateEmail(String email) {
		String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"; //Typical email structure
		return email.matches(regex);
	}
	
	public static boolean validatePassword(String password) {
		if (!(password.length() >= 8)) //Password length
			return false;
		
		if (!(password.matches("(.*[A-Z].*)"))) //Contains at least one upper case letter
				return false;
		
		if (!(password.matches("(.*[a-z].*)"))) //Contains at least one lower case letter
			return false;
		
		if (!(password.matches("(.*[^a-zA-Z0-9].*)"))) //Contains at least one special char
			return false;
		
		return true;
	}
	
	public static Alert createAlert(AlertType type, String header, String content) {
		Alert alert = new Alert(type);
		alert.getDialogPane().getStylesheets().add("styles/alerts.css");
		alert.setHeaderText(header);
		alert.setContentText(content);
		return alert;
	}
	
	public static String calculateTimeDifference(LocalDateTime old, LocalDateTime current) {
		int timediff = current.getYear() - old.getYear();
		if (timediff > 0) {
			return timediff + " y ago"; //Difference in years, if exists
		}
		
		timediff = current.getMonthValue() - old.getMonthValue();
		if (timediff > 0) {
			return timediff + " mon ago";
		}
		
		timediff = current.getDayOfMonth() - old.getDayOfMonth();
		if (timediff > 0) {
			return timediff + " d ago";
		}
		
		timediff = current.getHour() - old.getHour();
		if (timediff > 0) {
			return timediff + " h ago";
		}
		
		timediff = current.getMinute() - old.getMinute();
		if (timediff > 0) {
			return timediff + " min ago";
		}
		
		timediff = current.getSecond() - old.getSecond();
		return timediff + " s ago";
	}
	
	public static String copyFileToMediaFolder(File source) throws IOException { //Copies a file into the source folder and returns location
		 Path from = Paths.get(source.toURI());
	     Path  to = Paths.get("E:\\CSE 218\\project\\src\\media\\" + Data.getData().useMediaId() + source.getName());
	     Files.copy(from, to);
	     return to.toString();
	}
}
