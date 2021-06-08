module Test {
	requires transitive javafx.controls;
	requires javafx.graphics;
	
	opens boardgame to javafx.graphics, javafx.fxml;
}
