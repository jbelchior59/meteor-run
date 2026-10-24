package files;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
public class GameEngine extends Main implements KeyListener, Runnable {
    private ConcurrentHashMap<Integer, GameObject> objects;
    private final int n, mode;
    private int hashcodePlayer1, hashcodePlayer2;
    private GUI g;
    private Behaviour p1, p2;
    private final Set<Integer> keysPressed = new HashSet<>();
    private ScheduledExecutorService enemyScheduler;
    private Queue<GameObject> enemyBullets = new ConcurrentLinkedQueue<>();
    public GameEngine(int n, int hashcodePlayer, int mode) {
        assert(n >= 0);
        this.n = n;
        this.objects = new ConcurrentHashMap<>(n);
        this.g = new GUI(mode);
        this.hashcodePlayer1 = hashcodePlayer;
        this.hashcodePlayer2 = 0;
        this.mode = mode;
        this.enemyScheduler = Executors.newScheduledThreadPool(1);
    }
    public void start() {
        Thread gameThread = new Thread(this, "Game-Engine");
        gameThread.start();
    }
    public void setHashcodePlayer2(int hashcodePlayer2) {
        this.hashcodePlayer2 = hashcodePlayer2;
    }
    public void add(GameObject go) {
        synchronized (objects) {
            objects.put(go.hashCode(), go);
        }
    }
    public void destroy(GameObject go) {
        objects.remove(go.hashCode());
    }
    public ArrayList<GameObject> enabled() {
        return new ArrayList<>(objects.values());
    }
    private void spawnRandomPowerUp(Ponto position) {
        Random r = new Random();
        try {
            PowerUp.Type[] types = PowerUp.Type.values();
            PowerUp powerUp = new PowerUp(types[r.nextInt(types.length)],
                    position.getX(), position.getY());
            add(powerUp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void singlePlayer() {
        Queue<GameObject> enemies = new ConcurrentLinkedQueue<>();
        Set<String> npcTypes = Set.of("nave_inimiga", "nave_boss", "asteroid", "laser", "laser_inimigo");
        new Timer(8000, e -> {
            for (int i = 0; i < 2; i++) {
                try {
                    enemies.add(createRandomEnemy());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        new Timer(1000, e -> g.setScore()).start();
        comunParts();
        enemyScheduler.scheduleAtFixedRate(() -> {
            objects.values().stream()
                    .filter(go -> go.name().equals("nave_inimiga") || go.name().equals("nave_boss"))
                    .forEach(enemy -> {
                        GameObject bullet = p1.shoot(enemy);
                        enemyBullets.add(bullet);
                    });
        }, 0, 2, TimeUnit.SECONDS);
        while(true) {
            try {
                Thread.sleep(16);
                GameObject playerAction = p1.moveSet(keysPressed);
                if (playerAction != null && playerAction.name().equals("laser")) {
                    add(playerAction);
                }
                objects.forEach((i, go) -> {
                    if(npcTypes.contains(go.name())) {
                        GameObject tiro = p1.npcMoves(go);
                        if (tiro != null && tiro.name().equals("laser_inimigo")) {
                            add(tiro);
                        }
                    }
                });
                while(!enemies.isEmpty()) {
                    GameObject enemy = enemies.poll();
                    if(enemy != null) add(enemy);
                }
                while(!enemyBullets.isEmpty()) {
                    GameObject bullet = enemyBullets.poll();
                    add(bullet);
                }
                synchronized (objects) {
                    objects.forEach((i, go) -> {
                        if(npcTypes.contains(go.name())) {
                            GameObject gos = p1.npcMoves(go);
                            if (gos != null) {
                                go.setTranform((Transform) gos.transform());
                            }
                        }
                    });
                    GameObject player = p1.moveSet(keysPressed);
                    if(player.name().equals("nave")) {
                        objects.replace(hashcodePlayer1, player);
                    }
                    removeOutOfBoundsObjects();
                    colisaoSinglePlayer();
                    this.g.updateBackground();
                    this.g.render(this.enabled());
                    if(objects.get(hashcodePlayer1).getHp() <= 0){
                        jf.dispose();
                        Main.gameOverScreen();
                        if (backgroundMusic != null) {
                            backgroundMusic.stop();
                        }
                        return;
                    }
                    if(this.g.getScore() == 240){
                        jf.dispose();
                        Main.winScreen();
                        if (backgroundMusic != null) {
                            backgroundMusic.stop();
                        }
                        return;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (GameObject go : objects.values()) {
                if (go.name().equals("speed_nave") || go.name().equals("shield") || go.name().equals("speed_misseis")) {
                    Transform t = (Transform) go.transform();
                    t.move(new Ponto(0, 2), 0);
                    go.setTranform(t);
                }
            }
        }
    }
    private void colisaoSinglePlayer() {
        GameObject go1 = objects.get(hashcodePlayer1);
        Transform t = (Transform) go1.transform();
        outOfBounds();
        for(GameObject go : objects.values()) {
            if(go.hashCode() == go1.hashCode()) continue;
            if(!go.name().equals("parede")) {
                if(go.checkCollider(go1.collider(1))) {
                    collisionLogic(go);
                }
            }
            if (go.name().equals("laser") && ((Transform)go.transform()).angle() == 90) {
                for (GameObject enemy : objects.values()) {
                    if ((enemy.name().equals("nave_inimiga") || enemy.name().equals("nave_boss")) &&
                            enemy.checkCollider(go.collider(0))) {
                        enemy.getDamage(50);
                        if (enemy.getHp() <= 0) {
                            if (Math.random() < 0.5) {
                                spawnRandomPowerUp(enemy.transform().position());
                            }
                            destroy(enemy);
                        }
                        destroy(go);
                        break;
                    }
                }
                for (GameObject laser : objects.values()) {
                    if (laser.name().equals("laser")) {
                        for (GameObject asteroid : objects.values()) {
                            if (asteroid.name().equals("asteroid") &&
                                    asteroid.checkCollider(laser.collider(0))) {
                                asteroid.getDamage(40);
                                destroy(laser);
                                if (asteroid.getHp() <= 0) {
                                    if (Math.random() < 0.3) {
                                        spawnRandomPowerUp(asteroid.transform().position());
                                    }
                                    destroy(asteroid);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    private void collisionLogic(GameObject go) {
        GameObject player = objects.get(hashcodePlayer1);
        if (go instanceof PowerUp) {
            PowerUp powerUp = (PowerUp) go;
            powerUp.applyEffect(player);
            destroy(go);
            return;
        }
        if (player.hasShield() && player.getShieldHits() > 0) {
            player.setShieldHits(player.getShieldHits() - 1);
            if (player.getShieldHits() <= 0) {
                player.setShield(false);
            }
            destroy(go);
            return;
        }
        if (go.name().equals("speed_nave") || go.name().equals("speed_misseis") || go.name().equals("shield")) {
            if (go instanceof PowerUp) {
                PowerUp powerUp = (PowerUp) go;
                powerUp.applyEffect(player);
                destroy(go);
                return;
            }
        }
        if (go.name().equals("laser") && ((Transform)go.transform()).angle() == 90) {
            for (GameObject enemy : objects.values()) {
                if ((enemy.name().equals("nave_inimiga") || enemy.name().equals("nave_boss")) &&
                        enemy.checkCollider(go.collider(0))) {
                    enemy.getDamage(30);
                    if (enemy.getHp() <= 0) {
                        if (Math.random() < 0.5) {
                            spawnRandomPowerUp(enemy.transform().position());
                        }
                        destroy(enemy);
                    }
                    destroy(go);
                    break;
                }
            }
        }
        else if (go.name().equals("laser") && ((Transform)go.transform()).angle() == 90) {
            for (GameObject obj : objects.values()) {
                if (obj.name().equals("asteroid") && obj.checkCollider(go.collider(0))) {
                    obj.getDamage(40);
                    destroy(go);
                    if (obj.getHp() <= 0) {
                        if (Math.random() < 0.5) {
                            spawnRandomPowerUp(obj.transform().position());
                        }
                        destroy(obj);
                    }
                    break;
                }
            }
        }
        GameObject go1 = objects.get(hashcodePlayer1);
        switch (go.name()) {
            case "laser_inimigo":
                player.getDamage(30);
                this.g.setHp1(30);
                destroy(go);
                break;
            case "nave_inimiga":
                player.getDamage(20);
                this.g.setHp1(20);
                go.getDamage(50);
                if (go.getHp() <= 0) {
                    if (Math.random() < 0.5) {
                        spawnRandomPowerUp(go.transform().position());
                    }
                    destroy(go);
                }
                break;
            case "nave_boss":
                player.getDamage(40);
                this.g.setHp1(40);
                go.getDamage(20);
                if (go.getHp() <= 0) {
                    if (Math.random() < 0.5) {
                        spawnRandomPowerUp(go.transform().position());
                    }
                    destroy(go);
                }
                break;
            case "asteroid":
                go1.getDamage(5);
                this.g.setHp1(5);
                Transform t = (Transform) go1.transform();
                t.move(new Ponto(3, 20), 0);
                go1.setTranform(t);
                if (go.getHp() <= 0) {
                    if (Math.random() < 0.5) {
                        spawnRandomPowerUp(go.transform().position());
                    }
                    destroy(go);
                }
                break;
        }
    }
    private void multiplayer() {
        Set<String> movingObjects = Set.of("nave_inimiga", "nave_boss", "asteroid", "laser", "laser_inimigo");
        comunParts();
        this.enemyScheduler = Executors.newScheduledThreadPool(1);
        while(true) {
            try {
                Thread.sleep(16);
                GameObject player1Shot = p1.moveSet(keysPressed);
                if (player1Shot != null && player1Shot.name().equals("laser")) {
                    add(player1Shot);
                }
                GameObject player2Shot = p2.moveSet(keysPressed);
                if (player2Shot != null && player2Shot.name().equals("laser")) {
                    add(player2Shot);
                }
                while(!enemyBullets.isEmpty()) {
                    GameObject bullet = enemyBullets.poll();
                    add(bullet);
                }
                synchronized (objects) {
                    objects.forEach((id, go) -> {
                        if (movingObjects.contains(go.name())) {
                            GameObject updated = p1.npcMoves(go);
                            if (!updated.equals(go)) {
                                objects.replace(id, updated);
                            }
                        }
                    });
                    objects.replace(hashcodePlayer1, p1.moveSet(keysPressed));
                    objects.replace(hashcodePlayer2, p2.moveSet(keysPressed));
                    removeOutOfBoundsObjects();
                    colisaoMultiplayer();
                    g.updateBackground();
                    g.render(enabled());
                    if(this.g.getHp1() <= 0){
                        jf.dispose();
                        Main.winScreenMulti(2);
                        if (backgroundMusic != null) {
                            backgroundMusic.stop();
                        }
                        return;
                    }
                    if(this.g.getHp2() <= 0){
                        jf.dispose();
                        Main.winScreenMulti(1);
                        if (backgroundMusic != null) {
                            backgroundMusic.stop();
                        }
                        return;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void colisaoMultiplayer() {
        GameObject go1 = objects.get(hashcodePlayer1), go2 = objects.get(hashcodePlayer2);
        outOfBounds();
        if(go1.checkCollider(go2.collider(1))) {
            go1.getDamage(10);
            this.g.setHp1(10);
            go2.getDamage(10);
            this.g.setHp2(10);
        }
        for(GameObject go : objects.values()) {
            if(go.hashCode() == go1.hashCode() || go.hashCode() == go2.hashCode()) continue;
            if(go.checkCollider(go1.collider(1))) {
                go1.getDamage(30);
                this.g.setHp1(30);
                destroy(go);
            }
            if(go.checkCollider(go2.collider(1))) {
                go2.getDamage(30);
                this.g.setHp2(30);
                destroy(go);
            }
        }
    }
    public void outOfBounds() {
        GameObject go1 = objects.get(hashcodePlayer1);
        Transform t1 = (Transform) go1.transform();
        if(t1.position().getX() < 10) t1.move(new Ponto(10, 0), 0);
        else if(t1.position().getX() > Main.windowWidth - 70) t1.move(new Ponto(-10, 0), 0);
        if(t1.position().getY() < 10) t1.move(new Ponto(0, 10), 0);
        else if(t1.position().getY() > Main.windowHeigth - 70) t1.move(new Ponto(0, -10), 0);
        go1.setTranform(t1);
        if(this.mode == 1) {
            GameObject go2 = objects.get(hashcodePlayer2);
            Transform t2 = (Transform) go2.transform();
            if(t2.position().getX() < 10) t2.move(new Ponto(10, 0), 0);
            else if(t2.position().getX() > Main.windowWidth - 70) t2.move(new Ponto(-10, 0), 0);
            if(t2.position().getY() < 10) t2.move(new Ponto(0, 10), 0);
            else if(t2.position().getY() > Main.windowHeigth - 70) t2.move(new Ponto(0, -10), 0);
            go2.setTranform(t2);
        }
    }
    public GameObject createRandomEnemy() throws IOException {
        Random r = new Random();
        double x = 100 + r.nextDouble() * (Main.windowWidth - 200);
        double y = -100;
        double scale = 0.8 + r.nextDouble() * 0.5;
        String enemyShip;
        if (r.nextInt(10) < 3) {
            enemyShip = "asteroid\n" + x + " " + y + " 0 0.0 " + scale + "\n" +
                    "-20.0 -20.0 20.0 -20.0 20.0 20.0 -20.0 20.0";
        } else if (r.nextInt(10) > 7) {
            enemyShip = "nave_boss\n" + x + " " + y + " 0 0.0 " + scale + "\n" +
                    "-20.0 -20.0 20.0 -20.0 20.0 20.0 -20.0 20.0";
        } else {
            enemyShip = "nave_inimiga\n" + x + " " + y + " 0 0.0 " + scale + "\n" +
                    "-20.0 -20.0 20.0 -20.0 20.0 20.0 -20.0 20.0";
        }
        GameObject enemy = new GameObject(enemyShip);
        Transform t = (Transform) enemy.transform();
        t.rotate(90);
        enemy.setTranform(t);
        enemy.setLastShotTime(System.currentTimeMillis());
        return enemy;
    }
    @Override
    public void run() {
        g.setFocusable(true);
        g.requestFocusInWindow();
        g.addKeyListener(this);
        if (mode == 0) {
            p1 = new Behaviour(objects.get(hashcodePlayer1));
            singlePlayer();
        } else if (mode == 1) {
            p1 = new Behaviour(objects.get(hashcodePlayer1));
            p2 = new Behaviour(objects.get(hashcodePlayer2));
            multiplayer();
        }
        enemyScheduler.shutdown();
    }
    private void comunParts() {
        objects.forEach((i, go) -> {
            ITransform t = go.transform();
            if (go.name().compareTo("nave") == 0) {
                t.move(Main.spawnPoint, 0);
                t.rotate(90);
                p1 = new Behaviour(go);
            } else if (go.name().compareTo("nave2") == 0) {
                t.move(new Ponto(Main.windowWidth - 200, 150), 0);
                t.rotate(270);
                p2 = new Behaviour(go);
            } else {
                if (t.layer() == 2) t.move(new Ponto(Main.windowWidth - 30, -Main.windowHeigth / 2.0), -1);
                else t.move(new Ponto(-10, -Main.windowHeigth / 2), 0);
            }
            go.setTranform((Transform) t);
        });
    }
    Set<String> npcTypes = Set.of("nave_inimiga", "nave_boss", "asteroid", "laser", "laser_inimigo", "speed_nave", "speed_misseis", "shield");
    Set<String> movingObjects = Set.of("nave_inimiga", "nave_boss", "asteroid", "laser", "laser_inimigo", "speed_nave", "speed_misseis", "shield");
    private void removeOutOfBoundsObjects() {
        objects.entrySet().removeIf(entry -> {
            GameObject go = entry.getValue();
            Transform t = (Transform) go.transform();
            boolean outOfBounds = t.position().getY() > Main.windowHeigth + 50 ||
                    t.position().getY() < -100 ||
                    t.position().getX() < -100 ||
                    t.position().getX() > Main.windowWidth + 100;
            return outOfBounds && !go.name().equals("nave") && !go.name().equals("nave2") && !go.name().equals("parede");
        });
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.add(e.getKeyCode());
    }
    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove(e.getKeyCode());
    }
    public Container getGUI() {
        return g;
    }
}
