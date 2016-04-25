/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bru;

import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Andromeda
 */
public class RenameUtil {
    
    private String path = "";
    private final HashMap<String, String> map = new HashMap<>();
    
    public RenameUtil(String path){
        this.path = path;
    }
    
    public void IndexedRenaming(String name){
        System.out.println("Selected Path for indexed renaming: "+path);
        
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
                System.out.println("indexed renaming supported for single file");
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
}
