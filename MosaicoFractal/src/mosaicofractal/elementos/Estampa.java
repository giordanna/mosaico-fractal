package mosaicofractal.elementos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Estampa {
    private final Shape estampa;
    private final Preenchimento preenchimento;
    private final double area;
    private final double x, y;
    
    public Estampa(Shape estampa, Preenchimento preenchimento, double x, double y) {
        this.preenchimento = preenchimento;
        this.x = x;
        this.y = y;
        
        final AffineTransform translada = AffineTransform.getTranslateInstance(x, y);
        this.estampa = translada.createTransformedShape(estampa);
        
        this.area = calculaArea(this.estampa);
    }
    
    public Estampa(Estampa copia) {
        final AffineTransform copiadora = AffineTransform.getTranslateInstance(0, 0);
        this.preenchimento = new Preenchimento(copia.preenchimento);
        this.estampa = copiadora.createTransformedShape(copia.estampa);
        this.x = copia.x;
        this.y = copia.y;
        this.area = copia.area;
    }
    
    public Shape getShape() {
        return estampa;
    }
    
    public double getArea() {
        return area;
    }
    
    public static boolean intersecta(Estampa a, Estampa b) {
        Area areaA = new Area(a.getShape());
        areaA.intersect(new Area(b.getShape()));
        return !areaA.isEmpty();
    }
    
    public static boolean estaDentro(double x, double y, Shape area_grande, Shape forma) {
        final AffineTransform translada = AffineTransform.getTranslateInstance(x, y);
        Shape teste = translada.createTransformedShape(forma);
        
        Area areaA = new Area(area_grande);
        Area areaB = new Area(teste);
        
        areaA.intersect(areaB);
        areaB.subtract(areaA);
        return !areaB.isEmpty();
    }
    
    public void desenha(Graphics2D g2){
        if (preenchimento.isTextura()) {
            TexturePaint tp = new TexturePaint(preenchimento.getTextura(), estampa.getBounds());
            g2.setColor(Color.BLACK);
            g2.draw(estampa);
            
            g2.setPaint(tp);
        }
        else {
            g2.setColor(preenchimento.getCor());
        }
        
        g2.fill(estampa);
    }
    
    public static double calculaArea(Shape shape){
        
        BufferedImage imagem = new BufferedImage(shape.getBounds().width, shape.getBounds().height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagem.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fill(shape);
        
        int count = 0;
        for (int x = 0 ; x < shape.getBounds().width ; x++){
            for (int y = 0 ; y < shape.getBounds().height ; y++){
                int alpha = ( imagem.getRGB(x, y) >>24 ) & 0xff;
                if (alpha > 0)
                    count++; 
            }
        }
        return count;
    }
}
