package ascii_art;

public class MessageConstants {
    public static final String USER_INPUT_PROMPT = ">>> ";
    public static final String RESOLUTION_CHANGE_ERROR = "Did not change resolution due to incorrect format.";
    public static final String IMAGE_CHANGE_ERROR = "Did not execute due to problem with image file.";
    public static final String INCORRECT_COMMAND_ERROR = "Did not execute due to incorrect command.";
    public static final String ADD_CHAR_ERROR = "Did not add due to incorrect format.";
    public static final String INVALID_CHAR_ADD_ERROR = "Did not add due to invalid character.";
    public static final String REMOVE_CHAR_ERROR = "Did not remove due to incorrect format.";
    public static final String INVALID_CHAR_REMOVE_ERROR = "Did not remove due to invalid character.";
    public static final String RESOLUTION_BOUNDS_ERROR = "Did not change resolution due to exceeding boundaries.";
    public static final String OUTPUT_METHOD_ERROR = "Did not change output method due to incorrect format.";
    public static void printResolutionSetMessage(int resolution) {
        System.out.println("Resolution set to " + resolution + ".");
    }
    private MessageConstants() {}
}
