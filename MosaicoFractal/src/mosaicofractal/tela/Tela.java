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
    private Estampa estampaTeste = null;
    
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
    private final boolean isUSARTHREAD = true;
    
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
            if (isFormaTelaPersonalizada) {
                x = Math.random() * (LARGURA -  shapeFormaTela.getBounds2D().getWidth());
                y = Math.random() * (ALTURA -  shapeFormaTela.getBounds2D().getHeight());
                
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
            else {
                x = Math.random() * (LARGURA -  forma.getBounds2D().getWidth());
                y = Math.random() * (ALTURA -  forma.getBounds2D().getHeight());
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
    private boolean isUsarThread() {
        boolean teste = false;
        if (isUSARTHREAD) {
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
            for (int k = 0 ; k < quantidadeEstampas ; k++) {
                teste = Estampa.intersecta(estampaTeste, estampasAdicionadas.get(k));
                if (teste) break;
            }
        }
        
        return teste;
    }
    
    /**
     * Função principal da classe, onde realiza os devidos cálculos para 
     * posicionar as estampas na tela.
     *
     * @param formaDaEstampa forma das estampas a seren posicionadas
     * @param formaDaTela formato da tela
     * @param preenchimentos preenchimentos utilizados para as estampas
     * @param maximoFormas número máximo de estampas a serem inseridas na tela
     * @param corFundo cor utilizada no fundo da tela
     * @param maximoIteracoes número máximo de iterações para os testes
     * @param constante constante a ser utilizada na <code>funcaoZeta()</code>
     * @see #funcaoZeta(double, int) 
     */
    public void preencherArea(Shape formaDaEstampa, Shape formaDaTela,
            ArrayList<Preenchimento> preenchimentos, Color corFundo, 
            double constante, int maximoFormas, int maximoIteracoes) {
        
        final double maximoPorcentagemPreenchimento = 0.99,
                     expoente = 0.5 * constante;
        
        final int estouroMaxFormas = maximoFormas + 1;
        
        int numeroIteracoesTotal = 0,
            numeroIteracoesAcumulado,
            N = 2,
            indexPreenchimento;

        double  valorZeta = funcaoZeta(constante, N),
                razaoDaArea = 1.0 / valorZeta,
                porcentagem = razaoDaArea * valorControle(N, expoente),
                escala, areaPreenchidaPorcentagem, areaTotal;
        
        boolean testeInterseccao, casoExcepcional;
        
        Shape formaEscolhida;
        
        /* para executar transformações nas formas */
        AffineTransform transformador;
        
        corDoFundo = corFundo;
        
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
        
        /* escala a forma da estampa para o tamanho da tela */
        escala = getRazaoAreaTelaForma(formaDaEstampa);
        transformador = AffineTransform.getScaleInstance(escala, escala);
        shapeFormaUsada = transformador.createTransformedShape(formaDaEstampa);
        
        /* coloca forma na origem */
        transformador = AffineTransform.getTranslateInstance(
                -shapeFormaUsada.getBounds2D().getX(), 
                -shapeFormaUsada.getBounds2D().getY());
        
        shapeFormaUsada = transformador.createTransformedShape(shapeFormaUsada);
        
        /* inicia contador para ver a duração do algoritmo */
        long tempoInicial = System.currentTimeMillis();
        
        /* escala a forma para a porcentagem da área da tela */
        transformador = AffineTransform.getScaleInstance(porcentagem, porcentagem);
        formaEscolhida = transformador.createTransformedShape(shapeFormaUsada);
        
        /* se for rotacionar a forma, executa o que estiver dentro da condição
        rotação em radianos */
        if (isMudarAngulo) {
            transformador = AffineTransform.getRotateInstance(
                    Math.random() * Math.PI * 2, 
                    formaEscolhida.getBounds2D().getCenterX(), 
                    formaEscolhida.getBounds2D().getCenterY());
            
            formaEscolhida = transformador.createTransformedShape(formaEscolhida);
        }
        
        /* verifica se, depois de o número máximo de iterações, ainda não foi 
        possivel encontrar as posições X e Y. acontece quando o valor de c é 
        muito grande para a relação entre a forma e a tela */
        casoExcepcional = encontraXeY(formaEscolhida, maximoIteracoes);
        
        /* se aconteceu a exceção, reduz o tamanho da primeira forma */
        while (casoExcepcional) {
            N++;
            porcentagem = razaoDaArea * valorControle(N, expoente);
            transformador = AffineTransform.getScaleInstance(porcentagem, porcentagem);
            formaEscolhida = transformador.createTransformedShape(shapeFormaUsada);
            
            if (isMudarAngulo) {
                transformador = AffineTransform.getRotateInstance(
                        Math.random() * Math.PI * 2, 
                        formaEscolhida.getBounds2D().getCenterX(), 
                        formaEscolhida.getBounds2D().getCenterY());
                
                formaEscolhida = transformador.createTransformedShape(formaEscolhida);
            }
            casoExcepcional = encontraXeY(formaEscolhida, maximoIteracoes);
        }
        
        System.out.println("c = " + constante + " | zeta = " + valorZeta + " | razão = " + razaoDaArea
        + "| primeira forma = " + porcentagem);
        
        /* se utiliza textura */
        if (isUsarTextura) {
            estampasAdicionadas.add(new Estampa(formaEscolhida, 
                    preenchimentos.get(0), x, y));
        }
        else {
            /* cor aleatória */
            estampasAdicionadas.add(new Estampa(formaEscolhida, 
                    preenchimentos.get(RAND.nextInt(preenchimentos.size())), x, y));
        }
        
        /* inicia contagem da porcentagem preenchida da área */
        areaTotal = estampasAdicionadas.get(0).getArea();
        areaPreenchidaPorcentagem = areaTotal / valorAreaTela;
        
        quantidadeEstampas = 1;
        
        /* loop no número de círculos */
        do {
            numeroIteracoesAcumulado = 0;
            
            porcentagem = razaoDaArea * valorControle(quantidadeEstampas + N, expoente);
            transformador = AffineTransform.getScaleInstance(porcentagem, porcentagem);
            formaEscolhida = transformador.createTransformedShape(shapeFormaUsada);
            
            do {
                if (isMudarAngulo) {
                    transformador = AffineTransform.getRotateInstance(
                            Math.random() * Math.PI * 2, 
                            formaEscolhida.getBounds2D().getCenterX(), 
                            formaEscolhida.getBounds2D().getCenterY());
                    
                    formaEscolhida = transformador.createTransformedShape(formaEscolhida);
                }
                
                casoExcepcional = encontraXeY(formaEscolhida, maximoIteracoes);
                
                if (casoExcepcional) {
                    System.out.println("A estampa não consegue ser colocada na "
                            + "forma! Vamos terminar o processo mais cedo...");
                    
                    break;
                }
                
                if (isUsarTextura) {
                    estampaTeste = new Estampa(formaEscolhida, preenchimentos.get(0), x, y);
                }
                else {
                    indexPreenchimento = RAND.nextInt(preenchimentos.size());
                    estampaTeste = new Estampa(formaEscolhida, preenchimentos.get(indexPreenchimento), x, y);
                }
                
                testeInterseccao = isUsarThread();
                numeroIteracoesAcumulado++;
                
                if (numeroIteracoesAcumulado > maximoIteracoes){
                    casoExcepcional = true;
                    System.out.println("A estampa não consegue ser colocada na "
                            + "forma! Vamos terminar o processo mais cedo...");
                    break;
                }
                
            } while (testeInterseccao);
            if (casoExcepcional) {
                break;
            }
            
            numeroIteracoesTotal += numeroIteracoesAcumulado;
            
            estampasAdicionadas.add(estampaTeste);
            
            areaTotal += estampasAdicionadas.get(quantidadeEstampas).getArea();
            areaPreenchidaPorcentagem = areaTotal / valorAreaTela;
            quantidadeEstampas++;
        
        } while (numeroIteracoesTotal < maximoIteracoes && 
                quantidadeEstampas < estouroMaxFormas && 
                areaPreenchidaPorcentagem < maximoPorcentagemPreenchimento);
        
        System.out.println("Área da tela = " + valorAreaTela);
        System.out.println("Área da estampa = " + Estampa.calculaArea(shapeFormaUsada));
        
        System.out.println("área preenchida = " + Math.round(areaPreenchidaPorcentagem * 100) + "%");
        System.out.println("número de iterações = " + numeroIteracoesTotal);
        System.out.println("número de formas = " + quantidadeEstampas);
        System.out.println("Tempo de execução = " + (System.currentTimeMillis() - tempoInicial)/1000.0 + " segundos.");
        
        jFrame.revalidate();
        jFrame.repaint();
        
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
