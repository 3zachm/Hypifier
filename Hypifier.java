import java.io.IOException;
import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;

// Oh christ let's see if I can document my spaghetti code well

public class Hypifier {
    public static void main(String[] args) throws IOException {
        // Setup directories
        File dir = new File(".\\in");
        File out = new File(".\\out");

        // If directories don't exist, create them
        if (!dir.isDirectory()) {
            dir.mkdir();
        }
        if (!out.isDirectory()) {
            out.mkdir();
        }

        // Take all gif frames in "in" and put them in an array
        // This relies on being in ABC order, meaning that frames must be 01 and not 0 because yes
        String[] files = dirToArray(".\\in");
        String[] files2 = new String[files.length * 2]; // creates temporary array to store pattern
        
        int temp = 0; // "keep track of iterations but spaghetti version"
        int fcut = files.length / 2; // center of the frames
        for (int f = 0; f < files.length * 2; f = f + 2) {
            files2[f] = files[temp]; // 1st frame -> end, inserting every other index
            temp++; 
        }
        //
        for (int f = 1; f < files.length * 2; f = f + 2) {
            if (fcut >= files.length) { // starts at the center "fcut", so we need it to loop over
                fcut = 0;
            }
            files2[f] = files[fcut]; // same as last but center -> frame before center
            fcut++;
        }

        int temp2 = 1; // the return of spaghetti iteration tracker
        for (String string : files2) {
            File og = new File("in\\" + string); // initial file
            File nw = new File("out\\" + string); // copy position
            Files.copy(og.toPath(), nw.toPath(), StandardCopyOption.REPLACE_EXISTING);
            if (nw.renameTo(new File("out\\" + temp2 + ".png"))) { // rename copied file
                System.out.println(temp2 + "\\" + files2.length + ": \"" + string + "\" moved successfully");
            } else {
                System.out.println("Failed to rename file: " + "\"" + string + "\"");
            }
            temp2++;
        }
    }

    // yoinked from online because before I started I knew nothing about how files worked on java
    public static String[] dirToArray(String in) { 
        File dir = new File(in);
        ArrayList<String> list = new ArrayList<String>();
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File f : files) {
                if(f.isFile()) {
                    list.add(f.getName());
                }
            }
        }
        return list.toArray(new String[]{});
    }
}