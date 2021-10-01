package betterpaint;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

public class BetterTab extends Tab{
    private File path;           //the path that we will save to
    private String title;        //keep track of current pane title
    private BetterCanvas canvas; //now every tab comes with a canvas!
    private ScrollPane scroll;   //and it scrolls!
    private Group canvasGroup;
    
    public BetterTab(){
        super();
        this.path = null;
        this.title = "Untitled";
        this.canvas = new BetterCanvas();
        this.setup();
    }
    public BetterTab(File path){
        super();
        this.path = path;
        this.title = path.getName();
        this.canvas = new BetterCanvas();
        this.canvas.drawImage(path);
        this.setup();
    }
    /**
     * Commands used to set-up properties of the canvas, ScrollPane, etc.
     * Private because it will never need to be called outside of the constructor
     */
    private void setup(){
        this.canvasGroup = new Group(canvas);
        this.scroll = new ScrollPane(canvasGroup);   //these are defined here

        setContent(scroll);
        setText(title);
        this.setOnCloseRequest((Event e) -> {
            e.consume();
            if(canvas.getUnsavedChanges()) //if the canvas has unsaved changes, pull up the warning
                BetterPopups.createUnsavedAlert();
            else
                BetterPaint.tabPane.getTabs().remove(BetterPaint.getCurrentTab());
        });
    }
    /**
     * Sets the path of the canvas
     * @param path The path to set it to (will be used for saving)
     */
    public void setPath(File path){this.path = path;}
    /**
     * Sets the title of the tab to the specified string
     * @param title The new title to set the tab to 
     */
    public void setTitle(String title){
        if(canvas.getUnsavedChanges()) //gets what the tab is displaying
            setText("*" + title);
        else
            setText(this.title);            
        this.title = title;
    }
    /**
     * Gets the path of the canvas
     * @return The path that the canvas is at
     */
    public File getPath(){return path;}
    /**
     * Gets the title of the tab in a string
     * @return The title of the tab
     */
    public String getTitle(){return title;}
    /**
     * Gets the canvas in the tab
     * @return Returns the canvas object associated with the tab
     */
    public BetterCanvas getCanvas(){return canvas;}
    /**
     * Saves a snapshot of the current canvas to the image specified at the path
     */
    public void saveImage(){
        double tempScale = canvas.getScale();
        canvas.resetScale();    //returns the image to its original size for saving
        WritableImage im = canvas.snapshot(new SnapshotParameters(), null);
        try{
            if(this.path != null){
                ImageIO.write(SwingFXUtils.fromFXImage(im, null), "png", this.path);
                canvas.setUnsavedChanges(false);
                setTitle(this.getPath().getName());
            }
        }catch(IOException ex){ 
            System.out.println(ex.toString());
        }
        canvas.setScale(tempScale); //returns the image back to the zoom it was at
    }
    /**
     * Saves an image at a path that isn't the path the canvas is already at
     * @param path The path to save the canvas to (sets the path when saving as)
     */
    public void saveImageAs(File path){
        setPath(path);
        saveImage();
    }
}
