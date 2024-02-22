package image;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class ModifiedImage {

    public static final int RGB_MAX_BRIGHTNESS = 255;
    private Image image;

    public ModifiedImage(String filename) throws IOException {
        this.image = new Image(filename);
    }

    public ModifiedImage(Image image) {
        this.image = image;
    }

    public ModifiedImage(Color[][] pixelArray, int width, int height) {
        this.image = new Image(pixelArray, width, height);
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public LinkedList<ModifiedImage> getSubImages(int resolution) {
        LinkedList<ModifiedImage> subImages = new LinkedList<>();
        int subImageWidth = image.getWidth() / resolution;
        int subImageHeight = image.getHeight() / resolution;

        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                addSubImage(subImageHeight, subImageWidth, i, j, subImages);
            }
        }
        return subImages;
    }

    private void addSubImage(int subImageHeight, int subImageWidth, int i, int j, LinkedList<ModifiedImage> subImages) {
        Color[][] subImagePixelArray = new Color[subImageHeight][subImageWidth];
        for (int x = 0; x < subImageHeight; x++) {
            for (int y = 0; y < subImageWidth; y++) {
                subImagePixelArray[x][y] = image.getPixel(i * subImageWidth + y, j * subImageHeight + x);
            }
        }
        subImages.add(new ModifiedImage(subImagePixelArray, subImageWidth, subImageHeight));
    }

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
                    newPixelArray[x][y] = image.getPixel(x + paddingHeight / 2, y + paddingWidth / 2);
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

    public double getBrightness(){
        double totalBrightness = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                totalBrightness += greyPixel(image.getPixel(x,y));
            }
        }
        totalBrightness /= image.getWidth() * image.getHeight() * RGB_MAX_BRIGHTNESS;
        return totalBrightness;
    }

    private static double greyPixel(Color color) {
        return color.getRed() * 0.2126 + color.getGreen() * 0.7152 + color.getBlue() * 0.0722;
    }
}
