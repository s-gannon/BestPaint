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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 *
 * @author spencer
 */
public class CleanerMenuBar extends MenuBar{
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
                new FileChooser.ExtensionFilter("Raw Image", "*.raw")
        );      //all the filters that myself and Image support
        
        Menu fileOption = new Menu("File");
        MenuItem[] fileSubOptions = {new MenuItem("Open..."), new MenuItem("I'm feeling lucky"),
                                     new MenuItem("Save file"), new MenuItem("Save file as..."),
                                     new MenuItem("Exit")};
        
        Menu optionsOption = new Menu("Options");
        MenuItem[] optionsSubOptions = {};
        
        Menu helpOption = new Menu("Help");
        MenuItem[] helpSubOptions = {new MenuItem("About"), new MenuItem("Help"), 
                                     new MenuItem("Release Notes")};
        
        //add all main options to the menu bar (the ones you see on the bar)
        getMenus().add(fileOption);
        getMenus().add(optionsOption);
        getMenus().add(helpOption);
        
        for(MenuItem i : fileSubOptions)
            fileOption.getItems().add(i);   //adds all of the sub-options to the File option using the best for loop
        for(MenuItem i : optionsSubOptions)
            optionsOption.getItems().add(i);
        for(MenuItem i : helpSubOptions)
            helpOption.getItems().add(i);
        
        fileSubOptions[0].setOnAction((ActionEvent e) -> {    //open file
            BetterPaint.canvas.setPath(chooseFile.showOpenDialog(BetterPaint.mainStage));
            BetterPaint.canvas.drawImage();
        });
        fileSubOptions[1].setOnAction((ActionEvent e) -> {    //random file / I'm feeling lucky
            int attempts = 0;   //failsafe; after a number of attempts we will end the random
            File dir = chooseDir.showDialog(BetterPaint.mainStage);
            FileFilter imFilter = new FileFilter(){
                String[] extensions = {".jpg", ".png", ".raw", ".gif", ".bmp"}; //I've heard this is bad, but oh well
                public boolean accept(File file){
                    for(String ext : extensions)
                        if(file.getName().endsWith(ext))
                            return true;
                    return false;
                }
            };  //filters out images from non-images
            File[] subFiles = dir.listFiles(imFilter);
            try {
                BetterPaint.canvas.setPath(subFiles[rand.nextInt(subFiles.length)]);
                BetterPaint.canvas.drawImage();
            } catch (Exception f) {
                //I'll figure it out later; probably put a whoopsie printout here
            }
        });
        
        fileSubOptions[2].setOnAction((ActionEvent e) -> 
                BetterPaint.canvas.saveImage());
        fileSubOptions[3].setOnAction((ActionEvent e) -> 
                BetterPaint.canvas.saveImageAs(chooseFile.showSaveDialog(BetterPaint.mainStage)));
        fileSubOptions[fileSubOptions.length - 1].setOnAction((ActionEvent e) -> 
                BetterPopups.createExitWindow()); //exit
        
        helpSubOptions[0].setOnAction((ActionEvent e) -> {  //about window
            try {
                BetterPopups.createTextWindow(new File(BetterPaint.ABOUT_PATH), "About");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BetterPaint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        helpSubOptions[1].setOnAction((ActionEvent e) -> {  //help window
            try {
                BetterPopups.createTextWindow(new File(BetterPaint.HELP_PATH), "Help");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BetterPaint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        helpSubOptions[2].setOnAction((ActionEvent e) -> {  //release notes
            try {
                BetterPopups.createTextWindow(new File(BetterPaint.REL_NOTES_PATH), "Release Notes");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BetterPaint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
    }
}
