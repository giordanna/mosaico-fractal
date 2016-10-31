package mosaicofractal.tela;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import mosaicofractal.elementos.Estampa;
import mosaicofractal.elementos.Preenchimento;

public class Area extends JPanel{
    private static Area singleton;
    public final static int LARGURA = 500, ALTURA = 500;
    private final Dimension dimensao;
    private int largura_salva = 500, altura_salva = 500;
    private Color cor_fundo = Color.WHITE;
    private boolean considerar_bordas = false, mudar_angulo = false, tela_personalizada = false;
    private Shape forma_tela = null;
    private ArrayList<Estampa> estampas;
    private ArrayList<Shape> formas;
    private ArrayList<Preenchimento> preenchimentos;
    private static Random r = new Random();
    
    public Area() {
        estampas = new ArrayList<>();
        formas = new ArrayList<>();
        preenchimentos = new ArrayList<>();
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
        
        if (this.tela_personalizada) {
            g2d.draw(this.forma_tela);
        }
        else{
            g2d.fillRect(0, 0, LARGURA, ALTURA);
        }
        
        estampas.stream().forEach((estampa) -> {
            estampa.desenha(g2d);
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

    public void considerarBordas(boolean bordas) {
        this.considerar_bordas = bordas;
    }
    
    public void mudarAngulo(boolean angulo) {
        this.mudar_angulo = angulo;
    }
    
    public void mudarCorFundo(Color cor) {
        this.cor_fundo = cor;
    }
    
    public void setFormaFundo(Shape forma) {
        this.forma_tela = forma;
        this.tela_personalizada = this.forma_tela != null;
    }
    
    public double getArea() {
        if (!tela_personalizada){
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
    
    public Shape getFormaFundo() {
        return this.forma_tela;
    }
    
    public void preencherArea(ArrayList<Shape> formas, ArrayList<Preenchimento> preenchimentos, double c) {
        final int formas_max = 90000, iteracoes_max = 400000;
        final double preenchimento_max = 0.99;
        
        long tempoInicial = System.currentTimeMillis();
        double teste_raio, area_preenchida,
               exp_u = 0.5 * c; // metade desse valor
        
        int qtd_formas = 1,
            numero_iteracoes_total = 0,
            numero_iteracoes,
            valor_n = 2,
            nmax = formas_max + 1;

        double  valor_zeta = funcaoZeta(c, valor_n), // o valor que vai determinar a porcentagem. ex: 4 = 25%
                area_razao = 1.0 / valor_zeta, // ex: valor_zeta = 4, area_razao = 1/4 = 25%
                // raio gerado multiplicado por uma porcentagem de controle. quanto maior c, menor o valor multiplicado
                // então menor será o raio de fato, que não será um círculo gigante preenchendo 25% da tela, mas
                // um pouco menor
                raio_forma = (area_razao/2.0) * valorControle(valor_n, exp_u),
                raio_original_primeiro = area_razao/2.0;

        boolean teste;
        
        Random r = new Random();
        
        System.out.println("c = " + c + " | zeta = " + valor_zeta + " | razão = " + area_razao
        + "| raio = " + raio_forma);
        
        double x = 1 - r.nextDouble();
        double y = 1 - r.nextDouble();
        
        if (forma_tela != null) {
            // encontrar ponto aleatório dentro da forma
            while (!forma_tela.contains(x, y)){
                x = 1 - r.nextDouble();
                y = 1 - r.nextDouble();
            }
        }
        
        Estampa estampa_escolhida = new Estampa(estampas.get(r.nextInt(estampas.size())));
        if (!considerar_bordas){
            estampas.add(estampa_escolhida);
        }
        else{
            
        }

        double area_total = estampas.get(0).getArea();
        do { // loop no número de círculos
        
            numero_iteracoes = 0;
            teste_raio = raio_original_primeiro * valorControle(qtd_formas + valor_n, exp_u);
            do { // busca aleatória
            
                x = teste_raio + Math.random() * (Area.instancia().getLargura() -  teste_raio * 2);
                y = teste_raio + Math.random() * (Area.instancia().getAltura() - teste_raio * 2);
                numero_iteracoes++;
                teste = true;
                Circulo obj_teste = new Circulo((int) x, (int) y, (int) teste_raio);
                for (int k = 0; k < qtd_formas; k++) {//loop over old placements
                    teste = obj_teste.teste(Area.instancia().getFormas().get(k));
                    if (!teste) break;
                } // próximo k
            } while (!teste); // repetir se ficou muito perto de um círculo

            numero_iteracoes_total += numero_iteracoes;
            synchronized(estampas){
                estampas.add(new Circulo((int) x, (int) y, (int) teste_raio));
            }
            
            area_total += estampas.get(qtd_formas).getArea();
            area_preenchida = area_total / (Area.instancia().getArea());
            qtd_formas++;
        } while (numero_iteracoes_total < iteracoes_max && qtd_formas < nmax && area_preenchida < preenchimento_max);
        System.out.println("área preenchida = " + Math.round(area_preenchida * 100) + "%");
        System.out.println("número de iterações = " + numero_iteracoes_total);
        System.out.println("número de formas = " + qtd_formas);
        Area.instancia().revalidate();
        Area.instancia().repaint();
        
        System.out.println((System.currentTimeMillis() - tempoInicial)/1000.0);
    }
    
    public double funcaoZeta(double c, int N) {
        double soma = 0;
        int NEXP = 100000;
        for (double i = N; i < NEXP; i++) {
            soma += Math.pow(i, -c);
        }
        return soma + soma_estimada(c, NEXP);
    }

    public double soma_estimada(double c, double n) {
        return (1.0 / (c - 1)) * Math.pow(n, 1 - c);
    }
    
    public double valorControle(double valor_n, double exp_u){
        return Math.pow(valor_n, -exp_u);
    }
    
    public void salvarImagem() {
        // faz a mágica
    }
}
