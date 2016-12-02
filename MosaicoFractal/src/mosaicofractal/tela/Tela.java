package mosaicofractal.tela;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mosaicofractal.elementos.Estampa;
import mosaicofractal.elementos.MersenneTwisterFast;
import mosaicofractal.elementos.Preenchimento;

/**
 * A classe <code>Tela</code> é usada para encapsular as funções principais
 * do programa na geração da tela e preenchimento com estampas, de acordo com
 * os valores definidos pelo usuário através da {@link mosaicofractal.gui.InterfaceUsuario}. 
 * 
 * @author      Giordanna De Gregoriis
 * @see         mosaicofractal.gui.InterfaceUsuario
 * @see         mosaicofractal.elementos.Estampa
 * @see         mosaicofractal.elementos.Preenchimento
 * @see         Renderizador
 * @see         SalvaImagem
 */
public class Tela {
    
    /**
     * Variável "singleton" usada para ser manipulada.
     */
    public static Tela tela;
    
    /**
     * Valores que definem a altura e largura da tela da jFrame.
     */
    public static final int LARGURA = 500, ALTURA = 500;
    
    /**
     * jPanel utilizada para renderizar elementos da tela.
     */
    public Renderizador renderizador;
    
    /**
     * jFrame que receberá os componentes gráficos gerados por esta classe. O 
     * renderizador irá renderizar os componentes neste jFrame.
     */
    private final JFrame jFrame;
    
    /**
     * Cor do fundo da tela. Default é a cor branca.
     */
    private Color corDoFundo = Color.WHITE;
    
    /**
     * Variáveis utilizadas para manipular condições dentro do programa.
     */
    private boolean isConsiderarBordas = false, isMudarAngulo = false, isFormaTelaPersonalizada = false, isUsarTextura = false;
    
    /**
     * Formas utilizadas para gerar elementos gráficos na tela.
     */
    private Shape shapeFormaTela = null, shapeFormaUsada;
    
    /**
     * Estampa utilizada para verificar casos de intersecção com as demais 
     * estampas já inseridas na tela.
     */
    private Estampa estampaTeste;
    
    /**
     * Valor para controlar a quantidade de estampas já inseridas na tela.
     */
    private int quantidadeEstampas;
    
    /**
     * Valores para controlar a execução das threads. O programa só poderá 
     * prosseguir se as duas threads terminarem suas execuções.
     */
    private int threadA, threadB;
    
    /**
     * A área da tela. Default é a área da tela do jFrame.
     */
    private double valorAreaTela = LARGURA * ALTURA;
    
    /**
     * Valores das coordenadas X e Y. Utilizadas para determinar a posição das 
     * estampas a serem inseridas na tela.
     */
    private double x, y;
    
    /**
     * Valores para controlar a execução das threads. O programa só poderá 
     * prosseguir se as duas threads terminarem suas execuções.
     */
    private final ArrayList<Estampa> estampasAdicionadas;
    
    /**
     * Gerador de valores aleatórios.
     * @see MersenneTwisterFast
     */
    private final static MersenneTwisterFast RAND = new MersenneTwisterFast();
    
    /**
     * Usado para verificar se execução será sequencial ou paralelizada.
     */
    private final boolean isUSARTHREAD = false;
    
    /**
     * Cria uma tela na qual serão adicionados diversas estampas ao longo
     * de sua execução da função <code>preencherArea()</code>.
     * 
     * @param considerar_bordas se as bordas serão consideradas limites ou não
     * @param mudar_angulo se as estampas serão rotacionadas ou não
     * @param tela_personalizada se a tela terá um formato personalizado ou não
     * @param usa_textura se será usado texturas ou cores para as estampas
     * @see #preencherArea(java.awt.Shape, java.awt.Shape, java.util.ArrayList, java.awt.Color, double, int, int) 
     */
    public Tela(boolean considerar_bordas, boolean mudar_angulo, boolean tela_personalizada, boolean usa_textura) {
        isConsiderarBordas = considerar_bordas;
        isMudarAngulo = mudar_angulo;
        isFormaTelaPersonalizada = tela_personalizada;
        isUsarTextura = usa_textura;
        
        estampasAdicionadas = new ArrayList<>();
        renderizador = new Renderizador();
        
        jFrame = new JFrame("Resultado");
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.add(renderizador);
        jFrame.setContentPane(renderizador);
        jFrame.pack();
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setLocation(dim.width/2-jFrame.getSize().width/2, dim.height/2-jFrame.getSize().height/2);
        jFrame.setVisible(true);
        //jframe.setResizable(false);
    }
    
