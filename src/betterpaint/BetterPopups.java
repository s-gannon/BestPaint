package betterpaint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BetterPopups { //really just a container for the popups
    //constructor
    public BetterPopups(){}
    /**
     * Creates a window that takes text from a file and displays it in a
     * nice window
     * @param path The path where the text can be found
     * @param title The title that the window should take
     * @throws FileNotFoundException 
     */
    public static void createTextWindow(File path, String title) throws FileNotFoundException{
        Stage wind = new Stage();           //generic window to hold text
        BorderPane bp = new BorderPane();   //BorderPane to paste button to bottom
        ScrollPane textScroll = new ScrollPane();   //ScrollPane to scroll the text if needed
        HBox bottomBox = new HBox();        //literally only for the okay button
        Scene scene = new Scene(bp, 635, 500);  
        
        Scanner in = new Scanner(path); 
        String allText = "";
        while(in.hasNextLine())
            allText += in.nextLine() + "\n";   //takes the file and reads all the text
        
        Text text = new Text(allText);
        Button goBack = new Button("Okay!");
        goBack.setOnAction((ActionEvent e) -> { //closes the window when you're done reading the text
            wind.hide();
        });
        //makes the window look all nice
        bottomBox.getChildren().addAll(goBack);
        bottomBox.setAlignment(Pos.CENTER); //sets the button to center
        textScroll.setContent(text);        //makes text scroll
        bp.setPadding(new Insets(10));      //insets to decrease cramped feeling
        bp.setBottom(bottomBox);
        bp.setCenter(textScroll);
        wind.setScene(scene);
        wind.setTitle(title);
        wind.show();                        //lights, camera, action
    }
    /**
     * Creates a window that asks if the person is sure that they want to exit
     * the program
     */
    public static void createExitWindow(){
        Stage exitWindow = new Stage();
        GridPane gp = new GridPane();
        Scene sureScene = new Scene(gp, 400, 200);

        Label sureText = new Label("Are you sure you want to exit?");
        Button yesBtn = new Button("Yes I'm sure");
        Button noBtn = new Button("No, go back");

        gp.addRow(0, sureText);
        gp.addRow(2, yesBtn);
        gp.addRow(3, noBtn);
        gp.setHalignment(sureText, HPos.CENTER);
        gp.setHalignment(yesBtn, HPos.CENTER);
        gp.setHalignment(noBtn, HPos.CENTER);
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(10);

        exitWindow.setScene(sureScene);
        exitWindow.show();

        yesBtn.setOnAction((ActionEvent f) -> {
            exitWindow.close();
            BetterPaint.mainStage.close();
            Platform.exit();
            System.exit(0);     //I heard this was good for closing threads?
        });
        noBtn.setOnAction((ActionEvent f) -> {
            exitWindow.hide();  //Hide it if
        });
    } 
}
