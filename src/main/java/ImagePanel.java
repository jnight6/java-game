import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {

  private Image image;

  private JFrame frame;

  public ImagePanel() {
    this.frame = new JFrame();
    this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    File gifFile = new File("resources/space.gif");
    this.image = new ImageIcon(String.valueOf(new File(gifFile.getAbsolutePath()))).getImage();
    this.repaint();
    this.frame.add(this);
    this.frame.pack();
    this.frame.setSize(this.image.getWidth(this), this.image.getHeight(this));
    this.frame.setVisible(true);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(this.image, 0, 0, this);
  }
}


