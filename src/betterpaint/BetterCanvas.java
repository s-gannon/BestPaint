package betterpaint;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javax.imageio.ImageIO;

public class BetterCanvas extends Canvas{
    private static File path;   //the path that the canvas will save images to
    private GraphicsContext gc; //the GraphicsContext (how things are drawn)
    private Line line = null;
    //Constructors
    public BetterCanvas(){
        super();
        gc = getGraphicsContext2D();
        path = null;
        
        this.setOnMousePressed(e -> {
            switch(CleanerCanvasTools.getCurrentTool()){
                case("Line"):
                    line = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                case("Freehand"):
                    //todo; the freehand code
            }
            
        });
        this.setOnMouseDragged(e -> {
            switch(CleanerCanvasTools.getCurrentTool()){
                case("Line"):
                    line.setEndX(e.getX());
                    line.setEndY(e.getY());
                case("Freehand"):
                    //todo; the freehand code
            } 
        });
        this.setOnMouseReleased(e -> {
            switch(CleanerCanvasTools.getCurrentTool()){
                case("Line"):
                    line.setEndX(e.getX());
                    line.setEndY(e.getY());
                    gc.setLineWidth(CleanerCanvasTools.getLineWidth());
                    gc.setStroke(CleanerCanvasTools.getColor());
                    gc.setLineCap(StrokeLineCap.ROUND);
                    gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                    line = null;
                case("Freehand"):
                    //todo; the freehand code
            }
        });
    }
    /**
     * Sets the path of the canvas
     * @param path The path to set it to (will be used for saving)
     */
    public void setPath(File path){this.path = path;}
    /**
     * Gets the path of the canvas
     * @return The path that the canvas is at
     */
    public File getPath(){return path;}
    /**
     * Saves a snapshot of the current canvas to the image specified at the path
     */
    public void saveImage(){
        WritableImage im = snapshot(new SnapshotParameters(), null);
        try{
            if(this.path != null)
                ImageIO.write(SwingFXUtils.fromFXImage(im, null), "png", this.path);
        }catch(IOException ex){ 
            System.out.println(ex.toString());
        } 
    }
    /**
     * Saves an image at a path that isn't the path the canvas is already at
     * @param path The path to save the canvas to (sets the path when saving as)
     */
    public void saveImageAs(File path){
        setPath(path);
        saveImage();
    }
    /**
     * Draws a line from x1, y1 to x2, y2 using the Graphics Context
     * @param x1 starting x
     * @param y1 starting y
     * @param x2 ending x
     * @param y2 ending y
     */
    public void drawLine(double x1, double y1, double x2, double y2){
        gc.strokeLine(x1,y1,x2,y2);
    }
    /**
     * Draws a picture from a given file path onto a canvas
     */
    public void drawImage(){
        if(path != null){
            Image im = new Image(path.toURI().toString());
            gc.clearRect(0, 0, getWidth(), getHeight()); //cleans the canvas for next image
            setWidth(im.getWidth());
            setHeight(im.getHeight()); //sizes the canvas to the size of the image
            gc.drawImage(im, 0, 0);
        }
    }
}
