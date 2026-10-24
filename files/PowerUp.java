package files;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a power-up in the game that can provide temporary enhancements to player ships.
 * Power-ups fall from the top of the screen and can be collected by player ships to gain
 * temporary abilities like increased speed, faster missiles, or shields.
 *
 * @author [Your Name]
 * @version 1.0 - 2023-11-15
 * @inv Power-ups always move downward at a constant speed until collected or out of bounds.
 *       Each power-up type can have only one active instance at a time per game.
 */
public class PowerUp extends GameObject {
    /**
     * Enum representing the different types of power-ups available in the game.
     */
    public enum Type {
        SPEED_NAVE,
        SPEED_MISSEIS,
        SHIELD
    }

    private final Type type;
    private final BufferedImage image;
    private static final float POWERUP_SCALE = 1.5f; // Aumento do tamanho dos power-ups
    private static final float FALL_SPEED = 0.5f; // Velocidade de descida

    // Sistema de timers ativos
    private static final Map<Type, Timer> activeTimers = new HashMap<>();
    private static final Map<Type, GameObject> activeTargets = new HashMap<>();
    private static final int EFFECT_DURATION = 5000; // 5 segundos

    /**
     * Creates a new PowerUp instance at the specified position.
     *
     * @param type The type of power-up to create
     * @param x The initial x-coordinate of the power-up
     * @param y The initial y-coordinate of the power-up
     * @throws IOException If the power-up image file cannot be loaded
     */
    public PowerUp(Type type, double x, double y) throws IOException {
        super(getPowerUpName(type) + "\n" + x + " " + y + " 2 0.0 " + POWERUP_SCALE + "\n-10 -10 10 -10 10 10 -10 10");
        this.type = type;
        this.image = ImageIO.read(PowerUp.class.getResource("/resources/Images/" + getPowerUpName(type) + ".png"));
        this.refreshShape();
    }

    /**
     * Converts the power-up type enum to its lowercase string representation.
     *
     * @param type The power-up type to convert
     * @return The lowercase string representation of the power-up type
     */
    private static String getPowerUpName(Type type) {
        return type.name().toLowerCase();
    }

    /**
     * Updates the power-up's position, making it fall downward.
     */
    public void update() {
        Transform t = (Transform) this.transform();
        t.move(new Ponto(0, FALL_SPEED), 0);
        this.setTranform(t);
    }

    /**
     * Applies the power-up's effect to the specified target game object.
     * If an effect of the same type is already active, it will be replaced.
     *
     * @param target The game object to apply the effect to (must be a player ship)
     */
    public void applyEffect(GameObject target) {
        if (!(target.name().equals("nave") || target.name().equals("nave2"))) return;

        // Cancela o timer anterior se existir
        if (activeTimers.containsKey(type)) {
            activeTimers.get(type).stop();
            removeEffect(type); // Remove o efeito anterior
        }

        activeTargets.put(type, target);

        switch (type) {
            case SPEED_NAVE:
                Behaviour.setMoveSpeed(Behaviour.MOVE_SPEED * 2.5); // Aumento maior da velocidade
                break;

            case SPEED_MISSEIS:
                target.setFastShots(true);
                target.setFireRate(1); // Dispara mais rápido (valor menor = mais tiros)
                break;

            case SHIELD:
                target.setShield(true);
                target.setShieldHits(1); // Pode aguentar 1 hit sem dano
                break;
        }

        // Configura o timer
        Timer timer = new Timer(EFFECT_DURATION, e -> {
            removeEffect(type);
            activeTimers.remove(type);
            activeTargets.remove(type);
        });
        timer.setRepeats(false);
        timer.start();
        activeTimers.put(type, timer);
    }

    /**
     * Removes the specified power-up effect from its target.
     *
     * @param type The type of power-up effect to remove
     */
    private static void removeEffect(Type type) {
        GameObject target = activeTargets.get(type);
        if (target == null) return;

        switch (type) {
            case SPEED_NAVE:
                Behaviour.setMoveSpeed(Behaviour.MOVE_SPEED / 2.5);
                break;

            case SPEED_MISSEIS:
                target.setFastShots(false);
                target.setFireRate(300); // Volta ao fire rate normal
                break;

            case SHIELD:
                target.setShield(false);
                target.setShieldHits(0);
                break;
        }
    }

    /**
     * Checks if the shield power-up is currently active.
     *
     * @return true if shield is active, false otherwise
     */
    public static boolean isShieldActive() {
        return activeTimers.containsKey(Type.SHIELD);
    }

    /**
     * Checks if the speed boost power-up is currently active.
     *
     * @return true if speed boost is active, false otherwise
     */
    public static boolean isSpeedActive() {
        return activeTimers.containsKey(Type.SPEED_NAVE);
    }

    /**
     * Checks if the fast shots power-up is currently active.
     *
     * @return true if fast shots is active, false otherwise
     */
    public static boolean isFastShotsActive() {
        return activeTimers.containsKey(Type.SPEED_MISSEIS);
    }

    /**
     * Gets the type of this power-up.
     *
     * @return The power-up type
     */
    public Type getType() {
        return type;
    }
}
