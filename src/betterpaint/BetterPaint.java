package betterpaint;
/**Spencer Gannon
 * Better Paint
 * CS-250
 **/
import java.io.File;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class BetterPaint extends Application {
    private final String NAME = "BetterPaint";
    private final static String VER_NUM = "v0.2.0";
    private final static String[] TITLE_CARDS = {
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
        "Down with JES & Jython"};
    private final Random rand = new Random();
    
    public final static String REL_NOTES_PATH = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BetterPaint\\release-notes.txt";
    public final static String ABOUT_PATH = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BetterPaint\\about.txt";
    public final static String HELP_PATH = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BetterPaint\\help.txt";
    public static Stage mainStage;      //allows other classes to access the stage
    public static BetterCanvas canvas;  //allows other classes to access the canvas
    
    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;  //definitely janky; makes mainStage refer to the stage passed up top
        //----------------DECLARATIONS, INITIALIZATIONS, ETC.-----------------//
        final int SCENE_HEIGHT = 800;
        final int SCENE_WIDTH = 1000;   //consts for the starting width and height
        
        //layout objects
        StackPane stackPane = new StackPane();      //used for layering items
        BorderPane borderPane = new BorderPane();   //used for the menu bar (and etc)
        ScrollPane canvasScroll = new ScrollPane(); //used to scroll the canvas if it's too big (such a fucking godsend)
        HBox canvasTools = new HBox();              //holds all of the canvas tools (for right now)
        VBox separator = new VBox();                //separates the canvas tools and menu bar
        
        Scene scene = new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);   //lets me draw on top of things later using stack
        
        CleanerMenuBar betterMenuBar = new CleanerMenuBar();//my better menu bar (fantastic)
        canvas = new BetterCanvas();                        //my better canvas (also fantastic)
        
        //---------------------------DOING THINGS-----------------------------//
        //setting up the canvas and menu bar positons
        canvasScroll.setContent(canvas);            //adds the canvas to the ScrollPane for scrolling on large images
        borderPane.setCenter(canvasScroll);         //adds the scrolling canvas to the center of the BorderPane for centering things
        borderPane.setTop(separator);               //adds separator to the top
        separator.getChildren().addAll(betterMenuBar, canvasTools);   //adds menu bar to top and canvas tools under it
        stackPane.getChildren().add(borderPane);    //stackpane in order to draw over the canvas (to draw and whatever)        
        
        //setting the main scene and title
        mainStage.setTitle(NAME + " - " + VER_NUM + " - " + TITLE_CARDS[rand.nextInt(TITLE_CARDS.length)]);  //doing the Minecraft thing; ask for explanation
        mainStage.setScene(scene);
        mainStage.show();   //finally, makes everything visible
        
        BetterPopups.createTextWindow(new File(REL_NOTES_PATH), "Release Notes"); //opens the release notes at the start of the program
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}