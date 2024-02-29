package image_char_matching;

import ascii_art.MessageConstants;
import ascii_art.Shell;
import java.util.*;

public class BrightnessMap implements Iterable<AbstractMap.SimpleEntry<Character, Double>> {
    public static final char MIN_ASCII = Shell.MIN_ASCII;
    private final HashMap<Character, Node> charMap;
    private final TreeSet<Node> brightnessSet;
    private final TreeSet<Character> charSet;

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
        this.charSet = new TreeSet<>();
    }

    /**
     * Add a character to the map.
     * @param c The character to add
     * @param brightness The brightness of the character
     */
    public void add(char c, double brightness) {
        Node contains = charMap.get(c);
        if (contains != null) {
            brightnessSet.remove(contains);
        }
        Node matchNode = new Node(c, brightness);
        charMap.put(c, matchNode);
        brightnessSet.add(matchNode);
        charSet.add(c);
    }

    /**
     * Remove a character from the map.
     * @param c The character to remove
     */
    public void remove(char c) {
        Node matchNode = charMap.get(c);
        brightnessSet.remove(matchNode);
        charMap.remove(c);
        charSet.remove(c);
    }

    /**
     * Add all the characters and brightnesses from the given collection to the map.
     * @param entries The collection of characters and brightnesses to add
     */
    public void addAll(Collection<AbstractMap.SimpleEntry<Character, Double>> entries) {
        for (AbstractMap.SimpleEntry<Character, Double> entry : entries) {
            add(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Remove all the characters from the map.
     */
    public void clear() {
        charMap.clear();
        brightnessSet.clear();
        charSet.clear();
    }

    /**
     * Copy all the characters and brightnesses from the given map to this map.
     * @param other The map to copy
     */
    public void copy(BrightnessMap other) {
        clear();
        for (AbstractMap.SimpleEntry<Character, Double> entry : other) {
            add(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Get the character from the charset that best matches the given brightness.
     * @param brightness The brightness to match
     * @return The character from the charset that best matches the given brightness
     * @throws IllegalArgumentException If the map is empty
     */
    public char getCharByBrightness(double brightness) throws IllegalArgumentException {
        Node ceiling = brightnessSet.ceiling(new Node(MIN_ASCII, brightness));
        Node floor = brightnessSet.floor(new Node(MIN_ASCII, brightness));
        if (ceiling == null && floor == null) {
            throw new IllegalArgumentException(MessageConstants.CHAR_SET_EMPTY_EXCEPTION);
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
        private final Iterator<Character> iterator;
        public BrightnessIterator() {
            this.iterator = charSet.iterator();
        }
        public boolean hasNext() {
            return iterator.hasNext();
        }
        public AbstractMap.SimpleEntry<Character, Double> next() {
            char c = iterator.next();
            return new AbstractMap.SimpleEntry<>(c, charMap.get(c).brightness);
        }
    }
}
