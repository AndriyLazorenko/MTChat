package Lazorenko.Client.Test;

import java.io.File;

/**
 * Created by andriylazorenko on 12.07.15.
 */
public class TestPath {
    public static void main(String[] args) {
        String path = "/home/andriylazorenko";
        if (new File (path).exists()){
            System.out.println("location exists");
        }
        else {
            System.err.println("Motherfucker!!!");
        }
    }
}
