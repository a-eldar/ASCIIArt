package main;

import image_char_matching.BrightnessMap;
import image_char_matching.SubImgCharMatcher;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class BrightnessTests {

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String NUMBERS = "0123456789";
    public static final String SPECIAL_CHARS = "~ !@#$%^&*()_+-=[]{};':,.<>?/|\\\"";
    public static final char MIN_ASCII = '0';
    public static final char MAX_ASCII = '9';

    private static void testBrightnessMapGetCharByBrightness() {
        SubImgCharMatcher charMatcher = getMatcher();
        for (char c = MIN_ASCII; c <= MAX_ASCII; c++)
            System.out.println(c + " " + charMatcher.getCharBrightness(c));
        // Test cases
        for (double i = 0; i <= 1; i += 0.0001)
            testSingleBrightness(charMatcher, i);

    }

    private static void testSingleBrightness(SubImgCharMatcher charMatcher, double brightness) {
        char c = charMatcher.getCharByImageBrightness(brightness);
        double trueBrightness = charMatcher.getCharBrightness(c);
        for (char a = MIN_ASCII; a <= MAX_ASCII; a++) {
            assert Math.abs(charMatcher.getCharBrightness(a) - brightness) >= Math.abs(trueBrightness - brightness);
            assert Math.abs(charMatcher.getCharBrightness(a) - brightness) != Math.abs(trueBrightness - brightness) || a < c;
        }
    }

    private static SubImgCharMatcher getMatcher() {
        char[] chars = new char[MAX_ASCII - MIN_ASCII + 1];
        for (char c = MIN_ASCII; c <= MAX_ASCII; c++)
            chars[c - MIN_ASCII] = c;
        return new SubImgCharMatcher(chars);
    }

    private static void testLinearStretch() {

//        SubImgCharMatcher charMatcher = new SubImgCharMatcher();
    }

    public static void main(String[] args) {
        testBrightnessMapGetCharByBrightness();
    }

}
