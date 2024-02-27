package ascii_art;

import java.util.Scanner;

/**
 * This class is used to read input from the user.
 */
class KeyboardInput
{
    private static KeyboardInput keyboardInputObject = null;
    private Scanner scanner;
    
    private KeyboardInput()
    {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Get the singleton object of KeyboardInput.
     * @return The singleton object of KeyboardInput.
     */
    public static KeyboardInput getObject()
    {
        if(KeyboardInput.keyboardInputObject == null)
        {
            KeyboardInput.keyboardInputObject = new KeyboardInput();
        }
        return KeyboardInput.keyboardInputObject;
    }

    /**
     * Read a line from the user.
     * @return The line read from the user.
     */
    public static String readLine()
    {
        return KeyboardInput.getObject().scanner.nextLine().trim();
    }
}