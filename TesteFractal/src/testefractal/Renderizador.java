package testefractal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class Renderizador extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Dimension dimensao;

    public Renderizador(){
        dimensao = new Dimension(600, 600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Area.instancia().getLargura(), Area.instancia().getAltura());
        
        // formas e letras não ficarão serrilhadas
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        synchronized(Area.instancia().getFormas()){
            Area.instancia().getFormas().stream().forEach((forma) -> {
                forma.desenha(g2d);
            }); //Area.instancia().desenhaFormas((Graphics2D) g);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return this.dimensao;
    }
}