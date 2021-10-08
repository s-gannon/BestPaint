package bestpaint;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;

public class CMenuBar extends MenuBar{
    //made public in case they need to be accessed outside of the menu bar class
    public final static String REL_NOTES_PATH = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BestPaint\\release-notes.txt";
    public final static String ABOUT_PATH = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BestPaint\\about.txt";
    public final static String HELP_PATH = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BestPaint\\help.txt";
    public final static String CONFIG_PATH = "C:\\Users\\spencer\\Documents\\College Class Files\\CS-250\\BestPaint\\config.txt";
    
    public CMenuBar(){
        super();
        Random rand = new Random();
        
        Menu file = new Menu("_File");
        MenuItem[] fileOptions = {  new MenuItem("New file"), new MenuItem("Open file..."), 
                                    new MenuItem("I'm feeling lucky"), new MenuItem("Save file"),
                                    new MenuItem("Save file as..."), new MenuItem("Undo"),
                                    new MenuItem("Redo"), new MenuItem("_Exit")}; //All of my file suboptions
        
        Menu options = new Menu("_Options");
        MenuItem[] optionsOptions = {new MenuItem("Zoom In"), new MenuItem("Zoom Out")};
        
        Menu help = new Menu("_Help");
        MenuItem[] helpOptions = {new MenuItem("_About"), new MenuItem("Hel_p"), 
                                     new MenuItem("_Release Notes")};
        
        //add all main options to the menu bar (the ones you see on the bar)
        getMenus().add(file);
        getMenus().add(options);
        getMenus().add(help);
        
        for(MenuItem i : fileOptions)
            file.getItems().add(i);   //adds all of the sub-options to the File option using the best for loop
        for(MenuItem i : optionsOptions)
            options.getItems().add(i);//adds all of the sub-options to the Options option using the best for loop
        for(MenuItem i : helpOptions)
            help.getItems().add(i);   //adds all of the sub-options to the Help option using the best for loop
        
        fileOptions[0].setOnAction((ActionEvent e) -> { //open new/blank image
            BTab.openBlankImage();
        });
        fileOptions[1].setOnAction((ActionEvent e) -> { //open existing image
            BTab.openImage();
        });
        fileOptions[2].setOnAction((ActionEvent e) -> { //I'm feeling lucky/russian roulette mode/open random file
            DirectoryChooser chooseDir = new DirectoryChooser();
            File dir = chooseDir.showDialog(BestPaint.mainStage);
            FileFilter imFilter = new FileFilter(){
                String[] extensions = {".jpg", ".png", ".gif", ".bmp"}; //I've heard this is bad, but oh well
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
                BTab.openImage(subFiles[rand.nextInt(subFiles.length)]);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });
        fileOptions[3].setOnAction((ActionEvent e) -> { //save image
            if(BestPaint.getCurrentTab().getFilePath() == null)   //if there is no filepath, save as, otherwise save
                BestPaint.getCurrentTab().saveImageAs();
            else
                BestPaint.getCurrentTab().saveImage();
        });
        fileOptions[4].setOnAction((ActionEvent e) -> { //saves image as
            try{
                BestPaint.getCurrentTab().saveImageAs();
            }catch(Exception ex){
                System.out.println(ex);
            }
        });
        fileOptions[5].setOnAction((ActionEvent e) -> { //undo
            BestPaint.getCurrentTab().undo();
        });
        fileOptions[6].setOnAction((ActionEvent e) -> { //redo
            BestPaint.getCurrentTab().redo();
        });
        fileOptions[fileOptions.length - 1].setOnAction((ActionEvent e) -> {
            CPopups.createExitAlert(BestPaint.mainStage);
        });
        
        helpOptions[0].setOnAction((ActionEvent e) -> { //about window
            try {
                CPopups.createTextWindow(new File(ABOUT_PATH), "About", 650, 350);
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
        }); 
        helpOptions[1].setOnAction((ActionEvent e) -> {  //help window
            try {
                CPopups.createTextWindow(new File(HELP_PATH), "Help", 650, 350);
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
        }); 
        helpOptions[2].setOnAction((ActionEvent e) -> {  //release notes
            try {
                CPopups.createTextWindow(new File(REL_NOTES_PATH), "Release Notes", 635, 500);
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
        });
        
        optionsOptions[0].setOnAction((ActionEvent e) -> {  //zoom in
            BestPaint.getCurrentTab().zoomIn();
        });
        optionsOptions[1].setOnAction((ActionEvent e) -> {  //zoom out
            BestPaint.getCurrentTab().zoomOut();
        });
        
        //lol keybinds down below
        fileOptions[1].setAccelerator(KeyCombination.keyCombination("Ctrl+O"));      //sets open option to Ctrl+O
        fileOptions[3].setAccelerator(KeyCombination.keyCombination("Ctrl+S"));      //sets save option to Ctrl+S
        fileOptions[5].setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));      //sets undo to Ctrl+Z
        fileOptions[6].setAccelerator(KeyCombination.keyCombination("Ctrl+A"));      //sets redo to Ctrl+A
        optionsOptions[0].setAccelerator(KeyCombination.keyCombination("Ctrl+]"));   //sets zoom in to Ctrl+]
        optionsOptions[1].setAccelerator(KeyCombination.keyCombination("Ctrl+["));   //sets zoom out to Ctrl+[
        helpOptions[2].setAccelerator(KeyCombination.keyCombination("Ctrl+R"));      //sets release notes to Ctrl+R
    }
}
