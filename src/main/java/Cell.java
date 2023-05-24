public class Cell {

  private final int row;
  private final int column;
  private CellType cellType = CellType.EMPTY;

  private String imagePath = null;

  public Cell(int row, int column, String imagePath) {
    this.row = row;
    this.column = column;
    this.imagePath = imagePath;
  }

  public Cell(int row, int column) {
    this.row = row;
    this.column = column;
  }

  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }

  public CellType getCellType() {
    return cellType;
  }

  public void setCellType(CellType cellType) {
    this.cellType = cellType;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }
}
