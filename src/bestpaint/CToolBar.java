package bestpaint;

import java.io.File;
import java.io.FileFilter;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class CToolBar extends ToolBar{
    private final static String[] DRAW_TOOLS = {
        "None", "Dropper", "Cut", "Paste", "Eraser", "Text", "Line", "Freehand", "Rectangle", 
        "Round Rectangle", "Square", "Ellipse", "Circle", "N-gon"};
    private final static Integer[] LINE_WIDTH_VALS = {1,2,5,10,15,20,25,50,100};
    private static ComboBox<String> toolsBox;
    private static ComboBox<Integer> widthsBox;
    private static ColorPicker fillColorPicker;
    private static ColorPicker lineColorPicker;
    private static TextField numSides;
    private Button rosascoMode;
    private static Label zoomLabel;
    private Button randColorButton;
    private Button rainbowFunMode;
    private Button killButton;
    private static CheckBox setFill;
    private Random rnd;
    private static int usingWidth;
    private static int usingTool;
    private static int usingNumSides;
    
    public CToolBar(){
        super();
        //declaring all objects
        usingWidth = 1;
        usingTool = 0;
        usingNumSides = 3;
        
        numSides = new TextField("3");
        zoomLabel = new Label("100%");
        toolsBox = new ComboBox<>(FXCollections.observableArrayList(DRAW_TOOLS));
        widthsBox = new ComboBox<>(FXCollections.observableArrayList(LINE_WIDTH_VALS));
        rosascoMode = new Button("Rosasco");
        killButton = new Button("Dog Ate Homework");
        randColorButton = new Button("Random Color");
        rainbowFunMode = new Button("RFM");
        fillColorPicker = new ColorPicker();
        lineColorPicker = new ColorPicker();
        setFill = new CheckBox();
        rnd = new Random();
        
        getItems().addAll(new Label(" Tools: "), toolsBox, numSides, new Separator(),
                new Label(" Line Color: "), lineColorPicker, new Label(" Fill Color: "),
                fillColorPicker, randColorButton, new Separator(), killButton, rainbowFunMode, rosascoMode, 
                new Separator(), new Label(" Line Width: "), widthsBox, new Label(" Fill "), setFill, 
                new Separator(), new Label("Zoom: "), zoomLabel);   
        
        //setting the default values for all of the things that look bad w/o them
        lineColorPicker.setValue(Color.BLACK);
        fillColorPicker.setValue(Color.BLACK);
        toolsBox.setValue("None");
                
        widthsBox.setEditable(true);
        widthsBox.setPrefWidth(90);
        widthsBox.setValue(1);
        
        numSides.setVisible(false);
        numSides.setPrefWidth(75);
        
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
                widthsBox.setValue(Integer.parseInt(widthsBox.getEditor().getText()));
            }   //listens to the ComboBox TextInput; if it changes it sets the value to whatever was input
        });
        numSides.textProperty().addListener((observable, value, newValue) -> {
            usingNumSides = Integer.parseInt(newValue);
        });     //listens and returns num of sides to use in ngon
        
        //buttons/action events
        widthsBox.setOnAction((ActionEvent e) -> {   //changes the value of usingWidth when the ComboBox is used/value changes
            usingWidth = widthsBox.getValue();
        });    
        randColorButton.setOnAction((ActionEvent e) -> { //changes the colors in the fill and line color pickers to the same rand color
            double[] rgb = {rnd.nextDouble(),rnd.nextDouble(),rnd.nextDouble()};    //gets rand vals for RGB from 0 to 1
            Color randCol = Color.color(rgb[0],rgb[1],rgb[2]);
            lineColorPicker.setValue(randCol);
            fillColorPicker.setValue(randCol);
        });
        rainbowFunMode.setOnAction((ActionEvent e) -> {
            WritableImage wi = new WritableImage(
                    (int) BestPaint.getCurrentTab().getCanvasWidth(),
                    (int) BestPaint.getCurrentTab().getCanvasHeight());
            PixelWriter pw = wi.getPixelWriter();
            for(int y = 0; y < wi.getHeight(); y++)
                for(int x = 0; x < wi.getWidth(); x++)
                    pw.setColor(x, y, Color.color(rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble()));
            BestPaint.getCurrentTab().drawImageAt(wi, 0, 0);
        });
        rosascoMode.setOnAction((ActionEvent e) -> {
            File rosascoDir = new File("C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BestPaint\\images\\");
            File[] subFiles = rosascoDir.listFiles();   //list of files in the folder
            BestPaint.mainStage.setTitle("So do you want the good news or the bad news?");
            try {
                Image im = new Image(subFiles[rnd.nextInt(subFiles.length)].toURI().toString());
                BestPaint.getCurrentTab().drawImageAt(
                        im,
                        rnd.nextDouble() * BestPaint.getCurrentTab().getCanvasWidth() / 4,
                        rnd.nextDouble() * BestPaint.getCurrentTab().getCanvasHeight() / 4);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });
        killButton.setOnAction((ActionEvent e) -> {
            while(true){
                System.out.println("Bwaha haha ha");
            }
        });
    }
    /**
     * Sets the zoom label to the specified zoom value
     * @param zoomVal A double representing the zoom value (i.e. the scale of the canvas)
     */
    public static void setZoomLabel(double zoomVal){ zoomLabel.setText(String.format("%.0f", zoomVal * 100) + "%"); }
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
}
