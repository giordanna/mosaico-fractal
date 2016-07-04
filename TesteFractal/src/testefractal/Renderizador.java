package testefractal;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Renderizador extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dimension dimensao;

    public Renderizador(){
        dimensao = new Dimension(600, 600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Area.instancia().desenhaFormas((Graphics2D) g);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return this.dimensao;
    }
}