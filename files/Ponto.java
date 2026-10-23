package files;

import static java.lang.Math.*;

public class Ponto {
    private double x, y;

    public Ponto() {
        this.x = 0;
        this.y = 0;
    }

    public Ponto(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public Ponto translacaoPonto(double dx, double dy){
        double novoX = x + dx;
        double novoY = y + dy;
        return new Ponto(novoX, novoY);
    }

    double dist(Ponto that) {
        return sqrt((this.x * this.x) + (that.getX() * that.getX()) - (2 * this.x * that.getX() * cos(toRadians(this.y - that.getY()) ) ) );
    }

    boolean pontosIguais(Ponto that){
        return (Math.abs(this.getX() - that.getX()) < 1e-9 && Math.abs(this.getY() - that.getY()) < 1e-9);
    }

    @Override
    public String toString() {
        return String.format("%f %f", getX(), getY());
    }

    public Ponto sub(Ponto other) { return new Ponto(this.x - other.getX(), this.y - other.getY()); }
    public Ponto neg() { return new Ponto(-this.x, -this.y); }
    public double dotProduct(Ponto other) { return this.x * other.getX() + this.y * other.getY(); }
    public double magnitude() { return Math.sqrt(x * x + y * y); }
    public Ponto normalize() {
        double mag = magnitude();
        return new Ponto(x / mag, y / mag);
    }
    public Ponto[] perpendicularDirections() {
        return new Ponto[] { new Ponto(-this.y, this.x), new Ponto(this.y, -this.x) };
    }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void set(Ponto p) {
        this.x = p.getX();
        this.y = p.getY();
    }
}
