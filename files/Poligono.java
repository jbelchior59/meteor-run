package files;

import java.util.ArrayList;
import java.util.List;

public class Poligono {
    protected ArrayList<Ponto> vertices;

    public Poligono(List<Ponto> vertices) {
        this.vertices = new ArrayList<>(vertices);
    }

    private static boolean areVerticesInvalid(List<Ponto> vertices, List<Segmento> segments) {
        return hasCollinearVertices(vertices) || hasIntersectingEdges(segments);
    }

    private static boolean hasCollinearVertices(List<Ponto> vertices) {
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            Ponto a = vertices.get(i);
            Ponto b = vertices.get((i + 1) % n);
            Ponto c = vertices.get((i + 2) % n);
            if (Segmento.saoColineares(a, b, c)) return true;
        }
        return false;
    }

    private static boolean hasIntersectingEdges(List<Segmento> segments) {
        int n = segments.size();
        for (int i = 0; i < n; i++) {
            Segmento seg1 = segments.get(i);
            for (int j = i + 1; j < n; j++) {
                if (j == i || j == (i + 1) % n || ((j + 1) % n) == i) continue;
                Segmento seg2 = segments.get(j);
                if (Segmento.intersects(seg1, seg2)) return true;
            }
        }
        return false;
    }

    public void translacao(double dx, double dy) {
        for (int i = 0; i < vertices.size(); i++) {
            Ponto p = vertices.get(i);
            vertices.set(i, new Ponto(p.getX() + dx, p.getY() + dy));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Ponto p : vertices) {
            sb.append(String.format("%f %f ", p.getX(), p.getY()));
        }
        return sb.toString().trim();
    }

    public List<Ponto> getVert() {return this.vertices;}
}
