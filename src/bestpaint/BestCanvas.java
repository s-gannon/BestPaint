package bestpaint;

import java.util.Stack;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * Extends the DrawCanvas; mainly has the listeners that allow for drawing to the canvas
 * @author Spencer Gannon
 */
public class BestCanvas extends DrawCanvas{
    private double x, y;
    private Image clipboard;
    
    private Stack<Image> undoStack;
    private Stack<Image> redoStack;
    
    public BestCanvas(){
        super();
        x = y = 0;
        this.clipboard = null;
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        
        this.setWidth(1200);
        this.setHeight(800);
        //draws white background
        this.setFillColor(Color.WHITE);
        this.setLineColor(Color.WHITE);
        this.setFillShape(true);
        this.drawRect(0,0,this.getWidth(),this.getHeight());
        this.setFillShape(false);
        
        this.undoStack.push(this.getRegion(0,0, this.getWidth(), this.getHeight())); //pushes the blank/original image to first pos of undo stack

        this.setOnMousePressed(e -> {   //updates all of this on click
            x = e.getX();
            y = e.getY();
            this.setLineColor(CleanToolBar.getLineColor());
            this.setFillColor(CleanToolBar.getFillColor());
            this.setLineWidth(CleanToolBar.getLineWidth());
            this.setFillShape(CleanToolBar.getFillStatus());
            switch(CleanToolBar.getCurrentTool()){
                case("Eraser"):
                    this.setLineColor(Color.WHITE);
                    break;
                case("Line"):
                    this.drawLine(x, y, x, y);
                    this.updateStacks();
                    break;
                case("Rectangle"):
                    this.drawRect(x,y,x,y);
                    this.updateStacks();
                    break;
                case("Round Rectangle"):
                    this.drawRoundRect(x,y,x,y);
                    this.updateStacks();
                    break;
                case("Square"):
                    this.drawSquare(x, y, x, y);
                    this.updateStacks();
                    break;
                case("Ellipse"):
                    this.drawEllipse(x,y,x,y);
                    this.updateStacks();
                    break;
                case("Circle"):
                    this.drawNgon(x, y, x, y, 314);
                    this.updateStacks();
                    break;
                case("N-gon"):
                    this.drawNgon(x, y, x, y, 5);
                    this.updateStacks();
                    break;
                case("Cut"):
                    this.setLineWidth(2);
                    this.setFillShape(false);
                    this.setLineColor(Color.BLACK);
                    this.drawRect(x,y,x,y);
                    this.updateStacks();
                    break;
                case("Copy"):
                    this.setLineWidth(2);
                    this.setFillShape(false);
                    this.setLineColor(Color.BLACK);
                    this.drawRect(x,y,x,y);
                    this.updateStacks();
                    break;
                case("Paste"):
                    try{
                        this.drawImageAt(clipboard, e.getX(), e.getY());
                    }catch(Exception f){
                        System.out.println(e);
                    }
                    this.updateStacks();
                    break;
                case("Text"):
                    this.drawText("Sample Text", e.getX(), e.getY());
                    this.updateStacks();
                    break;
                case("Dropper"):
                    CleanToolBar.setFillColor(this.getColorAtPixel(x,y));
                    CleanToolBar.setLineColor(this.getColorAtPixel(x,y));
                    break;
            }
        });
        this.setOnMouseDragged(e -> {
            switch(CleanToolBar.getCurrentTool()){
                case("Freehand"):
                    this.drawLine(x, y, e.getX(), e.getY());
                    x = e.getX();
                    y = e.getY();
                    break;
                case("Dropper"):
                    CleanToolBar.setFillColor(this.getColorAtPixel(e.getX(),e.getY()));
                    CleanToolBar.setLineColor(this.getColorAtPixel(e.getX(),e.getY()));
                    break;
                case("Eraser"):
                    this.drawLine(x, y, e.getX(), e.getY());
                    x = e.getX();
                    y = e.getY();
                    break;
                case("Text"):
                    this.undo();
                    this.drawText("Sample Text", e.getX(), e.getY());
                    this.updateStacks();
                    break;
                case("Line"):
                    this.undo();
                    this.drawLine(x, y, e.getX(), e.getY());
                    this.updateStacks();
                    break;
                case("Rectangle"):
                    this.undo();
                    this.drawRect(x, y, e.getX(), e.getY());
                    this.updateStacks();
                    break;
                case("Round Rectangle"):
                    this.undo();
                    this.drawRoundRect(x,y,e.getX(),e.getY());
                    this.updateStacks();
                    break;
                case("Square"):
                    this.undo();
                    this.drawSquare(x, y, e.getX(), e.getY());
                    this.updateStacks();
                    break;
                case("Ellipse"):
                    this.undo();
                    this.drawEllipse(x,y,e.getX(),e.getY());
                    this.updateStacks();
                    break;
                case("Circle"):
                    this.undo();
                    this.drawNgon(x, y, e.getX(), e.getY(), 314);
                    this.updateStacks();
                    break;
                case("N-gon"):
                    this.undo();
                    this.drawNgon(x, y, e.getX(), e.getY(), CleanToolBar.getNumSides());
                    this.updateStacks();
                    break;
                case("Cut"):
                    this.undo();
                    this.drawRect(x, y, e.getX(), e.getY());
                    this.updateStacks();
                    break;
                case("Copy"):
                    this.undo();
                    this.drawRect(x, y, e.getX(), e.getY());
                    this.updateStacks();
                    break;
                case("Paste"):
                    this.undo();
                    try{
                        this.drawImageAt(clipboard, e.getX(), e.getY());
                    }catch(Exception f){
                        System.out.println(e);
                    }
                    this.updateStacks();
                    break;
            }
        });
        this.setOnMouseReleased(e -> {
            switch(CleanToolBar.getCurrentTool()){
                case("Freehand"):
                    this.drawLine(x, y, e.getX(), e.getY());
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Eraser"):
                    this.drawLine(x, y, e.getX(), e.getY());
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Line"):
                    this.undo();
                    this.drawLine(x, y, e.getX(), e.getY());
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Rectangle"):
                    this.undo();
                    this.drawRect(x, y, e.getX(), e.getY());
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Round Rectangle"):
                    this.undo();
                    this.drawRoundRect(x,y,e.getX(),e.getY());
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Square"):
                    this.undo();
                    this.drawSquare(x, y, e.getX(), e.getY());
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Ellipse"):
                    this.undo();
                    this.drawEllipse(x, y, e.getX(), e.getY());
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Circle"):
                    this.undo();
                    this.drawNgon(x, y, e.getX(), e.getY(), 314);
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("N-gon"):
                    this.undo();
                    this.drawNgon(x, y, e.getX(), e.getY(), CleanToolBar.getNumSides());
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Text"):
                    this.undo();
                    this.drawText("Sample Text", e.getX(), e.getY());
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Dropper"):
                    CleanToolBar.setFillColor(this.getColorAtPixel(e.getX(),e.getY()));
                    CleanToolBar.setLineColor(this.getColorAtPixel(e.getX(),e.getY()));
                    break;
                case("Cut"):
                    this.undo();
                    this.clipboard = this.getRegion(x, y, e.getX(), e.getY());
                    this.setLineWidth(1);
                    this.setFillShape(true);
                    this.setLineColor(CleanToolBar.getFillColor());
                    this.drawRect(x, y, e.getX(), e.getY());
                    this.updateStacks();
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Copy"):
                    this.undo();
                    this.clipboard = this.getRegion(x, y, e.getX(), e.getY());
                    this.updateStacks();
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Paste"):
                    this.undo();
                    if(this.clipboard != null){
                        this.drawImageAt(this.clipboard, e.getX(), e.getY());
                    }
                    BestPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
            }
            BestPaint.getCurrentTab().updateTabTitle();
            this.updateStacks();
            x = y = 0;  //resets the x and y coordinates at the end to be safe
        });
    }
    /**
     * Undoes the previous change to the image and draws to canvas
     */
    public void undo(){
        Image im = undoStack.pop();
        if(!undoStack.empty()){
            redoStack.push(im);
            this.drawImage(undoStack.peek());
        }
        else{   //puts image back because in this case it's the base/only one in stack
            this.drawImage(im);
            undoStack.push(im);
        }
    }
    /**
     * Redoes the change that you undid and draws to canvas
     */
    public void redo(){
        if(!redoStack.empty()){
            Image im = redoStack.pop();
            undoStack.push(im);
            this.drawImage(im);
        }   
    }
    /**
     * Updates the stacks
     */
    public void updateStacks(){
        undoStack.push(this.getRegion(0,0, this.getWidth(), this.getHeight())); 
        redoStack.clear();  //clear redo every time to avoid problems with breaking time
    }
}
