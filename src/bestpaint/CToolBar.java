package bestpaint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class CToolBar extends ToolBar{
    private final static String[] DRAW_TOOLS = {
        "None", "Dropper", "Cut", "Copy", "Paste", "Eraser", "Text", "Line", "Freehand", "Rectangle", 
        "Round Rectangle", "Square", "Ellipse", "Circle", "N-gon"};
    private final static Integer[] LINE_WIDTH_VALS = {1,2,5,10,15,20,25,50,100};
    //the buttons/modes
    private final Button RND_COLOR_BUTTON;
    private final Button RNBW_FUN_BUTTON;
    private final Button ROSASCO_BUTTON;
    private final Button KILL_BUTTON;
    private final Button GRADIENT_BUTTON;
    
    private static ComboBox<String> toolsBox;
    private static ComboBox<Integer> widthsBox;
    private static ColorPicker fillColorPicker;
    private static ColorPicker lineColorPicker;
    private static TextField autosaveTime;
    private static TextField numSides;
    private static Label zoomLabel;
    private static CheckBox setFill;
    private Random rnd;
    private static int usingWidth;
    private static int usingTool;
    private static int usingNumSides;
    private static int usingTime;
    
    public CToolBar(){
        super();
        //declaring all objects & values
        usingWidth = 1;
        usingTool = 0;
        usingNumSides = 3;
        usingTime = 30;
        
        ROSASCO_BUTTON = new Button("");
        KILL_BUTTON = new Button("");
        RND_COLOR_BUTTON = new Button("");
        RNBW_FUN_BUTTON = new Button("");
        GRADIENT_BUTTON = new Button("");
        
        numSides = new TextField("3");
        autosaveTime = new TextField(Integer.toString(usingTime));
        zoomLabel = new Label("100%");
        toolsBox = new ComboBox<>(FXCollections.observableArrayList(DRAW_TOOLS));
        widthsBox = new ComboBox<>(FXCollections.observableArrayList(LINE_WIDTH_VALS));
        fillColorPicker = new ColorPicker();
        lineColorPicker = new ColorPicker();
        setFill = new CheckBox();
        rnd = new Random();
        
        getItems().addAll(new Label(" Tools: "), toolsBox, numSides, new Separator(),
                new Label(" Line Color: "), lineColorPicker, new Label(" Fill Color: "),
                fillColorPicker, RND_COLOR_BUTTON, GRADIENT_BUTTON, new Separator(), KILL_BUTTON, RNBW_FUN_BUTTON, 
                ROSASCO_BUTTON, new Separator(), new Label(" Line Width: "), widthsBox, new Label(" Fill "), 
                setFill, new Separator(), new Label("Zoom: "), zoomLabel, new Separator(), new Label("Autosave every "), 
                autosaveTime, new Label(" seconds"));   
        
        //setting the default values for all of the things that look bad w/o them
        lineColorPicker.setValue(Color.BLACK);
        fillColorPicker.setValue(Color.WHITE);
        toolsBox.setValue("None");
                
        widthsBox.setEditable(true);
        widthsBox.setPrefWidth(90);
        widthsBox.setValue(1);
        
        numSides.setVisible(false);
        numSides.setPrefWidth(55);
        
        autosaveTime.setPrefWidth(55);
        
        //tooltips
        ROSASCO_BUTTON.setTooltip(new Tooltip("Rosasco Mode"));
        KILL_BUTTON.setTooltip(new Tooltip("Crash"));
        RND_COLOR_BUTTON.setTooltip(new Tooltip("Random Fill and Line Color"));
        RNBW_FUN_BUTTON.setTooltip(new Tooltip("Rainbow Fun Mode"));
        GRADIENT_BUTTON.setTooltip(new Tooltip("Gradient Fill Mode"));
        
        try {
            //setting graphics on buttons
            int size = 21;
            
            ROSASCO_BUTTON.setGraphic(new ImageView(new Image(new FileInputStream(BestPaint.IMAGE_FOLDER + "icons\\head.png"), size, size, true, true)));
            KILL_BUTTON.setGraphic(new ImageView(new Image(new FileInputStream(BestPaint.IMAGE_FOLDER + "icons\\crash.png"), size, size, true, true)));
            RND_COLOR_BUTTON.setGraphic(new ImageView(new Image(new FileInputStream(BestPaint.IMAGE_FOLDER + "icons\\random.png"), size, size, true, true)));
            RNBW_FUN_BUTTON.setGraphic(new ImageView(new Image(new FileInputStream(BestPaint.IMAGE_FOLDER + "icons\\rainbow.png"), size, size, true, true)));
            GRADIENT_BUTTON.setGraphic(new ImageView(new Image(new FileInputStream(BestPaint.IMAGE_FOLDER + "icons\\gradient.png"), size, size, true, true)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CToolBar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //listeners
        toolsBox.getSelectionModel().selectedIndexProperty().addListener((observable, value, newValue) -> {
            usingTool = newValue.intValue();
            if(DRAW_TOOLS[usingTool].equals("N-gon"))   //enables the text input for the n-gon option and disables it otherwise
                numSides.setVisible(true);
            else
                numSides.setVisible(false);
        });     //changes the index of the tool being used to whatever was selected
        widthsBox.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if(!isNowFocused){
                if(Integer.parseInt(widthsBox.getEditor().getText()) >= 1){
                    widthsBox.setValue(Integer.parseInt(widthsBox.getEditor().getText()));
                }
                else{
                    widthsBox.setValue(1);
                }
            }   //listens to the ComboBox TextInput; if it changes it sets the value to whatever was input
        });
        numSides.textProperty().addListener((observable, value, newValue) -> {
            if(Integer.parseInt(newValue) >= 3)
                usingNumSides = Integer.parseInt(newValue);
            else{
                numSides.setText("3");
            }
        });     //listens and returns num of sides to use in ngon
        autosaveTime.textProperty().addListener((observable, value, newValue) -> {
            if(Integer.parseInt(newValue) >= 1)
                usingTime = Integer.parseInt(newValue);
            else{
                autosaveTime.setText("1");
            }
            BestPaint.getCurrentTab().updateAutosaveTimer();
        });     //listens and returns time
        
        //buttons/action events
        widthsBox.setOnAction((ActionEvent e) -> {   //changes the value of usingWidth when the ComboBox is used/value changes
            usingWidth = widthsBox.getValue();
        });    
        RND_COLOR_BUTTON.setOnAction((ActionEvent e) -> { //changes the colors in the fill and line color pickers to the same rand color
            double[] rgb = {rnd.nextDouble(),rnd.nextDouble(),rnd.nextDouble()};    //gets rand vals for RGB from 0 to 1
            Color randCol = Color.color(rgb[0],rgb[1],rgb[2]);
            lineColorPicker.setValue(randCol);
            fillColorPicker.setValue(randCol);
        });
        //mode buttons
        RNBW_FUN_BUTTON.setOnAction((ActionEvent e) -> {
            WritableImage wi = new WritableImage(
                    (int) BestPaint.getCurrentTab().getCanvasWidth(),
                    (int) BestPaint.getCurrentTab().getCanvasHeight());
            PixelWriter pw = wi.getPixelWriter();
            for(int y = 0; y < wi.getHeight(); y++)
                for(int x = 0; x < wi.getWidth(); x++)
                    pw.setColor(x, y, Color.color(rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble()));
            BestPaint.getCurrentTab().drawImageAt(wi, 0, 0);
            BestPaint.getCurrentTab().updateStacks();
        });
        ROSASCO_BUTTON.setOnAction((ActionEvent e) -> {
            File rosascoDir = new File(BestPaint.IMAGE_FOLDER);
            File[] subFiles = rosascoDir.listFiles();   //list of files in the folder
            BestPaint.mainStage.setTitle("So do you want the good news or the bad news?");
            try {
                Image im = new Image(subFiles[rnd.nextInt(subFiles.length)].toURI().toString());
                BestPaint.getCurrentTab().drawImageAt(
                        im,
                        rnd.nextDouble() * BestPaint.getCurrentTab().getCanvasWidth()/3,
                        rnd.nextDouble() * BestPaint.getCurrentTab().getCanvasHeight()/3);
            } catch (Exception ex) {
                System.out.println(ex);
            }
            BestPaint.getCurrentTab().updateStacks();
        });
        KILL_BUTTON.setOnAction((ActionEvent e) -> {
            //later it will blur image before crashing
            try{
                BestPaint.getCurrentTab().saveImage();
            }catch(Exception ex){
                System.out.println("Could not save file. Error: " + ex);
            }
            for(;true;){    //cursed
                System.out.println("Buckle up motherfuckers, it's time to Crash");
            }
        });
        GRADIENT_BUTTON.setOnAction((ActionEvent e) -> {
            BestPaint.getCurrentTab().getCanvas().fillGradient();
            BestPaint.getCurrentTab().updateStacks();
        });
    }
    /**
     * Sets the zoom label to the specified zoom value
     * @param zoomVal A double representing the zoom value (i.e. the scale of the canvas)
     */
    public static void setZoomLabel(double zoomVal){ zoomLabel.setText(String.format("%.1f", zoomVal * 100) + "%"); }
    /**
     * Gets the number of sides to use in an n-gon
     * @return An integer representing the number of sides
     */
    public static int getNumSides(){return usingNumSides;}
    /**
     * Gets the current tool selected
     * @return The String representing the current tool
     */
    public static String getCurrentTool(){ return DRAW_TOOLS[usingTool]; }
    /**
     * Gets the color from the line color picker
     * @return The Color object representing the color in the picker
     */
    public static Color getLineColor(){ return lineColorPicker.getValue(); }
    /**
     * Gets the color from the fill color picker
     * @return The Color object representing the color in the picker
     */
    public static Color getFillColor(){ return fillColorPicker.getValue(); }
    /**
     * Sets the color of the line color picker
     * @param color The color to set the line color picker to
     */
    public static void setLineColor(Color color){ lineColorPicker.setValue(color); }
    /**
     * Sets the color of the fill color picker
     * @param color The color to set the fill color picker to
     */
    public static void setFillColor(Color color){ fillColorPicker.setValue(color); }
    /**
     * Gets the line width from the editable combo box
     * @return The integer representing the width
     */
    public static int getLineWidth(){ return usingWidth; }
    /**
     * Gets the status of the fill check box
     * @return The boolean value representative of whether the check box is checked
     */
    public static boolean getFillStatus(){ return setFill.isSelected();}
    /**
     * 
     * @return An int representing the amount of seconds 
     */
    public static int getAutosaveTime(){return usingTime;}
}
