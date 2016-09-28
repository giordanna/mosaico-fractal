package testefractal.formas;

import java.awt.Graphics;

public abstract class FormaAbstrata {
    public abstract void desenha(Graphics g);
    public abstract double getArea();
    public abstract boolean teste(FormaAbstrata f);
    
}
