import java.util.Set;

public class Room {

    public Room(int row, int column, Set<String> possDir, String name, String description, String roomItem, String roomImage, Boolean isVisited) {
        this.row = row;
        this.column = column;
        this.possDir = possDir;
        this.name = name;
        this.description = description;
        this.roomItem = roomItem;
        this.roomImage = roomImage;
        this.isVisited = isVisited;
    }
    private int row;

    private int column;

    private Set<String> possDir;

    private String name;

    private String description;

    private String roomImage;

    public Boolean getVisited() {
        return isVisited;
    }

    public void setVisited(Boolean visited) {
        isVisited = visited;
    }

    private Boolean isVisited;

    public String getRoomImage() {
        return roomImage;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set getPossDir() {
        return possDir;
    }

    public void setPossDir(Set possDir) {
        this.possDir = possDir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem() {
        return roomItem;
    }

    public void setItem(String item) {
        this.roomItem = item;
    }

    private String roomItem;

}
