package ascii_output;

/**
 * Output a 2D array of chars to the console.
 * @author Dan Nirel
 */
public class ConsoleAsciiOutput implements AsciiOutput{
    /**
     * Output the specified 2D array of chars to the console.
     */
    @Override
    public void out(char[][] chars) {
        for (int y = 0; y < chars.length ; y++) {
            for (int x = 0; x < chars[y].length; x++) {
                System.out.print(chars[y][x] + " ");
            }
            System.out.println();
        }
    }
}
