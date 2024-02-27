package image_char_matching;

import java.util.*;

/**
 * This class is used to match a character from a charset to a given brightness.
 */
public class SubImgCharMatcher {
    private final HashMap<Character, Double> brightnessMap;
    private static final HashMap<Character, Double> CHAR_BRIGHTNESS = new HashMap<>();
    double maxBrightness;
    double minBrightness;

    /**
     * Create a new SubImgCharMatcher with the given charset.
     * @param charset The charset to use
     * @throws IllegalArgumentException If the charset is empty
     */
    public SubImgCharMatcher(char[] charset) throws IllegalArgumentException {
        if (charset.length == 0) {
            throw new IllegalArgumentException("Charset must contain at least one character");
        }
        this.brightnessMap = new HashMap<>();
        maxBrightness = Double.MIN_VALUE;
        minBrightness = Double.MAX_VALUE;
        for (char c : charset) {
            double brightness = getBrightness(c);
            if (brightness > maxBrightness) {
                maxBrightness = brightness;
            }
            if (brightness < minBrightness) {
                minBrightness = brightness;
            }
            brightnessMap.put(c, brightness);
        }
        stretchCharBrightnessLinearly();
    }

    /**
     * Get the character from the charset that best matches the given brightness.
     * If there are multiple characters with the same brightness difference, return the one with the
     * smallest ASCII value.
     * @param brightness The brightness to match
     * @return The character from the charset that best matches the given brightness
     */
    public char getCharByImageBrightness(double brightness) {
        char bestPossibleChar = brightnessMap.keySet().iterator().next();
        double minBrightnessDifference = Double.MAX_VALUE; // Initialize to maximum value

        for (Map.Entry<Character, Double> entry : brightnessMap.entrySet()) {
            char c = entry.getKey();
            double currentBrightness = entry.getValue();
            double brightnessDifference = Math.abs(currentBrightness - brightness);

            if (brightnessDifference < minBrightnessDifference ||
                    (brightnessDifference == minBrightnessDifference && c < bestPossibleChar)) {
                bestPossibleChar = c;
                minBrightnessDifference = brightnessDifference;
            }
        }
        return bestPossibleChar;
    }

    /**
     * Add a character to the charset.
     * @param c The character to add
     */
    public void addChar(char c) {
        brightnessMap.put(c, getBrightness(c));
        boolean changed = false;
        if (brightnessMap.get(c) > maxBrightness) {
            maxBrightness = brightnessMap.get(c);
            changed = true;
        }
        if (brightnessMap.get(c) < minBrightness) {
            minBrightness = brightnessMap.get(c);
            changed = true;
        }
        if (changed) {
            stretchCharBrightnessLinearly();
        }
        else {
            stretchCharBrightnessLinearly(c);
        }
    }

    /**
     * Remove a character from the charset.
     * @param c The character to remove
     */
    public void removeChar(char c) {
        if (brightnessMap.get(c) != maxBrightness && brightnessMap.get(c) != minBrightness) {
            brightnessMap.remove(c);
            return;
        }
        brightnessMap.remove(c);
        maxBrightness = Double.MIN_VALUE;
        minBrightness = Double.MAX_VALUE;
        for (double brightness : brightnessMap.values()) {
            if (brightness > maxBrightness) {
                maxBrightness = brightness;
            }
            if (brightness < minBrightness) {
                minBrightness = brightness;
            }
        }
        stretchCharBrightnessLinearly();
    }

    /* Returns brightness between 0 and 1 */
    private double getBrightness(char c) {
        if (CHAR_BRIGHTNESS.containsKey(c)) {
            return CHAR_BRIGHTNESS.get(c);
        }

        boolean[][] charArray = CharConverter.convertToBoolArray(c);

        int whitePixelCount = 0;
        for (boolean[] booleans : charArray) {
            for (int j = 0; j < charArray[0].length; j++) {
                whitePixelCount += booleans[j] ? 1 : 0;
            }
        }

        double result = (double) whitePixelCount / (charArray.length * charArray[0].length);
        CHAR_BRIGHTNESS.put(c, result);
        return result;
    }

    private void stretchCharBrightnessLinearly() {
        for (char c : brightnessMap.keySet()) {
            stretchCharBrightnessLinearly(c);
        }
    }

    private void stretchCharBrightnessLinearly(char c) {
        double normalizedBrightness = (brightnessMap.get(c) - minBrightness) / (maxBrightness - minBrightness);
        brightnessMap.put(c, normalizedBrightness);
    }
}
