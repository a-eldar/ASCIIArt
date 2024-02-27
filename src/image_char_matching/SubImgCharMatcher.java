package image_char_matching;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * This class is used to match a character from a charset to a given brightness.
 */
public class SubImgCharMatcher {
    private final TreeSet<SimpleEntry<Character, Double>> brightnessMap;
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
        this.brightnessMap = new TreeSet<>(
                (o1, o2) -> {
                    if (o1.getValue().equals(o2.getValue())) {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                    return o1.getValue().compareTo(o2.getValue());
                }
        );
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
            brightnessMap.add(new SimpleEntry<>(c, brightness));
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
    public char getCharByImageBrightness(double brightness) throws IllegalArgumentException {
//        char bestPossibleChar = brightnessMap.keySet().iterator().next();
//        double minBrightnessDifference = Double.MAX_VALUE; // Initialize to maximum value
//
//        for (Map.Entry<Character, Double> entry : brightnessMap.entrySet()) {
//            char c = entry.getKey();
//            double currentBrightness = entry.getValue();
//            double brightnessDifference = Math.abs(currentBrightness - brightness);
//
//            if (brightnessDifference < minBrightnessDifference ||
//                    (brightnessDifference == minBrightnessDifference && c < bestPossibleChar)) {
//                bestPossibleChar = c;
//                minBrightnessDifference = brightnessDifference;
//            }
//        }
        SimpleEntry<Character, Double> ceilingCharEntry = brightnessMap.ceiling(new SimpleEntry<>(' ', brightness));
        SimpleEntry<Character, Double> floorCharEntry = brightnessMap.floor(new SimpleEntry<>(' ', brightness));
        // Return closest brightness, if there are multiple, return the one with the smallest ASCII value
        char bestPossibleChar;
        if (ceilingCharEntry == null && floorCharEntry == null) {
            throw new IllegalArgumentException("Charset must contain at least one character");
        }
        if (ceilingCharEntry == null) {
            bestPossibleChar = floorCharEntry.getKey();
        }
        else if (floorCharEntry == null) {
            bestPossibleChar = ceilingCharEntry.getKey();
        }
        else {
            double ceilingDiff = ceilingCharEntry.getValue() - brightness;
            double floorDiff = brightness - floorCharEntry.getValue();
            if (ceilingDiff < floorDiff) {
                bestPossibleChar = ceilingCharEntry.getKey();
            }
            else if (floorDiff < ceilingDiff) {
                bestPossibleChar = floorCharEntry.getKey();
            }
            else {
                bestPossibleChar = ceilingCharEntry.getKey() < floorCharEntry.getKey() ? ceilingCharEntry.getKey() : floorCharEntry.getKey();
            }
        }
        return bestPossibleChar;
    }

    /**
     * Add a character to the charset.
     * @param c The character to add
     */
    public void addChar(char c) {
        brightnessMap.add(new SimpleEntry<>(c, getBrightness(c));
        boolean changed = false;
        if (getBrightness(c) > maxBrightness) {
            maxBrightness = getBrightness(c);
            changed = true;
        }
        if (getBrightness(c) < minBrightness) {
            minBrightness = getBrightness(c);
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
        double brightness = brightnessMap.floor()
        if (brightness != maxBrightness && brightness != minBrightness) {
            brightnessMap.remove(new SimpleEntry<>(c, getBrightness(c)));
            return;
        }
        brightnessMap.remove(new SimpleEntry<>(c, getBrightness(c)));
        maxBrightness = Double.MIN_VALUE;
        minBrightness = Double.MAX_VALUE;
        for (SimpleEntry<Character, Double> entry : brightnessMap) {
            double brightness = entry.getValue();
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
        for (SimpleEntry<Character, Double> entry : brightnessMap) {
            stretchCharBrightnessLinearly(entry.getKey();
        }
    }

    private void stretchCharBrightnessLinearly(char c) {
        double normalizedBrightness = (getBrightness(c) - minBrightness) / (maxBrightness - minBrightness);
        brightnessMap.remove(new SimpleEntry<>(c, getBrightness(c)));
        brightnessMap.add(new SimpleEntry<>(c, normalizedBrightness));
    }
}