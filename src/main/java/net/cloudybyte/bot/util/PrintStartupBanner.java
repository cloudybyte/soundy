package net.cloudybyte.bot.util;

public class PrintStartupBanner {
    public static void print(){
        String CYAN = Colors.CYAN;
        String RESET = Colors.RESET;

        System.out.println(CYAN + "                           _       ");
        System.out.println("                          | |      ");
        System.out.println(" ___  ___  _   _ _ __   __| |_   _ ");
        System.out.println("/ __|/ _ \\| | | | '_ \\ / _` | | | |");
        System.out.println("\\__ \\ (_) | |_| | | | | (_| | |_| |");
        System.out.println("|___/\\___/ \\__,_|_| |_|\\__,_|\\__, |");
        System.out.println("                              __/ |");
        System.out.println("                             |___/ " + RESET);
    }
}
