package mosaicofractal.tela;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.util.ArrayList;
import javax.swing.JPanel;
import mosaicofractal.elementos.Estampa;
import mosaicofractal.elementos.Preenchimento;

public class Area extends JPanel{
    private static Area singleton;
    public final static int LARGURA = 500, ALTURA = 500;
    private final Dimension dimensao;
    private int largura_salva = 500, altura_salva = 500;
    private Color cor_fundo = Color.WHITE;
    private boolean considerar_bordas = false, mudar_angulo = false, forma_personalizada = false;
    private Shape forma_area = null;
    private ArrayList<Estampa> estampas;
    
    public Area() {
        estampas = new ArrayList<>();
        dimensao = new Dimension(LARGURA, ALTURA);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return this.dimensao;
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(this.cor_fundo);
        
        if (this.forma_personalizada) {
            g2d.draw(this.forma_area);
        }
        else{
            g2d.fillRect(0, 0, LARGURA, ALTURA);
        }
        
        estampas.stream().forEach((forma) -> {
            forma.desenha(g2d);
        });
        
        revalidate();
        repaint();
    }
    
    public static Area instancia() {
        if (singleton == null){
            singleton = new Area();
        }
        return singleton;
    }
    
    public void setLargura(int largura) {
        this.largura_salva = largura;
    }
    
    public void setAltura(int altura) {
        this.altura_salva = altura;
    }

    public void considerarBordas() {
        this.considerar_bordas = !this.considerar_bordas;
    }
    
    public void mudarAngulo() {
        this.mudar_angulo = !this.mudar_angulo;
    }
    
    public void mudarCorFundo(Color cor) {
        this.cor_fundo = cor;
    }
    
    public void setFormaArea(Shape forma) {
        this.forma_area = forma;
        this.forma_personalizada = this.forma_area != null;
    }
    
    public double getArea() {
        if (!forma_personalizada){
            return largura_salva * altura_salva;
        }
        return 0; // ver como calcular área de qualquer shape
    }
    
    public Color getCorFundo() {
        return this.cor_fundo;
    }
    
    public ArrayList<Estampa> getEstampas() {
        return this.estampas;
    }
    
    public int getLargura() {
        return this.largura_salva;
    }
    
    public int getAltura() {
        return this.altura_salva;
    }
    
    public boolean isFormaPersonalizada() {
        return this.forma_personalizada;
    }
    
    public Shape getFormaFundo() {
        return this.forma_area;
    }
    
    public void preencherArea(ArrayList<Shape> formas, ArrayList<Preenchimento> preenchimentos) {
        // faz a mágica
    }
    
    public void salvarImagem() {
        // faz a mágica
    }
}
