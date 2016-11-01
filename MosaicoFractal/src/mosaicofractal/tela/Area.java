package mosaicofractal.tela;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mosaicofractal.elementos.Estampa;
import mosaicofractal.elementos.Preenchimento;

public class Area extends JFrame{
    
    public static Area area;
    public static int LARGURA = 500, ALTURA = 500;
    private int largura_salva = 500, altura_salva = 500;
    private Color cor_fundo = Color.WHITE;
    private boolean considerar_bordas = false, mudar_angulo = false, tela_personalizada = false, usa_textura = false;
    private Shape forma_tela = null;
    private final ArrayList<Estampa> estampas;
    private Shape forma;
    private final static Random r = new Random();
    private static double x, y;
    private double area_tela = LARGURA * ALTURA;
    public Renderizador renderizador;
    
    public Area(boolean considerar_bordas, boolean mudar_angulo, boolean tela_personalizada, boolean usa_textura) {
        this.considerar_bordas = considerar_bordas;
        this.mudar_angulo = mudar_angulo;
        this.tela_personalizada = tela_personalizada;
        this.usa_textura = usa_textura;
        
        estampas = new ArrayList<>();
        renderizador = new Renderizador();
        
        setTitle("Resultado");
        
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        add(renderizador);
        setContentPane(renderizador);
        pack();
        setVisible(true);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
    }
    
    public static void iniciar(boolean considerar_bordas, boolean mudar_angulo, boolean tela_personalizada, boolean usa_textura) {
        area = new Area(considerar_bordas, mudar_angulo, tela_personalizada, usa_textura);
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
    
    public boolean isTelaPersonalizada() {
        return tela_personalizada;
    }
    
    public double getArea() {
        return area_tela;
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
    
    public void preencherArea(Shape forma, Shape forma_tela, ArrayList<Preenchimento> preenchimentos, Color cor_fundo, double c, int formas_max, int iteracoes_max) {
        final double preenchimento_max = 0.99;
        this.cor_fundo = cor_fundo;
        
        AffineTransform ajusta = AffineTransform.getScaleInstance(LARGURA/100.0, ALTURA/100.0);
        this.forma = ajusta.createTransformedShape(forma);
        
        if (tela_personalizada) {
            this.forma_tela = ajusta.createTransformedShape(forma_tela);
            area_tela = Estampa.calculaArea(this.forma_tela);
        }
        
        long tempoInicial = System.currentTimeMillis();
        double teste_porcentagem, area_preenchida,
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
                porcentagem = area_razao * valorControle(valor_n, exp_u),
                porcentagem_original = area_razao;

        boolean teste;
        
        System.out.println("c = " + c + " | zeta = " + valor_zeta + " | razão = " + area_razao
        + "| primeira forma = " + porcentagem);
        
        ajusta = AffineTransform.getScaleInstance(porcentagem, porcentagem);
        Shape forma_escolhida = ajusta.createTransformedShape(this.forma);
        
        if (mudar_angulo) {
            ajusta = AffineTransform.getRotateInstance(Math.random() * Math.PI * 2, forma_escolhida.getBounds2D().getCenterX(), forma_escolhida.getBounds2D().getCenterY());
            forma_escolhida = ajusta.createTransformedShape(forma_escolhida);
        }
        
        encontraXeY(forma_escolhida);
        
        if (usa_textura) {
            estampas.add(new Estampa(forma_escolhida, preenchimentos.get(0), x, y));
        }
        else {
            estampas.add(new Estampa(forma_escolhida, preenchimentos.get(r.nextInt(preenchimentos.size())), x, y));
        }
        
        double area_total = estampas.get(0).getArea();
        
        do { // loop no número de círculos
        
            numero_iteracoes = 0;
            int index = 0;
            teste_porcentagem = porcentagem_original * valorControle(qtd_formas + valor_n, exp_u);
            
            ajusta = AffineTransform.getScaleInstance(teste_porcentagem, teste_porcentagem);
            forma_escolhida = ajusta.createTransformedShape(this.forma);
            do { // busca aleatória
                
                if (mudar_angulo) {
                    ajusta = AffineTransform.getRotateInstance(Math.random() * Math.PI * 2, forma_escolhida.getBounds2D().getCenterX(), forma_escolhida.getBounds2D().getCenterY());
                    forma_escolhida = ajusta.createTransformedShape(forma_escolhida);
                }
                encontraXeY(forma_escolhida);
                
                numero_iteracoes++;
                teste = true;
                
                Estampa obj_teste;
                if (usa_textura) {
                    obj_teste = new Estampa(forma_escolhida, preenchimentos.get(0), x, y);
                    index = 0;
                }
                else {
                    index = r.nextInt(preenchimentos.size());
                    obj_teste = new Estampa(forma_escolhida, preenchimentos.get(index), x, y);
                }
                
                for (int k = 0 ; k < qtd_formas ; k++) {//loops pelas outras formas
                    teste = Estampa.intersecta(obj_teste, estampas.get(k));
                    if (teste) break;
                } // próximo k
            } while (teste); // repetir se ficou muito perto de um círculo
            numero_iteracoes_total += numero_iteracoes;
            synchronized(estampas){
                estampas.add(new Estampa(forma_escolhida, preenchimentos.get(index), x, y));
            }
            
            area_total += estampas.get(qtd_formas).getArea();
            area_preenchida = area_total / getArea();
            qtd_formas++;
        
        } while (numero_iteracoes_total < iteracoes_max && qtd_formas < nmax && area_preenchida < preenchimento_max);
        System.out.println("área preenchida = " + Math.round(area_preenchida * 100) + "%");
        System.out.println("número de iterações = " + numero_iteracoes_total);
        System.out.println("número de formas = " + qtd_formas);
        revalidate();
        repaint();
        
        System.out.println("Tempo de execução = " + (System.currentTimeMillis() - tempoInicial)/1000.0);
        
        salvarImagem();
    }
    
    public void encontraXeY(Shape forma) {
        if (considerar_bordas) {
            x = Math.random() * (LARGURA -  forma.getBounds2D().getWidth());
            y = Math.random() * (ALTURA -  forma.getBounds2D().getHeight());
            if (tela_personalizada) {
                // encontrar ponto aleatório dentro da forma
                while (!forma_tela.contains(x, y) ||
                        !forma_tela.contains(x + forma.getBounds2D().getWidth(),
                                y + forma.getBounds2D().getHeight()) ||
                        !forma_tela.contains(x + forma.getBounds2D().getWidth(), y) ||
                        !forma_tela.contains(x,y + forma.getBounds2D().getHeight())
                        ){
                    x = Math.random() * (LARGURA -  forma.getBounds2D().getWidth());
                    y = Math.random() * (ALTURA -  forma.getBounds2D().getHeight());
                }
            }
        }
        else {
            x = Math.random() * (LARGURA -  forma.getBounds2D().getWidth());
            y = Math.random() * (ALTURA -  forma.getBounds2D().getHeight());
        }
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
        int n;
        do {
            String[] opcao_salva = { "Salvar como PNG", "Salvar como SVG", "Não" };
            n = JOptionPane.showOptionDialog(this, "Deseja salvar esta imagem?", "Salvar?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcao_salva, opcao_salva[0]);
            switch(n) {
                case 0:{
                    SalvaImagem.salvarPNG();
                    break;
                }
                case 1:{
                    SalvaImagem.salvarSVG();
                    break;
                }
            }
        } while (n != -1 && n != 2);
    }
}
