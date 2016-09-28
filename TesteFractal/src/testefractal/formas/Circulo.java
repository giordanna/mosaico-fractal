package testefractal.formas;

import java.awt.Color;
import java.awt.Graphics;
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
        g.setColor(cor);
        g.fillOval(x - raio, y - raio, raio *  2, raio * 2);
    }

    @Override
    public double getArea() {
        return Math.PI * raio * raio;
    }
}
