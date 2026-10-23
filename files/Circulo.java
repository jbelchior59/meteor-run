package files;

/**
 * Representa um círculo no plano cartesiano.
 * @author POO2425p1g4
 * @version 01/04/2025
 * @inv O raio deve ser maior que 0, o centro deve estar no primeiro quadrante
 */
public class Circulo {
    private Ponto centro;
    private double raio;

    public Circulo(String params) {
        String[] tokens = params.trim().split("\\s+");
        if (tokens.length != 3) {
            System.out.println("Circulo:vi");
            System.exit(0);
        }
        int cx = Integer.parseInt(tokens[0]);
        int cy = Integer.parseInt(tokens[1]);
        double r = Double.parseDouble(tokens[2]);
        if (validaCirculo(cx, cy, r)) {
            System.out.println("Circulo:vi");
            System.exit(0);
        }
        centro = new Ponto(cx, cy);
        raio = r;
    }

    public Circulo(Ponto centro, double raio) {
        double cx = centro.getX();
        double cy = centro.getY();
        this.centro = new Ponto(cx, cy);
        this.raio = raio;
    }

    private static boolean validaCirculo(double cx, double cy, double r) {
        return (cx < 0 || cy < 0 || r <= 0);
    }

    public void translacao(double dx, double dy) {
        Ponto novoCentro = centro.translacaoPonto(dx, dy);
        if (validaCirculo((int) novoCentro.getX(), (int) novoCentro.getY(), this.raio)) {
            System.out.println("Circulo:vi");
            System.exit(0);
        }
        this.centro = novoCentro;
    }

    public double getRaio() { return raio; }
    public Ponto getCentro() { return centro; }
    public void setRaio(double raio) { this.raio = raio; }

    @Override
    public String toString() {
        return String.format("%s %f", centro, raio);
    }
}
