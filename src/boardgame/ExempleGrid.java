package boardgame;
 
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
 
public class ExempleGrid extends Application {
 
   @Override
   public void start(Stage primaryStage) throws Exception {
       GridPane root = new GridPane();
 
       root.setPadding(new Insets(20));
       root.setHgap(25);
       root.setVgap(15);
 
       Label labelTitle = new Label("Enter your user name and password!");
 
       // Put on cell (0,0), span 2 column, 1 row.
       root.add(labelTitle, 0, 0, 2, 1);
 
       Label labelUserName = new Label("User Name");
       TextField fieldUserName = new TextField();
 
       Label labelPassword = new Label("Password");
 
       PasswordField fieldPassword = new PasswordField();
 
       Button loginButton = new Button("Login");
 
       //GridPane.setHalignment(labelUserName, HPos.RIGHT);
 
       // Put on cell (0,1)
       root.add(labelUserName, 0, 1);
 
        
       GridPane.setHalignment(labelPassword, HPos.RIGHT);
       root.add(labelPassword, 0, 2);
 
       // Horizontal alignment for User Name field.
       GridPane.setHalignment(fieldUserName, HPos.LEFT);
       root.add(fieldUserName, 1, 1);
 
       // Horizontal alignment for Password field.
       GridPane.setHalignment(fieldPassword, HPos.LEFT);
       root.add(fieldPassword, 1, 2);
 
       // Horizontal alignment for Login button.
       GridPane.setHalignment(loginButton, HPos.RIGHT);
       root.add(loginButton, 1, 3);
 
       Scene scene = new Scene(root, 300, 300);
       primaryStage.setTitle("GridPanel Layout Demo (o7planning.org)");
       primaryStage.setScene(scene);
       primaryStage.show();
   }
 
   public static void main(String[] args) {
       launch(args);
   }
 
}