package me.ubuntoof.utils;

import me.ubuntoof.Main;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Colorizer
{
    private static final Random random = new Random();
    private static final int INDENT_LENGTH = Main.useTerminal ? 2 : 0;

    public static final String ANSI_CLS = "\033[2J";
    public static final String ANSI_HOME = "\u001b[H";

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String REVERSE = "\u001B[7m";
    public static final String STRIKETHROUGH = "\u001B[9m";

    public static final String WHITE = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String GRAY = "\u001B[90m";

    public static final String LIGHT_GRAY = "\u001B[37m";
    public static final String LIGHT_RED = "\u001B[91m";
    public static final String LIGHT_GREEN = "\u001B[92m";
    public static final String LIGHT_YELLOW = "\u001B[93m";
    public static final String LIGHT_BLUE = "\u001B[94m";
    public static final String LIGHT_PURPLE = "\u001B[95m";
    public static final String LIGHT_CYAN = "\u001B[96m";
    public static final String BLACK = "\u001B[97m";

    public static final String WHITE_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String GRAY_BACKGROUND = "\u001B[100m";

    public static final String LIGHT_GRAY_BACKGROUND = "\u001B[47m";
    public static final String LIGHT_RED_BACKGROUND = "\u001B[101m";
    public static final String LIGHT_GREEN_BACKGROUND = "\u001B[102m";
    public static final String LIGHT_YELLOW_BACKGROUND = "\u001B[103m";
    public static final String LIGHT_BLUE_BACKGROUND = "\u001B[104m";
    public static final String LIGHT_PURPLE_BACKGROUND = "\u001B[105m";
    public static final String LIGHT_CYAN_BACKGROUND = "\u001B[106m";
    public static final String BLACK_BACKGROUND = "\u001B[107m";

    public enum Formats
    {
        RESET("\u001B[0m"),
        BOLD("\u001B[1m"),
        ITALIC("\u001B[3m"),
        UNDERLINE("\u001B[4m"),
        REVERSE("\u001B[7m"),
        STRIKETHROUGH("\u001B[9m");

        private final String val;
        Formats(String val) { this.val = val; }
        public static final List<Formats> VALUES = List.of(Formats.values());
        public static String getRandomValue() { return VALUES.get(random.nextInt(VALUES.size())).toString(); }
        public String toString() { return val; }

    }

    public enum Colors
    {
        WHITE("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        GRAY("\u001B[90m"),

        LIGHT_GRAY("\u001B[37m"),
        LIGHT_RED("\u001B[91m"),
        LIGHT_GREEN("\u001B[92m"),
        LIGHT_YELLOW("\u001B[93m"),
        LIGHT_BLUE("\u001B[94m"),
        LIGHT_PURPLE("\u001B[95m"),
        LIGHT_CYAN("\u001B[96m"),
        BLACK("\u001B[97m");

        private final String val;
        Colors(String val) { this.val = val; }
        public static final List<Colors> VALUES = List.of(Colors.values());
        public static String getRandomValue() { return VALUES.get(random.nextInt(VALUES.size())).toString(); }
        public String toString() { return val; }
    }

    public enum Backgrounds
    {

        WHITE_BACKGROUND("\u001B[40m"),
        RED_BACKGROUND("\u001B[41m"),
        GREEN_BACKGROUND("\u001B[42m"),
        YELLOW_BACKGROUND("\u001B[43m"),
        BLUE_BACKGROUND("\u001B[44m"),
        PURPLE_BACKGROUND("\u001B[45m"),
        CYAN_BACKGROUND("\u001B[46m"),
        GRAY_BACKGROUND("\u001B[100m"),
        LIGHT_GRAY_BACKGROUND("\u001B[47m"),
        LIGHT_RED_BACKGROUND("\u001B[101m"),
        LIGHT_GREEN_BACKGROUND("\u001B[102m"),
        LIGHT_YELLOW_BACKGROUND("\u001B[103m"),
        LIGHT_BLUE_BACKGROUND("\u001B[104m"),
        LIGHT_PURPLE_BACKGROUND("\u001B[105m"),
        LIGHT_CYAN_BACKGROUND("\u001B[106m"),
        BLACK_BACKGROUND("\u001B[107m");

        private final String val;
        Backgrounds(String val) { this.val = val; }
        public static final List<Backgrounds> VALUES = List.of(Backgrounds.values());
        public static String getRandomValue() { return VALUES.get(random.nextInt(VALUES.size())).toString(); }
        public String toString() { return val; }
    }

    public enum Icons
    {
        UNFILLED_KING("♔"),
        UNFILLED_QUEEN("♕"),
        UNFILLED_ROOK("♖"),
        UNFILLED_BISHOP("♗"),
        UNFILLED_KNIGHT("♘"),
        UNFILLED_PAWN("♙"),

        FILLED_KING("♚"),
        FILLED_QUEEN("♛"),
        FILLED_ROOK("♜"),
        FILLED_BISHOP("♝"),
        FILLED_KNIGHT("♞"),
        FILLED_PAWN("♟");

        private final String val;
        Icons(String val) { this.val = val; }
        public static final List<Icons> VALUES = List.of(Icons.values());
        public static String getRandomValue() { return VALUES.get(random.nextInt(VALUES.size())).toString(); }
        public String toString() { return val; }

    }

    public static String indent()
    {
        return " ".repeat(Math.max(0, INDENT_LENGTH));
    }

    public static void clear()
    {
        if(!Main.useTerminal)
        {
            System.out.print(ANSI_CLS + ANSI_HOME);
            System.out.flush();
        }
    }

    public static void clear(boolean silent)
    {
        if(Main.useTerminal && !silent)
        {
            System.out.print(ANSI_CLS + ANSI_HOME);
            System.out.flush();
        }
    }

    public static String getDivider(int len)
    {
        return Colorizer.GRAY + Colorizer.STRIKETHROUGH + " ".repeat(Math.max(0, len)) + Colorizer.RESET + "\n";
    }

    public static String getSubdivider(int len)
    {
        return Colorizer.LIGHT_GRAY + Colorizer.STRIKETHROUGH + " ".repeat(Math.max(0, len)) + Colorizer.RESET + "\n";
    }

    public static void print(String s)
    {
        System.out.print(Colorizer.indent() + s);
    }
    public static void println(String s)
    {
        System.out.println(Colorizer.indent() + s);
    }
    public static void println()
    {
        System.out.println();
    }
}

