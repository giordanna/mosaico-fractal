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
    private Shape apoio_1 = null, apoio_2 = null, apoio_3 = null;
    private boolean sem_borda_x = false, sem_borda_y = false;
    private final Preenchimento preenchimento;
    private final double area, x, y;
    
    public Estampa(Shape estampa, Preenchimento preenchimento, double x, double y) {
        this.preenchimento = preenchimento;
        this.x = x;
        this.y = y;
        
        AffineTransform translada = AffineTransform.getTranslateInstance(x, y);
        this.estampa = translada.createTransformedShape(estampa);
        this.area = calculaArea(this.estampa);
        if (x + estampa.getBounds2D().getWidth() > mosaicofractal.tela.Area.LARGURA){
            sem_borda_x = true;
            translada = AffineTransform.getTranslateInstance(-mosaicofractal.tela.Area.LARGURA, 0);
            this.apoio_1 = translada.createTransformedShape(this.estampa);
        }
        if (y + estampa.getBounds2D().getHeight() > mosaicofractal.tela.Area.ALTURA){
            sem_borda_y = true;
            if (sem_borda_x){
                translada = AffineTransform.getTranslateInstance(0, -mosaicofractal.tela.Area.ALTURA);
                this.apoio_2 = translada.createTransformedShape(this.estampa);
                translada = AffineTransform.getTranslateInstance(-mosaicofractal.tela.Area.LARGURA, -mosaicofractal.tela.Area.ALTURA);
                this.apoio_3 = translada.createTransformedShape(this.estampa);
            }
            else{
                translada = AffineTransform.getTranslateInstance(0, -mosaicofractal.tela.Area.ALTURA);
                this.apoio_1 = translada.createTransformedShape(this.estampa);
            }
        }
    }
    
    public Estampa(Estampa copia) {
        final AffineTransform copiadora = AffineTransform.getTranslateInstance(0, 0);
        this.preenchimento = new Preenchimento(copia.preenchimento);
        this.estampa = copiadora.createTransformedShape(copia.estampa);
        if (copia.apoio_1 != null){
            this.apoio_1 = copiadora.createTransformedShape(copia.apoio_1);
            
            if (copia.apoio_2 != null){
                this.apoio_2 = copiadora.createTransformedShape(copia.apoio_2);
                this.apoio_3 = copiadora.createTransformedShape(copia.apoio_3);
            }
        }
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
        Area areaA = new Area(a.estampa);
        areaA.intersect(new Area(b.estampa));
        if (!areaA.isEmpty()) return true;
        
        if (a.apoio_1 != null){
            areaA = new Area(a.apoio_1);
            areaA.intersect(new Area(b.estampa));
            if (!areaA.isEmpty()) return true;
            
            if (a.apoio_2 != null){
                areaA = new Area(a.apoio_2);
                areaA.intersect(new Area(b.estampa));
                if (!areaA.isEmpty()) return true;
                
                areaA = new Area(a.apoio_3);
                areaA.intersect(new Area(b.estampa));
                if (!areaA.isEmpty()) return true;
            }
        }
        
        if (b.apoio_1 != null){
            areaA = new Area(b.apoio_1);
            areaA.intersect(new Area(a.estampa));
            if (!areaA.isEmpty()) return true;
            
            if (b.apoio_2 != null){
                areaA = new Area(b.apoio_2);
                areaA.intersect(new Area(a.estampa));
                if (!areaA.isEmpty()) return true;
                
                areaA = new Area(b.apoio_3);
                areaA.intersect(new Area(a.estampa));
                if (!areaA.isEmpty()) return true;
            }
        }

        return false;
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
            g2.setPaint(tp);
        }
        else {
            g2.setColor(preenchimento.getCor());
        }
        
        g2.fill(estampa);
        
        if (apoio_1 != null){
            g2.fill(apoio_1);
            
            if (apoio_2 != null){
                g2.fill(apoio_2);
                g2.fill(apoio_3);
            }
        }
    }
    
    public static double calculaArea(Shape shape){
        
        BufferedImage imagem = new BufferedImage(shape.getBounds().width, shape.getBounds().height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagem.createGraphics();
        g2.setColor(Color.BLACK);
        
        // coloca forma na origem
        AffineTransform transforma = AffineTransform.getTranslateInstance(-shape.getBounds2D().getX(), -shape.getBounds2D().getY());
        Shape forma = transforma.createTransformedShape(shape);
        g2.fill(forma);
        
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
