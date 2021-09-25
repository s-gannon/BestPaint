package betterpaint;

import java.io.File;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * BetterPaint, a better Microsoft Paint
 * @author Spencer Gannon
 * @version 0.2.1, 9/17/2021
 */
public class BetterPaint extends Application {
    private final String NAME = "BetterPaint";
    private final static String VER_NUM = "v0.3.0";
    public final static String[] TITLE_CARDS = {
        "Doing the Minecraft and Terraria thing!", 
        "Praise to the Lord Rosasco", 
        "Zebra Party at my house!",
        "Writing this in Computational Music",
        "Down with Scratch",
        "Pulling a Zuckerberg every now and again",
        "Get Winkelvoss'd",
        "Orginization, shmorginization",
        "I do a little bit of trolling from time to time", 
        "DeGoode pain in my ass",
        "Down with JES & Jython",
        "Hilarious",
        "Stop pushing issues to my goddamn repo",
        "Git rebasing Nick's Paint project to nothingness"};
    private final Random rand = new Random();
    
    public final static String REL_NOTES_PATH = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BetterPaint\\release-notes.txt";
    public final static String ABOUT_PATH = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BetterPaint\\about.txt";
    public final static String HELP_PATH = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BetterPaint\\help.txt";
    public static Stage mainStage;      //allows other classes to access the stage
    public static TabPane tabPane;
    
    @Override
    public void start(Stage stage) throws Exception {
        //----------------DECLARATIONS, INITIALIZATIONS, ETC.-----------------//
        mainStage = stage;  //definitely janky; makes mainStage refer to the stage passed up top
        final int SCENE_HEIGHT = 900;
        final int SCENE_WIDTH = 1550;   //consts for the starting width and height
        
        //layout objects
        tabPane = new TabPane();
        StackPane mainStackPane = new StackPane();      //used for layering items
        BorderPane mainBorderPane = new BorderPane();   //used for the menu bar (and etc)
        VBox separator = new VBox();                //separates the canvas tools and menu bar
        
        Scene scene = new Scene(mainStackPane, SCENE_WIDTH, SCENE_HEIGHT);   //lets me draw on top of things later using stack
        
        CleanerMenuBar cleanMenuBar = new CleanerMenuBar();//my better menu bar (fantastic)
        CleanerCanvasTools cleanCanvasTools = new CleanerCanvasTools();
        
        mainStage.setOnCloseRequest((WindowEvent w) -> {
            w.consume();
            BetterPopups.createExitAlert(mainStage);
        });
        //---------------------------DOING THINGS-----------------------------//
        //setting up the menu bar, tool bar, and tabs positons
        mainBorderPane.setCenter(tabPane);           //adds the scrolling canvas to the center of the BorderPane for centering things
        mainBorderPane.setTop(separator);               //adds separator to the top
        separator.getChildren().addAll(cleanMenuBar, cleanCanvasTools);   //adds menu bar to top and canvas tools under it
        mainStackPane.getChildren().add(mainBorderPane);//stackpane in order to draw over the canvas (to draw and whatever)        
        
        //setting the main scene and title
        mainStage.setTitle(NAME + " - " + VER_NUM + " - " + TITLE_CARDS[rand.nextInt(TITLE_CARDS.length)]);
        mainStage.setScene(scene);
        mainStage.show();   //finally, makes everything visible
        
        BetterPopups.createTextWindow(new File(REL_NOTES_PATH), "Release Notes", 635, 500); //opens the release notes at the start of the program
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Gets the current tab in use
     * @return The index of the currently selected tab 
     */
    public static BetterTab getCurrentTab(){return (BetterTab)tabPane.getSelectionModel().getSelectedItem();}
}