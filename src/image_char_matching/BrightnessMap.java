package image_char_matching;

import java.util.TreeMap;
import java.util.TreeSet;

public class BrightnessMap {
    public static final char MIN_ASCII = ' ';
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
        Node matchNode = new Node(c, brightness);
        Node contains = charMap.get(c);
        if (contains != null) {
            brightnessMap.remove(contains);
        }
        charMap.put(c, matchNode);
        brightnessMap.add(matchNode);
    }

    public void remove(char c) {
        Node matchNode = charMap.get(c);
        brightnessMap.remove(matchNode);
        charMap.remove(c);
    }

    public char getCharByImageBrightness(double brightness) throws IllegalArgumentException {
        Node ceiling = brightnessMap.ceiling(new Node(MIN_ASCII, brightness));
        Node floor = brightnessMap.floor(new Node(MIN_ASCII, brightness));
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

    public Double getBrightness(char c) {
        Node node = charMap.get(c);
        return node == null ? null : node.brightness;
    }

    private static class Node {
        private final char c;
        private final double brightness;
        public Node(char c, double brightness) {
            this.c = c;
            this.brightness = brightness;
        }
    }
}
