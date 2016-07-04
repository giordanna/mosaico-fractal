package testefractal;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public abstract class Forma {
    protected Color cor;
    protected int x, y, raio;
    
    public Forma(){
        x = y = 0;
        raio = 10;
        cor = corAleatoria();
    }
    
    public Forma(Color cor){
        x = y = 0;
        raio = 10;
        this.cor = cor;
    }
    
    public Forma(int x, int y, int raio){
        this.x = x;
        this.y = y;
        this.raio = raio;
        cor = corAleatoria();
    }
    
    public Forma(int x, int y, int raio, Color cor){
        this.x = x;
        this.y = y;
        this.raio = raio;
        this.cor = cor;
    }
    
    public static Color corAleatoria(){
        Random rand = new Random();
        int r = rand.nextInt(128) + 128;
        int g = rand.nextInt(128) + 128;
        int b = rand.nextInt(128) + 128;
        
        return new Color(r, g, b);
    }
    
    public abstract double distancia(Forma c2);
    public abstract boolean teste(Forma c2, double soma_raios);
    public abstract double raioGerado(double area_razao);
    public abstract double getArea();
    public abstract void desenha(Graphics g);
}
