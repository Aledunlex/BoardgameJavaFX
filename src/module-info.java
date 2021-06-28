module Test {
	requires transitive javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.desktop;

	opens boardgame to javafx.graphics, javafx.fxml;
}
