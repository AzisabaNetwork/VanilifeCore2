package net.azisaba.vanilife.ui;

public class CLI
{
    public static final String RED = "\u001b[00;31m";
    public static final String GREEN = "\u001b[00;32m";
    public static final String YELLOW = "\u001b[00;33m";
    public static final String PURPLE = "\u001b[00;34m";
    public static final String PINK = "\u001b[00;35m";
    public static final String CYAN = "\u001b[00;36m";
    public static final String END = "\u001b[00m";

    public static final String SEPARATOR = "⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵⩵";
    public static final String SHORT_SEPARATOR = "-----------------------";

    public static String getSpaces(int length)
    {
        return " ".repeat(Math.max(0, length));
    }
}
