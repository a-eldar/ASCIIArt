package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.ModifiedImage;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;

/**
 * This class is used to run the shell for the ASCII art program.
 * It handles user input and runs the algorithm to create the ASCII art.
 * @author Eldar Amar & Ofek Kelly
 */
public class Shell {

    /** Default image file path */
    private static final String DEFAULT_IMAGE_PATH = "cat.jpeg";
    /** Default font for HTML output */
    public static final String HTML_FONT = "Courier New";
    /** HTML file extension */
    public static final String HTML_EXTENSION = ".html";
    /** Character to start file extension */
    public static final char EXTENSION_START = '.';
    /** Minimum ASCII character */
    public static final char MIN_ASCII = ' ';
    /** Maximum ASCII character */
    public static final char MAX_ASCII = '~';
    public static final char RANGE_SEPARATOR = '-';
    private final SubImgCharMatcher charMatcher;
    private int resolution;
    private OutputMethod outputMethod;
    private static final char[] DEFAULT_CHAR_SET = {'0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9'};
    private final static int DEFAULT_RESOLUTION = 128;

    private ModifiedImage image = new ModifiedImage(DEFAULT_IMAGE_PATH);
    private String imageFilePath = DEFAULT_IMAGE_PATH;

    /**
     * Create a new Shell with the default image and character set.
     * @throws IOException If there is a problem with the image file
     */
    public Shell() throws IOException {
        this.charMatcher = new SubImgCharMatcher(DEFAULT_CHAR_SET);
        this.resolution = DEFAULT_RESOLUTION;
        this.outputMethod = OutputMethod.CONSOLE; // Default output method
    }

    /**
     * Start the shell.
     */
    public void run(){
        getUserInput();
    }

    private void getUserInput() {
        System.out.print(MessageConstants.USER_INPUT_PROMPT);
        String userInput = KeyboardInput.readLine();
        while (!userInput.equals("exit")){
            String[] command = userInput.split(" ");
            switch (command[0]){
                case "chars":
                    printCharacterSet();
                    break;
                case "add":
                    handleAddCommand(command);
                    break;
                case "remove":
                    handleRemoveCommand(command);
                    break;
                case "res":
                    if (command.length == 2) {
                        resolutionChange(command[1]);
                    } else {
                        System.out.println(MessageConstants.RESOLUTION_CHANGE_ERROR);
                    }
                    break;
                case "image":
                    if (command.length != 2) {
                        System.out.println(MessageConstants.IMAGE_CHANGE_ERROR);
                        break;
                    }
                    try {
                        changeImage(command[1]);
                    } catch (IOException e) {
                        System.out.println(MessageConstants.IMAGE_CHANGE_ERROR);
                    }
                    break;
                case "output":
                    if (command.length != 2) {
                        System.out.println(MessageConstants.OUTPUT_METHOD_ERROR);
                        break;
                    }
                    changeOutputMethod(command[1]);
                    break;

                case "asciiArt":
                    AsciiArtAlgorithm algo = new AsciiArtAlgorithm(image, resolution, charMatcher);
                    switch (outputMethod) {
                        case HTML:
                            String path = imageFilePath.substring(0,
                                    imageFilePath.lastIndexOf(EXTENSION_START));
                            HtmlAsciiOutput htmlAsciiOutput = new HtmlAsciiOutput(path +
                                    HTML_EXTENSION, HTML_FONT);
                            htmlAsciiOutput.out(algo.run());
                            break;
                        case CONSOLE:
                            ConsoleAsciiOutput consoleAsciiOutput = new ConsoleAsciiOutput();
                            consoleAsciiOutput.out(algo.run());
                            break;
                    }
                    break;
                default:
                    System.out.println(MessageConstants.INCORRECT_COMMAND_ERROR);
                    break;
            }

            System.out.print(MessageConstants.USER_INPUT_PROMPT);
            userInput = KeyboardInput.readLine();
        }
    }

    private void handleAddCommand(String[] command) {
        if (command.length != 2) {
            System.out.println(MessageConstants.ADD_CHAR_ERROR);
            return;
        }
        String option = command[1];
        switch (option) {
            case "all":
                for (char c = MIN_ASCII; c <= MAX_ASCII; c++) {
                    addCharToCharMatcher(c);
                }
                break;
            case "space":
                addCharToCharMatcher(' ');
                break;
            default:
                if (option.length() == 1) {
                    char singleChar = option.charAt(0);
                    if (isValidAsciiChar(singleChar)) {
                        addCharToCharMatcher(singleChar);
                    } else {
                        System.out.println(MessageConstants.INVALID_CHAR_ADD_ERROR);
                    }
                } else if (option.length() == 3 && option.charAt(1) == RANGE_SEPARATOR) {
                    applyToRange(option, this::addCharToCharMatcher);
                } else {
                    System.out.println(MessageConstants.ADD_CHAR_ERROR);
                }
                break;
        }
    }

