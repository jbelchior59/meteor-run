package files;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Transform extends GameObject implements ITransform {
    private double x, y, angle, scale;
    private int layer;
    private ArrayList<Ponto> vert;
    private Collider c;

    protected Transform(){}

    public Transform(double x, double y, int layer, double angle, double scale, ArrayList<Ponto> vert, Collider c) {
        new Ponto(x, y);
        this.x = x;
        this.y = y;
        this.layer = layer;
        this.angle = angle;
        this.scale = scale;
        this.vert = vert;
        this.c = new ColliderPoligono(vert);
    }

    public Transform(double x, double y, int layer, double angle, double scale, ArrayList<Ponto> vert, Collider c, double radius) {
        new Ponto(x, y);
        this.x = x;
        this.y = y;
        this.layer = layer;
        this.angle = angle;
        this.scale = scale;
        this.vert = vert;
        this.c = new ColliderCirculo(vert, radius);
    }

    @Override
    public void move(Ponto dPos, int dlayer) {
        ArrayList<Ponto> result = new ArrayList<>();
        for (Ponto p : this.vert) {
            Ponto res = new Ponto(p.getX() + dPos.getX(), p.getY() + dPos.getY());
            result.add(res);
        }
        vert = result;
        this.x += dPos.getX();
        this.y += dPos.getY();
        this.layer += dlayer;
        c.update(this);
        checkCentroid();
    }

    public void rotate(double dTheta) {
        this.angle += dTheta;
        ArrayList<Ponto> result = new ArrayList<>();
        if(vert.size() > 1) {
            for (Ponto p : vert) {
                double x = p.getX() - this.x;
                double y = p.getY() - this.y;
                double rad = dTheta * Math.PI / 180;
                BigDecimal newX = BigDecimal.valueOf(x * Math.cos(rad) - y * Math.sin(rad));
                BigDecimal newY = BigDecimal.valueOf(x * Math.sin(rad) + y * Math.cos(rad));
                x = newX.doubleValue();
                y = newY.doubleValue();
                x += this.x;
                y += this.y;
                result.add(new Ponto(x, y));
            }
        }else result = vert;
        vert = result;
        c.update(this);
    }

    @Override
    public void scale(double dScale) {
        this.scale += dScale;
        ArrayList<Ponto> result = new ArrayList<>();
        if (vert.size() > 1){
            for (Ponto p : vert) {
                double x = p.getX() - this.x;
                double y = p.getY() - this.y;
                x *= this.scale;
                y *= this.scale;
                x += this.x;
                y += this.y;
                result.add(new Ponto(x, y));
            }
        } else if(vert.size() == 1){
            ColliderCirculo cc = (ColliderCirculo) this.c;
            double radius = cc.getRadius();
            radius *= dScale;
            cc.setRadius(radius);
            this.c = cc;
        }
        vert = result;
        c.update(this);
    }

    @Override public Ponto position() { return new Ponto(x, y); }
    @Override public int layer() { return this.layer; }
    @Override public double angle() { return angle; }
    @Override public double scale() { return scale; }
    protected ArrayList<Ponto> vert(){ return this.vert; }

    @Override
    public String toString() {
        c.update(this);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%f %f %d %f %f", x, y, layer, angle, scale));
        sb.append("\n");
        sb.append(c);
        return sb.toString();
    }

    private void checkCentroid(){
        Ponto centroid = c.centroid();
        double vDCx = this.x - centroid.getX(), vDCy = this.y - centroid.getY();
        if(vDCy == 0 && vDCx == 0) return;
        ArrayList<Ponto> result = new ArrayList<>();
        if(this.vert.size() > 2){
            for (Ponto p : vert){
                Ponto res = new Ponto(p.getX() + vDCx, p.getY() + vDCy);
                result.add(res);
            }
        }else{
            Ponto res = new Ponto(this.vert.getFirst().getX() + vDCx, this.vert.getFirst().getY() + vDCy);
            result.add(res);
            result.add(this.vert.getLast());
        }
        vert = result;
        c.update(this);
    }
}
