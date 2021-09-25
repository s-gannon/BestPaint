/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package betterpaint;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class CleanerMenuBar extends MenuBar{    //it can't be better without some theft
    public CleanerMenuBar(){
        super();
        Random rand = new Random();
        FileChooser chooseFile = new FileChooser();
        DirectoryChooser chooseDir = new DirectoryChooser();
        
        chooseFile.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG (stupid lossy)", "*.jpg"),
                new FileChooser.ExtensionFilter("Bitmap", "*.bmp"),
                new FileChooser.ExtensionFilter("Graphics Interchange Format", "*.gif"),
                new FileChooser.ExtensionFilter("Raw Image", "*.raw")); //all of the extensions I "support"
        
        Menu fileOption = new Menu("_File");
        MenuItem[] fileSubOptions = {new MenuItem("Open file..."), new MenuItem("I'm feeling lucky"),
                                     new MenuItem("Save file"), new MenuItem("Save file as..."),
                                     new MenuItem("_Exit")}; //All of my file suboptions
        
        Menu optionsOption = new Menu("_Options");
        MenuItem[] optionsSubOptions = {new MenuItem("Zoom In"), new MenuItem("Zoom Out")};            
        //setAccelerator does the keybinds
        Menu helpOption = new Menu("_Help");
        MenuItem[] helpSubOptions = {new MenuItem("_About"), new MenuItem("Hel_p"), 
                                     new MenuItem("_Release Notes")};
        
        //add all main options to the menu bar (the ones you see on the bar)
        getMenus().add(fileOption);
        getMenus().add(optionsOption);
        getMenus().add(helpOption);
        
        for(MenuItem i : fileSubOptions)
            fileOption.getItems().add(i);   //adds all of the sub-options to the File option using the best for loop
        for(MenuItem i : optionsSubOptions)
            optionsOption.getItems().add(i);//adds all of the sub-options to the Options option using the best for loop
        for(MenuItem i : helpSubOptions)
            helpOption.getItems().add(i);   //adds all of the sub-options to the Help option using the best for loop
        
        fileSubOptions[0].setOnAction((ActionEvent e) ->        //"Open file"
            openFile(chooseFile.showOpenDialog(BetterPaint.mainStage)));
        fileSubOptions[1].setOnAction((ActionEvent e) -> {      //"I'm feeling lucky"
            File dir = chooseDir.showDialog(BetterPaint.mainStage);
            FileFilter imFilter = new FileFilter(){
                String[] extensions = {".jpg", ".png", ".raw", ".gif", ".bmp"}; //I've heard this is bad, but oh well
                @Override
                public boolean accept(File file){
                    for(String ext : extensions)
                        if(file.getName().endsWith(ext))
                            return true;
                    return false;
                }
            };  //filters out images from non-images (should probably use filter up top)
            File[] subFiles = dir.listFiles(imFilter);             //list of files in the folder
            try {   
                openFile(subFiles[rand.nextInt(subFiles.length)]);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });
        fileSubOptions[2].setOnAction((ActionEvent e) -> 
                BetterPaint.getCurrentTab().saveImage());    //saves image to the current path
        fileSubOptions[3].setOnAction((ActionEvent e) -> 
                BetterPaint.getCurrentTab().saveImageAs(chooseFile.showSaveDialog(BetterPaint.mainStage)));  //saves image to new path
        fileSubOptions[4].setOnAction((ActionEvent e) -> {
                BetterPopups.createExitAlert(BetterPaint.mainStage);   //god I love alerts
        });   //good/safe exit
        
        helpSubOptions[0].setOnAction((ActionEvent e) -> {  //about window
            try {
                BetterPopups.createTextWindow(new File(BetterPaint.ABOUT_PATH), "About", 635, 250);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BetterPaint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        helpSubOptions[1].setOnAction((ActionEvent e) -> {  //help window
            try {
                BetterPopups.createTextWindow(new File(BetterPaint.HELP_PATH), "Help", 635, 350);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BetterPaint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        helpSubOptions[2].setOnAction((ActionEvent e) -> {  //release notes
            try {
                BetterPopups.createTextWindow(new File(BetterPaint.REL_NOTES_PATH), "Release Notes", 635, 500);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BetterPaint.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        optionsSubOptions[0].setOnAction((ActionEvent e) -> {   //zoom in
            BetterPaint.getCurrentTab().getCanvas().zoomIn();
        });
        optionsSubOptions[1].setOnAction((ActionEvent e) -> {   //zoom out
            BetterPaint.getCurrentTab().getCanvas().zoomOut();
        });
        
        //lol keybinds down below
        fileSubOptions[0].setAccelerator(KeyCombination.keyCombination("Ctrl+O"));      //sets open option to Ctrl+O
        fileSubOptions[2].setAccelerator(KeyCombination.keyCombination("Ctrl+S"));      //sets save option to Ctrl+S
        optionsSubOptions[0].setAccelerator(KeyCombination.keyCombination("Ctrl+]"));   //sets zoom in to Ctrl+]
        optionsSubOptions[1].setAccelerator(KeyCombination.keyCombination("Ctrl+["));   //sets zoom out to Ctrl+[
        helpSubOptions[2].setAccelerator(KeyCombination.keyCombination("Ctrl+R"));      //sets release notes to Ctrl+R
    }
    /**
     * Opens the image at the specified file path
     * @param path The path of the file to open
     */
    private void openFile(File path){
        BetterTab temp = new BetterTab(path);   //creates temp object
        temp.getCanvas().drawImage(path);       //draws image from path to canvas
        temp.setTitle(temp.getPath().getName());//sets the tab title
        BetterPaint.tabPane.getTabs().add(temp);//and add it to the list of tabs
    }
}
