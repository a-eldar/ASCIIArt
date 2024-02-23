package ascii_art;

import java.util.ArrayList;
import java.util.List;

public class Shell {

    private final List<Character> charSet;
    private final char[] DEFAULT_CHAR_SET = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private final int DEFAULT_RESOLUTION = 128;
    private final String DEFAULT_IMAGE_PATH = "examples/cat.jpeg";

    public Shell() {
        this.charSet = new ArrayList<>();
        for (char c : DEFAULT_CHAR_SET) {
            charSet.add(c);
        }
    }

    public void run(){
        getUserInput();
    }

    private void getUserInput() {
        System.out.println(">>>");
        String userInput = KeyboardInput.readLine();
        while (!userInput.equals("exit")){
            if (userInput.equals("chars")){
                for (char c : charSet) {
                    System.out.print(c + " ");
                }
            }

            if (userInput.startsWith("add")){
                handleAddCommand(userInput);
            }

            System.out.println(">>>");
            userInput = KeyboardInput.readLine();
        }
    }

    private void handleAddCommand(String command) {
        String[] parts = command.split(" ");
        if (parts.length != 2) {
            System.err.println("Did not add due to incorrect format.");
            return;
        }

        String option = parts[1];
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
            switch (option) {
                case "all":
                    addAllChars();
                    break;
                case "space":
                    addSpaceChar();
                    break;
                default:
                    System.err.println("Did not add due to incorrect format");
                    break;
            }
        }
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

    private boolean isValidAsciiChar(char c) {
        return c >= ' ' && c <= '~';
    }

    private void addAllChars() {
        for (char c = ' '; c <= '~'; c++) {
            addCharToCharset(c);
        }
    }

    private void addSpaceChar() {
        addCharToCharset(' ');
    }

    private void addCharToCharset(char c) {
        if (!charSet.contains(c)) {
            charSet.add(c);
        }
    }
}
