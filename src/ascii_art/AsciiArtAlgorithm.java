package ascii_art;

import image.ModifiedImage;
import image_char_matching.SubImgCharMatcher;

import java.util.Iterator;
import java.util.LinkedList;

public class AsciiArtAlgorithm {

    private final ModifiedImage image;
    private final int width;
    private final SubImgCharMatcher subImgCharMatcher;
    private char[][] asciiArt = null;

    /** Create a new AsciiArtAlgorithm with the given image, width, and character matcher.
     * @param image The image to create the ASCII art from.
     * @param width The width of the ASCII art.
     * @param subImgCharMatcher The character matcher to use.
     */
    public AsciiArtAlgorithm(ModifiedImage image, int width, SubImgCharMatcher subImgCharMatcher) {
        this.image = image;
        this.width = width;
        this.subImgCharMatcher = subImgCharMatcher;
    }

    public char[][] run(){
        if (asciiArt != null) {
            return asciiArt;
        }
        image.addPadding();
        int height = image.getHeight() * width / image.getWidth();
        char[][] asciiArt = new char[height][width];
        LinkedList<ModifiedImage> subImages = image.getSubImages(image.getWidth() / width);
        Iterator<ModifiedImage> subImagesIterator = subImages.iterator();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ModifiedImage subImage = subImagesIterator.next();
                double brightness = subImage.getBrightness();
                char asciiChar = subImgCharMatcher.getCharByImageBrightness(brightness);
                asciiArt[i][j] = asciiChar;
            }
        }
        this.asciiArt = asciiArt;
        return asciiArt;
    }
}
