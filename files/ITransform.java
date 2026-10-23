package files;

/**
 * Interface que define operações de transformação geométrica
 * @version 01/04/2025
 */
public interface ITransform {
    void move(Ponto dPos, int dlayer);
    void rotate(double dTheta);
    void scale(double dScale);
    Ponto position();
    int layer();
    double angle();
    double scale();
}