package bestpaint;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

public class BTab extends Tab{
    public Pane canvasPane;   //static
    private static FileChooser chooseFile;  
    
    private double scale;
    private boolean unsavedChanges;
    private File path;
    private String title;
    private BCanvas canvas;
    private ScrollPane scroll;
    private StackPane canvasStack;
    
    public BTab(){
        super();
        this.unsavedChanges = true;
        this.path = null;
        this.title = "Untitled";
        this.canvas = new BCanvas();
        setup();
    }
    public BTab(File file){
        super();
        this.unsavedChanges = false;
        this.path = file;
        this.title = path.getName();
        this.canvas = new BCanvas();
        setup();
    }
    /**
     * Function to assist with constructor setup; should not be called outside of BTab
     */
    private void setup(){
        this.scale = 1;
        chooseFile = new FileChooser();
        chooseFile.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("PNG", "*.png"),
            new FileChooser.ExtensionFilter("JPEG (stupid lossy)", "*.jpg"),
            new FileChooser.ExtensionFilter("Bitmap", "*.bmp"),
            new FileChooser.ExtensionFilter("Graphics Interchange Format", "*.gif")); //all of the extensions I "support"
        
        this.canvasPane = new Pane(canvas);
        this.canvasStack = new StackPane();
        this.canvasStack.getChildren().addAll(canvasPane);   //canvas -> canvasPane -> stackPane -> scroll
        this.scroll = new ScrollPane(this.canvasStack);

