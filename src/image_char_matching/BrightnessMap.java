package image_char_matching;

import java.util.TreeMap;
import java.util.TreeSet;

public class BrightnessMap {
    private final TreeMap<Character, Node> charMap;
    private final TreeSet<Node> brightnessMap;

    public BrightnessMap() {
        this.charMap = new TreeMap<>();
        this.brightnessMap = new TreeSet<>(
                (o1, o2) -> {
                    if (o1.brightness == o2.brightness) {
                        return o1.c - o2.c;
                    }
                    return Double.compare(o1.brightness, o2.brightness);
                }
        );
    }

    public void add(char c, double brightness) {
        Node matchNode = new Node(c, brightness, null);
        matchNode.match = new Node(c, brightness, matchNode);
        charMap.put(c, matchNode);
        brightnessMap.add(matchNode.match);
    }

    public void remove(char c) {
        Node matchNode = charMap.get(c);
        brightnessMap.remove(matchNode.match);
        charMap.remove(c);
    }

    public char getCharByImageBrightness(double brightness) throws IllegalArgumentException {
        Node ceiling = brightnessMap.ceiling(new Node(' ', brightness, null));
        Node floor = brightnessMap.floor(new Node(' ', brightness, null));
        if (ceiling == null && floor == null) {
            throw new IllegalArgumentException("Brightness not found in map");
        } else if (ceiling == null) {
            return floor.c;
        } else if (floor == null) {
            return ceiling.c;
        } else {
            double ceilingDiff = ceiling.brightness - brightness;
            double floorDiff = brightness - floor.brightness;
            if (ceilingDiff < floorDiff) {
                return ceiling.c;
            } else if (ceilingDiff > floorDiff) {
                return floor.c;
            } else {
                return ceiling.c < floor.c ? ceiling.c : floor.c;
            }
        }
    }

    public void getBrightness(char c) {
        if (!charMap.containsKey(c)) {
            throw new IllegalArgumentException("Character not found in map");
        }
        System.out.println(charMap.get(c).brightness);
    }

    private static class Node {
        private final char c;
        private final double brightness;
        private Node match;

        public Node(char c, double brightness, Node match) {
            this.c = c;
            this.brightness = brightness;
            this.match = match;
        }
    }
}
