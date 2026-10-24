package files;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends JPanel {
    protected JFrame jf = new JFrame();
    private GameEngine ge;
    protected Clip starterClip, backgroundMusic;
    public final static int windowWidth = 600, windowHeigth = 550;
    public final static Ponto spawnPoint = new Ponto(200, 400);

    public Main(){}

    private void mainMenu() throws IOException {
        JPanel jp = new JPanel(){
            private BufferedImage backGround = ImageIO.read(Main.class.getResource("/resources/Images/background.png"));

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(backGround, 0 , 0, backGround.getWidth(), backGround.getHeight(), null);
            }
        };
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

        JLabel jl = new JLabel("Meteor Run");
        JButton single = new JButton("SinglePlayer"), exit = new JButton("Exit Game"), multi = new JButton("Multiplayer");
        jf.setSize((int) windowWidth, (int) windowHeigth);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jl.setAlignmentX(Component.CENTER_ALIGNMENT);
        jl.setFont(new Font("Pacifico", Font.BOLD, 50));
        jl.setForeground(Color.YELLOW);
        jl.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        jp.add(jl);

        single.setAlignmentX(Component.CENTER_ALIGNMENT);
        single.setMaximumSize(new Dimension(200, 60));
        single.setBorder(BorderFactory.createEmptyBorder(30, 0, 30,0));
        single.addActionListener(e -> startGame(0));
        jp.add(single);
        jp.add(Box.createRigidArea(new Dimension(0, 30)));

        multi.setAlignmentX(Component.CENTER_ALIGNMENT);
        multi.setMaximumSize(new Dimension(200, 60));
        multi.setBorder(BorderFactory.createEmptyBorder(30, 0, 30,0));
        multi.addActionListener(e -> startGame(1));
        jp.add(multi);
        jp.add(Box.createRigidArea(new Dimension(0, 30)));

        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setMaximumSize(new Dimension(200, 60));
        exit.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        exit.addActionListener(e -> System.exit(0));
        jp.add(exit);

        jf.add(jp);
        try {
            AudioInputStream startStream = AudioSystem.getAudioInputStream(
                    Main.class.getResource("/resources/Sounds/starter_music.wav")
            );
            AudioInputStream backgroundStream = AudioSystem.getAudioInputStream(
                    Main.class.getResource("/resources/Sounds/background_music.wav")
            );
            starterClip = AudioSystem.getClip();
            starterClip.open(startStream);
            starterClip.loop(Clip.LOOP_CONTINUOUSLY);
            starterClip.start();
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(backgroundStream);
        } catch (Exception e) {
            System.err.println("Erro ao carregar starter_music: " + e.getMessage());
        }

        jf.setVisible(true);
    }

    private void startGame(int mode){
        GameObject wall1 = new GameObject(String.format("parede\n0 0 1 0 0\n-30 0 30 0 30 %s -30 %s", Main.windowHeigth, Main.windowHeigth));
        GameObject wall2 = new GameObject(String.format("parede\n0 0 2 0 0\n-0 0 30 0 30 %s -30 %s", Main.windowHeigth, Main.windowHeigth));
        jf.getContentPane().removeAll();
        if (starterClip != null) starterClip.stop();
        if (backgroundMusic != null) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        }
        if(mode == 0){
            String s = "nave\n0 0 1 0 0\n0 -8 -8 8 8 8";
            GameObject go = new GameObject(s);
            ge = new GameEngine(3, go.hashCode(), 0);
            ge.add(go);
            ge.add(wall1);
            ge.add(wall2);
            jf.setContentPane(ge.getGUI());
            jf.revalidate();
            jf.repaint();
            ge.start();
        }else{
            String p1 = "nave\n0 0 1 0 0\n0 -8 -8 8 8 8";
            String p2 = "nave2\n0 0 1 0 0\n0 -8 -8 8 8 8";
            GameObject go1 = new GameObject(p1), go2 = new GameObject(p2);
            ge = new GameEngine(5, go1.hashCode(), 1);
            ge.setHashcodePlayer2(go2.hashCode());
            ge.add(go1);
            ge.add(go2);
            ge.add(wall1);
            ge.add(wall2);
            jf.setContentPane(ge.getGUI());
            jf.revalidate();
            jf.repaint();
            ge.start();
        }
    }

    public static void winScreen() throws IOException {
        shutdownGame();
        Main m = new Main();
        JFrame jf = new JFrame();
        JPanel jp = new JPanel(){
            private BufferedImage backGround = ImageIO.read(Main.class.getResource("/resources/Images/background.png"));
            private BufferedImage naveMae = ImageIO.read(Main.class.getResource("/resources/Images/nave_mae.png"));
            private BufferedImage nave = ImageIO.read(Main.class.getResource("/resources/Images/nave.png"));

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(backGround, 0 , 0, backGround.getWidth(), backGround.getHeight(), null);
                g2.drawImage(naveMae, 50, 20, (int)(windowWidth*0.1), (int)(windowHeigth*0.1), null);
                g2.drawImage(nave, 50, 100, (int)(windowWidth*0.05), (int)(windowHeigth*0.05), null);
            }
        };
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

        JLabel jl = new JLabel("Parabens");
        JButton single = new JButton("Main Menu"), exit = new JButton("Exit Game");
        jf.setSize((int) windowWidth, (int) windowHeigth);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jl.setAlignmentX(Component.CENTER_ALIGNMENT);
        jl.setFont(new Font("Pacifico", Font.BOLD, 50));
        jl.setForeground(Color.YELLOW);
        jl.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        jp.add(jl);

        single.setAlignmentX(Component.CENTER_ALIGNMENT);
        single.setMaximumSize(new Dimension(200, 60));
        single.setBorder(BorderFactory.createEmptyBorder(30, 0, 30,0));
        single.addActionListener(e -> {
            jf.dispose();
            cleanAndShowMenu();
        });
        jp.add(single);
        jp.add(Box.createRigidArea(new Dimension(0, 30)));

        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setMaximumSize(new Dimension(200, 60));
        exit.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        exit.addActionListener(e -> System.exit(0));
        jp.add(exit);

        jf.add(jp);
        jf.setVisible(true);
    }

    public static void winScreenMulti(int ctrl) throws IOException {
        shutdownGame();
        Main m = new Main();
        JFrame jf = new JFrame();
        JPanel jp = new JPanel(){
            private BufferedImage backGround = ImageIO.read(Main.class.getResource("/resources/Images/background.png"));
            private BufferedImage nave2 = ImageIO.read(Main.class.getResource("/resources/Images/nave2.png"));
            private BufferedImage nave = ImageIO.read(Main.class.getResource("/resources/Images/nave.png"));

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(backGround, 0 , 0, backGround.getWidth(), backGround.getHeight(), null);
                g2.drawImage(nave2, 200, 20, (int)(windowWidth*0.1), (int)(windowHeigth*0.1), null);
                g2.drawImage(nave, 200, 460, (int)(windowWidth*0.05), (int)(windowHeigth*0.05), null);
            }
        };
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

        JLabel jl = new JLabel(String.format("PARABENS Player %d", ctrl));
        JButton single = new JButton("Main Menu"), exit = new JButton("Exit Game");
        jf.setSize((int) windowWidth, (int) windowHeigth);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jl.setAlignmentX(Component.CENTER_ALIGNMENT);
        jl.setFont(new Font("Pacifico", Font.BOLD, 50));
        jl.setForeground(Color.YELLOW);
        jl.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        jp.add(jl);

        single.setAlignmentX(Component.CENTER_ALIGNMENT);
        single.setMaximumSize(new Dimension(200, 60));
        single.setBorder(BorderFactory.createEmptyBorder(30, 0, 30,0));
        single.addActionListener(e -> {
            jf.dispose();
            cleanAndShowMenu();
        });
        jp.add(single);
        jp.add(Box.createRigidArea(new Dimension(0, 30)));

        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setMaximumSize(new Dimension(200, 60));
        exit.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        exit.addActionListener(e -> System.exit(0));
        jp.add(exit);

        jf.add(jp);
        jf.setVisible(true);
    }

    public static void gameOverScreen() throws IOException {
        shutdownGame();

        Main m = new Main();
        JFrame jf = new JFrame();
        JPanel jp = new JPanel(){
            private BufferedImage backGround = ImageIO.read(Main.class.getResource("/resources/Images/background.png"));

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(backGround, 0 , 0, backGround.getWidth(), backGround.getHeight(), null);
            }
        };
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

        JLabel jl = new JLabel("MORREU");
        JButton single = new JButton("Main Menu"), exit = new JButton("Exit Game");
        jf.setSize((int) windowWidth, (int) windowHeigth);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jl.setAlignmentX(Component.CENTER_ALIGNMENT);
        jl.setFont(new Font("Pacifico", Font.BOLD, 50));
        jl.setForeground(Color.YELLOW);
        jl.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        jp.add(jl);

        single.setAlignmentX(Component.CENTER_ALIGNMENT);
        single.setMaximumSize(new Dimension(200, 60));
        single.setBorder(BorderFactory.createEmptyBorder(30, 0, 30,0));
        single.addActionListener(e -> {
            jf.dispose();
            cleanAndShowMenu();
        });
        jp.add(single);
        jp.add(Box.createRigidArea(new Dimension(0, 30)));

        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setMaximumSize(new Dimension(200, 60));
        exit.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        exit.addActionListener(e -> System.exit(0));
        jp.add(exit);

        jf.add(jp);
        jf.setVisible(true);
    }

    private static void shutdownGame() {
        Thread.getAllStackTraces().keySet().stream()
                .filter(t -> t.getName().startsWith("Game-"))
                .forEach(Thread::interrupt);
    }

    private static void cleanAndShowMenu() {
        System.gc();
        EventQueue.invokeLater(() -> {
            try {
                new Main().mainMenu();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws IOException {
        Main m = new Main();
        m.mainMenu();
    }
}
