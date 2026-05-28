module fr.uge.but.schtroumpf {
    requires javafx.controls;
    requires javafx.fxml;

    opens fr.uge.but.schtroumpf.controller to javafx.fxml;
    exports fr.uge.but.schtroumpf.launcher;
    exports fr.uge.but.schtroumpf.model;
}
