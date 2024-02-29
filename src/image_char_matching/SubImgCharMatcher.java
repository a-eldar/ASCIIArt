package image_char_matching;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * This class is used to match a character from a charset to a given brightness.
 * @author Eldar Amar & Ofek Kelly
 */
public class SubImgCharMatcher implements Iterable<SimpleEntry<Character, Double>>{
    private final BrightnessMap brightnessMap = new BrightnessMap();
    private static final HashMap<Character, Double> CHAR_BRIGHTNESS = new HashMap<>();
    double maxBrightness;
    double minBrightness;

    /**
     * Create a new SubImgCharMatcher with the given charset.
     * @param charset The charset to use
     * @throws IllegalArgumentException If the charset is empty
     */
    public SubImgCharMatcher(Collection<Character> charset) throws IllegalArgumentException {
        if (charset.isEmpty()) {
            throw new IllegalArgumentException("Charset must contain at least one character");
        }

        maxBrightness = Double.MIN_VALUE;
        minBrightness = Double.MAX_VALUE;
        for (char c : charset) {
            double brightness = getBrightness(c);
            updateMaxMinBrightness(brightness);
            brightnessMap.add(c, brightness);
        }
        stretchCharBrightnessLinearly();
    }

    /**
     * Get the character from the charset that best matches the given brightness.
     * If there are multiple characters with the same brightness difference, return the one with the
     * smallest ASCII value.
     * @param brightness The brightness to match
     * @throws IllegalArgumentException If the map is empty
     * @return The character from the charset that best matches the given brightness
     */
    public char getCharByImageBrightness(double brightness) throws IllegalArgumentException {
        return brightnessMap.getCharByBrightness(brightness);
    }

    /**
     * Add a character to the charset.
     * @param c The character to add
     */
    public void addChar(char c) {
        brightnessMap.add(c, getBrightness(c));
        if (updateMaxMinBrightness(getBrightness(c))) {
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
        brightnessMap.remove(c);
        if (getBrightness(c) != maxBrightness && getBrightness(c) != minBrightness) {
            return;
        }
        maxBrightness = Double.MIN_VALUE;
        minBrightness = Double.MAX_VALUE;
        for (SimpleEntry<Character, Double> entry : brightnessMap) {
            updateMaxMinBrightness(getBrightness(entry.getKey()));
        }
        stretchCharBrightnessLinearly();
    }

    public void clearChars() {
        brightnessMap.clear();
        maxBrightness = Double.MIN_VALUE;
        minBrightness = Double.MAX_VALUE;
    }

    private boolean updateMaxMinBrightness(double brightness) {
        boolean changed = false;
        if (brightness > maxBrightness) {
            maxBrightness = brightness;
            changed = true;
        }
        if (brightness < minBrightness) {
            minBrightness = brightness;
            changed = true;
        }
        return changed;
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
        BrightnessMap newMap = new BrightnessMap();
        for (SimpleEntry<Character, Double> entry : brightnessMap) {
            stretchCharBrightnessLinearly(newMap, entry.getKey());
        }
        brightnessMap.copy(newMap);
    }

    private void stretchCharBrightnessLinearly(char c) {
        double normalizedBrightness = (getBrightness(c) - minBrightness) / (maxBrightness - minBrightness);
        brightnessMap.add(c, normalizedBrightness);
    }

    private void stretchCharBrightnessLinearly(BrightnessMap map, char c) {
        double normalizedBrightness = (getBrightness(c) - minBrightness) / (maxBrightness - minBrightness);
        map.add(c, normalizedBrightness);
    }

    @Override
    public Iterator<SimpleEntry<Character, Double>> iterator() {
        return brightnessMap.iterator();
    }
}