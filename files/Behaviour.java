package files;

import java.awt.event.KeyEvent;
import java.util.Set;

/**
 * Classe responsável por definir e gerenciar o comportamento dos objetos do jogo,
 * incluindo movimento do jogador, IA dos inimigos e manipulação de projéteis.
 * @author POO2425p1g4
 * @version 01/06/2025
 * @inv As velocidades de movimento e tempos de recarga devem ser valores positivos
 */
public class Behaviour {
    private final GameObject go;
    static double MOVE_SPEED = 5.0;
    private static final double ROTATE_SPEED = 5.0;

    // Tempos de recarga ajustados
    private static final long PLAYER_SHOT_COOLDOWN = 300;  // 0.3s para jogador
    private static final long ENEMY_SHOT_COOLDOWN = 2500;  // 2.5s para inimigos

    private long lastPlayerShotTime = 0;
    private long lastEnemyShotTime = 0;

    /**
     * Constrói uma instância de Behaviour para um objeto do jogo específico
     * @param go O objeto do jogo que este comportamento irá controlar
     */
    public Behaviour(GameObject go) {
        this.go = go;
    }

    /**
     * Controla o movimento de NPCs e a lógica de disparo
     * @param go O objeto do jogo a ser movido
     * @return O objeto do jogo após o movimento, ou um novo projétil se um tiro foi disparado
     */
    public GameObject npcMoves(GameObject go) {
        Transform t = (Transform) go.transform();
        Ponto currentPos = t.position();

        // Movimento para power-ups
        if (go.name().equals("speed_nave") || go.name().equals("speed_misseis") || go.name().equals("shield")) {
            t.move(new Ponto(0, 2), 0);  // Move para baixo com velocidade fixa 2
            go.setTranform(t);
            return go;
        }

        // Restante do código existente...
        switch(go.name()) {
            case "nave_inimiga":
            case "nave_boss":
                if (currentPos.getY() < 50) {
                    t.move(new Ponto(0, 1), 0);
                } else {
                    double moveX = Math.sin(System.currentTimeMillis() * 0.003) * 1.5;
                    t.move(new Ponto(moveX, 0), 0);

                    long currentTime = System.currentTimeMillis();
                    if (currentTime - go.getLastShotTime() >= ENEMY_SHOT_COOLDOWN) {
                        go.setLastShotTime(currentTime);
                        GameObject tiro = shoot(go);
                        if (tiro != null) {
                            tiro.setName("laser_inimigo");
                            return tiro;
                        }
                    }
                }
                break;

            case "asteroid":
                t.move(new Ponto(0, MOVE_SPEED * 0.7), 0);
                break;

            case "laser":
                double speed = t.angle() == 90 ? -MOVE_SPEED * 3 : MOVE_SPEED * 2;
                t.move(new Ponto(0, speed), 0);
                break;

            case "laser_inimigo":
                t.move(new Ponto(0, MOVE_SPEED * 1.5), 0);
                break;
        }

        go.setTranform(t);
        return go;
    }

    /**
     * Controla o movimento do jogador baseado nas teclas pressionadas
     * @param keys Conjunto de teclas atualmente pressionadas
     * @return O objeto do jogo movido ou um novo projétil se um tiro foi disparado
     */
    public GameObject moveSet(Set<Integer> keys) {
        Transform t = (Transform) go.transform();
        double moveX = 0;
        double moveY = 0;
        GameObject bullet = null;

        if (go.name().equals("nave")) {
            // Controles de movimento
            if (keys.contains(KeyEvent.VK_A)) moveX -= MOVE_SPEED;
            if (keys.contains(KeyEvent.VK_D)) moveX += MOVE_SPEED;
            if (keys.contains(KeyEvent.VK_W)) moveY -= MOVE_SPEED;
            if (keys.contains(KeyEvent.VK_S)) moveY += MOVE_SPEED;

            // Disparo com recarga de 0.3 segundos
            if (keys.contains(KeyEvent.VK_SPACE)) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastPlayerShotTime >= PLAYER_SHOT_COOLDOWN) {
                    lastPlayerShotTime = currentTime;
                    bullet = shoot(go);
                }
            }
        }
        else if (go.name().equals("nave2")) {
            // Controles de movimento
            if (keys.contains(KeyEvent.VK_LEFT)) moveX -= MOVE_SPEED;
            if (keys.contains(KeyEvent.VK_RIGHT)) moveX += MOVE_SPEED;
            if (keys.contains(KeyEvent.VK_UP)) moveY -= MOVE_SPEED;
            if (keys.contains(KeyEvent.VK_DOWN)) moveY += MOVE_SPEED;

            // Disparo com recarga de 0.3 segundos
            if (keys.contains(KeyEvent.VK_SHIFT)) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastPlayerShotTime >= PLAYER_SHOT_COOLDOWN) {
                    lastPlayerShotTime = currentTime;
                    bullet = shoot(go);
                }
            }
        }

        t.move(new Ponto(moveX, moveY), 0);
        go.setTranform(t);
        return bullet != null ? bullet : go;
    }

    /**
     * Cria e retorna um novo projétil disparado pelo atirador
     * @param shooter O objeto do jogo que está atirando
     * @return O novo objeto de projétil, ou null se a criação falhar
     */
    public GameObject shoot(GameObject shooter) {
        try {
            boolean isPlayer = shooter.name().equals("nave") || shooter.name().equals("nave2");
            boolean isEnemy = shooter.name().equals("nave_inimiga") || shooter.name().equals("nave_boss");
            int damage = isPlayer ? 40 : 30;

            double angle = 90; // Padrão para jogador (para cima)

            if (isEnemy) {
                angle = 270; // Inimigos atiram para baixo
            } else if (shooter.name().equals("nave2")) {
                angle = 270; // Player 2 atira para baixo (CORRIGIDO)
            } else {
                angle = 90;  // Nave1 atira para cima
            }

            // Posição do tiro ajustada baseada na direção
            double yOffset;
            if (shooter.name().equals("nave2")) {
                yOffset = 40; // nave2 atira para baixo, então o tiro sai abaixo da nave
            } else if (isEnemy) {
                yOffset = 30; // Inimigos atiram para baixo
            } else {
                yOffset = -40; // nave1 atira para cima, então o tiro sai acima da nave
            }

            GameObject bullet = new GameObject(
                    (isPlayer ? "laser" : "laser_inimigo") + "\n" +
                            shooter.transform().position().getX() + " " +
                            (shooter.transform().position().getY() + yOffset) + " 1 0.0 0.7\n" +
                            "-3 -7 3 -7 3 7 -3 7"
            );

            Transform t = (Transform) bullet.transform();
            t.rotate(angle); // Usa o ângulo calculado
            bullet.setTranform(t);
            bullet.refreshShape();
            return bullet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Define a velocidade de movimento global para os objetos do jogo
     * @param speed O novo valor de velocidade de movimento
     */
    public static void setMoveSpeed(double speed) {
        MOVE_SPEED = speed;
    }
}
