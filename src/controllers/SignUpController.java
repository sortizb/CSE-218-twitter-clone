package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;
import application.Data;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SignUpController implements Initializable{

	@FXML
	private VBox parent;
	@FXML
	private ProgressBar pbProgress;
	
	private static String username, name, gender, email, password;
	private static Stack<VBox> pervScenes = new Stack<>();
	private static VBox staticParent;
	private static ProgressBar staticPb;
	
	private static double progress = 0.33333;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			VBox temp = FXMLLoader.load(getClass().getResource("/sub_scenes/SignUpStep1.fxml"));
			parent.getChildren().add(temp);
			staticParent = parent;
			staticPb = pbProgress;
			staticPb.setProgress(progress);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void noFocus(MouseEvent event) {
		parent.requestFocus();
	}
	
	public static void nextStep(VBox temp) {
		VBox prevStep = (VBox)staticParent.getChildren().get(2);
		pervScenes.push(prevStep);
		staticParent.getChildren().set(2, temp);
		progress += 0.333333;
		staticPb.setProgress(progress);
	}
	
	public static void previusStep() {
		staticParent.getChildren().set(2, pervScenes.pop());
		progress -= 0.33333;
		staticPb.setProgress(progress);
	}
	
	public static void setUsername(String username) {
		SignUpController.username = username;
	}
	
	public static void setEmail(String email) {
		SignUpController.email = email;
	}
	
	public static void setName(String name) {
		SignUpController.name = name;
	}

	public static void setGender(String gender) {
		SignUpController.gender = gender;
	}
	
	public static void setPassword(String password) {
		SignUpController.password = password;
	}
	
	public static void createUser() {
		Data.getData().addUser(username, email, name, gender, password);
		Data.getData().saveData();
	}
}
