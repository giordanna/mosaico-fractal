package mosaicofractal.tela;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * A classe <code>Renderizador</code> serve para renderizar corretamente os 
 * elementos gráficos da tela. Ela possui como objetivo impedir que elementos 
 * sejam renderizador abaixo da barra de título da janela que é aberta quando 
 * se deseja visualizar a tela. Além de permitir que a tela possua as devidas 
 * dimensões de largura e altura.s
 * 
 * @author Giordanna
 * @see Tela
 */
public class Renderizador extends JPanel{
    /**
     * Variável para definir a dimensão correta da tela do renderizador.
     */
    private final Dimension dimensao;
    
    /**
     * Construtor para definir a dimensão da tela.
     */
    public Renderizador(){
        dimensao = new Dimension(Tela.LARGURA, Tela.ALTURA);
        setPreferredSize(dimensao);
    }
    
    /**
     * Função para repassar o objeto <code>Graphics2D</code> para a função 
     * <code>pintaTela()</code>. Esta função sobrescrita realiza um parsing 
     * de <code>Graphics</code> para <code>Graphics2D</code>, assim podendo 
     * definir que a renderização use antialiasing, tendo uma qualidade melhor.
     * 
     * @param g objeto usado para realizar as operações de pintura
     * @see mosaicofractal.tela.Tela#pintaTela(java.awt.Graphics2D) 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Tela.tela.pintaTela(g2d);
    }
    
    /**
     * Retorna a variável <code>dimensao</code> contida no renderizador.
     * 
     * @return a dimensão da tela
     */
    @Override
    public Dimension getPreferredSize() {
        return this.dimensao;
    }
}
