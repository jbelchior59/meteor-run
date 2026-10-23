package files;

/**
 * Implementação concreta de colisor para figuras geométricas
 * @author POO2425p1g4
 * @version 01/04/2025
 */
public abstract class Collider extends Transform implements ICollider{
    protected Transform t;

    public Collider(){}

    public abstract Ponto centroid();
    public abstract void update(Transform t);
    public abstract boolean isColliding(Collider that);
    public abstract boolean isColliding(ColliderPoligono that);
    public abstract boolean isColliding(ColliderCirculo that);
    public abstract String toString();
}
