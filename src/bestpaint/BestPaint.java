package bestpaint;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * @author Spencer Gannon
 * @version 1.3.0
 */
public class BestPaint extends Application {
    public final static String IMAGE_FOLDER = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BestPaint\\images\\";

    private final static String TITLE = "BestPaint";
    private final static String VER_NUM = "1.3.0";
    private final static String SUBTITLE = "The final countdown";
    private final static int WINDOW_LENGTH = 1700;
    private final static int WINDOW_HEIGHT = 1000;
    
    public static int autosaveInterval;
    //major window elements made public and static to access outside of main
    public static Stage mainStage;
    public static TabPane tabpane;
    public static CleanToolBar toolbar;
    public static CleanMenuBar menubar;
    public static Timeline autosave;
    
    @Override
    public void start(Stage primeStage) {
        BestPaint.mainStage = primeStage;   //associates the stage being shown with the public mainStage
        //layout objects
        tabpane = new TabPane();
        toolbar = new CleanToolBar();
        menubar = new CleanMenuBar();
        BorderPane bp = new BorderPane();
        VBox topbars = new VBox(menubar, toolbar);
        Scene scene = new Scene(bp, WINDOW_LENGTH, WINDOW_HEIGHT);
        
        mainStage.setOnCloseRequest((WindowEvent w) -> {
            w.consume();
            Popups.createExitAlert(mainStage);
        });
        //Setting up layout
        bp.setTop(topbars);
        bp.setCenter(tabpane);
        
        tabpane.getTabs().add(new BestTab());
        tabpane.getSelectionModel().selectFirst();
        
        primeStage.setMaximized(true);
        primeStage.setTitle(TITLE + " - " + VER_NUM + " - " + SUBTITLE);
        primeStage.setScene(scene);
        primeStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Gets the current tab in use
     * @return The BestTab object currently selected
     */
    public static BestTab getCurrentTab(){return (BestTab)tabpane.getSelectionModel().getSelectedItem();}
    /**
     * Removes the current tab in use
     */
    public static void removeCurrentTab(){BestPaint.tabpane.getTabs().remove(BestPaint.getCurrentTab());}
}
