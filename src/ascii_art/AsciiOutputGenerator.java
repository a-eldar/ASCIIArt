package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;

/**
 * Generate the output of the ASCII art.
 * The output can be anything from OutputMethod.
 * The HTML file will be generated in the same directory as the image file.
 * The HTML file will use the default font.
 * The output will be generated using the specified algorithm
 * and the specified image file.
 * @author Eldar Amar
 */
public class AsciiOutputGenerator {
    /** Default font for HTML output */
    public static final String HTML_FONT = "Courier New";
    /** HTML file extension */
    public static final String HTML_EXTENSION = ".html";
    /** Character to start file extension */
    public static final char EXTENSION_START = '.';
    private AsciiOutputGenerator() {
    }

    /**
     * Generate the output of the ASCII art.
     * @param imageFilePath The path of the image file
     * @param outputMethod The method of output
     * @param algo The algorithm to generate the ASCII art
     */
    public static void generateOutput(String imageFilePath, OutputMethod outputMethod, AsciiArtAlgorithm algo) {
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
    }

    /**
     * Change the output method.
     * @param outputMethod The new output method. Indifferent to case.
     * @return The new output method from OutputMethod. If the output method is not valid, return null.
     */
    public static OutputMethod changeOutputMethod(String outputMethod) {
        switch (outputMethod.toLowerCase()) {
            case "html":
                return OutputMethod.HTML;
            case "console":
                return OutputMethod.CONSOLE;
            default:
                return null;
        }
    }
}
