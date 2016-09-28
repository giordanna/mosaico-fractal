package testefractal.formas;

import java.awt.Color;
import java.util.Random;

public class CirculoPolonio {
    public Color cor;
    public double x, y, raio;
    
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
}
