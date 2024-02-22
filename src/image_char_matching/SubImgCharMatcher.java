package image_char_matching;

import java.util.*;

public class SubImgCharMatcher {
    private final HashMap<Character, Double> brightnessMap;

    public SubImgCharMatcher(char[] charset) {
        this.brightnessMap = new HashMap<>();
        for (char c : charset) {
            brightnessMap.put(c, getBrightness(c));
        }
        stretchCharBrightnessLinearly();
    }

    /**
     * Get the character from the charset that best matches the given brightness.
     * If there are multiple characters with the same brightness difference, return the one with the smallest ASCII value.
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

    public void addChar(char c) {
        brightnessMap.put(c, getBrightness(c));
        stretchCharBrightnessLinearly();
    }


    public void removeChar(char c) {
        brightnessMap.remove(c);
        stretchCharBrightnessLinearly();
    }

    /* Returns brightness between 0 and 1 */
    private double getBrightness(char c) {
        boolean[][] charArray = CharConverter.convertToBoolArray(c);

        int whitePixelCount = 0;
        for (boolean[] booleans : charArray) {
            for (int j = 0; j < charArray[0].length; j++) {
                whitePixelCount += booleans[j] ? 1 : 0;
            }
        }
        return (double) whitePixelCount / (charArray.length * charArray[0].length);
    }

    private void stretchCharBrightnessLinearly() {
        double minBrightness = brightnessMap.values().stream().min(Double::compareTo).orElse(0.0);
        double maxBrightness = brightnessMap.values().stream().max(Double::compareTo).orElse(1.0);

        for (Map.Entry<Character, Double> entry : brightnessMap.entrySet()) {
            double normalizedBrightness = (entry.getValue() - minBrightness) / (maxBrightness - minBrightness);
            brightnessMap.put(entry.getKey(), normalizedBrightness);
        }
    }
}