        this.setContent(scroll);
        this.setText((unsavedChanges ? "*" : "") + this.title);
        this.setOnCloseRequest((Event e) -> {
            e.consume();            //consumes the normal event call
            if(this.unsavedChanges) //if the canvas has unsaved changes, pull up the warning
                CPopups.createUnsavedAlert();
            else
                BestPaint.removeCurrentTab();
        });
        this.scroll.setPrefViewportWidth(this.canvas.getWidth());
        this.scroll.setPrefViewportHeight(this.canvas.getHeight());
    }
    /**
     * Gets the height of the canvas on the tab
     * @return The height as a double
     */
    public double getCanvasHeight(){return this.canvas.getHeight();}
    /**
     * Gets the width of the canvas on the tab
     * @return The width as a double
     */
    public double getCanvasWidth(){return this.canvas.getWidth();}
    /**
     * Draws an image to the canvas in the tab starting at the point x, y
     * @param im The image object to draw
     * @param x The x coordinate to draw the top left point of the image
     * @param y The y coordinate to draw the top left point of the image
     */
    public void drawImageAt(Image im, double x, double y){this.canvas.drawImageAt(im, x, y);}
    /**
     * Sets whether or not there is unsaved changes to true or false
     * @param unsavedChanges The boolean value to set the unsaved changes attribute to
     */
    public void setUnsavedChanges(boolean unsavedChanges){this.unsavedChanges = unsavedChanges;}
    /**
     * Sets the title to the specified string
     * @param title The string representative of the new title
     */
    public void setTitle(String title){
        this.title = title;
        this.updateTabTitle();
    }
    /**
     * Sets the file path
     * @param path The File object to set the new path to
     */
    public void setFilePath(File path){this.path = path;}
    /**
     * Updates the title of the tab to reflect unsaved changes
     */
    public boolean getUnsavedChanges(){return unsavedChanges;}
    /**
     * Gets the title
     * @return The String representative of the title
     */
    public String getTitle(){return this.title;}
    /**
     * Gets the File that represents the path of the tab
     * @return The File object representative of the path
     */
    public File getFilePath(){return this.path;}
    /**
     * Updates the tab's title to reflect title changes or unsaved changes
     */
    public void updateTabTitle(){
        if(this.path != null)
            this.title = this.path.getName();   //sets it to path name in case of update/save as
        if(this.unsavedChanges)
            this.setText("*" + this.title);
        else
            this.setText(this.title);
    }
    /**
     * Saves a snapshot of the current canvas to the image specified at the path
     */
    public void saveImage(){
        WritableImage im = canvas.snapshot(new SnapshotParameters(), null);
        try{
            if(this.path != null){
                ImageIO.write(SwingFXUtils.fromFXImage(im, null), "png", this.path);
                this.setUnsavedChanges(false);
                this.setTitle(this.getFilePath().getName());
            }
        }catch(IOException ex){ 
            System.out.println(ex.toString());
        }
        this.setUnsavedChanges(false);
        this.updateTabTitle();          //updates the title of the tab at the end to remove *
    }
    /**
     * Saves an image at a path that isn't the path the canvas is already at
     */
    public void saveImageAs(){
        File path = chooseFile.showSaveDialog(BestPaint.mainStage);
        this.setFilePath(path);
        this.saveImage();
    }
    /**
     * Saves an image at a given path
     * @param path The given path to save the image to
     */
    public void saveImageAs(File path){
        this.setFilePath(path);
        this.saveImage();
    }
    /**
     * Opens an image from a file selected by the user
     */
    public static void openImage(){
        File path = chooseFile.showOpenDialog(BestPaint.mainStage);
        BTab temp;
        if(path == null)
            temp = new BTab();
        else
            temp = new BTab(path);
        temp.updateTabTitle();          //makes sure the title is updated
        temp.canvas.drawImage(path);    //draws image from same path
        BestPaint.tabpane.getTabs().add(temp);  //adds it to the tabpane
        BestPaint.tabpane.getSelectionModel().select(temp);
    }
    /**
     * Opens an image from a file passed to the function
     * @param path The path to attempt to open the image from
     */
    public static void openImage(File path){
        BTab temp;
        if(path == null)
            temp = new BTab();
        else
            temp = new BTab(path);
        temp.updateTabTitle();          //makes sure the title is updated
        temp.canvas.drawImage(path);    //draws image from same path
        BestPaint.tabpane.getTabs().add(temp);  //adds it to the tabpane
        BestPaint.tabpane.getSelectionModel().select(temp);
    }
    /**
     * Opens a default/blank image to a new untitled tab
     */
    public static void openBlankImage(){
        BTab temp = new BTab(); //will open blank image by default
        BestPaint.tabpane.getTabs().add(temp);  //adds it to the tabpane
        BestPaint.tabpane.getSelectionModel().select(temp);
    }
    /**
     * Zooms in the pane by a factor of 1.15
     */
    public void zoomIn(){
        if(this.getScale() <= 10){
            this.setScale(this.getScale() * 1.15);
        }
        this.updateScale();
    }
    /**
     * Zooms out the pane by a factor of 1.15
     */
    public void zoomOut(){
        if(this.getScale() >= 0.1){
            this.setScale(this.getScale() / 1.15);
        }
        this.updateScale();
    }
    /**
     * Sets the scale to the specified value
     * @param scale The double to set scale to
     */
    public void setScale(double scale){this.scale = scale;}
    /**
     * Gets the scale of the stuff inside the tab
     * @return The scale as a double
     */
    public double getScale(){return this.scale;}
    /**
     * Resets the scale to normal (1)
     */
    public void resetScale(){this.setScale(1);}
    /**
     * Updates the scale of the canvas pane
     */
    public void updateScale(){  
        this.canvasPane.setScaleX(this.getScale());
        this.canvasPane.setScaleY(this.getScale());
        this.canvasPane.setPrefSize(this.canvas.getWidth()*this.getScale(), this.canvas.getHeight()*this.getScale());
        CToolBar.setZoomLabel(this.getScale());
    }
    /**
     * Undoes the previous change to the image and draws to canvas
     */
    public void undo(){
        this.canvas.undo();
        this.setUnsavedChanges(true);
    }
    /**
     * Redoes the change that you undid and draws to canvas
     */
    public void redo(){
        this.canvas.redo();
        this.setUnsavedChanges(true);
    }
}
