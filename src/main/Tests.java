package main;

import ascii_art.Shell;
import image_char_matching.BrightnessMap;

public class Tests {
    private static void testBrightnessMapGetCharByBrightness() {
        BrightnessMap brightnessMap = new BrightnessMap();
        brightnessMap.add('A', 0.9);
        brightnessMap.add('B', 0.6);
        brightnessMap.add('C', 0.4);
        brightnessMap.add('D', 0.8);
        brightnessMap.add('E', 0.2);
        brightnessMap.add('F', 1.0);
        brightnessMap.add('G', 0.0);
        brightnessMap.add('H', 0.5);
        brightnessMap.add('I', 0.3);
        brightnessMap.add('J', 0.7);
        brightnessMap.add('K', 0.1);
        brightnessMap.add('L', 0.9);
        brightnessMap.add('M', 0.6);
        brightnessMap.add('N', 0.4);
        brightnessMap.add('a', 0.5);
        brightnessMap.add('b', 0.3);
        brightnessMap.add('c', 0.7);
        brightnessMap.add('d', 0.1);
        brightnessMap.add('e', 0.9);
        brightnessMap.add('f', 0.6);
        brightnessMap.add('g', 0.4);
        brightnessMap.add('h', 0.8);
        brightnessMap.add('i', 0.2);
        brightnessMap.add('j', 1.0);
        brightnessMap.add('k', 0.0);
        brightnessMap.add('l', 0.5);
        brightnessMap.add('m', 0.3);
        brightnessMap.add('n', 0.7);
        brightnessMap.add('o', 0.1);
        brightnessMap.add('p', 0.9);
        brightnessMap.add('q', 0.6);
        brightnessMap.add('r', 0.4);
        brightnessMap.add('s', 0.8);
        brightnessMap.add('t', 0.2);
        brightnessMap.add('u', 1.0);
        brightnessMap.add('v', 0.0);
        brightnessMap.add('w', 0.5);
        brightnessMap.add('x', 0.3);
        brightnessMap.add('y', 0.7);
        brightnessMap.add('z', 0.1);
        assert brightnessMap.getCharByBrightness(0.5) == 'H';
        assert brightnessMap.getCharByBrightness(0.55) == 'B';
        assert brightnessMap.getCharByBrightness(0.05) == 'G';
    }

    public static void main(String[] args) {
        testBrightnessMapGetCharByBrightness();
    }

}
