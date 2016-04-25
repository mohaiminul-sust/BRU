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
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
    private HashMap<String, String> filePaths = new HashMap<>();
    
    @FXML
    public Window stage;
    public ListView fileList;
    public Button removeButton;
    public Button removeAllButton;
    
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
        
        for (String listItem: list){
            if(filePaths.containsKey(listItem)){
                String mapItem = filePaths.get(listItem);
                String path = getPath(mapItem);
                System.out.println(path);
            }
        }
    }
    
    @FXML
    private void handleListMenuRemoveAction(ActionEvent event) {
        String fileName = fileList.getSelectionModel().getSelectedItem().toString();
        listItems.remove(fileName);
        filePaths.remove(fileName);
        fileList.setItems(listItems);
        printHashMap(); //debug
    }
    
    @FXML
    private void handleListMenuRemoveAllAction(ActionEvent event){
        initList();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    
    private static void BulkFileRenamer(String path, String name){
        File dir = new File(path);

        if (dir.exists()) {

            if (dir.isDirectory()) {
                File[] files = dir.listFiles();

                int i = 0;
                for (File file : files) {
                    String oldName = file.getName();
                    System.out.println("Old Name: " + oldName);
                    String extension = getExtension(oldName);
                    String newName = name + "_" + i + extension;
                    String newPath = path + "\\" + newName;
                    file.renameTo(new File(newPath));
                    System.out.println(oldName + " changed to " + newName);
                    i++;
                }
            } else if(dir.isFile()){
                String oldName = dir.getName();
                System.out.println("Old Name: " + oldName);
                String extension = getExtension(oldName);
                String newName = name + extension;
                String newPath = path + "\\" + newName;
                dir.renameTo(new File(newPath));
                System.out.println(oldName + " changed to " + newName);
            }
            
            System.out.println("task finished !");

        }

    }
    
    private static String getExtension(String fileName){
        
        try {
            return "."+fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (Exception ex) {
            return "";
        }
    }
    
    private static String getPath(String mapItem){
        try {
            return mapItem.substring(mapItem.lastIndexOf("=") + 1);
        } catch (Exception ex) {
            return "";
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
