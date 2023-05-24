import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    public Map<String, List<String>> possibleCommands;
    public ArrayList<Room> roomArray = new ArrayList<>();
    public Room currentRoom;

    public List<Room> visitedRooms = new ArrayList<>();
    public int energyLevel = 3;

    public BoardWindow boardWindow;

    public int Level;

    public Game() throws RoomNotFoundException {
        possibleCommands = CommandMap.getPossibleCommands();
        setupLevel1();
    }

    public void checkEnergy() {
        if (energyLevel == 2) {
            System.out.println("You only have 2 energy left, find something to eat quickly!");
        }
        if (energyLevel == 1) {
            System.out.println("You only have 1 energy left, find something to eat really quickly!");
        }
        if (energyLevel == 0) {
            System.out.println("Game over. You didn't listen to your stomach and died.");
            System.exit(0);
        }
    }

    private void setupLevel(int level, String title, String startingRoom, int row, int column) throws RoomNotFoundException {
        roomArray = new ArrayList<>();
        loadRooms(level);
        Board board = new Board(row, column);
        this.boardWindow = new BoardWindow(board, title, 100);
        hide("map");
        currentRoom = roomArray.stream().filter(room -> room.getName().equals(startingRoom)).findFirst()
                .orElseThrow(RoomNotFoundException::new);
        boardWindow.getBoard().setCell(currentRoom.getRow(), currentRoom.getColumn(), CellType.START);
        show("map");
        Level = level;
        currentRoom.setVisited(true);
        numberVisited();
    }

    private void setupLevel1() throws RoomNotFoundException {
        String title = "Level 1: the beginning";
        String startingRoom = "Sheffield";
        setupLevel(1, title, startingRoom, 7, 7);
        System.out.println("You are in " + currentRoom.getName() + ". What do you want to do now?");
    }

    private void setupLevel2() throws RoomNotFoundException {
        String title = "Level 2: the middle";
        String startingRoom = "Thailand";
        setupLevel(2, title, startingRoom, 7, 9);
        System.out.println("You are now in " + currentRoom.getName());
        System.out.println(currentRoom.getDescription());
    }

    private void setupLevel3() {
        hide("map");
        System.out.println("Your space suit malfunctioned ... THE END");
        ImagePanel myImagePanel = new ImagePanel();
    }

    public void loadRooms(int level) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("resources/Level" + level + ".txt"));
            Iterator<String> it = br.lines().iterator();
            while (it.hasNext()) {
                String rowString = it.next();
                String columnString = it.next();
                String possDirString = it.next();
                String name = it.next();
                String description = it.next();
                String roomItem = it.next();
                String roomImage = it.next();
                Boolean visited = false;
                int row = Integer.parseInt(rowString);
                int column = Integer.parseInt(columnString);
                Set<String> possDir = new TreeSet<>();
                List<String> strs = Arrays.asList(possDirString.split(", "));
                strs.forEach(str -> possDir.add(str));
                Room newRoom = new Room(row, column, possDir, name, description, roomItem, roomImage, visited);
                roomArray.add(newRoom);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void visitedCell() {
        boardWindow.getBoard().setCell(currentRoom.getRow(), currentRoom.getColumn(), CellType.VISITED, currentRoom.getRoomImage());
        boardWindow.repaint();
    }

    public void numberVisited(){
        visitedRooms = roomArray.stream().filter(Room::getVisited).collect(Collectors.toList());
    }

    public void moveRoom(String newRoomName) throws RoomNotFoundException {
        currentRoom = roomArray.stream().filter(room -> room.getName().equals(newRoomName))
                .findFirst().orElseThrow(RoomNotFoundException::new);
        boardWindow.getBoard().setCell(currentRoom.getRow(), currentRoom.getColumn(), CellType.CURRENT_ROOM);
        boardWindow.repaint();
        System.out.println("You are now in " + currentRoom.getName());
        System.out.println(currentRoom.getDescription());
        currentRoom.setVisited(true);
        numberVisited();
    }

    public static List<String> getWordList(String input) {
        String delims = " \t,.:;?!\"'";
        List<String> words = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input, delims);
        while (tokenizer.hasMoreTokens()) {
            words.add(tokenizer.nextToken());
        }
        return words;
    }

    public static Command parseCommand(List<String> words, Map<String, List<String>> possibleCommands) throws CommandNotFoundException {
        String verb;
        String noun;

        if (words.size() != 2) {
            System.out.println("Commands should be 2 words");
            throw new CommandNotFoundException();
        } else {
            verb = words.get(0);
            if (!possibleCommands.containsKey(verb)) {
                System.out.println(verb + " is not a known verb");
                throw new CommandNotFoundException();
            }
            noun = words.get(1);
            List<String> nouns = possibleCommands.get(verb);
            if (!nouns.contains(noun)) {
                System.out.println(noun + " is not a known noun");
                throw new CommandNotFoundException();
            }
        }
        return new Command(verb, noun);
    }

    public String runCommand(String input) {
        List<String> wordList;
        String output = "";
        String lowerCaseTrimmed = input.trim().toLowerCase();
        if (lowerCaseTrimmed.isBlank()) {
            output = "...Well?";
        } else {
            wordList = getWordList(lowerCaseTrimmed);
            wordList.forEach(System.out::println);
            try {
                Command parsedCommand = parseCommand(wordList, this.possibleCommands);
                runParsedCommand(parsedCommand);
            } catch (CommandNotFoundException e) {
                output = "Command not found";
            } catch (CommandNotPossibleException e) {
                output = "Command not possible";
            } catch (RoomNotFoundException e) {
                output = "Room not found";
            }
        }
        return output;
    }

    public void runParsedCommand(Command command) throws CommandNotPossibleException, RoomNotFoundException {
        switch (command.getVerb()) {
            case "move":
                move(command.getNoun());
                break;
            case "m":
                move(command.getNoun());
                break;
            case "show":
                show(command.getNoun());
                break;
            case "hide":
                hide(command.getNoun());
                break;
            case "pickup":
                pickup(command.getNoun());
                break;
            case "drop":
                drop(command.getNoun());
                break;
            case "drink":
                drink(command.getNoun());
                break;
            case "eat":
                eat(command.getNoun());
                break;
            case "fly":
                fly(command.getNoun());
                break;
            case "go":
                go(command.getNoun());
                break;
            case "quit":
                quit(command.getNoun());
                break;
            default:
                throw new CommandNotPossibleException();
        }
        checkEnergy();
    }

    private void go(String noun) throws RoomNotFoundException {
        if (noun.equals("home")) {
            hide("map");
            setupLevel(1, "Level 1: again", "Sheffield", 7, 7);
        }
    }

    private void quit(String noun) {
        if (noun.equals("game")) {
            System.exit(0);
        }
    }

    private void eat(String noun) throws RoomNotFoundException {
        if (noun.equals("fruit") || noun.equals("pizza") || noun.equals("croissant") || noun.equals("khinkali") || noun.equals("kumquat")) {
            if (!currentRoom.getItem().contains(noun)) {
                System.out.println("None to be found here, keep searching.");
            } else {
                System.out.println("You now have energy to continue your journey.");
                energyLevel++;
            }
        }
        if (noun.equals("mushroom")) {
            if (!currentRoom.getItem().contains("mushroom")) {
                System.out.println("No mushroom to be found here, keep searching at your peril.");
            } else {
                System.out.println("Bad luck. It was a toxic mushroom. You have been transported to A&E.");
                energyLevel--;
                visitedCell();
                moveRoom("the Northern General");
            }
        }
        if (noun.equals("rock")) {
            if (!currentRoom.getItem().contains("rock")) {
                System.out.println("No rock to be found here, keep searching at your peril.");
            } else {
                System.out.println("Bad luck. This rock is not a food, you're not at the seaside. Try again.");
                energyLevel--;
            }
        }

        if (noun.equals("egg") && currentRoom.getName().equals("Madagascar")) {
            System.out.println("You join Madagascar 2 and MOVE IT MOVE IT to Egypt \nGo to https://youtu.be/ApuFuuCJc3s?t=30 and join the party.");
            visitedCell();
            moveRoom("Egypt");
        }
    }

    private void drink(String noun) throws RoomNotFoundException {
        if (noun.equals("martini")) {
            if (!currentRoom.getItem().contains("martini")) {
                System.out.println("No martini to be found here, keep searching at your peril.");
            } else {
                System.out.println("Congratulations!!! You have woken up in the airport and you miraculously have " +
                        "everything that you need for travel. Time to fly away ...");
                visitedCell();
                moveRoom("the airport");
                itemsCollected.add("passport");
                itemsCollected.add("rucksack");
                itemsCollected.add("lacrosse-stick");
            }
        }
        if (noun.equals("beer") || noun.equals("moonshine")) {
            if (!currentRoom.getItem().contains(noun)) {
                System.out.println("None to be found here, keep searching.");
            } else {
                System.out.println("No disaster occurred. You now have less energy but you had great night :)");
                energyLevel--;
            }
        }
        if (noun.equals("water")) {
            if (!currentRoom.getItem().contains(noun)) {
                System.out.println("None to be found here, keep searching.");
            } else {
                System.out.println("Good idea. Trekking is thirsty work.");
            }
        }
    }

    private void drop(String noun) {
        if (!itemsCollected.contains(noun)) {
            System.out.println("You have to have picked it up before you can drop it.");
        } else {
            itemsCollected.remove(noun);
            System.out.println("To pick up the " + noun + " again you need to go back to where you first found it. " +
                    "You now only have " + itemsCollected);
        }
    }

    public void show(String noun) {
        if (noun.equals("map")) {
            boardWindow.showBoard();
        }
        if (noun.equals("help")) {
            System.out.println("All the possible commands in the game:");
            for (Map.Entry entry : possibleCommands.entrySet()) {
                System.out.println(entry);
            }
        }
        if (noun.equals("inventory")) {
            System.out.println("You have collected " + itemsCollected);
        }
        if (noun.equals("stuff")) {
            System.out.println("This room contains " + currentRoom.getItem());
        }
        if (noun.equals("energy")) {
            System.out.println("Your energy level is " + energyLevel);
        }
        if (noun.equals("directions")) {
            System.out.println("The directions possible from your current room are " + currentRoom.getPossDir());
        }
        if (noun.equals("unvisited")) {
            if (Level == 1) {
                System.out.println("You have " + (13 - visitedRooms.size()) + " rooms still to visit in this level.");
            }
            if (Level == 2) {
                System.out.println("You have " + (17 - visitedRooms.size()) + " rooms still to visit in this level.");
            }
        }
    }

    public void hide(String noun) {
        if (noun.equals("map")) {
            boardWindow.hideBoard();
        }
    }

    public Set<String> itemsCollected = new HashSet<>();

    private static Scanner scan = new Scanner(System.in);

    public void pickup(String noun) throws RoomNotFoundException {
        if (noun.equals("passport")) {
            if (!itemsCollected.contains("rucksack")) {
                System.out.println("You can't collect any items before you have found your rucksack.");
            } else if (itemsCollected.contains("passport")) {
                System.out.println("You already have a passport, you can't have two. No dual citizenship allowed in this game.");
            } else if (!currentRoom.getItem().contains("passport")) {
                System.out.println("No passport to be found here, keep searching.");
            } else {
                System.out.println("What is 2 + 2");
                String answer = scan.nextLine();
                if (answer.equals("4")) {
                    itemsCollected.add("passport");
                    System.out.println("You now have " + itemsCollected);
                } else {
                    System.out.println("Get better at maths and try again.");
                }
            }
        }

        if (noun.equals("rucksack")) {
            if (itemsCollected.contains("rucksack")) {
                System.out.println("You only have one back, you can't have two rucksacks.");
            } else if (!currentRoom.getItem().contains("rucksack")) {
                System.out.println("No rucksack to be found here, keep searching.");
            } else {
                itemsCollected.add("rucksack");
                System.out.println("You now have " + itemsCollected);
            }
        }

        if (noun.equals("lacrosse-stick")) {
            if (!itemsCollected.contains("rucksack")) {
                System.out.println("You can't collect any items before you have found your rucksack.");
            } else if (itemsCollected.contains("lacrosse-stick")) {
                System.out.println("You only need one lacrosse-stick.");
            } else if (!currentRoom.getItem().contains("lacrosse-stick")) {
                System.out.println("No lacrosse-stick to be found here, keep searching.");
            } else {
                itemsCollected.add("lacrosse-stick");
                System.out.println("You now have " + itemsCollected);
            }
        }

        if (noun.equals("bicycle")) {
            if (itemsCollected.contains("bicycle")) {
                System.out.println("You only need one bicycle.");
            } else if (!currentRoom.getItem().contains("bicycle")) {
                System.out.println("No bicycle to be found here, keep searching.");
            } else {
                itemsCollected.add("bicycle");
                System.out.println("You now have " + itemsCollected);
            }
        }

        if (noun.equals("supplies")) {
            if (!itemsCollected.contains("rucksack")) {
                System.out.println("You can't collect any items before you have found your rucksack.");
            } else if (itemsCollected.contains("supplies")) {
                System.out.println("You already have supplies, don't be greedy.");
            } else if (!currentRoom.getItem().contains("supplies")) {
                System.out.println("No supplies to be found here, keep searching.");
            } else {
                itemsCollected.add("supplies");
                energyLevel += 2;
                System.out.println("You now have " + itemsCollected);
            }
        }
        if (noun.equals("skis")) {
            if (itemsCollected.contains("skis")) {
                System.out.println("You only need two skis.");
            } else if (!currentRoom.getItem().contains("skis")) {
                System.out.println("No skis to be found here, keep searching.");
            } else {
                itemsCollected.add("skis");
                hide("map");
                System.out.println("Bad luck. You suffered a freak accident even picking up your skis. You are again in the Northern General.");
                setupLevel(1, "Level 1: again", "the Northern General", 7, 7);
                moveRoom("the Northern General");
            }
        }
        if (noun.equals("snorkel")) {
            if (itemsCollected.contains("snorkel")) {
                System.out.println("You only need one snorkel.");
            } else if (!currentRoom.getItem().contains("snorkel")) {
                System.out.println("No snorkel to be found here, keep searching.");
            } else {
                itemsCollected.add("snorkel");
                System.out.println("Turns out you're a really good swimmer and somehow manage to swim all the way to landlocked Nepal.");
                visitedCell();
                moveRoom("the Monkey Temple - Kathmandu");
                if (itemsCollected.contains("passport")) {
                    itemsCollected.remove("passport");
                }
            }
        }
        if (noun.equals("spider")) {
            if (!currentRoom.getItem().contains("spider")) {
                System.out.println("No spider to be found here, keep searching.");
            } else {
                energyLevel--;
                System.out.println("Opps, he wasn't that adorable after all. You used up some energy recovering from the poisonous bite.");
            }
        }
        if (noun.equals("jet")) {
            if (itemsCollected.contains("jet")) {
                System.out.println("You already have a jet, you can't have two.");
            } else if (!currentRoom.getItem().contains("jet")) {
                System.out.println("No jet to be found here, improve your negotiation skills.");
            } else {
                System.out.println("What is the Ukrainian national dish?");
                String answer = scan.nextLine();
                if (answer.equals("borscht")) {
                    itemsCollected.add("jet");
                    System.out.println("Amazing work. Your first mission was successful and Moscow is on fire. As a " +
                            "thank you for efforts, you are allowed to borrow the jet and fly to the Kennedy Space Center for a day out.");
                    visitedCell();
                    moveRoom("the Kennedy Space Center");
                } else {
                    System.out.println("Do some more research and try again.");
                }
            }
        }
    }

    public void fly(String noun) throws RoomNotFoundException {
        if (noun.equals("away") && currentRoom.getName().equals("the airport") && itemsCollected.contains("passport") && itemsCollected.contains("rucksack") && energyLevel >= 4) {
            System.out.println("Welcome to level 2");
            hide("map");
            setupLevel2();
            itemsCollected.remove("supplies");
            show("map");
        } else if (noun.equals("away") && currentRoom.getName().equals("the Kennedy Space Center") && !itemsCollected.contains("bicycle")) {
            setupLevel3();
        } else if (noun.equals("away") && currentRoom.getName().equals("the Kennedy Space Center") && itemsCollected.contains("bicycle")) {
            System.out.println("You can't take your bicycle to Space. Leave it on Earth for Drew to find.");
        } else {
            System.out.println("You need to have your rucksack, passport and at least 4 energy to go to the next level.");
        }
    }

    public void move(String noun) throws RoomNotFoundException, CommandNotPossibleException {
        int row = currentRoom.getRow();
        int column = currentRoom.getColumn();
        boolean isPossible = currentRoom.getPossDir().contains(noun);

        if (!isPossible) {
            throw new CommandNotPossibleException();
        }

        if (noun.equals("north") || noun.equals("n")) {
            row--;
        }
        if (noun.equals("south") || noun.equals("s")) {
            if ((currentRoom.getName().equals("Georgia")) && energyLevel <= 4) {
                System.out.println("You can't go to Peru without enough energy. The Machu Picchu trek is very demanding. Eat some more.");
            } else {
                row++;
            }
        }
        if (noun.equals("east") || noun.equals("e")) {
            if ((currentRoom.getName().equals("Peru")) && !(itemsCollected.contains("lacrosse-stick"))) {
                System.out.println("You can't go to your lacrosse game in California without your lacrosse stick. GO HOME to find it.");
            } else if ((currentRoom.getName().equals("the Netherlands")) && !(itemsCollected.contains("bicycle"))) {
                System.out.println("There are a lot of strikes and the only way of getting to France is to cycle. You must take Drew up on his offer.");
                return;
            } else if ((currentRoom.getName().equals("the Czech Republic")) && energyLevel <= 5) {
                System.out.println("You can't go to Ukraine without enough energy. You need to be brave like Ukrainians to proceed. Go back to France and eat some more.");
            } else {
                column++;
            }
        }
        if (noun.equals("west") || noun.equals("w")) {
            if ((currentRoom.getName().equals("Georgia")) && energyLevel <= 5) {
                System.out.println("You can't go to Ukraine without enough energy. You need to be brave like Ukrainians to proceed. Eat some more.");
            } else {
                column--;
            }
        }

        int finalRow = row;
        int finalColumn = column;
        Optional<Room> optionalRoom = roomArray.stream().filter(room -> room.getColumn() == finalColumn && room.getRow() == finalRow)
                .findFirst();
        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException();
        }
        visitedCell();
        currentRoom = optionalRoom.get();
        boardWindow.getBoard().setCell(currentRoom.getRow(), currentRoom.getColumn(), CellType.CURRENT_ROOM);
        boardWindow.repaint();
        System.out.println("You are now in " + currentRoom.getName());
        System.out.println(currentRoom.getDescription());
        currentRoom.setVisited(true);
        numberVisited();

        if (currentRoom.getName().equals("the Monkey Temple - Kathmandu") && itemsCollected.contains("passport")) {
            itemsCollected.remove("passport");
        }
        if (currentRoom.getName().equals("Australia")) {
            energyLevel -= 2;
        }
        if (currentRoom.getName().equals("Egypt")) {
            energyLevel--;
        }
    }
}
