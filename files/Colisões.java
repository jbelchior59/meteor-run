package files;

import java.util.List;

public class Colisões {
    public static boolean colideCirculoPoligono(Circulo circulo, Poligono poligono) {
        Ponto centro = circulo.getCentro();
        double raio = circulo.getRaio();
        List<Ponto> vertices = poligono.getVert();

        if (pontoDentroPoligono(centro, vertices)) return true;

        for (int i = 0; i < vertices.size(); i++) {
            Ponto a = vertices.get(i);
            Ponto b = vertices.get((i + 1) % vertices.size());
            double distancia = distanciaPontoSegmento(centro, a, b);
            if (distancia <= raio) return true;
        }

        for (Ponto vertice : vertices) {
            double dx = vertice.getX() - centro.getX();
            double dy = vertice.getY() - centro.getY();
            if (dx * dx + dy * dy <= raio * raio) return true;
        }
        return false;
    }

    private static boolean pontoDentroPoligono(Ponto pt, List<Ponto> vertices) {
        boolean result = false;
        int n = vertices.size();
        for (int i = 0, j = n - 1; i < n; j = i++) {
            Ponto vi = vertices.get(i);
            Ponto vj = vertices.get(j);
            double xi = vi.getX(), yi = vi.getY();
            double xj = vj.getX(), yj = vj.getY();

            if (Math.abs(xi - xj) < 1e-9) {
                if (pt.getX() == xi) {
                    if (pt.getY() >= Math.min(yi, yj) && pt.getY() <= Math.max(yi, yj)) return true;
                } else if (pt.getX() < xi) {
                    if ((yi > pt.getY()) != (yj > pt.getY())) result = !result;
                }
                continue;
            }

            if ((yi > pt.getY()) != (yj > pt.getY())) {
                double xIntersect = (xj - xi) * (pt.getY() - yi) / (yj - yi + 1e-9) + xi;
                if (pt.getX() < xIntersect) result = !result;
            }
        }
        return result;
    }

    private static double distanciaPontoSegmento(Ponto p, Ponto a, Ponto b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double segLengthSq = dx * dx + dy * dy;

        if (segLengthSq < 1e-9)
            return Math.hypot(p.getX() - a.getX(), p.getY() - a.getY());

        double t = ((p.getX() - a.getX()) * dx + (p.getY() - a.getY()) * dy) / segLengthSq;
        t = Math.max(0, Math.min(1, t));
        Ponto closest = new Ponto(a.getX() + t * dx, a.getY() + t * dy);
        return Math.hypot(p.getX() - closest.getX(), p.getY() - closest.getY());
    }
}
