package com.dl630.isocalc.guielement;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ElementEntryField {
    private String title;
    private String promptText;
    private String unit;
    private DoubleProperty width;

    private VBox parentBox;
    private TextField textField;
    private Label titleLabel;
    private Label unitLabel;

    private Float titleOffsetTop;
    private Float titleOffsetBottom;

    public ElementEntryField(String title) {
        this(title, null);
    }

    public ElementEntryField(String title, String unit) {
        this(title, unit, null);
    }

    public ElementEntryField(String title, String unit, String promptText) {
        this.title = title;
        this.unit = unit;
        this.promptText = promptText;

        generateNodes();
    }

    public void generateNodes() {
        generateNodes(50, 12);
    }
    public void generateNodes(float size, float fontSize) {
        parentBox = new VBox();
        if (titleOffsetTop == null) titleOffsetTop = 0F;
        if (titleOffsetBottom == null) titleOffsetBottom = 0F;
        if (width != null) parentBox.prefWidthProperty().bind(width);

        if (title == null) title = "";
        titleLabel = new Label(title);
        titleLabel.setFont(new Font(fontSize));
        titleLabel.setPadding(new Insets(titleOffsetTop, 0, (fontSize - 3) + titleOffsetBottom, 0));
        parentBox.getChildren().add(titleLabel);

        textField = new TextField();
        textField.setFont(new Font(fontSize));

        if (unit == null) {
            parentBox.getChildren().add(textField);
        } else {
            HBox textBox = new HBox();
            textBox.setSpacing(6);
            textBox.setAlignment(Pos.CENTER_LEFT);

            unitLabel = new Label(unit);
            unitLabel.setFont(new Font(fontSize));

            textBox.getChildren().add(textField);
            textBox.getChildren().add(unitLabel);

            parentBox.getChildren().add(textBox);
        }
    }

    private Font getDefaultFont() {
        return new Font("System", 16);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public String getFieldText() {
        if (textField == null) generateNodes();
        return textField.getText();
    }

    public void setFieldText(String text) {
        if (textField == null) generateNodes();
        textField.setText(text);
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public DoubleProperty getWidth() {
        return width;
    }

    public void setWidth(DoubleProperty width) {
        this.width = width;
    }


    // Node getters and setters from this point onwards
    public VBox getParentBox() {
        if (parentBox == null) generateNodes();
        return parentBox;
    }

    public TextField getTextField() {
        if (textField == null) generateNodes();
        return textField;
    }

    public Label getTitleLabel() {
        if (titleLabel == null) generateNodes();
        return titleLabel;
    }

    public Label getUnitLabel() {
        return unitLabel;
    }

    public void setTitleOffset(Float titleOffset) {
        this.titleOffsetTop = titleOffset;
        this.titleOffsetBottom = titleOffset;
    }

    public void setTitleOffsetTop(Float offset) {
        this.titleOffsetTop = offset;
    }

    public void setTitleOffsetBottom(Float offset) {
        this.titleOffsetBottom = offset;
    }
}
