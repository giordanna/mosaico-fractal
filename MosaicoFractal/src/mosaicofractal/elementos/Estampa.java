package mosaicofractal.elementos;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;

public class Estampa {
    private final Shape estampa;
    private final Preenchimento preenchimento;
    private double x, y;
    
    public Estampa(Shape estampa, Preenchimento preenchimento, double angulo) {
        this.preenchimento = preenchimento;
        if (angulo != 0){
            final AffineTransform rotate = AffineTransform.getRotateInstance(angulo);
            this.estampa = rotate.createTransformedShape(estampa);
        }
        else this.estampa = estampa;
        
        this.x = this.estampa.getBounds2D().getX();
        this.y = this.estampa.getBounds2D().getY();
    }
    
    public Estampa(Shape estampa, Preenchimento preenchimento) {
        this.preenchimento = preenchimento;
        this.estampa = estampa;
    }
    
    public Shape getShape() {
        return estampa;
    }
    
    public static boolean intersecta(Estampa a, Estampa b) {
        Area areaA = new Area(a.getShape());
        areaA.intersect(new Area(b.getShape()));
        return !areaA.isEmpty();
    }
    
    public void desenha(Graphics2D g2){
        if (preenchimento.isTextura()) {
            TexturePaint tp = new TexturePaint(preenchimento.getTextura(), estampa.getBounds());
            g2.setPaint(tp);
        }
        else {
            g2.setColor(preenchimento.getCor());
        }
        
        g2.draw(estampa);
    }
}
