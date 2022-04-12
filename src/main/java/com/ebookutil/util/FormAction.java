package com.ebookutil.util;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FormAction {
    public static void clearFormGroupLecteur(TextField textField1, TextField textField2, TextField textField3, TextField textField4, Label errorMsg) {
        textField1.clear();
        textField2.clear();
        textField3.clear();
        textField4.clear();
        errorMsg.setText("");
        errorMsg.setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    public static void clearFormGroupBook(TextField textField1, TextField textField2, DatePicker datePicker, TextField textField3, Label errorMsg) {
        textField1.clear();
        textField2.clear();
        datePicker.setValue(null);
        textField3.clear();
        errorMsg.setText("");
        errorMsg.setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

}
