public class Tile {

    private static final boolean COLOR_MODE = false;
    int value;
    char color;

    /*
     * Creates a tile using the given color and value, colors are represented
     * using the following letters: Y: Yellow, B: Blue, R: Red, K: Black
     * Values can be in the range [1,7]. There are four tiles of each color value
     * combination (7 * 4 * 4) = 112 tiles, false jokers are not included in this
     * game.
     */
    public Tile(int value, char color) {
        this.value = value;
        this.color = color;
    }

    /*
     * Compares tiles so that they can be added to the hands in order
     */
    public int compareTo(Tile t) {
        if (getValue() < t.getValue()) {
            return -1;
        } else if (getValue() > t.getValue()) {
            return 1;
        } else {
            if (colorNameToInt() < t.colorNameToInt()) {
                return -1;
            } else if (colorNameToInt() > t.colorNameToInt()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public int colorNameToInt() {
        if (color == 'Y') {
            return 0;
        } else if (color == 'B') {
            return 1;
        } else if (color == 'R') {
            return 2;
        } else {
            return 3;
        }
    }

    // determines if this tile can make a chain with the given tile
    public boolean canFormChainWith(Tile t) {

        // can make chain if same number but different color
        if (t.getColor() != color && t.getValue() == value) {
            return true;
        } else {
            return false;
        }

    }

    enum Color {
        BLACK(0, 0, 0),
        YELLOW(255, 255, 0),
        // YELLOW(255, 255, 143),
        BLUE(0, 0, 255),
        RED(255, 0, 0),
        WHITE(255, 255, 255);

        private Color(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public String ansiFGString() {
            return String.format("\33[38;2;%d;%d;%dm", r, g, b);
        }

        public String ansiBGString() {
            return String.format("\33[48;2;%d;%d;%dm", r, g, b);
        }

        int r, g, b;
    }

    private String plainToString() {
        return "" + value + color;
    }

    private String colorToString() {
        final String ANSI_WHITE_BG = "";
        // final String ANSI_WHITE_BG = Color.WHITE.ansiBGString() + "\33[1m";
        final String ANSI_RESET_ALL = "\33[0m";

        String ansiStyle;
        switch (color) {
            case 'B':
                ansiStyle = ANSI_WHITE_BG + Color.BLUE.ansiFGString();
                break;
            case 'Y':
                ansiStyle = ANSI_WHITE_BG + Color.YELLOW.ansiFGString();
                break;
            case 'R':
                ansiStyle = ANSI_WHITE_BG + Color.RED.ansiFGString();
                break;
            case 'K':
                ansiStyle = ANSI_WHITE_BG + Color.BLACK.ansiFGString();
                break;
            default:
                ansiStyle = "";
                break;

        }
        return ansiStyle + value + ANSI_RESET_ALL;

    }

    @Override
    public String toString() {
        if (COLOR_MODE) {
            return colorToString();
        } else {
            return plainToString();
        }
    }

    public int getValue() {
        return value;
    }

    public char getColor() {
        return color;
    }

}
