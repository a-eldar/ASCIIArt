package main;

import ascii_art.AsciiArtAlgorithm;
import ascii_output.HtmlAsciiOutput;
import image.ModifiedImage;
import image_char_matching.SubImgCharMatcher;
import ascii_art.Shell;

public class AsciiArt {
    public static void main(String[] args) {
//        ModifiedImage image;
//        try {
//            image = new ModifiedImage("cat.jpeg");
//        }
//        catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//            return;
//        }
//        int resolution = 128;
//        char[] asciiChars = new char[95];
//        for (int i = 0; i < 95; i++) {
//            asciiChars[i] = (char) (i + 32);
//        }
//
//
//        AsciiArtAlgorithm algorithm = new AsciiArtAlgorithm(image, resolution, new SubImgCharMatcher(asciiChars));
//        char[][] asciiArt = algorithm.run();
//        HtmlAsciiOutput htmlAsciiOutput = new HtmlAsciiOutput("cat.html", "Courier New");
//        htmlAsciiOutput.out(asciiArt);
        try {
            Shell shell = new Shell();
            shell.run();
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
