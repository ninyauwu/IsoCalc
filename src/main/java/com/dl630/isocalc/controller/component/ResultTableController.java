package com.dl630.isocalc.controller.component;

import com.dl630.isocalc.OutputDataModel;
import com.dl630.isocalc.scene.SceneController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultTableController extends SceneController implements Initializable {
    @FXML
    public TableView<OutputDataModel> tableView;

    @FXML
    public HBox tableBox;

    @FXML
    public Button exportButton;

    @FXML
    public TableColumn<OutputDataModel, String> columnTarget;
    @FXML
    public TableColumn<OutputDataModel, String> columnProduct;
    @FXML
    public TableColumn<OutputDataModel, String> columnHalfLife;
    @FXML
    public TableColumn<OutputDataModel, String> columnH10_1m;
    @FXML
    public TableColumn<OutputDataModel, String> columnH07;
    @FXML
    public TableColumn<OutputDataModel, String> columnH10_shielded_1m;
    @FXML
    public TableColumn<OutputDataModel, String> columnH10_containerSurface;

    private EventHandler<ActionEvent> exportEvent;

    public ResultTableController() {
        super("component/result_table.fxml");
    }

    public void setItems(ObservableList<OutputDataModel> observableList) {
        tableView.setItems(observableList);
        tableView.prefWidthProperty().bind(tableBox.widthProperty()
                .subtract(exportButton.widthProperty())
                .subtract(tableBox.spacingProperty()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnTarget.setCellValueFactory(new PropertyValueFactory<>("target"));
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        columnHalfLife.setCellValueFactory(new PropertyValueFactory<>("halfLife"));
        columnH10_1m.setCellValueFactory(new PropertyValueFactory<>("h10_1m"));
        columnH07.setCellValueFactory(new PropertyValueFactory<>("h07"));
        columnH10_shielded_1m.setCellValueFactory(new PropertyValueFactory<>("h10_shieldedAt1m"));
        columnH10_containerSurface.setCellValueFactory(new PropertyValueFactory<>("h10_atContainerSurface"));

        exportButton.setOnAction(exportEvent);
    }

    public void setOnExport(EventHandler<ActionEvent> exportEvent) {
        this.exportEvent = exportEvent;
        if (exportButton == null) return;

        exportButton.setOnAction(exportEvent);
    }
}
