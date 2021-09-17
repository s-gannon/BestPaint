package betterpaint;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;

public class CleanerCanvasTools extends ToolBar{
    private static ColorPicker colorPicker;
    private static ChoiceBox drawToolBox;
    private static ComboBox<Integer> lineWidthBox;
    private static int usingTool;
    private static int usingWidth;
    private int[] lineWidthValues = {1,2,5,10,15,20,25,50}; //some standard line width values
    
    public final static String[] DRAW_TOOLS = {
        "None", "Line", "Freehand"};
    
    public CleanerCanvasTools(){
        super();
        colorPicker = new ColorPicker();
        lineWidthBox = new ComboBox<Integer>();
        drawToolBox = new ChoiceBox(FXCollections.observableArrayList(DRAW_TOOLS));
        
        colorPicker.setValue(Color.BLACK);  //sets default color to black
        lineWidthBox.setEditable(true);
        for(int i : lineWidthValues)
            lineWidthBox.getItems().add(i);    //adds all of the default line widths
        drawToolBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue ov, Number value, Number new_value){
                usingTool = new_value.intValue();
            }   //changes the index of the tool being used to whatever was selected
        });
        lineWidthBox.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if(! isNowFocused) {
                lineWidthBox.setValue(Integer.parseInt(lineWidthBox.getEditor().getText()));
            }   //listens to the ComboBox TextInput; if it changes it sets the value to whatever was input
        });
        lineWidthBox.setOnAction((ActionEvent e) -> {
            usingWidth = lineWidthBox.getValue();
        }); //changes the value of usingWidth when the ComboBox is used/value changes
        
        getItems().addAll(drawToolBox, colorPicker, lineWidthBox);
    }
    public static Color getColor(){
        return colorPicker.getValue();
    }
    public static String getCurrentTool(){
        return DRAW_TOOLS[usingTool];
    }
    public static int getLineWidth(){
        return usingWidth;
    }
}
