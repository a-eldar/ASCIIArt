package image;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

/**
 * A package-private class of the package image.
 * This class is used to represent an image and perform operations on it.
 */
public class ModifiedImage {
    // The maximum brightness value for a pixel.
    public static final int RGB_MAX_BRIGHTNESS = 255;
    private Image image;
    private Double brightness = null;

    /**
     * Create a new ModifiedImage from the given filename.
     * @param filename The filename of the image to create.
     * @throws IOException If the file does not exist or is not an image file.
     */
    public ModifiedImage(String filename) throws IOException {
        this.image = new Image(filename);
    }

    /**
     * Create a new ModifiedImage from the given Image.
     * @param image The Image to create the ModifiedImage from.
     */
    public ModifiedImage(Image image) {
        this.image = image;
    }

    /**
     * Create a new ModifiedImage from the given pixel array, width, and height.
     * @param pixelArray The pixel array of the image.
     * @param width The width of the image.
     * @param height The height of the image.
     */
    public ModifiedImage(Color[][] pixelArray, int width, int height) {
        this.image = new Image(pixelArray, width, height);
    }

    /**
     * Get the width of the image.
     * @return The width of the image.
     */
    public int getWidth() {
        return image.getWidth();
    }

    /**
     * Get the height of the image.
     * @return The height of the image.
     */
    public int getHeight() {
        return image.getHeight();
    }

    /**
     * Get a list of sub-images of the image, each of size subImageSize x subImageSize.
     * @param subImageSize The size of each sub-image.
     * @return A list of sub-images of the image.
     */
    public LinkedList<ModifiedImage> getSubImages(int subImageSize) {
        LinkedList<ModifiedImage> subImages = new LinkedList<>();
        int numSubImagesHorizontally = image.getWidth() / subImageSize;
        int numSubImagesVertically = image.getHeight() / subImageSize;

        for (int i = 0; i < numSubImagesVertically; i++) {
            for (int j = 0; j < numSubImagesHorizontally; j++) {
                addSubImage(subImageSize, i, j, subImages);
            }
        }
        return subImages;
    }

    private void addSubImage(int subImageSize, int i, int j, LinkedList<ModifiedImage> subImages) {
        Color[][] subImagePixelArray = new Color[subImageSize][subImageSize];
        for (int x = 0; x < subImageSize; x++) {
            for (int y = 0; y < subImageSize; y++) {
                subImagePixelArray[x][y] = image.getPixel(i * subImageSize + x, j * subImageSize + y);
            }
        }
        subImages.add(new ModifiedImage(subImagePixelArray, subImageSize, subImageSize));
    }

    /**
     * Add padding to the image to make its dimensions a power of 2.
     */
    public void addPadding() {
        int newHeight = nextPowerOfTwo(image.getHeight());
        int newWidth = nextPowerOfTwo(image.getWidth());
        int paddingHeight = newHeight - image.getHeight();
        int paddingWidth = newWidth - image.getWidth();
        if (paddingHeight == 0 && paddingWidth == 0) {
            return;
        }
        Color[][] newPixelArray = new Color[newHeight][newWidth];
        // Put white pixels on the new padding symmetrically around the image.
        for (int x = 0; x < newHeight; x++) {
            for (int y = 0; y < newWidth; y++) {
                if (inPadding(x, y, paddingHeight, paddingWidth)) {
                    newPixelArray[x][y] = Color.WHITE;
                } else {
                    newPixelArray[x][y] = image.getPixel(x - paddingHeight / 2, y - paddingWidth / 2);
                }
            }
        }
        image = new Image(newPixelArray, newWidth, newHeight);
    }

    private boolean inPadding(int x, int y, int paddingHeight, int paddingWidth) {
        return x < (paddingHeight / 2) || x >= (paddingHeight / 2 + image.getHeight()) ||
                y < (paddingWidth / 2) || y >= (paddingWidth / 2 + image.getWidth());
    }

    private int nextPowerOfTwo(int n) {
        int power = 1;
        while (power < n) {
            power *= 2;
        }
        return power;
    }

    /**
     * Get the brightness of the image.
     * @return The brightness of the image.
     */
    public double getBrightness(){
        if (brightness != null) {
            return brightness;
        }
        double totalBrightness = 0;
        for (int x = 0; x < image.getHeight(); x++) {
            for (int y = 0; y < image.getWidth(); y++) {
                totalBrightness += greyPixel(image.getPixel(x,y));
            }
        }
        totalBrightness /= image.getWidth() * image.getHeight() * RGB_MAX_BRIGHTNESS;
        brightness = totalBrightness;
        return totalBrightness;
    }

    private static double greyPixel(Color color) {
        return color.getRed() * 0.2126 + color.getGreen() * 0.7152 + color.getBlue() * 0.0722;
    }
}