    /**
     * Pinta todas as formas na tela. Esta função é utilizada na renderização 
     * da tela atráves do {@link Renderizador}.
     *
     * @param g2d objeto usado para realizar as operações de pintura
     * @see Renderizador
     */
    public void pintaTela(Graphics2D g2d) {
        g2d.setColor(corDoFundo);
        
        if (isFormaTelaPersonalizada) {
            g2d.fill(shapeFormaTela);
        }
        else {
            g2d.fillRect(0, 0, LARGURA, ALTURA);
        }
        
        estampasAdicionadas.stream().forEach((estampa) -> {
            estampa.desenha(g2d);
        });
    }
    
    /**
     * Retorna a soma aproximada dos valores da função Zeta. Valor utilizado 
     * para definir uma razão que será usada para determinar a área da primeira 
     * forma a ser inserida na região
     *
     * @param c constante utilizada na função, a qual deve ser maior que 1
     * @param N onde inicia o somatório
     * @return a soma dos valores da função Zeta
     */
    private double funcaoZeta(double c, int N) {
        double soma = 0;
        int NMAX = 100000;
        for (double i = N; i < NMAX; i++) {
            soma += Math.pow(i, -c);
        }
        return soma + soma_estimada(c, NMAX);
    }
    
    /**
     * Retorna valor usado para corrigir a função Zeta.
     *
     * @param c constante utilizada na função, a qual deve ser maior que 1
     * @param NMAX o último elemento do somatório
     * @return valor da correção da função Zeta
     */
    private double soma_estimada(double c, double NMAX) {
        return (1.0 / (c - 1)) * Math.pow(NMAX, 1 - c);
    }
    
    /**
     * Retorna a soma aproximada dos valores da função Zeta. Valor utilizado 
     * para definir uma razão que será usada para determinar a área da primeira 
     * forma a ser inserida na região
     *
     * @param c constante utilizada na função, a qual deve ser maior que 1
     * @param N onde inicia o somatório
     * @return a soma dos valores da função Zeta
     */
    private double valorControle(double valor_n, double exp_u){
        return Math.pow(valor_n, -exp_u);
    }
    
    /**
     * Retorna a razão entre a área da tela e a forma dada na entrada. 
     * Este valor é utilizado para redimensionar a estampa corretamente, 
     * igualando a sua área com a área da tela, como é utilizada em 
     * <code>preencherArea()</code>.
     *
     * @param forma a forma usada para calcular sua área
     * @return a razão área da tela / área da forma
     * @see #preencherArea(java.awt.Shape, java.awt.Shape, java.util.ArrayList, java.awt.Color, double, int, int) 
     */
    private double razaoArea(Shape forma) {
        double area_forma, razao;
        
        area_forma = Estampa.calculaArea(forma);
        razao = valorAreaTela / area_forma;
        
        return Math.sqrt(razao);
    }
    
    /**
     * Função para definir aleatóriamente as posições x e y da forma. 
     * Nesta função há verificações se as bordas serão consideradas ou não. 
     * Caso sim, deve-se verificar se a tela possui forma personalizada. Se 
     * sim, a posição a ser encontrada da forma deve estar dentro da forma da 
     * tela. Se houver demora para encontrar esta posição, será retornado que 
     * houve um caso excepcional. Caso não considere as os limites da tela, 
     * uma posição aleatória dentro da largura e altura da tela serão 
     * utilizadas.
     * 
     * @param forma a forma usada para calcular sua área
     * @param iteracoes_max número de iterações máximas
     * @return se houve um caso excepcional ou não
     */
    private boolean encontraXeY(Shape forma, int iteracoes_max) {
        if (isConsiderarBordas) {
            x = Math.random() * (LARGURA -  forma.getBounds2D().getWidth());
            y = Math.random() * (ALTURA -  forma.getBounds2D().getHeight());
            if (isFormaTelaPersonalizada) {
                int iteracoes_posicao = 0;
                
                while (!shapeFormaTela.contains(x, y) ||
                        Estampa.estaDentro(x, y, shapeFormaTela, forma)){
                    
                    x = Math.random() * (LARGURA -  forma.getBounds2D().getWidth());
                    y = Math.random() * (ALTURA -  forma.getBounds2D().getHeight());
                    iteracoes_posicao++;
                    if (iteracoes_posicao > iteracoes_max) {
                        return true;
                    }
                }
            }
        }
        else {
            x = Math.random() * LARGURA;
            y = Math.random() * ALTURA;
        }
        return false;
    }
    
