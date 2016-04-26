/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bru;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Andromeda
 */
public class RenameUtil {

    private List<String> paths;
    private final HashMap<String, String> map = new HashMap<>();

    public RenameUtil(List<String> paths) {
        this.paths = paths;
    }

    public void IndexedRenaming(int starts_from, String name, String location, String separator, int numDigits) {

        Iterator iter = paths.iterator();
        String newName = "";
        
        while (iter.hasNext()) {
            
            String path = iter.next().toString();
            System.out.println("Selected Path for indexed renaming: " + path);

            File dir = new File(path);

            if (dir.exists()) {

                if (dir.isDirectory()) {
                    File[] files = dir.listFiles();

                    int i = starts_from;
                    for (File file : files) {
                        String oldName = file.getName();
                        System.out.println("Old Name: " + oldName);
                        String extension = getExtension(oldName);
                        if (location.equals("Before Index")) {
                            newName = name + separator + formatNum(i, numDigits) + extension;
                        } else if(location.equals("After Index")){
                            newName = formatNum(i, numDigits) + separator + name + extension;
                        }
                        String newPath = path + "\\" + newName;
                        file.renameTo(new File(newPath));
                        System.out.println(oldName + " changed to " + newName);
                        i++;
                    }
                } else if (dir.isFile()) {
                    System.out.println("indexed renaming not supported for single file");
                }

                System.out.println("task finished !");

            }
        }
    }

    private static String getExtension(String fileName) {

        try {
            return "." + fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (Exception ex) {
            return "";
        }
    }
    
    private int formatNum(int num, int digits){
        
        String format = String.format("%%0%dd", digits);
        String result = String.format(format, num);
        return Integer.valueOf(result);
    }
}
