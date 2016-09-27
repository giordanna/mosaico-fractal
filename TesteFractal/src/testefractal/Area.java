package testefractal;

import testefractal.formas.Forma;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFrame;

public class Area extends JFrame {
    private static Area singleton;
    public Renderizador renderizador;
    
    private ArrayList<Forma> formas;
    private final int altura = 600, largura = 600;
    
    private Area(){
        formas = new ArrayList<>();
        renderizador = new Renderizador();
        
        setTitle("TesteFractal");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(renderizador);
        setContentPane(renderizador);
        pack();
        setVisible(true);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
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
    
    public int getAltura() { return altura; }
    public int getLargura() { return altura; }
    
    public ArrayList<Forma> getFormas() { return formas; }

    public void desenhaFormas(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, largura, altura);
        
        // formas e letras não ficarão serrilhadas
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Iterator<Forma> x = formas.iterator() ; x.hasNext();){
            Forma forma = x.next();
            forma.desenha(g2d);
        }
    }
}
