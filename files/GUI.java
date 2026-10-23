package files;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class GUI extends JPanel {
    private final BufferedImage backGround;
    private final CopyOnWriteArrayList<GameObject> enabledObjects;

    public GUI() {
        enabledObjects = new CopyOnWriteArrayList<>();
        try {
            backGround = ImageIO.read(GUI.class.getResource("/resources/Images/background.png"));
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Nao encontrou o ficheiro de background", e);
        }
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(backGround, 0, 0, backGround.getWidth(), backGround.getHeight(), null);
        for (GameObject go : enabledObjects) {
            Shape s = go.shape();
            Ponto p = go.transform().position();
            if (!go.name().equals("parede"))
                g2.drawImage(s.img(), (int) p.getX(), (int) p.getY(), (int) (Main.windowWidth * 0.1), (int) (Main.windowHeigth * 0.1), null);
            else
                g2.drawImage(s.img(), (int) p.getX(), (int) p.getY(), 20, Main.windowHeigth * 2, null);
        }
        g2.setFont(new Font("Times New Roman", Font.LAYOUT_LEFT_TO_RIGHT, 20));
        g2.setColor(Color.white);
        g2.drawString("Score:", 10, 20);
    }

    public void render(ArrayList<GameObject> enabled) {
        enabledObjects.clear();
        enabledObjects.addAll(enabled);
        repaint();
    }
}
