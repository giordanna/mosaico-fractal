package mosaicofractal.tela;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class Renderizador extends JPanel{
    private static final long serialVersionUID = 1L;
    private final Dimension dimensao;

    public Renderizador(){
        dimensao = new Dimension(Area.LARGURA, Area.ALTURA);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Area.area.getCorFundo());
        
        if (Area.area.isTelaPersonalizada()) {
            g2d.fill(Area.area.getFormaFundo());
        }
        else{
            g2d.fillRect(0, 0, Area.LARGURA, Area.ALTURA);
        }
        
        // formas e letras não ficarão serrilhadas
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        synchronized(Area.area.getEstampas()){
            Area.area.getEstampas().stream().forEach((estampa) -> {
                estampa.desenha(g2d);
            }); //Area.instancia().desenhaFormas((Graphics2D) g);
        }
        Area.area.revalidate();
        Area.area.repaint();
    }
    
    @Override
    public Dimension getPreferredSize() {
        return this.dimensao;
    }
}
