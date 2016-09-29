package testefractal.formas;

import java.awt.Color;
import java.awt.Graphics;
import testefractal.Area;

public class CirculoApolonio extends FormaAbstrata implements Comparable{
    public Color cor;
    public double x, y, raio;
    public static double escala = Area.instancia().getAltura();
    
    public CirculoApolonio(){
        x = y = 0;
        raio = 10;
        cor = Forma.corAleatoria2();
    }
    
    public CirculoApolonio(Color cor){
        x = y = 0;
        raio = 10;
        this.cor = cor;
    }
    
    public CirculoApolonio(double x, double y, double raio){
        this.x = x;
        this.y = y;
        this.raio = raio;
        cor = Forma.corAleatoria2();
    }
    
    public CirculoApolonio(double x, double y, double raio, Color cor){
        this.x = x;
        this.y = y;
        this.raio = raio;
        this.cor = cor;
    }

    @Override
    public void desenha(Graphics g) {
        g.setColor(cor);
        int raio_local = (int) Math.abs(this.raio * escala/2 - 0.5);
        int x_local = (int) Math.abs(this.x * escala/2 + escala/2);
        int y_local = (int) Math.abs(this.y * escala/2 + escala/2);

        g.fillOval(x_local - raio_local, y_local - raio_local, raio_local *  2, raio_local * 2);
        
    }
    
    @Override
    public double getArea() {
        double raio_local = (int) Math.abs(this.raio * escala/2);
        return Math.PI * raio_local * raio_local;
    }

    @Override
    public boolean teste(FormaAbstrata c){
        return false;
    }
    
    @Override
    public String toString(){
        return "(" + (x*escala) + " , " + (y*escala) + ") | raio=" + (raio*escala/2);
    }

    @Override
    public int compareTo(Object o) {
        CirculoApolonio c2 = (CirculoApolonio) o;
        if (this.raio == c2.raio) return 0;
        return (this.raio > c2.raio)? -1 : 1;
    }
}
