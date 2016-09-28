package testefractal.formas;

import java.awt.Color;
import java.awt.Graphics;
import testefractal.Area;

public class Quadrado extends Forma {
    
    public Quadrado(){
        super();
        raio = 10;
    }
    
    public Quadrado(int x, int y, int raio){
        super(x, y, raio);
    }
    
    public Quadrado(int x, int y, int raio, Color cor){
        super(x, y, raio, cor);
    }
    
    @Override
    public double distancia(Forma c2){
        return Math.sqrt(Math.pow(x - c2.x - c2.raio, 2) + Math.pow(y - c2.y - c2.raio, 2));
    }
    
    @Override
    public boolean teste(FormaAbstrata c){
        Quadrado c2 = (Quadrado) c;
        if (!(x <= c2.x + c2.raio * 2 && x + raio * 2 >= c2.x && y <= c2.y + c2.raio * 2 && y + raio * 2 >= c2.y)) {
            return distancia(c2) > (c2.raio + raio);
        }
        return false;
    }
    
    @Override
    public double raioGerado(double area_razao){
        return Math.sqrt(Area.instancia().getArea()) * 
                Math.sqrt(area_razao/4.0);
    }
    
    public static double raioGeradoEstatico(double area_razao){
        return Math.sqrt(Area.instancia().getArea()) * 
                Math.sqrt(area_razao/4.0);
    }
    
    @Override
    public void desenha(Graphics g) {
        g.setColor(cor);
        g.fillRect(x, y , raio *  2, raio * 2);
    }

    @Override
    public double getArea() {
        return raio * raio * 4;
    }
}

