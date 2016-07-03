package testefractal;

import java.awt.Color;
import java.awt.Graphics;

public class Circulo extends Forma {
    protected int x, y, raio;
    
    public Circulo(){
        super();
        x = y = 0;
        raio = 10;
    }
    
    public Circulo(int x, int y, int raio){
        super();
        this.x = x;
        this.y = y;
        this.raio = raio;
    }
    
    public Circulo(int x, int y, int raio, Color cor){
        super(cor);
        this.x = x;
        this.y = y;
        this.raio = raio;
    }
    
    public boolean estaDentro(Circulo c2) {
        return distancia(c2) + raio <= c2.raio;
    }

    public boolean estaFora(Circulo c2) {
        return distancia(c2) > ((raio) + (c2.raio));

    }

    public boolean temInterseccao(Circulo c2) {
        return Math.abs((raio - c2.raio)) <= distancia(c2) && distancia(c2) <= (raio + c2.raio);
    }
    
    public double distancia(Circulo c2){
        return Math.sqrt(Math.pow(x - c2.x, 2) + Math.pow(y - c2.y, 2));
    }
    
    public boolean teste(Circulo c2, double soma_raios){
        double deltax = Math.abs(c2.x - x), deltay, q2;
        
        if (deltax < soma_raios) { //coarse test x

            deltay = Math.abs(c2.y - y);
            if (deltay < soma_raios) {//coarse test y

                q2 = Math.sqrt(Math.pow(deltax, 2) + Math.pow(deltay, 2));
                if (q2 < soma_raios) {//fine test

                    return false;
                }
            }
        }
        
        return true;
    }
    
    public static double raioGerado(double area_razao){
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
