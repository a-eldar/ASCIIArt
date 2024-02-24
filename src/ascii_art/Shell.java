package ascii_art;

import image.Image;

import java.util.HashSet;

public class Shell {

    private final String DEFAULT_IMAGE_PATH = "examples/cat.jpeg";
    private final HashSet<Character> charSet;
    private int resolution;

    public Shell() {
        this.charSet = new HashSet<>();
        char[] DEFAULT_CHAR_SET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (char c : DEFAULT_CHAR_SET) {
            charSet.add(c);
        }
        this.resolution = 128;
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
                default:
                    System.err.println("Invalid command.");
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
        if (command.equals("up")){
            this.resolution *= 2;
            System.out.println("Resolution set to " + this.resolution);
        } else if (command.equals("down")) {
            this.resolution /= 2;
            System.out.println("Resolution set to " + this.resolution);
        } else {
            System.err.println("Did not change resolution due to incorrect format.");
        }
    }
}
