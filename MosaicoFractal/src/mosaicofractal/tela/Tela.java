/*
Este arquivo é parte do programa Mosaico Fractal

Mosaico Fractal é um software livre; você pode redistribuí-lo e/ou 
modificá-lo dentro dos termos da Licença Pública Geral GNU como 
publicada pela Fundação do Software Livre (FSF); na versão 3 da 
Licença, ou (a seu critério) qualquer versão posterior.

Este programa é distribuído na esperança de que possa ser útil, 
mas SEM NENHUMA GARANTIA; sem uma garantia implícita de ADEQUAÇÃO
a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a
Licença Pública Geral GNU para maiores detalhes.

Você deve ter recebido uma cópia da Licença Pública Geral GNU junto
com este programa, Se não, veja <http://www.gnu.org/licenses/>.
*/

package mosaicofractal.tela;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mosaicofractal.elementos.Estampa;
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
     * Valor para controlar a quantidade de estampas já inseridas na tela.
     */
    private int quantidadeEstampas = 0;
    
    /**
     * Valores para controlar a execução das threads. O programa só poderá 
     * prosseguir se as duas threads terminarem suas execuções.
     */
    private int threadA, threadB;
    
    /**
     * Threads usadas para paralelização.
     */
    private InterseccionaThread1 t1;
    private InterseccionaThread2 t2;
    
    /**
     * Porcentagem usada para reduzir a forma.
     */
    private double porcentagemTeste;
    
    /**
     * Preenchimentos utilizados nas estampas posicionadas na tela.
     */
    private ArrayList<Preenchimento> preenchimentos = new ArrayList<>();
    
    /**
     * Estampas utilizadas para preencher a região.
     */
    private ArrayList<Shape> estampas = new ArrayList<>();
    
    /**
     * Variável para determinar o tempo limite de execução do programa.
     */
    private int tempoLimite = 1;
    
    /**
     * Índice utilizado para demarcar a estampa atual a ser posicionada.
     */
    private int indiceAtualShape = 0;
    
    /**
     * Índice utilizado para demarcar a estampa atual a ser posicionada.
     */
    private int indiceAtualPreenchimento = 0;
    
    /**
     * Máximo de iterações no programa.
     */
    private int maximoIteracoes;
    
    /**
     * Estampas utilizadas nas threads. Utilizadas para verificar casos de 
     * intersecção com as demais estampas já inseridas na tela.
     */
    private Estampa estampaTeste1 = null, estampaTeste2 = null;
    
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
     * @param tempo_limite tempo limite da execução do programa
     * @see #preencherArea(java.util.ArrayList, java.awt.Shape, java.util.ArrayList, java.awt.Color, double, int, int) 
     */
    public Tela(boolean considerar_bordas, boolean mudar_angulo, boolean tela_personalizada, boolean usa_textura, int tempo_limite) {
        isConsiderarBordas = considerar_bordas;
        isMudarAngulo = mudar_angulo;
        isFormaTelaPersonalizada = tela_personalizada;
        isUsarTextura = usa_textura;
        tempoLimite = tempo_limite;
        
        estampasAdicionadas = new ArrayList<>();
        renderizador = new Renderizador();
        
        jFrame = new JFrame("Resultado");
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.add(renderizador);
        jFrame.setContentPane(renderizador);
        javax.swing.ImageIcon icone = new javax.swing.ImageIcon("./img/icones/icone_small.png");
        jFrame.setIconImage(icone.getImage());
        jFrame.pack();
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setLocation(dim.width/2-jFrame.getSize().width/2, dim.height/2-jFrame.getSize().height/2);
        jFrame.setVisible(true);
        //jFrame.setResizable(false);
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
        
        g2d.fill(shapeFormaTela);
        
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
    private double funcaoZeta(double c, double N) {
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
     * @param N constante utilizada na função, a qual deve ser maior que 1
     * @param expoente onde inicia o somatório
     * @return a soma dos valores da função Zeta
     */
    private double valorControle(double N, double expoente){
        return Math.pow(N, -expoente);
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
    private double getRazaoAreaTelaForma(Shape forma) {
        double areaDaForma, razao;
        
        areaDaForma = Estampa.calculaArea(forma);
        razao = valorAreaTela / areaDaForma;
        
        return Math.sqrt(razao);
    }
    
    /**
     * Retorna a razão entre a área da tela e a forma dada na entrada. 
     * Este valor é utilizado para redimensionar a estampa corretamente, 
     * igualando a sua área com a área da tela, como é utilizada em 
     * <code>preencherArea()</code>.
     *
     * @param forma a forma usada para calcular sua área
     * @param tela a tela usada para pedir para verificar sua razão
     * @return a razão área da tela / área da forma
     * @see #preencherArea(java.awt.Shape, java.awt.Shape, java.util.ArrayList, java.awt.Color, double, int, int) 
     */
    private double getRazaoAreaTelaForma(Shape tela, Shape forma) {
        double areaDaForma, razao, valorArea;
        
        valorArea = Estampa.calculaArea(tela);
        areaDaForma = Estampa.calculaArea(forma);
        
        razao = valorArea / areaDaForma;
        
        return Math.sqrt(razao);
    }
    
    /**
     * Retorna a razão entre a área da tela e a forma dada na entrada. 
     * Este valor é utilizado para redimensionar a estampa corretamente, 
     * igualando a sua área com a área da tela, como é utilizada em 
     * <code>preencherArea()</code>.
     *
     * @param forma a forma usada para calcular sua área
     * @param tela a tela usada para pedir para verificar sua razão
     * @return a razão área da tela / área da forma
     * @see #preencherArea(java.awt.Shape, java.awt.Shape, java.util.ArrayList, java.awt.Color, double, int, int) 
     */
    private double getRazaoForma(Shape forma) {
        double areaDaForma, razao, valorArea;
        
        valorArea = forma.getBounds2D().getWidth() * forma.getBounds2D().getHeight();
        areaDaForma = Estampa.calculaArea(forma);
        
        razao = valorArea / areaDaForma;
        
        return Math.sqrt(razao);
    }
    
    //TODO: fazer este método funcionar da maneira desejada.
    //O decaimento das formas está rápido demais. Falta encontrar uma maneira
    //de balancear isto.
    /**
     * Retorna a porcentagem corrigida para corrigir a estampa. Ainda em construção
     * 
     * @param porcentagem a porcentagem que era utilizada antigamente
     * @param forma a forma para pegar a sua bounding box
     * @return a porcentagem corrigida para reduzir a forma
     */
    private double getPorcentagemCorreta(double porcentagem, Shape forma) {
        double razao, valorArea;
        
        valorArea = Math.sqrt(valorAreaTela) * 
                Math.sqrt(porcentagem);
        
        razao = valorArea / shapeFormaTela.getBounds2D().getWidth();
        
        AffineTransform transformador = AffineTransform.getScaleInstance(razao, razao);
        Shape aux = transformador.createTransformedShape(shapeFormaTela);
        
        transformador = AffineTransform.getScaleInstance(1, 1);
        Shape aux2 = transformador.createTransformedShape(forma);
        
        razao = getRazaoAreaTelaForma(aux, aux2);
        transformador = AffineTransform.getScaleInstance(razao, razao);
        aux2 = transformador.createTransformedShape(aux2);
        
        System.out.println(Estampa.calculaArea(aux2));
        return razao;
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
     * @return se houve um caso excepcional ou não
     */
    private boolean encontraXeY(Shape forma) {
        if (isConsiderarBordas) {
            double larguraForma = forma.getBounds2D().getWidth();
            double alturaForma = forma.getBounds2D().getHeight();
            
            if (isFormaTelaPersonalizada) {
                
                double larguraTela = shapeFormaTela.getBounds2D().getWidth();
                double alturaTela = shapeFormaTela.getBounds2D().getHeight();
                
                x = (LARGURA - larguraTela)/2 + RAND.nextDouble(true, true)* (larguraTela - larguraForma);
                y = (ALTURA - alturaTela)/2 +RAND.nextDouble(true, true) * (alturaTela - alturaForma);
                
                int iteracoes_posicao = 0;
                
                while (!shapeFormaTela.contains(x, y) ||
                        Estampa.estaDentro(x, y, shapeFormaTela, forma) ||
                        !shapeFormaTela.contains(x + larguraForma, y + alturaForma) ||
                        !shapeFormaTela.contains(x, y + alturaForma) ||
                        !shapeFormaTela.contains(x + larguraForma, y)){
                    
                    x = (LARGURA - larguraTela)/2 + RAND.nextDouble(true, true) * (larguraTela - larguraForma);
                    y = (ALTURA - alturaTela)/2 + RAND.nextDouble(true, true) * (alturaTela - alturaForma);
                    iteracoes_posicao++;
                    if (iteracoes_posicao > maximoIteracoes * 1000) {
                        return true;
                    }
                }
            }
            else {
                x = RAND.nextDouble(true, true) * (LARGURA - larguraForma);
                y = RAND.nextDouble(true, true) * (ALTURA - alturaForma);
            }
        }
        else {
            x = RAND.nextDouble(true, true) * LARGURA;
            y = RAND.nextDouble(true, true) * ALTURA;
        }
        return false;
    }
    
    private Estampa criarEstampa() {
        
        AffineTransform transformador = AffineTransform.getScaleInstance(porcentagemTeste, porcentagemTeste);
        Shape forma = transformador.createTransformedShape(estampas.get(indiceAtualShape));
        boolean casoExcepcional;
        
        if (isMudarAngulo) {
            transformador = AffineTransform.getRotateInstance(
                    RAND.nextDouble(true, true) * Math.PI * 2, 
                    forma.getBounds2D().getCenterX(), 
                    forma.getBounds2D().getCenterY());

            forma = transformador.createTransformedShape(forma);
        }

        casoExcepcional = encontraXeY(forma);

        if (casoExcepcional) {
            System.out.println("A estampa não consegue ser colocada na "
                    + "forma! Vamos terminar o processo mais cedo...");

            return null;
        }

        if (isUsarTextura) {
            return new Estampa(forma, preenchimentos.get(0), x, y);
        }
        else {
            return new Estampa(forma, preenchimentos.get(indiceAtualPreenchimento), x, y);
        }
    }
    
    /**
     * A classe privada <code>InterseccionaThread1</code> serve para realizar a função de 
     * paralelizar a função de verificação se há intersecção com a forma que se 
     * deseja posicionar na tela com as demais que já foram inseridas.
     * 
     * @author Giordanna De Gregoriis
     */
    private class InterseccionaThread1 extends Thread {
        
        /**
         * Esta thread cuida da primeira metade das estampas adicionadas no 
         * momento.
         */
        @Override
        public void run() {
            threadA = 0;
            boolean teste;
            int iteracoesAcumuladas = 0;
            
            do {
                teste = true;
                estampaTeste1 = criarEstampa();
                iteracoesAcumuladas++;
                
                if (iteracoesAcumuladas > maximoIteracoes) {
                    System.out.println("Esta demorando demais para encontrar "
                            + "uma posição! Terminando mais cedo...");
                    estampaTeste1 = null;
                    threadA = 1;
                    return;
                }
                
                for (int i = 0 ; i < quantidadeEstampas ; i++) {
                    if (threadB == 1) {
                        return;
                    }

                    if (Estampa.intersecta(estampaTeste1, estampasAdicionadas.get(i))) {
                        teste = false;
                        break;
                    }
                } 
            } while (!teste);
            threadA = 1;
        }
    }
    
    /**
     * A classe privada <code>InterseccionaThread2</code> serve para realizar a função de 
     * paralelizar a função de verificação se há intersecção com a forma que se 
     * deseja posicionar na tela com as demais que já foram inseridas.
     * 
     * @author Giordanna De Gregoriis
     */
    private class InterseccionaThread2 extends Thread {
        
        /**
         * Esta thread cuida da segunda metade das estampas adicionadas no 
         * momento.
         */
        @Override
        public void run() {
            threadB = 0;
            boolean teste;
            int iteracoesAcumuladas = 0;
            
            do {
                estampaTeste2 = criarEstampa();
                teste = true;
                iteracoesAcumuladas++;
                
                if (iteracoesAcumuladas > maximoIteracoes) {
                    System.out.println("Esta demorando demais para encontrar "
                            + "uma posição! Terminando mais cedo...");
                    estampaTeste2 = null;
                    threadB = 1;
                    return;
                }
                
                for (int i = 0 ; i < quantidadeEstampas ; i++) {
                    if (threadA == 1) {
                        return;
                    }

                    if (Estampa.intersecta(estampaTeste2, estampasAdicionadas.get(i))) {
                        teste = false;
                        break;
                    }
                }
                
            } while (!teste);
            threadB = 1;
        }
    }
    
    /**
     * Verifica se o teste de verificação de intersecção entre estampas será 
     * executada sequencialmente ou irá utilizar Threads.
     * 
     * @return se houve intersecção ou não
     * @see InterseccionaThread1
     * @see InterseccionaThread2
     */
    private Estampa isUsarThread() {
        if (isUSARTHREAD) {
            threadA = threadB = 0;
            t1 = new InterseccionaThread1();
            t2 = new InterseccionaThread2();
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("oops");
            }
            
            return (threadA == 1) ? estampaTeste1 : estampaTeste2;
        }
        else{
            Estampa estampa;
            boolean teste;
            int iteracoesAcumuladas = 0;
            
            do {
                estampa = criarEstampa();
                teste = true;
                iteracoesAcumuladas++;
                
                if (iteracoesAcumuladas > maximoIteracoes) {
                    System.out.println("Esta demorando demais para encontrar "
                            + "uma posição! Terminando mais cedo...");
                    
                    return null;
                }
                
                for (int i = 0 ; i < quantidadeEstampas ; i++) {

                    if (Estampa.intersecta(estampa, estampasAdicionadas.get(i))) {
                        teste = false;
                        break;
                    }
                }
                
            } while (!teste);
            return estampa;
        }
    }
    
    /**
     * Função principal da classe, onde realiza os devidos cálculos para 
     * posicionar as estampas na tela.
     *
     * @param listaEstampas forma(s) das estampas a serem posicionadas
     * @param formaDaTela formato da tela
     * @param listaPreenchimentos preenchimentos utilizados para as estampas
     * @param maximoFormas número máximo de estampas a serem inseridas na tela
     * @param corFundo cor utilizada no fundo da tela
     * @param valor_N valor inicial de N
     * @param maxIteracoes número máximo de iterações para os testes
     * @param constante constante a ser utilizada na <code>funcaoZeta()</code>
     * @see #funcaoZeta(double, int) 
     */
    public void preencherArea(ArrayList<Shape> listaEstampas, Shape formaDaTela,
            ArrayList<Preenchimento> listaPreenchimentos, Color corFundo, 
            double constante, double valor_N, int maximoFormas, int maxIteracoes) {
        
        final int numeroShapes, numeroPreenchimentos;
        
        boolean ultrapassouTempoAntes = false;
        
        Shape formaDaEstampa;
        
        final double maximoPorcentagemPreenchimento = 0.99,
                     expoente = 0.5 * constante;
        
        int numeroIteracoesTotal = 0;

        double  N = valor_N,
                valorZeta = funcaoZeta(constante, N),
                razaoDaArea = 1.0 / valorZeta,
                escala, areaPreenchidaPorcentagem, areaTotal;
        
        Estampa estampaEscolhida;
        
        /* para executar transformações nas formas */
        AffineTransform transformador;
        
        maximoIteracoes = maxIteracoes;
        corDoFundo = corFundo;
        preenchimentos = listaPreenchimentos;
        
        numeroShapes = listaEstampas.size();
        numeroPreenchimentos = preenchimentos.size();
        
        /* se a forma da tela é personalizada */
        if (isFormaTelaPersonalizada) {
            
            if (formaDaTela.getBounds2D().getWidth() > 
                    formaDaTela.getBounds2D().getHeight()) {
                escala = LARGURA / formaDaTela.getBounds2D().getWidth();
            }
            else {
                escala = ALTURA / formaDaTela.getBounds2D().getHeight();
            }
            transformador = AffineTransform.getScaleInstance(escala, escala);
        
            shapeFormaTela = transformador.createTransformedShape(formaDaTela);
            
            /* coloca primeiro na origem */
            transformador = AffineTransform.getTranslateInstance(
                    -shapeFormaTela.getBounds2D().getX(), 
                    -shapeFormaTela.getBounds2D().getY());
            
            shapeFormaTela = transformador.createTransformedShape(shapeFormaTela);
            
            /* coloca no centro da janela */
            transformador = AffineTransform.getTranslateInstance(
                    LARGURA/2.0 - (shapeFormaTela.getBounds2D().getCenterX()), 
                    ALTURA/2.0 - (shapeFormaTela.getBounds2D().getCenterY()));
            
            shapeFormaTela = transformador.createTransformedShape(shapeFormaTela);
            
            /* calcula a área da tela */
            valorAreaTela = Estampa.calculaArea(shapeFormaTela);
        }
        else {
            shapeFormaTela = new Rectangle(LARGURA, ALTURA);
            
            /* coloca forma na origem */
            transformador = AffineTransform.getTranslateInstance(
                    -shapeFormaTela.getBounds2D().getX(),
                    -shapeFormaTela.getBounds2D().getY());
            shapeFormaTela = transformador.createTransformedShape(shapeFormaTela);
            
            /* calcula a área da tela */
            valorAreaTela = LARGURA * ALTURA;
        }
        
        /* escala a(s) forma(s) da(s) estampa(s) para o tamanho da tela */
        for (int i = 0 ; i < numeroShapes ; i++){
            formaDaEstampa = listaEstampas.get(i);
            escala = getRazaoAreaTelaForma(formaDaEstampa);
            transformador = AffineTransform.getScaleInstance(escala, escala);
            shapeFormaUsada = transformador.createTransformedShape(formaDaEstampa);

            /* coloca forma na origem */
            transformador = AffineTransform.getTranslateInstance(
                    -shapeFormaUsada.getBounds2D().getX(), 
                    -shapeFormaUsada.getBounds2D().getY());

            estampas.add(transformador.createTransformedShape(shapeFormaUsada));
        }

        /* inicia contador para ver a duração do algoritmo */
        long tempoInicial = System.currentTimeMillis();
        
        porcentagemTeste = razaoDaArea * valorControle(N, expoente);

        //porcentagemTeste = getPorcentagemCorreta(porcentagemTeste);
        
        estampaEscolhida = criarEstampa();
        
        /* se aconteceu a exceção, reduz o tamanho da primeira forma */
        while (estampaEscolhida == null) {
            // passou do tempo limite de execução
            if (tempoLimite * 60 < (System.currentTimeMillis() - tempoInicial)/1000.0){
                System.out.println("Tempo limite de execução atingido!");
                ultrapassouTempoAntes = true;
                break;
            }
            
            N++;
            porcentagemTeste = razaoDaArea * valorControle(N, expoente);
            estampaEscolhida = criarEstampa();
        }
        
        if (!ultrapassouTempoAntes) {
            estampasAdicionadas.add(estampaEscolhida);
        
            /* inicia contagem da porcentagem preenchida da área */
            areaTotal = estampaEscolhida.getArea();
            areaPreenchidaPorcentagem = areaTotal / valorAreaTela;

            quantidadeEstampas = 1;

            System.out.println("c = " + constante + " | N = " + N + 
                    " | zeta = " + valorZeta + " | razão = " + razaoDaArea + 
                    "| primeira forma = " + porcentagemTeste);
            System.out.println("Limites inclusivos: " + isConsiderarBordas);
            System.out.println("Rotacionar estampas: " + isMudarAngulo);
            System.out.println("Forma personalizada: " + isFormaTelaPersonalizada);
            System.out.println("Tempo limite: " + tempoLimite);

            /* loop no número de formas */
            do {

                // passou do tempo limite de execução
                if (tempoLimite * 60 < (System.currentTimeMillis() - tempoInicial)/1000.0){
                    System.out.println("Tempo limite de execução atingido!");
                    break;
                }

                porcentagemTeste = razaoDaArea * valorControle(quantidadeEstampas + N, expoente);
                //porcentagemTeste = getPorcentagemCorreta(porcentagemTeste);

                if (numeroShapes > 1){
                    indiceAtualShape++;
                    if (indiceAtualShape == numeroShapes){
                        indiceAtualShape = 0;
                    }
                }

                if (numeroPreenchimentos > 1){
                    indiceAtualPreenchimento++;
                    if (indiceAtualPreenchimento == numeroPreenchimentos){
                        indiceAtualPreenchimento = 0;
                    }
                }

                estampaEscolhida = isUsarThread();

                /* se for null, houve caso excepcional */
                if (estampaEscolhida == null) break;

                numeroIteracoesTotal++;

                estampasAdicionadas.add(estampaEscolhida);

                areaTotal += estampasAdicionadas.get(quantidadeEstampas).getArea();
                areaPreenchidaPorcentagem = areaTotal / valorAreaTela;
                quantidadeEstampas++;

            } while (numeroIteracoesTotal < maxIteracoes && 
                    quantidadeEstampas < maximoFormas && 
                    areaPreenchidaPorcentagem < maximoPorcentagemPreenchimento);

            System.out.println("Área preenchida = " + Math.round(areaPreenchidaPorcentagem * 100) + "%");
            System.out.println("Número de formas = " + quantidadeEstampas);
            System.out.println("Tempo de execução = " + (System.currentTimeMillis() - tempoInicial)/1000.0 + " segundos.");

            jFrame.revalidate();
            jFrame.repaint();

            salvarImagem();
        }
        else {
            JOptionPane.showMessageDialog(jFrame, "O tempo limite foi atingido... para a primeira estampa. Sinto muito. ):", "Ops", JOptionPane.ERROR_MESSAGE);
        }
        
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
