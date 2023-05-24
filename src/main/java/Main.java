import java.util.*;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    private static Game game;

    public static void main(String[] args) throws RoomNotFoundException, CommandNotFoundException {
        System.out.println("Welcome to George's hazardous text adventure game. Begin your perilous and unpredictable " +
                "journey now and see how far you can travel. \n This is the story of our friend George, he loves to travel " +
                "and have adventures but chaos always happens along the way. \n The aim is to survive ... mwwaahahahaaa \n \n" +
                "You start your journey in Sheffield but want to travel further. Journey around the UK trying to find the things you need to travel the world. " +
                "\nIf you need help, type show help. \nDisclaimer, maps have no bearing on reality or geography. " +
                "\nLogic will not help you here, this is world invented by Юлія i Катерина.");
        game = new Game();
        String input;
        String output;
        do {
            System.out.print("> ");
            input = sc.nextLine();
            output = game.runCommand(input);
            System.out.println(output);
        } while (!"quit".equals(input));
    }
}
