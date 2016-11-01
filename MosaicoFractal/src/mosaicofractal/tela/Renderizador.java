package mosaicofractal.tela;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class Renderizador extends JPanel{
    private static final long serialVersionUID = 1L;
    private final Dimension dimensao;
    private static int qtd = 0;

    public Renderizador(){
        dimensao = new Dimension(Area.LARGURA, Area.ALTURA);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Area.area.pintar(g2d);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return this.dimensao;
    }
}