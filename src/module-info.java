module Test {
	requires transitive javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	
	opens boardgame to javafx.graphics, javafx.fxml;
}
