package files;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Classe responsável pela exibição gráfica e interface do usuário do jogo
 * @author poo2425p1g4
 * @version 1.0 01/06/2025
 * @inv O background deve sempre ser renderizado corretamente e os objetos habilitados devem ser exibidos conforme seu estado atual
 * @see javax.swing.JPanel
 * @see java.awt.Graphics
 */
public class GUI extends JPanel {
    private final BufferedImage backGround;
    private CopyOnWriteArrayList<GameObject> enabledObjects;
    private int score = 0, mode;
    private int backgroundY = 0, hp1 = 300, hp2 = 300;
    private final int scrollSpeed = 2;
    private int backgroundOffset = 0;

    /**
     * Constrói a interface gráfica do jogo
     * @param mode Modo de jogo (0 = single player, 1 = multiplayer)
     * @throws RuntimeException Se não conseguir carregar a imagem de background
     */
    public GUI(int mode) {
        enabledObjects = new CopyOnWriteArrayList<>();
        try{
            backGround = ImageIO.read(GUI.class.getResource("/resources/Images/background.png"));
        }catch(IOException e) {throw new RuntimeException("Nao encontrou o ficheiro");}

        setBackground(Color.BLACK);
        this.mode = mode;
    }

    public void setScore(){this.score++;}
    public int getScore(){return this.score;}
    public void setHp1(int damage){this.hp1 -= damage;}
    public int getHp1(){return this.hp1;}
    public void setHp2(int damage){this.hp2 -= damage;}
    public int getHp2(){return this.hp2;}

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int bgHeight = backGround.getHeight();
        int y = backgroundOffset % bgHeight;
        g2.drawImage(backGround, 0, y - bgHeight, backGround.getWidth(), bgHeight, null);
        g2.drawImage(backGround, 0, y, backGround.getWidth(), bgHeight, null);

        for(GameObject go : enabledObjects){
            Shape s = go.shape();
            Ponto p = go.transform().position();
            if(go.name().compareTo("parede") != 0)g2.drawImage(s.img(), (int) p.getX(), (int) p.getY(),(int) (Main.windowWidth * 0.1), (int) (Main.windowHeigth * 0.1), null);
            else g2.drawImage(s.img(), (int) p.getX(), (int) p.getY(), 20, Main.windowHeigth*2, null);
        }

        if(mode == 0) {
            g2.setFont(new Font("Times New Roman", Font.LAYOUT_LEFT_TO_RIGHT, 20));
            g2.setColor(Color.white);
            g2.drawString(String.format("Score: %s", score), 10, 20);
            g2.drawString(String.format("HP: %d", hp1), Main.windowWidth - 90, 20);
        }else if(mode == 1){
            g2.setFont(new Font("Times New Roman", Font.LAYOUT_LEFT_TO_RIGHT, 20));
            g2.setColor(Color.white);
            g2.drawString(String.format("HP: %d", hp1), 10, 20);
            g2.drawString(String.format("HP: %d", hp2), Main.windowWidth - 90, 20);
        }
    }

    public void updateBackground() {
        backgroundOffset += 2;
    }

    public void render(ArrayList<GameObject> enabled){
        enabledObjects.clear();
        enabledObjects.addAll(enabled);
        repaint();
    }
}
