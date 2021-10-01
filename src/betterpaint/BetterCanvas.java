package betterpaint;

import java.io.File;
import java.util.Random;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;

public class BetterCanvas extends Canvas{
    private boolean hasUnsavedChanges;
    private double x, y, scale;     //x and y position of the mouse and the current scale of the picture
    private GraphicsContext gc;     //the GraphicsContext (how things are drawn) 
    private Random rnd;
    //Constructors
    public BetterCanvas(){
        super();
        hasUnsavedChanges = false;
        scale = 1;
        x = y = 0;
        gc = getGraphicsContext2D();
        rnd = new Random();
        
        gc.setLineCap(StrokeLineCap.ROUND); //sets it to nice round line caps
        this.setOnMousePressed(e -> {
            x = e.getX();
            y = e.getY();
            gc.setLineWidth(CleanerCanvasTools.getLineWidth()); //setting all of the paint settings
            gc.setStroke(CleanerCanvasTools.getLineColor());    //on click before anything is truly
            gc.setFill(CleanerCanvasTools.getFillColor());      //drawn
        });
        this.setOnMouseDragged(e -> {
            switch (CleanerCanvasTools.getCurrentTool()) {
                case ("Freehand"):
                    gc.strokeLine(x, y, e.getX(), e.getY());
                    x = e.getX();
                    y = e.getY();
                    break;
                case ("Dropper"):   //clean up later
                    SnapshotParameters sp = new SnapshotParameters();
                    WritableImage wi = new WritableImage(1,1);
                    PixelReader pr = wi.getPixelReader();
                    
                    sp.setViewport(new Rectangle2D(x,y,1,1));
                    this.snapshot(sp, wi);
                    
                    CleanerCanvasTools.setLineColor(pr.getColor(0,0));
                    CleanerCanvasTools.setFillColor(pr.getColor(0,0));
                    x = e.getX();
                    y = e.getY();
                    break;
            }
        });
        this.setOnMouseReleased(e -> {
            switch (CleanerCanvasTools.getCurrentTool()) {
                case ("Line"):
                    gc.strokeLine(x, y, e.getX(), e.getY());
                    this.setUnsavedChanges(true);
                    break;
                case ("Freehand"):
                    this.setUnsavedChanges(true);
                    break;
                case ("Rectangle"):
                    if (CleanerCanvasTools.getFillStatus()) {
                        gc.fillRect((x < e.getX() ? x : e.getX()), (y < e.getY() ? y : e.getY()), //top left x and y coordinate
                                Math.abs(e.getX() - x), Math.abs(e.getY() - y));        //distance between start and end x and y
                    }
                    gc.strokeRect((x < e.getX() ? x : e.getX()), (y < e.getY() ? y : e.getY()), //top left x and y coordinate
                            Math.abs(e.getX() - x), Math.abs(e.getY() - y));            //distance between start and end x and y
                    this.setUnsavedChanges(true);
                    break;
                case ("Round Rectangle"):
                    if (CleanerCanvasTools.getFillStatus()) {
                        gc.fillRoundRect((x < e.getX() ? x : e.getX()), (y < e.getY() ? y : e.getY()), //top left x and y coordinate
                                Math.abs(e.getX() - x), Math.abs(e.getY() - y), 10, 10);//distance between start and end x and y
                    }
                    gc.strokeRoundRect((x < e.getX() ? x : e.getX()), (y < e.getY() ? y : e.getY()), //top left x and y coordinate
                            Math.abs(e.getX() - x), Math.abs(e.getY() - y), 10, 10);    //distance between start and end x and y
                    this.setUnsavedChanges(true);
                    break;
                case ("Square"): //logic is somewhat different
                    if (CleanerCanvasTools.getFillStatus()) {
                        gc.fillRect((x < e.getX() ? x : e.getX()), (y < e.getY() ? y : e.getY()), //top left x and y coordinate
                                Math.abs(e.getX() - x), Math.abs(e.getX() - x));        //distance between start and end x and y
                    }
                    gc.strokeRect((x < e.getX() ? x : e.getX()), (y < e.getY() ? y : e.getY()), //top left x and y coordinate
                            Math.abs(e.getX() - x), Math.abs(e.getX() - x));            //distance between start and end x and y
                    this.setUnsavedChanges(true);
                    break;
                case ("Ellipse"):
                    if (CleanerCanvasTools.getFillStatus()) {
                        gc.fillOval((x < e.getX() ? x : e.getX()), (y < e.getY() ? y : e.getY()),
                                Math.abs(e.getX() - x), Math.abs(e.getY() - y));
                    }
                    gc.strokeOval((x < e.getX() ? x : e.getX()), (y < e.getY() ? y : e.getY()),
                            Math.abs(e.getX() - x), Math.abs(e.getY() - y));
                    this.setUnsavedChanges(true);
                    break;
                case ("Circle"): //logic is somewhat different; see squares
                    if (CleanerCanvasTools.getFillStatus()) {
                        gc.fillOval((x < e.getX() ? x : e.getX()), (y < e.getY() ? y : e.getY()),
                                Math.abs(e.getX() - x), Math.abs(e.getX() - x));
                    }
                    gc.strokeOval((x < e.getX() ? x : e.getX()), (y < e.getY() ? y : e.getY()),
                            Math.abs(e.getX() - x), Math.abs(e.getX() - x));
                    this.setUnsavedChanges(true);
                    break;
                case ("Text"):
                    String text = BetterPaint.TITLE_CARDS[rnd.nextInt(BetterPaint.TITLE_CARDS.length)];
                    gc.setLineWidth(1); //line width and text do NOT play well together
                    gc.setFont(new Font("calibri", CleanerCanvasTools.getLineWidth())); //font size taken from line width
                    gc.setFill((CleanerCanvasTools.getFillStatus() ? 
                            CleanerCanvasTools.getFillColor() : CleanerCanvasTools.getLineColor()));
                    //sets the fill to the line color if the fill is not clicked, else sets to fill color
                    if(CleanerCanvasTools.getFillStatus())
                        gc.fillText(text, x, y);
                    gc.strokeText(text, x, y);
                    this.setUnsavedChanges(true);
                    break;
                case ("Dropper"):   //clean up later
                    SnapshotParameters sp = new SnapshotParameters();
                    WritableImage wi = new WritableImage(1,1);
                    PixelReader pr = wi.getPixelReader();
                    
                    sp.setViewport(new Rectangle2D(x,y,1,1));
                    this.snapshot(sp, wi);
                    
                    CleanerCanvasTools.setLineColor(pr.getColor(0,0));
                    CleanerCanvasTools.setFillColor(pr.getColor(0,0));
                    this.setUnsavedChanges(true);
                    break;
            }
            //if there are now unsaved changes, sets the tab title with the * to reflect it
            CleanerCanvasTools.textTool.setSelected(false);
            CleanerCanvasTools.colorDropper.setSelected(false);
            BetterPaint.getCurrentTab().setTitle(BetterPaint.getCurrentTab().getTitle());
            x = y = 0;
        });
    }
    /**
     * Sets the value of hasUnsavedChanges to a boolean value
     * @param uc The boolean value to set the hasUnsavedChanges var to
     */
    public void setUnsavedChanges(boolean uc){this.hasUnsavedChanges = uc;}
    /**
     * Gets the value of hasUnsavedChanges from the variable
     * @return The value of hasUnsavedChanges
     */
    public boolean getUnsavedChanges(){return hasUnsavedChanges;}
    /**
     * Sets the scale of the canvas
     * @param scale A double that represents the scale
     */
    public void setScale(double scale){
        this.scale = scale;
        setScaleX(scale);
        setScaleY(scale);
    }
    /**
     * Gets the current scale of the canvas
     * @return A double that represents the scale
     */
    public double getScale(){return scale;}
    /**
     * Resets the scale of the canvas to scale 1
     */
    public void resetScale(){setScale(1);}
    /**
     * Draws a picture from a given file path onto a canvas at the origin
     * @param path the path to get the image from
     */
    public void drawImage(File path){
        if(path != null){
            Image im = new Image(path.toURI().toString());
            clearCanvas();
            setWidth(im.getWidth());
            setHeight(im.getHeight()); //sizes the canvas to the size of the image
            gc.drawImage(im, 0, 0);
        }
    }
    /**
     * Clears everything on the canvas
     */
    public void clearCanvas(){gc.clearRect(0, 0, getWidth(), getHeight());}
    /**
     * Zooms the canvas in
     */
    public void zoomIn(){
        if(scale <= 10){
            scale *= 1.15;
            setScaleX(scale);
            setScaleY(scale);
        }
    }
    /**
     * Zooms the canvas out
     */
    public void zoomOut(){
        if(scale >= 0.15){
            scale /= 1.15;
            setScaleX(scale);
            setScaleY(scale);
        }
    }
}
