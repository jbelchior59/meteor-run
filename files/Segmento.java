package files;

public class Segmento {
    private final Ponto pA, pB;

    public Segmento(Ponto pA, Ponto pB) {
        if (pA.pontosIguais(pB)) {
            System.out.println("Segmento:vi");
            System.exit(0);
        }
        this.pA = pA;
        this.pB = pB;
    }

    public Ponto getpA() { return pA; }
    public Ponto getpB() { return pB; }

    public static boolean saoColineares(Ponto a, Ponto b, Ponto c) {
        double area = a.getX() * (b.getY() - c.getY()) +
                b.getX() * (c.getY() - a.getY()) +
                c.getX() * (a.getY() - b.getY());
        return Math.abs(area) < 1e-9;
    }

    public static boolean intersects(Segmento r1, Segmento r2) {
        return segmentoIntersecao.intersects(r1, r2);
    }
}
