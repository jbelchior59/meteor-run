package files;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * The engine of the game, it's here that every calculation occurs and then is transmitted to the GUI
 * @author POO2425p1g4
 * @version 01/04/2025
 * @inv none
 */
public class GameEngine implements KeyListener, Runnable{
    private HashMap<Integer, GameObject> objects;
    private final int n, mode;
    private int hashcodePlayer1, hashcodePlayer2;
    private GUI g;
    private Behaviour p1, p2;
    private final Set<Integer> keysPressed = new HashSet<>();

    public GameEngine(int n, int hashcodePlayer, int mode) {
        assert(n >= 0);
        this.n = n;
        this.objects = new HashMap<>(n);
        this.g = new GUI();
        this.hashcodePlayer1 = hashcodePlayer;
        this.hashcodePlayer2 = 0;
        this.mode = mode;
        g.addKeyListener(this);
    }

    public void start(){
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public void setHashcodePlayer2(int hashcodePlayer2){this.hashcodePlayer2 = hashcodePlayer2;}
    public void add(GameObject go){objects.put(go.hashCode(), go);}
    public void destroy(GameObject go){objects.remove(go.hashCode());}

    @Override
    public String toString(){ return objects.toString();}

    public ArrayList<GameObject> enabled(){
        ArrayList<GameObject> enabled = new ArrayList<>(this.objects.size());
        objects.forEach((i, go) -> enabled.add(go));
        return enabled;
    }

    private void singlePlayer() {
        Set<Integer> index;
        objects.forEach((i, go) -> {
            ITransform t;
            t = go.transform();
            if (go.name().compareTo("nave") == 0) {
                t.move(Main.spawnPoint, 0);
                p1 = new Behaviour(go);
            } else {
                if (t.layer() == 2) t.move(new Ponto(Main.windowWidth - 30, -Main.windowHeigth / 2.0), -1);
                else t.move(new Ponto(-10, -Main.windowHeigth / 2), 0);
            }
            go.setTranform((Transform) t);
        });
        while(true) {
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            objects.replace(hashcodePlayer1, p1.moveSet(keysPressed));
            index = objects.keySet();
            for (int i : index) {
                GameObject go1 = objects.get(i);
                GameObject[] colisions = new GameObject[objects.size()];
                colisions[0] = go1;
                int length = 1;
                for (int j : index) {
                    GameObject go2 = objects.get(j);
                    if (i == j || go1 == null || go2 == null) continue;
                    if (go1.checkCollider(go2.collider(0)) && go2.checkCollider(go1.collider(1))) ;
                }
            }
            this.g.render(this.enabled());
        }
    }

    @Override
    public void run(){
        if(mode == 0){
            singlePlayer();
        }else if(mode == 1){

        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public Container getGUI(){return g;}
}
