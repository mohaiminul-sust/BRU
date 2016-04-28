/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bru;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author Andromeda
 */
public class FXMLDocumentController implements Initializable {
    
    private final Desktop desktop = Desktop.getDesktop();
    private ObservableList<String> listItems;
//    private ObservableList<String> BackuplistItems;
    private HashMap<String, String> filePaths = new HashMap<>();
    private RenameUtil BRU;
    
    @FXML
    public Window stage;
    public ListView fileList;
    public Button removeButton;
    public Button removeAllButton;
    public TextField patternHeader;
    public TextField startingAt;
    public TextField nameSeparator;
    public ComboBox patternLocation;
    public TextField numberOfDigits;
    public Accordion optionContainer;
    public TitledPane renameIndexedPane;
    public TitledPane renamePatternPane;    
    public TitledPane renameMetaPane;
    public TextField numChar;
    public ComboBox startLoc;
    public ComboBox patternMode;
    public TextField toAppend;
    
    
    @FXML
    private void handleExitFileMenuAction(ActionEvent event) {
        System.exit(0);
    }
        
    @FXML
    private void handleOpenFileMenuAction(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, "Open files for renaming");
        final List<File> files = fileChooser.showOpenMultipleDialog(stage);
        
        //clear and enable list view
        fileList.setItems(null);
        fileList.setDisable(false);
        listItems = FXCollections.observableArrayList();
        filePaths.clear();
        
        for (File file: files){
            String fileName = file.getName();
            String filePath = file.getPath();
            listItems.add(fileName);
            filePaths.put(fileName, filePath);
        }
        
        fileList.setItems(listItems);
        printHashMap(); //debug
    }
    
    @FXML
    private void handleOpenDirectoryFileMenuAction(ActionEvent event) {
        final DirectoryChooser dirChooser = new DirectoryChooser();
        final File selectedDir = dirChooser.showDialog(stage);
        
        if(selectedDir != null){
            String path = selectedDir.getAbsolutePath();
            File dir = new File(path);
            
            if(dir.isDirectory()){
                File[] files = dir.listFiles();
                
                //clear and enable list view
                fileList.setItems(null);
                fileList.setDisable(false);
                listItems = FXCollections.observableArrayList();
                filePaths.clear();
                
                //add file names to list view
                for(File file: files){
                    String fileName = file.getName();
                    String filePath = file.getPath();
                    listItems.add(fileName);
                    filePaths.put(fileName, filePath);
                }
                
                fileList.setItems(listItems);
                printHashMap(); //debug
            }
        }
    }
    
    @FXML
    private void handleRenameFileAction(ActionEvent event){
        ObservableList<String> list = fileList.getItems();
        List<String> paths = new ArrayList<String>();

        for (String listItem: list){
            if(filePaths.containsKey(listItem)){
                String mapItem = filePaths.get(listItem);
                String path = getPath(mapItem);
                paths.add(path);
            }else{
                System.err.println("Item not in hashmap !");
            }
        }
        
        String pattern = patternHeader.getText();
        String location = patternLocation.getValue().toString();
        String separator = nameSeparator.getText().toString();
        int startsAt = Integer.valueOf(startingAt.getText());
        int numDigits = Integer.valueOf(numberOfDigits.getText());
        
        System.out.println("Paths to rename: "+paths);
        
        //call renamer func from RenameUtil Class
        BRU = new RenameUtil(paths);
        BRU.IndexedRenaming(startsAt, pattern, location, separator, numDigits);
        
        //repopulate listView with changed files
        initList();
        
        
    }
    
    @FXML
    private void handleListMenuRemoveAction(ActionEvent event) {
        List<String> fileNames = new ArrayList<>();
        fileNames = fileList.getSelectionModel().getSelectedItems();
        
        Iterator listIter = fileNames.iterator();
        
        while(listIter.hasNext()){
            String fileName = listIter.next().toString();
            filePaths.remove(fileName);
        }
        
        listItems.clear();
        listItems.addAll(filePaths.keySet());
        fileList.setItems(listItems);
        
        //Empty List Handler
        if(listItems.isEmpty()){
            initList();
        }
        
        printHashMap(); //debug
    }
    
    @FXML
    private void handleListMenuRemoveAllAction(ActionEvent event){
        initList();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initAccordianIR();
        initAccordianPR();
        initAccordian();
        initList();
        fileList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }    
    
    //empty file list and listItems
    private void initList(){
        listItems = FXCollections.observableArrayList("No files or directory selected");
        filePaths.clear();
        fileList.setItems(listItems);
        fileList.setDisable(true);
        printHashMap(); //debug
    }
    
    private void initAccordian(){
        optionContainer.setExpandedPane(renameIndexedPane);
    }
    
    private void initAccordianIR(){
        //pattern header
        patternHeader.setText("dummy");
        
        //location
        ObservableList<String> patternLocationChoices = FXCollections.observableArrayList("Before Index", "After Index");
        patternLocation.setItems(patternLocationChoices);
        patternLocation.setValue(patternLocationChoices.get(0));
        
        //index starting at
        startingAt.setText("1");
        
        //# of digits
        numberOfDigits.setText("2");
        
        //set separator
        nameSeparator.setText("_");
    }
    
    private void initAccordianPR(){
        
        numChar.setText("");
        
        ObservableList<String> startLocs = FXCollections.observableArrayList("Start", "End");
        startLoc.setItems(startLocs);
        startLoc.setValue(startLocs.get(0));
    
        ObservableList<String> renameModes = FXCollections.observableArrayList("Remove", "Append");
        patternMode.setItems(renameModes);
        patternMode.setValue(renameModes.get(0));
        
        toAppend.setDisable(true);
        
    }
    
    @FXML
    private void handleModeComboAction(ActionEvent event){
        
        String mode = patternMode.getValue().toString();
        
        if(mode.equals("Remove")){
            toAppend.setDisable(true);
        }else if(mode.equals("Append")){
            toAppend.setDisable(false);
        }
    }
   
    private static void configureFileChooser(final FileChooser fileChooser, String title){
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        
    }
    
    private static String getPath(String mapItem){
        String path = "";

        try {
            path = mapItem.substring(mapItem.lastIndexOf("=") + 1);
            return path;
        } catch (Exception ex) {
            System.out.println("Error : "+ex);
            return path;
        }
    }
    
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    this.getClass().getName()).log(
                            Level.SEVERE, null, ex
                    );
        }
    }
    
    private void printHashMap(){
        System.out.println(filePaths);
    }
    
}
