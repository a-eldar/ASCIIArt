package image_char_matching;

import java.util.HashMap;
import java.util.Map;

public class SubImgCharMatcher {
    private final char[] charset;
    private final Map<Character, Double> brightnessMap;

    public SubImgCharMatcher(char[] charset) {
        this.charset = charset;
        this.brightnessMap = new HashMap<>();
        // Precompute brightness values for each character in charset
        for (char c : charset) {
            brightnessMap.put(c, getBrightness(c));
        }
    }

    public char getCharByImageBrightness(double brightness) {
        char bestPossibleChar = charset[0];
        double minBrightnessDifference = Math.abs(brightnessMap.get(bestPossibleChar) - brightness);

        for (char c : charset) {
            double currentBrightness = brightnessMap.get(c);
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
        if (!brightnessMap.containsKey(c)) {
            charset[findEmptyIndex()] = c;
            brightnessMap.put(c, getBrightness(c));
        }
    }

    public void removeChar(char c) {
        if (brightnessMap.containsKey(c)) {
            for (int i = 0; i < charset.length; i++) {
                if (charset[i] == c) {
                    charset[i] = '\0';
                    break;
                }
            }
            brightnessMap.remove(c);
        }
    }

    // add methods if needed
    private double getBrightness(char c) {
        boolean[][] charArray = CharConverter.convertToBoolArray(c);

        int whitePixelCount = 0;
        for (boolean[] booleans : charArray) {
            for (int j = 0; j < charArray[0].length; j++) {
                if (booleans[j]) {
                    whitePixelCount++;
                }
            }
        }

        return (double) whitePixelCount / (charArray.length * charArray[0].length);
    }

}