    /**
     * A classe privada <code>Thread1</code> serve para realizar a função de 
     * paralelizar a função de verificação se há intersecção com a forma que se 
     * deseja posicionar na tela com as demais que já foram inseridas.
     * @author Giordanna De Gregoriis
     */
    private class Thread1 extends Thread {
        
        /**
         * Esta thread cuida da primeira metade das estampas adicionadas no 
         * momento.
         */
        @Override
        public void run() {
            threadA = 0;
            for (int i = 0 ; i < quantidadeEstampas / 2 ; i++) {
                if (Estampa.intersecta(estampaTeste, estampasAdicionadas.get(i))) {
                    threadA = 2; 
                    return;
                }
            }
            threadA = 1;
        }
    }
    
    /**
     * A classe privada <code>Thread2</code> serve para realizar a função de 
     * paralelizar a função de verificação se há intersecção com a forma que se 
     * deseja posicionar na tela com as demais que já foram inseridas.
     * @author Giordanna De Gregoriis
     */
    private class Thread2 extends Thread {
        
        /**
         * Esta thread cuida da segunda metade das estampas adicionadas no 
         * momento.
         */
        @Override
        public void run() {
            threadB = 0;
            for (int i = quantidadeEstampas / 2 ; i < quantidadeEstampas ; i++) {
                if (Estampa.intersecta(estampaTeste, estampasAdicionadas.get(i))) {
                    threadB = 2; 
                    return;
                }
            }
            threadB = 1;
        }
    }
    
