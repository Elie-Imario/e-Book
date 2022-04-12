module com.ebookutil.ebook {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires AnimateFX;
    requires org.controlsfx.controls;

    opens com.ebookutil.ebook to javafx.fxml;
    opens com.ebookutil.entity to javafx.fxml;

    exports com.ebookutil.ebook;
    exports com.ebookutil.entity;

}