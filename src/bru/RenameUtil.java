/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bru;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Andromeda
 */
public class RenameUtil {

    private final List<String> paths;

    public RenameUtil(List<String> paths) {
        this.paths = paths;
    }

    public void IndexedRenaming(int starts_from, String name, String location, String separator, int numDigits) {

        Iterator iter = paths.iterator();
        String newName = "";

        int i_file = starts_from;

        while (iter.hasNext()) {

            String path = iter.next().toString();
            System.out.println("Selected Path for indexed renaming: " + path);

            File dir = new File(path);

            String parent = dir.getParent();

            if (dir.exists()) {

                if (dir.isFile()) {
                    String oldName = dir.getName();
                    System.out.println("Old Name: " + oldName);
                    String extension = getExtension(oldName);
                    if (location.equals("Before Index")) {
                        newName = name + separator + formatNum(i_file, numDigits) + extension;
                    } else if (location.equals("After Index")) {
                        newName = formatNum(i_file, numDigits) + separator + name + extension;
                    }
                    String newPath = parent + "\\" + newName;
                    dir.renameTo(new File(newPath));
                    System.out.println(oldName + " changed to " + newName);
                    i_file++;
                } else if (dir.isDirectory()) {
                    File[] files = dir.listFiles();

                    int i = starts_from;
                    for (File file : files) {
                        String oldName = file.getName();
                        System.out.println("Old Name: " + oldName);
                        String extension = getExtension(oldName);
                        if (location.equals("Before Index")) {
                            newName = name + separator + formatNum(i, numDigits) + extension;
                        } else if (location.equals("After Index")) {
                            newName = formatNum(i, numDigits) + separator + name + extension;
                        }
                        String newPath = path + "\\" + newName;
                        file.renameTo(new File(newPath));
                        System.out.println(oldName + " changed to " + newName);
                        i++;
                    }
                }

                System.out.println("task finished for " + dir.getName() + " !");

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

    private int formatNum(int num, int digits) {

        String format = String.format("%%0%dd", digits);
        String result = String.format(format, num);
        return Integer.valueOf(result);
    }
}
