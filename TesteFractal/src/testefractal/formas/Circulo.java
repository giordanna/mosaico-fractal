package testefractal.formas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import testefractal.Area;

public class Circulo extends Forma {
    
    public Circulo(){
        super();
        raio = 10;
    }
    
    public Circulo(int x, int y, int raio){
        super(x, y, raio);
    }
    
    public Circulo(int x, int y, int raio, Color cor){
        super(x, y, raio, cor);
    }
    
    @Override
    public double distancia(Forma c2){
        return Math.sqrt(Math.pow(x - c2.x, 2) + Math.pow(y - c2.y, 2));
    }
    
    @Override
    public boolean teste(FormaAbstrata c){
        Circulo c2 = (Circulo) c;
        double deltax = Math.abs(c2.x - x), deltay;
        
        if (deltax < (c2.raio + raio)) {

            deltay = Math.abs(c2.y - y);
            if (deltay < (c2.raio + raio)) {
                return (distancia(c2) >= (c2.raio + raio));
            }
        }
        
        return true;
    }
    
    @Override
    public double raioGerado(double area_razao){
        return Math.sqrt(Area.instancia().getArea()) * 
                Math.sqrt(area_razao / Math.PI);
    }
    
    public static double raioGeradoEstatico(double area_razao){
        return Math.sqrt(Area.instancia().getArea()) * 
                Math.sqrt(area_razao / Math.PI);
    }
    
    @Override
    public void desenha(Graphics g) {
        //try {
            //Graphics2D g2d = (Graphics2D) g;
            //BufferedImage img = ImageIO.read(new File("./basquete.png"));
            //TexturePaint tp = new TexturePaint(img, new Rectangle(x - raio, y - raio, raio *  2, raio * 2));
            //g2d.setPaint(tp);
            //g2d.setColor(cor);
            //g2d.fillOval(x - raio, y - raio, raio *  2, raio * 2);
            g.setColor(cor);
            g.fillOval(x - raio, y - raio, raio *  2, raio * 2);
        //} catch (IOException ex) {
        //    Logger.getLogger(Circulo.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }

    @Override
    public double getArea() {
        return Math.PI * raio * raio;
    }
}
