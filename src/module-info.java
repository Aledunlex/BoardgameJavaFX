module Test {
	requires transitive javafx.controls;
	
	opens boardgame to javafx.graphics, javafx.fxml;
}
