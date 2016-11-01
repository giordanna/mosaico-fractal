package mosaicofractal.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import mosaicofractal.elementos.Preenchimento;

public class frameTeste extends JFrame{
    Shape shape;
    int area;
    Preenchimento preenchimento;
    public frameTeste(Shape shape, boolean is_rotaciona, Preenchimento preenchimento) {
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setUndecorated(true);
        AffineTransform transforma = new AffineTransform();
        if (is_rotaciona){
            transforma.rotate(Math.random() * Math.PI * 2, shape.getBounds2D().getCenterX(), shape.getBounds2D().getCenterY());
            this.shape = transforma.createTransformedShape(shape);
        }
        else{
            this.shape = shape;
        }
        
        transforma = AffineTransform.getScaleInstance(3, 3);
        this.shape = transforma.createTransformedShape(this.shape);
        
        transforma = AffineTransform.getScaleInstance(0.5, 0.5);
        this.shape = transforma.createTransformedShape(this.shape);
        
        this.preenchimento = preenchimento;
        
        this.pack();
        this.setVisible(true);
        calculaArea(shape);
    }
    
    public void inicia(){
        this.revalidate();
        this.repaint();
    }
    
    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        if (preenchimento.isTextura()) {
            TexturePaint tp = new TexturePaint(preenchimento.getTextura(), shape.getBounds());
            g2.setPaint(tp);
        }
        else {
            g2.setColor(preenchimento.getCor());
        }
        g2.fill(shape);
        
    }
    
    private void calculaArea(Shape shape){
        int count = 0;
        BufferedImage imagem = new BufferedImage(shape.getBounds().width, shape.getBounds().height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagem.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fill(shape);
        
        for (int x = 0 ; x < shape.getBounds().width ; x++){
            for (int y = 0 ; y < shape.getBounds().height ; y++){
                int alpha = ( imagem.getRGB(x, y) >>24 ) & 0xff;
                if (alpha != 0)
                    count++;
                }
        }
        area = count;
        System.out.println(area);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }
}