    /**
     * Verifica se o teste de verificação de intersecção entre estampas será 
     * executada sequencialmente ou irá utilizar Threads.
     * 
     * @return se houve intersecção ou não
     * @see Thread1
     * @see Thread2
     */
    private boolean isUsarThread(int qtd_formas, Estampa obj_teste) {
        boolean teste = false;
        if (isUSARTHREAD) {
            quantidadeEstampas = qtd_formas;
            this.estampaTeste = new Estampa(obj_teste);

            threadA = threadB = 0;
            new Thread1().start();
            new Thread2().start();
            while (threadA == 0 || threadB == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            teste = (threadA == 2 || threadB == 2);
        }
        else{
            for (int k = 0 ; k < qtd_formas ; k++) {
                teste = Estampa.intersecta(obj_teste, estampasAdicionadas.get(k));
                if (teste) break;
            }
        }
        
        return teste;
    }
    
    /**
     * Função principal da classe, onde realiza os devidos cálculos para 
     * posicionar as estampas na tela.
     *
     * @param forma forma das estampas a seren posicionadas
     * @param forma_tela formato da tela
     * @param preenchimentos preenchimentos utilizados para as estampas
     * @param formas_max número máximo de estampas a serem inseridas na tela
     * @param cor_fundo cor utilizada no fundo da tela
     * @param iteracoes_max número máximo de iterações para os testes realizados
     * @param c constante a ser utilizada na <code>funcaoZeta()</code>
     * @see #funcaoZeta(double, int) 
     */
    public void preencherArea(Shape forma, Shape forma_tela, ArrayList<Preenchimento> preenchimentos, Color cor_fundo, double c, int formas_max, int iteracoes_max) {
        
        Estampa obj_teste = null;
        
        final double preenchimento_max = 0.99; // um dos critérios de parada, se o preenchimento da área for maior que 99%
        this.corDoFundo = cor_fundo; // cor da tela do fundo
        
        double escala;
        AffineTransform ajusta;
        
        // se a tela do fundo for uma forma específica, define a forma da tela escalada (de 100x100 para 500x500)
        if (this.isFormaTelaPersonalizada) {
            
            if (forma_tela.getBounds2D().getWidth() > forma_tela.getBounds2D().getHeight()) {
                escala = LARGURA / forma_tela.getBounds2D().getWidth();
            }
            else {
                escala = ALTURA / forma_tela.getBounds2D().getHeight();
            }
            ajusta = AffineTransform.getScaleInstance(escala, escala);
        
            this.shapeFormaTela = ajusta.createTransformedShape(forma_tela);
            this.valorAreaTela = Estampa.calculaArea(this.shapeFormaTela);
            
            // coloca na origem
            ajusta = AffineTransform.getTranslateInstance(-this.shapeFormaTela.getBounds2D().getX(), -this.shapeFormaTela.getBounds2D().getY());
            this.shapeFormaTela = ajusta.createTransformedShape(this.shapeFormaTela);
            
            // coloca no centro da janela
            ajusta = AffineTransform.getTranslateInstance(LARGURA/2.0 - (this.shapeFormaTela.getBounds2D().getCenterX()), ALTURA/2.0 - (this.shapeFormaTela.getBounds2D().getCenterY()));
            this.shapeFormaTela = ajusta.createTransformedShape(this.shapeFormaTela);
        }
        
        // escala a forma para o tamanho da tela
        double razao_ajuste = razaoArea(forma);
        ajusta = AffineTransform.getScaleInstance(razao_ajuste, razao_ajuste);
        this.shapeFormaUsada = ajusta.createTransformedShape(forma);
        
        // coloca forma na origem
        ajusta = AffineTransform.getTranslateInstance(-this.shapeFormaUsada.getBounds2D().getX(), -this.shapeFormaUsada.getBounds2D().getY());
        this.shapeFormaUsada = ajusta.createTransformedShape(this.shapeFormaUsada);
        
        // inicia contador para ver a duração do algoritmo
        long tempoInicial = System.currentTimeMillis();
        
        double teste_porcentagem, area_preenchida,
               exp_u = 0.5 * c; // metade desse valor c
        
        int qtd_formas = 1,
            numero_iteracoes_total = 0,
            numero_iteracoes,
            valor_n = 2,
            nmax = formas_max + 1;

        double  valor_zeta = funcaoZeta(c, valor_n), // o valor que vai determinar a porcentagem. ex: 4 = 25%
                area_razao = 1.0 / valor_zeta, // ex: valor_zeta = 4, area_razao = 1/4 = 25%
                porcentagem = area_razao * valorControle(valor_n, exp_u),
                porcentagem_original = area_razao;
        
        boolean teste; // variável para verificar se uma tarefa passou no teste
        boolean caso_excepcional = false; // variável para tratar exceções
        
        ajusta = AffineTransform.getScaleInstance(porcentagem, porcentagem);
        Shape forma_escolhida = ajusta.createTransformedShape(this.shapeFormaUsada);
        
        // se for rotacionar a forma, executa o que estiver dentro da condição
        // rotação em radianos
        if (isMudarAngulo) {
            ajusta = AffineTransform.getRotateInstance(Math.random() * Math.PI * 2, forma_escolhida.getBounds2D().getCenterX(), forma_escolhida.getBounds2D().getCenterY());
            forma_escolhida = ajusta.createTransformedShape(forma_escolhida);
        }
        
        // verifica se, depois de o número máximo de iterações, ainda não foi possivel encontrar as posições X e Y
        // acontece quando o valor de c é muito grande para a relação entre a forma e a tela
        caso_excepcional = encontraXeY(forma_escolhida, iteracoes_max);
        // se aconteceu a exceção, reduz o tamanho da primeira forma
        while (caso_excepcional) {
            valor_n++;
            porcentagem = area_razao * valorControle(valor_n, exp_u);
            ajusta = AffineTransform.getScaleInstance(porcentagem, porcentagem);
            forma_escolhida = ajusta.createTransformedShape(this.shapeFormaUsada);
            if (isMudarAngulo) {
                ajusta = AffineTransform.getRotateInstance(Math.random() * Math.PI * 2, forma_escolhida.getBounds2D().getCenterX(), forma_escolhida.getBounds2D().getCenterY());
                forma_escolhida = ajusta.createTransformedShape(forma_escolhida);
            }
            caso_excepcional = encontraXeY(forma_escolhida, iteracoes_max);
        }
        
        System.out.println("c = " + c + " | zeta = " + valor_zeta + " | razão = " + area_razao
        + "| primeira forma = " + porcentagem);
        
        // se utiliza textura
        if (isUsarTextura) {
            estampasAdicionadas.add(new Estampa(forma_escolhida, preenchimentos.get(0), x, y));
        }
        else {
            // cor aleatória
            estampasAdicionadas.add(new Estampa(forma_escolhida, preenchimentos.get(RAND.nextInt(preenchimentos.size())), x, y));
        }
        
        // inicia contagem da porcentagem preenchida da área
        double area_total = estampasAdicionadas.get(0).getArea();
        area_preenchida = area_total / valorAreaTela;
        
        do { // loop no número de círculos
        
            numero_iteracoes = 0;
            int index = 0;
            
            teste_porcentagem = porcentagem_original * valorControle(qtd_formas + valor_n, exp_u);
            ajusta = AffineTransform.getScaleInstance(teste_porcentagem, teste_porcentagem);
            forma_escolhida = ajusta.createTransformedShape(this.shapeFormaUsada);
            
            do { // busca aleatória
                
                if (isMudarAngulo) {
                    ajusta = AffineTransform.getRotateInstance(Math.random() * Math.PI * 2, forma_escolhida.getBounds2D().getCenterX(), forma_escolhida.getBounds2D().getCenterY());
                    forma_escolhida = ajusta.createTransformedShape(forma_escolhida);
                }
                caso_excepcional = encontraXeY(forma_escolhida, iteracoes_max);
                
                if (caso_excepcional) {
                    System.out.println("opa! a estampa não consegue ser colocada na forma! vamos terminar o processo mais cedo...");
                    break;
                }
                
                numero_iteracoes++;
                teste = true;
                
                if (isUsarTextura) {
                    obj_teste = new Estampa(forma_escolhida, preenchimentos.get(0), x, y);
                    index = 0;
                }
                else {
                    index = RAND.nextInt(preenchimentos.size());
                    obj_teste = new Estampa(forma_escolhida, preenchimentos.get(index), x, y);
                }
                
                teste = isUsarThread(qtd_formas, obj_teste);
                
                if (numero_iteracoes > iteracoes_max){
                    caso_excepcional = true;
                    System.out.println("opa! a estampa não consegue ser colocada na forma! vamos terminar o processo mais cedo...");
                    break;
                }
            } while (teste);
            if (caso_excepcional) break;
            
            numero_iteracoes_total += numero_iteracoes;
            
            if (isUsarTextura) {
                estampasAdicionadas.add(obj_teste);
            }
            else {
                estampasAdicionadas.add(obj_teste);
            }
            
            area_total += estampasAdicionadas.get(qtd_formas).getArea();
            area_preenchida = area_total / valorAreaTela;
            qtd_formas++;
        
        } while (numero_iteracoes_total < iteracoes_max && qtd_formas < nmax && area_preenchida < preenchimento_max);
        System.out.println("área preenchida = " + Math.round(area_preenchida * 100) + "%");
        System.out.println("número de iterações = " + numero_iteracoes_total);
        System.out.println("número de formas = " + qtd_formas);
        jFrame.revalidate();
        jFrame.repaint();
        
        System.out.println("Tempo de execução = " + (System.currentTimeMillis() - tempoInicial)/1000.0 + " segundos.");
        
        salvarImagem();
    }
    
    /**
     * Traz o prompt para o usuário se deseja salvar a imagem gerada 
     * ou não, dando as opções de PNG e SVG. Caso uma textura seja usada, não 
     * será possível salvar a imagem como SVG, visto que isso gera dependência 
     * com imagens rasterizadas exteriores.
     * @see SalvaImagem
     */
    private void salvarImagem() {
        int n;
        do {
            String[] opcao_salva;
            if (isUsarTextura){
                opcao_salva = new String[1];
                opcao_salva[0] = "Salvar como PNG";
            }
            else{
                opcao_salva = new String[2];
                opcao_salva[0] = "Salvar como PNG";
                opcao_salva[1] = "Salvar como SVG";
            }
            n = JOptionPane.showOptionDialog(jFrame, "Deseja salvar esta imagem?", "Salvar?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcao_salva, opcao_salva[0]);
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
        } while (n != -1);
    }
}
