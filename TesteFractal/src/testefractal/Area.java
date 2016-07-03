package testefractal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Area extends JFrame {
    private static Area singleton;
    
    private ArrayList<Circulo> formas;
    private final int altura = 600, largura = 600;
    
    private Area(){
        formas = new ArrayList<>();
        
        setTitle("TesteFractal");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(largura, altura));
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - largura/2, dim.height/2 - altura/2);
        pack();
        setVisible(true);
    }
    
    @Override
    public void paint(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, largura, altura);
        
        formas.stream().forEach((x) -> {
            x.desenha(g);
        });
    }
    
    public static Area instancia(){
        if (singleton == null){
            singleton = new Area();
        }
        return singleton;
    }
    
    public double getArea(){
        return largura * altura;
    }
    
    public double getAltura() { return altura; }
    
    public double getLargura() { return altura; }
    
    public ArrayList<Circulo> getFormas() { return formas; }
}
