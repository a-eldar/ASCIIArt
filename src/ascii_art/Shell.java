package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.ModifiedImage;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

/**
 * This class is used to run the shell for the ASCII art program.
 */
public class Shell {

    private static final String DEFAULT_IMAGE_PATH = "cat.jpeg";
    private final TreeSet<Character> charSet;
    private int resolution;
    private OutputMethod outputMethod;
    private static final List<Character> DEFAULT_CHAR_SET = List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private final static int DEFAULT_RESOLUTION = 128;

    private ModifiedImage image = new ModifiedImage(DEFAULT_IMAGE_PATH);
    private String imageFilePath = DEFAULT_IMAGE_PATH;

    /**
     * Create a new Shell with the default image and character set.
     * @throws IOException If there is a problem with the image file
     */
    public Shell() throws IOException {
        this.charSet = new TreeSet<>();
        charSet.addAll(DEFAULT_CHAR_SET);
        this.resolution = DEFAULT_RESOLUTION;
        this.outputMethod = OutputMethod.CONSOLE; // Default output method
    }

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
                    AsciiArtAlgorithm algo = new AsciiArtAlgorithm(image, resolution, new SubImgCharMatcher(charSet));
                    switch (outputMethod) {
                        case HTML:
                            String path = imageFilePath.substring(0, imageFilePath.lastIndexOf('.'));
                            HtmlAsciiOutput htmlAsciiOutput = new HtmlAsciiOutput(path + ".html", "Courier New");
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
                addAllChars();
                break;
            case "space":
                addSpaceChar();
                break;
            default:
                if (option.length() == 1) {
                    char singleChar = option.charAt(0);
                    if (isValidAsciiChar(singleChar)) {
                        addCharToCharset(singleChar);
                    } else {
                        System.out.println(MessageConstants.INVALID_CHAR_ADD_ERROR);
                    }
                } else if (option.length() == 3 && option.charAt(1) == '-') {
                    addCharsInRange(option);
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
                removeAllChars();
                break;
            case "space":
                removeSpaceChar();
                break;
            default:
                if (option.length() == 1) {
                    char singleChar = option.charAt(0);
                    if (isValidAsciiChar(singleChar)) {
                        removeCharToCharset(singleChar);
                    } else {
                        System.out.println(MessageConstants.INVALID_CHAR_REMOVE_ERROR);
                    }
                } else if (option.length() == 3 && option.charAt(1) == '-') {
                    removeCharsInRange(option);
                } else {
                    System.out.println(MessageConstants.REMOVE_CHAR_ERROR);
                }
                break;
        }
    }

    private void printCharacterSet() {
        for (char c : charSet) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    private void addCharsInRange(String range) {
        char start = range.charAt(0);
        char end = range.charAt(2);

        if (start > end) {
            char temp = start;
            start = end;
            end = temp;
        }

        for (char c = start; c <= end; c++) {
            if (isValidAsciiChar(c)) {
                addCharToCharset(c);
            }
        }
    }

    private void removeCharsInRange(String range) {
        char start = range.charAt(0);
        char end = range.charAt(2);

        if (start > end) {
            char temp = start;
            start = end;
            end = temp;
        }

        for (char c = start; c <= end; c++) {
            if (isValidAsciiChar(c)) {
                removeCharToCharset(c);
            }
        }
    }

    private boolean isValidAsciiChar(char c) {
        return c >= ' ' && c <= '~';
    }

    private void addAllChars() {
        for (char c = ' '; c <= '~'; c++) {
            addCharToCharset(c);
        }
    }

    private void removeAllChars() {
        charSet.clear();
    }

    private void addSpaceChar() {
        addCharToCharset(' ');
    }

    private void removeSpaceChar() {
        removeCharToCharset(' ');
    }

    private void addCharToCharset(char c) {
        charSet.add(c);
    }

    private void removeCharToCharset(char c) {
        charSet.remove(c);
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

}