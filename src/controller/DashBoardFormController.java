package controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class DashBoardFormController {

    public Label txtmain;
    public Label txtDiscription;
    public ImageView imgStudent;
    public ImageView imgForm;
    public ImageView imgpeople;
    public AnchorPane root;


    public void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "imgStudent":
                        root = FXMLLoader.load(this.getClass().getResource("/view/ManageStudentRegistrationForm.fxml"));
                    break;
                case "imgForm":
                        root = FXMLLoader.load(this.getClass().getResource("/view/ManageCourseForm.fxml"));
                    break;
                case "imgpeople":
                        root = FXMLLoader.load(this.getClass().getResource("/view/RegistrationDetalsForm.fxml"));
                    break;
            }

            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(1000), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }
    }

    public void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "imgStudent":
                    txtmain.setText("Student Details");
                    txtDiscription.setText("Click to add, edit, delete, search or view students");
                    break;
                case "imgForm":
                    txtmain.setText("Manage Courses");
                    txtDiscription.setText("Click to add, edit, delete, search or view courses");
                    break;
                case "imgpeople":
                    txtmain.setText("Manage Registrations");
                    txtDiscription.setText("Click here if you want to manage Registrations");
                    break;
            }

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.DARKOLIVEGREEN);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    public void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            txtmain.setText("Welcome");
            txtDiscription.setText("Please select one of above main operations to proceed");
        }
    }
}
