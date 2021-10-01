package betterpaint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BetterPopups { //really just a container for the popups
    /**
     * Creates a window that takes text from a file and displays it in a
     * nice window
     * @param path The path where the text that will be displayed can be found
     * @param title The title that the window should take on
     * @param width The width of the window
     * @param height The height of the window
     * @throws FileNotFoundException 
     */
    public static void createTextWindow(File path, String title, int width, int height) throws FileNotFoundException{
        Stage wind = new Stage();           //generic window to hold text
        wind.initOwner(BetterPaint.mainStage);
        BorderPane bp = new BorderPane();   //BorderPane to paste button to bottom
        ScrollPane textScroll = new ScrollPane();   //ScrollPane to scroll the text if needed
        HBox bottomBox = new HBox();        //literally only for the okay button
        Scene scene = new Scene(bp, width, height);  
        
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
        wind.show();
    }
    /**
     * Creates the window that asks for the text to use in the Text Tool
     *
    public static void createTextToolWindow(){
        Stage wind = new Stage();
    }*/
    /**
     * Creates an alert that asks if the person is sure that they want to exit
     * the program
     * @param stage The stage to safely close
     */
    public static void createExitAlert(Stage stage){
        Alert sureAlert = new Alert(AlertType.CONFIRMATION, 
                "Are you sure you want to exit?",
                ButtonType.YES, ButtonType.NO);
        sureAlert.setTitle("Exit");
        sureAlert.initOwner(BetterPaint.mainStage);
        if(sureAlert.showAndWait().get() == ButtonType.YES){    //IF YOU CREATE OWN BUTTON TYPE, GENERIC WON'T WORK HERE
            stage.close();
            Platform.exit();
            System.exit(0);     //supposedly this is 'good' closing? stops threads?
        }
    }
    /**
     * Creates an alert that asks if the user wants to save before closing a tab/the program
     */
    public static void createUnsavedAlert(){
        ButtonType[] buttons = {new ButtonType("Save"), new ButtonType("Save As"), 
                                new ButtonType("Cancel"), new ButtonType("Close")};
        Alert unsavedChanges = new Alert(AlertType.WARNING,
                "You have unsaved changes. Are you sure you want to close?",
                buttons[0], buttons[1], buttons[2], buttons[3]);
        unsavedChanges.setTitle("Unsaved Changes");
        ButtonType nextStep = unsavedChanges.showAndWait().get();
        if(nextStep == buttons[0]){         //save
            BetterPaint.getCurrentTab().saveImage();
            BetterPaint.tabPane.getTabs().remove(BetterPaint.getCurrentTab());
        }
        else if(nextStep == buttons[1]){    //save as
            FileChooser fc = new FileChooser();
            BetterPaint.getCurrentTab().saveImageAs(fc.showSaveDialog(BetterPaint.mainStage));
            BetterPaint.tabPane.getTabs().remove(BetterPaint.getCurrentTab());
        }
        else if(nextStep == buttons[3]){    //close
            BetterPaint.tabPane.getTabs().remove(BetterPaint.getCurrentTab());
            BetterPaint.tabPane.getSelectionModel().selectNext();   //selects the next open tab
        }
    }
}
