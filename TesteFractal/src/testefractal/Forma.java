package testefractal;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Forma {
    protected Color cor;
    
    public Forma(){
        cor = Color.BLACK;
    }
    
    public Forma(Color cor){
        this.cor = cor;
    }
    
    public abstract double getArea();
    
    public abstract void desenha(Graphics g);
}
