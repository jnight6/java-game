import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandMap {

    public static Map<String, List<String>> getPossibleCommands() {
        Map<String, List<String>> possibleCommands = new HashMap<>();
        possibleCommands.put("move", List.of("north", "south", "east", "west", "n", "s", "e", "w"));
        possibleCommands.put("m", List.of("n", "s", "e", "w"));
        possibleCommands.put("pickup", List.of("rucksack", "passport", "bicycle", "lacrosse-stick", "supplies", "snorkel", "bicycle", "spider", "skis", "jet"));
        possibleCommands.put("drop", List.of("rucksack", "passport", "bicycle", "lacrosse-stick", "supplies", "snorkel", "bicycle", "spider", "skis", "jet"));
        possibleCommands.put("eat", List.of("fruit", "mushroom", "pizza", "croissant", "khinkali", "kumquat", "egg", "rock"));
        possibleCommands.put("drink", List.of("water", "beer", "moonshine", "martini"));
        possibleCommands.put("show", List.of("map", "help", "inventory", "stuff", "energy", "directions", "unvisited"));
        possibleCommands.put("hide", List.of("map"));
        possibleCommands.put("fly", List.of("away"));
        possibleCommands.put("go", List.of("home"));
        possibleCommands.put("quit", List.of("game"));
        return possibleCommands;
    }
}

