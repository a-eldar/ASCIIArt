package main;

import ascii_art.AsciiArtAlgorithm;
import ascii_output.HtmlAsciiOutput;
import image.ModifiedImage;
import image_char_matching.SubImgCharMatcher;

public class AsciiArt {
    public static void main(String[] args) {
        ModifiedImage image;
        try {
            image = new ModifiedImage("examples/asaf.jpeg");
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        int resolution = 512;
        char[] asciiChars = new char[95];
        for (int i = 0; i < 95; i++) {
            asciiChars[i] = (char) (i + 32);
        }


        AsciiArtAlgorithm algorithm = new AsciiArtAlgorithm(image, resolution, new SubImgCharMatcher(asciiChars));
        char[][] asciiArt = algorithm.run();
        HtmlAsciiOutput htmlAsciiOutput = new HtmlAsciiOutput("examples/asaf.html", "Courier New");
        htmlAsciiOutput.out(asciiArt);
    }
}
