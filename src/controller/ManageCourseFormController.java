package controller;

import Util.ValidationUtil;
import bo.BOFactory;
import bo.custom.impl.ProgramBOImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.xdevapi.Schema;
import dto.ProgramDTO;
import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.tm.ProgramTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class ManageCourseFormController {
    public AnchorPane cmContext;
    public Label lblDate;
    public Label lblTime;
    public JFXTextField txtSearch;
    public JFXTextField txtProgramId;
    public JFXTextField txtProgramName;
    public JFXTextField txtDuration;
    public JFXTextField txtFee;
    public TableView<ProgramTM> tblProgram;
    public TableColumn colProgramId;
    public TableColumn colProgramName;
    public TableColumn colDuration;
    public TableColumn colFee;
    public Button btnAdd;
    public ImageView imgHome;
    public AnchorPane root;
    public Label txtConfirmation;
    int count = 0;

    ProgramBOImpl programBO = (ProgramBOImpl) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.PROGRAM);
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    Pattern courserIdPattern = Pattern.compile("^(CTO)[-]?[0-9]{3}$");
    Pattern courserNamePattern = Pattern.compile("^[A-z ]{1,30}$");
    Pattern courserDurationPattern = Pattern.compile("^[A-z 0-9 ]{1,10}$");
    Pattern courserFeePattern = Pattern.compile("^(?:0|[1-9]\\d*)(?:\\.(?!.*000)\\d+)?$");
    public void initialize() {
        loadDateAndTime();
        try {
            showProgramsOnTable();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        storeValidations();
        btnAdd.setDisable(true);
    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(
                    currentTime.getHour() + " : " + currentTime.getMinute() + " : " + currentTime.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void saveProgramOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ProgramDTO programDTO = new ProgramDTO(
                txtProgramId.getText(),
                txtProgramName.getText(),
                txtDuration.getText(),
                Double.parseDouble(txtFee.getText())
        );
        if (programBO.add(programDTO)) {
            /*new Alert(Alert.AlertType.CONFIRMATION, "ProgramDTO Add To Database").show();*/
            txtConfirmation.setText("SAVE ...");
            //setTime();
            showProgramsOnTable();
            clear();

        } else {
            /*new Alert(Alert.AlertType.WARNING, "Try Again").show();*/
            txtConfirmation.setText("TRY AGAIN ...");
            //setTime();
        }
    }

    public void showProgramsOnTable() throws SQLException, ClassNotFoundException {

        ObservableList<ProgramTM> list = programBO.find();

        colProgramId.setCellValueFactory(new PropertyValueFactory<>("programID"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));

        tblProgram.setItems(list);
    }

    public void removeProgramOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ProgramTM selectedItem = tblProgram.getSelectionModel().getSelectedItem();
        String programId = selectedItem.getProgramID();

        if (programBO.delete(programId)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
            txtConfirmation.setText("DELETED ...");
            //setTime();
            showProgramsOnTable();
            clear();
        } else {
            /*new Alert(Alert.AlertType.WARNING, "Try Again").show();*/
            txtConfirmation.setText("TRY AGAIN ...");
            //setTime();
        }
    }

    public void updateProgramOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ProgramTM selectedItem = tblProgram.getSelectionModel().getSelectedItem();
        String programId = selectedItem.getProgramID();

        ProgramDTO program = new ProgramDTO(
                txtProgramId.getText(),
                txtProgramName.getText(),
                txtDuration.getText(),
                Double.parseDouble(txtFee.getText())
        );
        if (programBO.update(program)) {
            /*new Alert(Alert.AlertType.CONFIRMATION, "Program Updated").show();*/
            txtConfirmation.setText("UPDATED ...");
            //setTime();
            showProgramsOnTable();
            clear();
        } else {
            /*new Alert(Alert.AlertType.WARNING, "Try Again").show();*/
            txtConfirmation.setText("TRY AGAIN ...");
            //setTime();
        }
    }

    public void clearOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clear();

    }

    private void clear(){
        txtProgramId.clear();
        txtProgramName.clear();
        txtDuration.clear();
        txtFee.clear();

    }

    public void keyEvent(KeyEvent keyEvent) {
        ObservableList<ProgramTM> search = programBO.search(txtSearch.getText());
        tblProgram.setItems(search);
    }

    public void onMouseClick(MouseEvent mouseEvent) {
        try {
            ProgramTM selectedProgram = tblProgram.getSelectionModel().getSelectedItem();
            txtProgramId.setText(selectedProgram.getProgramID());
            txtProgramName.setText(selectedProgram.getProgramName());
            txtDuration.setText(selectedProgram.getDuration());
            txtFee.setText("" + selectedProgram.getFee());

        } catch (Exception e) {

        }
    }

    private void storeValidations() {
        map.put(txtProgramId,courserIdPattern);
        map.put(txtProgramName,courserNamePattern);
        map.put(txtDuration,courserDurationPattern);
        map.put(txtFee,courserFeePattern);

    }

    public void onkeyAdd(KeyEvent keyEvent) {
        btnAdd.setDisable(true);
        Object response = ValidationUtil.validate(map,btnAdd);
        if (keyEvent.getCode()== KeyCode.ENTER) {
            if (response instanceof TextField){
                TextField error  = (TextField) response;
                error.requestFocus();
            }else if (response instanceof Boolean){
                /*new Alert(Alert.AlertType.CONFIRMATION, "Done").show();*/
                txtConfirmation.setText("ADDED ...");
                //setTime();
            }
        }
    }
    /*public void setTime(){
        if (count!=0){
            txtConfirmation.setText(null);
        }
    }*/
    /*public void timeMaker(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }*/
    public void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "imgHome":
                    root = FXMLLoader.load(this.getClass().getResource("/view/DashBoardForm.fxml"));
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
        }
    }
}

