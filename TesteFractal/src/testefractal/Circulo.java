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
        return Math.sqrt((x - c2.x) * (x - c2.x) + (y - c2.y) * (y - c2.y));
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
