package me.ubuntoof.Utils;

import me.ubuntoof.Action.ActionType;

public class TextFormatter {

    public static String formatAction(ActionType type, String n, String d) { return "(" + type + ") " + n + " - " + d; }
}
