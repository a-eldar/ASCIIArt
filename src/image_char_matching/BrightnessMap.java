package image_char_matching;

import ascii_art.MessageConstants;

import java.util.*;

public class BrightnessMap implements Iterable<AbstractMap.SimpleEntry<Character, Double>> {
    public static final char MIN_ASCII = ' ';
    private final HashMap<Character, Node> charMap;
    private final TreeSet<Node> brightnessSet;

    public BrightnessMap() {
        this.charMap = new HashMap<>();
        this.brightnessSet = new TreeSet<>(
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
            brightnessSet.remove(contains);
        }
        charMap.put(c, matchNode);
        brightnessSet.add(matchNode);
    }

    public void remove(char c) {
        Node matchNode = charMap.get(c);
        brightnessSet.remove(matchNode);
        charMap.remove(c);
    }

    public char getCharByImageBrightness(double brightness) throws IllegalArgumentException {
        Node ceiling = brightnessSet.ceiling(new Node(MIN_ASCII, brightness));
        Node floor = brightnessSet.floor(new Node(MIN_ASCII, brightness));
        if (ceiling == null && floor == null) {
            throw new IllegalArgumentException(MessageConstants.CHAR_SET_EMPTY_ERROR);
        }
        if (ceiling == null) {
            return floor.c;
        }
        if (floor == null) {
            return ceiling.c;
        }
        double ceilingDiff = ceiling.brightness - brightness;
        double floorDiff = brightness - floor.brightness;
        if (ceilingDiff < floorDiff) {
            return ceiling.c;
        }
        if (ceilingDiff > floorDiff) {
            return floor.c;
        }
        return ceiling.c < floor.c ? ceiling.c : floor.c;
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

    @Override
    public Iterator<AbstractMap.SimpleEntry<Character, Double>> iterator() {
        return new BrightnessIterator();
    }

    private class BrightnessIterator implements Iterator<AbstractMap.SimpleEntry<Character, Double>> {
        private final Iterator<Node> iterator;
        public BrightnessIterator() {
            this.iterator = brightnessSet.iterator();
        }
        public boolean hasNext() {
            return iterator.hasNext();
        }
        public AbstractMap.SimpleEntry<Character, Double> next() {
            Node node = iterator.next();
            return new AbstractMap.SimpleEntry<>(node.c, node.brightness);
        }
    }
}
