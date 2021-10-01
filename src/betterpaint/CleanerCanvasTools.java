package betterpaint;

import java.util.Random;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CleanerCanvasTools extends ToolBar{
    private static ColorPicker lineColorPicker;
    private static ColorPicker fillColorPicker;
    private static ComboBox<Integer> widthBox;
    private static ToggleGroup otherTools;
    public static ToggleButton colorDropper;  //janked together
    public static ToggleButton textTool;      //change later
    private static ChoiceBox drawToolBox;
    private static Button randButton;
    private static CheckBox setFill;
    private static int usingTool;
    private static int usingWidth;
    
    private final int[] LINE_WIDTH_VALS = {1,2,5,10,15,20,25,50}; //some standard line width values
    private final static String TEXT_IMAGE = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BetterPaint\\images\\text.png";
    private final static String DROPPER_IMAGE = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BetterPaint\\images\\dropper.png";
    
    public final static String[] DRAW_TOOLS = {
        "None", "Line", "Freehand", "Rectangle", "Round Rectangle", 
        "Square", "Ellipse", "Circle"};
    
    public CleanerCanvasTools(){
        super();
        Random rnd = new Random();
        setFill = new CheckBox("");
        widthBox = new ComboBox<>();
        lineColorPicker = new ColorPicker();
        fillColorPicker = new ColorPicker();
        randButton = new Button("Random Color");
        otherTools = new ToggleGroup();
        textTool = new ToggleButton("Add Text");
        colorDropper = new ToggleButton("Color Dropper");
        drawToolBox = new ChoiceBox(FXCollections.observableArrayList(DRAW_TOOLS));
        
        lineColorPicker.setValue(Color.BLACK);  //sets default color to black
        
        drawToolBox.setValue("None");           //sets default value to "None" tool
        
        widthBox.setEditable(true);     //edit ComboBox for custom line widths!!
        widthBox.setMaxWidth(80);       //sets the max width of the box to 80 pixels
        widthBox.setValue(1);           //sets the default value to 1
        for(int i : LINE_WIDTH_VALS)
            widthBox.getItems().add(i); //adds all of the 'default' line widths
        
        getItems().addAll(new Label(" Drawing Tool: "), drawToolBox, textTool, colorDropper, new Separator(),
                new Label(" Line Color: "), lineColorPicker, new Label(" Fill Color: "),
                fillColorPicker, randButton, new Label(" Line Width: "), widthBox, 
                new Label(" Fill "), setFill);    
                //layout things; making the toolbar look pretty
        
        drawToolBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue ov, Number value, Number new_value){
                usingTool = new_value.intValue();
            }   //changes the index of the tool being used to whatever was selected
        });
        widthBox.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if(!isNowFocused){
                widthBox.setValue(Integer.parseInt(widthBox.getEditor().getText()));
            }   //listens to the ComboBox TextInput; if it changes it sets the value to whatever was input
        });
        //buttons and action events
        widthBox.setOnAction((ActionEvent e) -> {   //changes the value of usingWidth when the ComboBox is used/value changes
            usingWidth = widthBox.getValue();});    
        randButton.setOnAction((ActionEvent e) -> { //changes the colors in the fill and line color pickers to the same rand color
            double[] rgb = {rnd.nextDouble(),rnd.nextDouble(),rnd.nextDouble()};    //gets rand vals for RGB from 0 to 1
            Color randCol = Color.color(rgb[0],rgb[1],rgb[2]);
            lineColorPicker.setValue(randCol);
            fillColorPicker.setValue(randCol);
        });
    }
    /**
     * Sets the line color given a Color object
     * @param color The color to set the line to
     */
    public static void setLineColor(Color color){lineColorPicker.setValue(color);}
    /**
     * Sets the fill color given a Color object
     * @param color The color to set the fill to
     */
    public static void setFillColor(Color color){fillColorPicker.setValue(color);}
    /**
     * Gets the color that the user chose in the line color picker
     * @return the Color object chosen in the color picker
     */
    public static Color getLineColor(){return lineColorPicker.getValue();}
    /**
     * Gets the color that the user chose in the color picker
     * @return the Color object chosen from the color picker
     */
    public static Color getFillColor(){return fillColorPicker.getValue();}
    /**
     * Gets the status of the setFill check box
     * @return Whether or not the 'Fill' check box is checked 
     */
    public static boolean getFillStatus(){return setFill.isSelected();}
    /**
     * Gets the width that 
     * @return integer that represent the pt width
     */
    public static int getLineWidth(){return usingWidth;}
    /**
     * Gets the tool that has been selected
     * @return String that represent the current tool being used
     */
    public static String getCurrentTool(){
        if(textTool.isSelected())
            return "Text";
        else if(colorDropper.isSelected())
            return "Dropper";
        return DRAW_TOOLS[usingTool];
    }
}
