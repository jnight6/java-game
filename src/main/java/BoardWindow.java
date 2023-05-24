import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class BoardWindow extends Canvas {

    private JFrame frame;
    private Board board;

    private String title;

    private int cellSize = 30;
    private Image gif;

    public CellType getState() {
        return state;
    }

    public void setState(CellType state) {
        this.state = state;
    }

    private static CellType state;

    private BufferedImage image;

    public BoardWindow(Board board, String title, int cellSize) {
        this.board = board;
        this.title = title;
        this.cellSize = cellSize;
        File file = new File("resources/guybrush.jpg");
        try {
            this.image = ImageIO.read(new File(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initBoardWindow();
    }

    private void initBoardWindow() {
        this.frame = new JFrame(this.title);
        this.frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setSize(board.getColumnCount() * cellSize, board.getRowCount() * cellSize);
        this.frame.add(this);
        this.frame.pack();
        this.frame.setVisible(false);
    }

    public void showBoard() {
        this.frame.setVisible(true);
    }

    public void hideBoard() {
        this.frame.setVisible(false);
    }

    private void drawCell(int row, int column, Color color, Graphics g) {
        g.setColor(color);
        g.fillRect(column * cellSize, row * cellSize, cellSize, cellSize);
    }

    private void drawChar(int row, int column, Graphics g) {
        g.drawImage(this.image, column * cellSize, row * cellSize, cellSize, cellSize, this);
    }

    private void drawImg(int row, int column, Graphics g, String imagePath) {
        File file = new File(imagePath);
        BufferedImage imageToDraw = null;
        try {
            imageToDraw = ImageIO.read(new File(file.getAbsolutePath()));
            g.drawImage(imageToDraw, column * cellSize, row * cellSize, cellSize, cellSize, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                Cell cell = board.getCells()[i][j];
                switch (cell.getCellType()) {
                    case VISITED:
                        if (!cell.getImagePath().equals("i")) {
                            drawImg(i, j, g, cell.getImagePath());
                        } else {
                            drawCell(i, j, new Color(0xCAF0F8), g);
                        }
                        break;
                    case CURRENT_ROOM:
                        drawChar(i, j, g);
                        break;
                    case START:
                        drawChar(i, j, g);
                        break;
                    case EMPTY:
                        drawCell(i, j, new Color(0x3C6255), g);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
