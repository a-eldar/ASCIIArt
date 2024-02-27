package ascii_art;

import image.Image;
import image.ModifiedImage;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.HashSet;

/**
 * This class is used to run the shell for the ASCII art program.
 */
public class Shell {

    private final String DEFAULT_IMAGE_PATH = "cat.jpeg";
    private final HashSet<Character> charSet;
    private int resolution;
    private OutputMethod outputMethod;


    private ModifiedImage image = new ModifiedImage(DEFAULT_IMAGE_PATH);

    /**
     * Create a new Shell with the default image and character set.
     * @throws IOException If there is a problem with the image file
     */
    public Shell() throws IOException {
        this.charSet = new HashSet<>();
        char[] DEFAULT_CHAR_SET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (char c : DEFAULT_CHAR_SET) {
            charSet.add(c);
        }
        this.resolution = 128;
        this.outputMethod = OutputMethod.CONSOLE; // Default output method
        this.image = new ModifiedImage(DEFAULT_IMAGE_PATH); // need to check if the image is ok

    }

    public void run(){
        getUserInput();
    }

    private void getUserInput() {
        System.out.println(">>>");
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
                        System.err.println("Did not change resolution due to incorrect format.");
                    }
                    break;
                case "image":
                    if (command.length == 2) {
                        try {
                            changeImage(command[1]);
                        } catch (IOException e) {
                            System.err.println("Did not execute due to problem with image file.");
                        }
                    }
                case "output":
                    if (command.length == 2) {
                        changeOutputMethod(command[1]);
                    }
                    break;

//                case "asciiArt":
//                    new AsciiArtAlgorithm(image, resolution, new SubImgCharMatcher(charSet));
//
//                    break;
                default:
                    System.err.println("Did not execute due to incorrect command");
                    break;
            }

            System.out.println(">>>");
            userInput = KeyboardInput.readLine();
        }
    }

    private void handleAddCommand(String[] command) {
        if (command.length != 2) {
            System.err.println("Did not add due to incorrect format.");
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
                        System.err.println("Did not add due to invalid character.");
                    }
                } else if (option.length() == 3 && option.charAt(1) == '-') {
                    addCharsInRange(option);
                } else {
                    System.err.println("Did not add due to incorrect format.");
                }
                break;
        }
    }

    private void handleRemoveCommand(String[] command) {
        if (command.length != 2) {
            System.err.println("Did not remove due to incorrect format.");
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
                        System.err.println("Did not remove due to invalid character.");
                    }
                } else if (option.length() == 3 && option.charAt(1) == '-') {
                    removeCharsInRange(option);
                } else {
                    System.err.println("Did not remove due to incorrect format.");
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
                    System.out.println("Resolution set to " + this.resolution + ".");
                } else {
                    System.err.println("Did not change resolution due to exceeding boundaries.");
                }
                break;
            case "down":
                if (this.resolution / 2 >= getMinResolution()) {
                    this.resolution /= 2;
                    System.out.println("Resolution set to " + this.resolution + ".");
                } else {
                    System.err.println("Did not change resolution due to exceeding boundaries.");
                }
                break;
            default:
                System.err.println("Did not change resolution due to incorrect format.");
                break;
        }
    }

    private int getMaxResolution() {
        // Calculate and return the maximum resolution based on image dimensions
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        return Math.max(1, imgWidth / imgHeight);
    }

    private int getMinResolution() {
        return 1;
    }

    private void changeImage(String imagePath) throws IOException {
        try {
            this.image = new ModifiedImage(imagePath);
        } catch (IOException e) {
            System.err.println("Did not change image due to problem with image file.");
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
                System.err.println("Did not change output method due to incorrect format.");
        }
    }

    private enum OutputMethod {
        HTML, CONSOLE
    }

}