    private void handleRemoveCommand(String[] command) {
        if (command.length != 2) {
            System.out.println(MessageConstants.REMOVE_CHAR_ERROR);
            return;
        }
        String option = command[1];
        switch (option) {
            case "all":
                charMatcher.clearChars();
                break;
            case "space":
                removeCharFromCharMatcher(' ');
                break;
            default:
                if (option.length() == 1) {
                    char singleChar = option.charAt(0);
                    if (isValidAsciiChar(singleChar)) {
                        removeCharFromCharMatcher(singleChar);
                    } else {
                        System.out.println(MessageConstants.INVALID_CHAR_REMOVE_ERROR);
                    }
                } else if (option.length() == 3 && option.charAt(1) == RANGE_SEPARATOR) {
                    applyToRange(option, this::removeCharFromCharMatcher);
                } else {
                    System.out.println(MessageConstants.REMOVE_CHAR_ERROR);
                }
                break;
        }
    }

    private void printCharacterSet() {
        for (AbstractMap.SimpleEntry<Character, Double> entry : charMatcher) {
            System.out.print(entry.getKey() + " ");
        }
        System.out.println();
    }

    /**
     * Apply the given applier to all characters in the given range.
     * @param range The range of characters to apply the applier to.
     *              Must be in the format "a-b" where a and b are characters.
     * @param applier The applier to apply to the characters in the range.
     */
    private void applyToRange(String range, CharApplier applier) {
        char start = range.charAt(0);
        char end = range.charAt(2);

        if (start > end) {
            char temp = start;
            start = end;
            end = temp;
        }

        for (char c = start; c <= end; c++) {
            if (isValidAsciiChar(c)) {
                applier.apply(c);
            }
        }
    }

    private boolean isValidAsciiChar(char c) {
        return c >= MIN_ASCII && c <= MAX_ASCII;
    }

    private void addCharToCharMatcher(char c) {
        charMatcher.addChar(c);
    }

    private void removeCharFromCharMatcher(char c) {
        charMatcher.removeChar(c);
    }

    private void resolutionChange(String command) {
        switch (command) {
            case "up":
                if (this.resolution * 2 <= getMaxResolution()) {
                    this.resolution *= 2;
                    MessageConstants.printResolutionSetMessage(this.resolution);
                } else {
                    System.out.println(MessageConstants.RESOLUTION_BOUNDS_ERROR);
                }
                break;
            case "down":
                if (this.resolution / 2 >= getMinResolution()) {
                    this.resolution /= 2;
                    MessageConstants.printResolutionSetMessage(this.resolution);
                } else {
                    System.out.println(MessageConstants.RESOLUTION_BOUNDS_ERROR);
                }
                break;
            default:
                System.out.println(MessageConstants.RESOLUTION_CHANGE_ERROR);
                break;
        }
    }



    private int getMaxResolution() {
        return image.getWidth();
    }

    private int getMinResolution() {
        // Calculate and return the minimum resolution based on image dimensions
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        return Math.max(1, imgWidth / imgHeight);
    }

    private void changeImage(String imagePath) throws IOException {
        try {
            this.image = new ModifiedImage(imagePath);
            this.imageFilePath = imagePath;
        } catch (IOException e) {
            System.out.println(MessageConstants.IMAGE_CHANGE_ERROR);
        }

    }

    private void changeOutputMethod(String method) {
        switch (method.toLowerCase()) {
            case "html":
                this.outputMethod = OutputMethod.HTML;
                break;
            case "console":
                this.outputMethod = OutputMethod.CONSOLE;
                break;
            default:
                System.out.println(MessageConstants.OUTPUT_METHOD_ERROR);
        }
    }

    private enum OutputMethod {
        HTML, CONSOLE
    }

    private interface CharApplier {
        void apply(char c);
    }


    /**
     * Main method to run the shell.
     * @param args Command line arguments
     * @throws IOException If there is a problem with the image file
     */
    public static void main(String[] args) throws IOException {
        Shell shell = new Shell();
        shell.run();
    }

}