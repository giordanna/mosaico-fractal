package testefractal.formas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import testefractal.Area;

public class CirculoPolonio extends FormaAbstrata{
    public Color cor;
    public double x, y, raio;
    public static int escala = Area.instancia().getLargura();
    
    public CirculoPolonio(){
        x = y = 0;
        raio = 10;
        cor = corAleatoria();
    }
    
    public CirculoPolonio(Color cor){
        x = y = 0;
        raio = 10;
        this.cor = cor;
    }
    
    public CirculoPolonio(double x, double y, double raio){
        this.x = x;
        this.y = y;
        this.raio = raio;
        cor = corAleatoria();
    }
    
    public CirculoPolonio(double x, double y, double raio, Color cor){
        this.x = x;
        this.y = y;
        this.raio = raio;
        this.cor = cor;
    }
    
    public static Color corAleatoriaClara(){
        Random rand = new Random();

        int r = rand.nextInt(128) + 128;
        int g = rand.nextInt(128) + 128;
        int b = rand.nextInt(128) + 128;

        return new Color(r, g, b);
    }
    
    public static Color corAleatoria(){
        Random rand = new Random();

        int r = rand.nextInt(254) + 1;
        int g = rand.nextInt(254) + 1;
        int b = rand.nextInt(254) + 1;

        return new Color(r, g, b);
    }

    @Override
    public void desenha(Graphics g) {
        g.setColor(cor);
        int raio_local = (int) this.raio * CirculoPolonio.escala;
        int x_local = (int) this.x * CirculoPolonio.escala;
        int y_local = (int) this.y * CirculoPolonio.escala;
        g.fillOval(x_local - raio_local, y_local - raio_local, raio_local *  2, raio_local * 2);
    }
    
    @Override
    public double getArea() {
        double raio_local = (int) this.raio * CirculoPolonio.escala;
        return Math.PI * raio_local * raio_local;
    }

    @Override
    public boolean teste(FormaAbstrata c){
        return false;
    }
}
