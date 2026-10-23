package files;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;

public class Behaviour implements KeyListener {
    private final GameObject go;
    private static final double MOVE_SPEED = 5.0;

    public Behaviour(GameObject go){
        this.go = go;
    }

    public GameObject moveSet(Set<Integer> keys){
        Transform t = (Transform) go.transform();
        if (keys.contains(KeyEvent.VK_A)) t.move(new Ponto(-MOVE_SPEED, 0), 0);
        if (keys.contains(KeyEvent.VK_D)) t.move(new Ponto(MOVE_SPEED, 0), 0);
        if (keys.contains(KeyEvent.VK_W)) t.move(new Ponto(0, -MOVE_SPEED), 0);
        if (keys.contains(KeyEvent.VK_S)) t.move(new Ponto(0, MOVE_SPEED), 0);
        go.setTranform(t);
        return go;
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
