package mosaicofractal.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;

public class frameTeste extends JFrame{
    Shape shape;
    public frameTeste(Shape shape, boolean is_rotaciona) {
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        if (is_rotaciona){
            final AffineTransform rotaciona = AffineTransform.getRotateInstance(Math.random() * Math.PI * 2);
            shape = rotaciona.createTransformedShape(shape);
        }
        
        final AffineTransform translada = AffineTransform.getTranslateInstance(150 - shape.getBounds2D().getCenterX(), 150 - shape.getBounds2D().getCenterY());
        this.shape = translada.createTransformedShape(shape);
        
        this.pack();
        this.setVisible(true);
    }
    
    public void inicia(){
        System.out.println(shape);
        this.revalidate();
        this.repaint();
    }
    
    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.red);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fill(shape);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }
}
