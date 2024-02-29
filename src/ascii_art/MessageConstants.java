package ascii_art;

/**
 * Class containing constants for messages to be printed to the console.
 * This class is not meant to be instantiated.
 */
public class MessageConstants {

    /** Message prompt for the user to enter a command */
    public static final String USER_INPUT_PROMPT = ">>> ";

    /** Error message when failed to change resolution due to incorrect format */
    public static final String RESOLUTION_CHANGE_ERROR = "Did not change resolution due to incorrect format.";

    /** Error message when failed to execute due to problem with image file */
    public static final String IMAGE_CHANGE_ERROR = "Did not execute due to problem with image file.";

    /** Error message when failed to execute due to incorrect command */
    public static final String INCORRECT_COMMAND_ERROR = "Did not execute due to incorrect command.";

    /** Error message when failed to add a character due to incorrect format */
    public static final String ADD_CHAR_ERROR = "Did not add due to incorrect format.";

    /** Error message when failed to add a character due to invalid character */
    public static final String INVALID_CHAR_ADD_ERROR = "Did not add due to invalid character.";

    /** Error message when failed to remove a character due to incorrect format */
    public static final String REMOVE_CHAR_ERROR = "Did not remove due to incorrect format.";

    /** Error message when failed to remove a character due to invalid character */
    public static final String INVALID_CHAR_REMOVE_ERROR = "Did not remove due to invalid character.";

    /** Error message when failed to change resolution due to exceeding boundaries */
    public static final String RESOLUTION_BOUNDS_ERROR = "Did not change resolution due to exceeding boundaries.";

    /** Error message when failed to change output method due to incorrect format */
    public static final String OUTPUT_METHOD_ERROR = "Did not change output method due to incorrect format.";

    /** Exception message when brightness is not found in the map */
    public static final String CHAR_SET_EMPTY_EXCEPTION = "Brightness not found in map";

    /**
     * Prints the message for setting the resolution.
     * @param resolution The resolution to be set
     */
    public static void printResolutionSetMessage(int resolution) {
        System.out.println("Resolution set to " + resolution + ".");
    }

    // Private constructor to prevent instantiation
    private MessageConstants() {}
}
