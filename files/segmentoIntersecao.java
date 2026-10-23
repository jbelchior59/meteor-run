package files;

class segmentoIntersecao {
    public static boolean intersects(Segmento r1, Segmento r2) {
        Ponto p1 = r1.getpA();
        Ponto p2 = r1.getpB();
        Ponto p3 = r2.getpA();
        Ponto p4 = r2.getpB();

        int o1 = orientacao(p1, p2, p3);
        int o2 = orientacao(p1, p2, p4);
        int o3 = orientacao(p3, p4, p1);
        int o4 = orientacao(p3, p4, p2);

        if (o1 != o2 && o3 != o4) return true;
        if (o1 == 0 && onSegment(p1, p3, p2)) return true;
        if (o2 == 0 && onSegment(p1, p4, p2)) return true;
        if (o3 == 0 && onSegment(p3, p1, p4)) return true;
        return o4 == 0 && onSegment(p3, p2, p4);
    }

    private static int orientacao(Ponto p, Ponto q, Ponto r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (Math.abs(val) < 1e-9) return 0;
        return (val > 0) ? 1 : 2;
    }

    private static boolean onSegment(Ponto p, Ponto q, Ponto r) {
        return q.getX() <= Math.max(p.getX(), r.getX()) &&
                q.getX() >= Math.min(p.getX(), r.getX()) &&
                q.getY() <= Math.max(p.getY(), r.getY()) &&
                q.getY() >= Math.min(p.getY(), r.getY());
    }
}
