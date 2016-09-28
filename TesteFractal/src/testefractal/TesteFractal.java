package testefractal;

import testefractal.formas.Circulo;
import testefractal.formas.Quadrado;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 * @author Giordanna De Gregoriis
 * código original em C: http://john-art.com/circle_fractal_demo.c 
 */

public class TesteFractal {

    public static void main(String[] args) {
        
        Area.instancia();
        BufferedImage bImg = new BufferedImage(Area.instancia().getLargura(), Area.instancia().getAltura(), BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        opcao();
        
        Area.instancia().renderizador.paintAll(cg);
        try {
            if (ImageIO.write(bImg, "png", new File("./exemplo.png")))
            {
                System.out.println("-- saved");
            }
        } catch (IOException e) {
        }
    }
    
    public static void opcao(){
        JPanel p = new JPanel();
        int n;
        // para impedir valores negativos e 0 nos lados e raio
        SpinnerModel modelo_spinner = new SpinnerNumberModel(1.45, 1.01, 1.48, 0.01);
        
        JSpinner valor_c = new JSpinner(modelo_spinner);
        
        Dimension d = valor_c.getPreferredSize();  
        d.width = 50;
        valor_c.setPreferredSize(d);

        p.add(new JLabel("Defina o valor de c: "));
        p.add(valor_c);
        
        n = JOptionPane.showConfirmDialog(Area.instancia(), p, "TesteFractal", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (n == 0){
            double c = ((double) valor_c.getValue());
            long tempoInicial = System.currentTimeMillis();
            opcao_preenchimento(90000, 400000, 0.99, c);
            System.out.println("tempo de execução: " + (System.currentTimeMillis() - tempoInicial)/1000.0 + " segundos.");
        }
        else System.exit(0);
        
    }
    
    public static void opcao_preenchimento(int formas_max, int iteracoes_max, double preenchimento_max, double c){
        String [] opcoes = {"Círculo", "Quadrado", "Apolonio"};
        String opcao = (String) JOptionPane.showInputDialog(null, "Escolha um tipo de forma", "Escolha",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        
        switch (opcao){
            case "Círculo":
                preencheCirculo(formas_max, iteracoes_max, preenchimento_max, c);
                break;
            case "Quadrado":
                preencheQuadrado(formas_max, iteracoes_max, preenchimento_max, c);
                break;
            case "Apolonio":
                preencheApolonio(formas_max, iteracoes_max, preenchimento_max, c);
                break;
        } 
    }

    public static void preencheCirculo(int formas_max, int iteracoes_max, double preenchimento_max, double c) {

        //final double c_max = 1.48;
        // função exponencial da área
        double  teste_raio, area_preenchida,
                //c = 1.1 + Math.random() * 0.38, // função exponencial da área (usada pra determinar o primeiro círculo)
                exp_u = 0.5 * c; // metade desse valor
        
        
        int     formas = 1,
                numero_iteracoes_total = 0,
                numero_iteracoes,
                valor_n = 2,
                nmax = formas_max + 1;

        double  valor_zeta = funcaoZeta(c, valor_n), // o valor que vai determinar a porcentagem. ex: 4 = 25%
                
                area_razao = 1.0 / valor_zeta, // ex: valor_zeta = 4, area_razao = 1/4 = 25%
        
                // raio gerado multiplicado por uma porcentagem de controle. quanto maior c, menor o valor multiplicado
                // então menor será o raio de fato, que não será um círculo gigante preenchendo 25% da tela, mas
                // um pouco menor
                raio_forma = Circulo.raioGeradoEstatico(area_razao) * valorControle(valor_n, exp_u),
        
                raio_original_primeiro = Circulo.raioGeradoEstatico(area_razao);

        boolean teste;
        
        System.out.println("c = " + c + " | zeta = " + valor_zeta + " | razão = " + area_razao
        + "| raio = " + raio_forma);
        
        double x = (int) (raio_forma + Math.random() * (Area.instancia().getLargura() -  raio_forma * 2));
        double y = (int) (raio_forma + Math.random() * (Area.instancia().getAltura() -  raio_forma * 2));
        
        Area.instancia().getFormas().add(new Circulo((int) x, (int) y, (int) raio_forma));

        double area_total = Area.instancia().getFormas().get(0).getArea();
        do { // loop no número de círculos
        
            numero_iteracoes = 0;
            teste_raio = raio_original_primeiro * valorControle(formas + valor_n, exp_u);
            do { // busca aleatória
            
                x = teste_raio + Math.random() * (Area.instancia().getLargura() -  teste_raio * 2);
                y = teste_raio + Math.random() * (Area.instancia().getAltura() - teste_raio * 2);
                numero_iteracoes++;
                teste = true;
                Circulo obj_teste = new Circulo((int) x, (int) y, (int) teste_raio);
                for (int k = 0; k < formas; k++) {//loop over old placements
                    teste = obj_teste.teste(Area.instancia().getFormas().get(k));
                    if (!teste) break;
                } // próximo k
            } while (!teste); // repetir se ficou muito perto de um círculo

            numero_iteracoes_total += numero_iteracoes;
            synchronized(Area.instancia().getFormas()){
                Area.instancia().getFormas().add(new Circulo((int) x, (int) y, (int) teste_raio));
            }
            
            area_total += Area.instancia().getFormas().get(formas).getArea();
            area_preenchida = area_total / (Area.instancia().getArea());
            formas++;
        } while (numero_iteracoes_total < iteracoes_max && formas < nmax && area_preenchida < preenchimento_max);
        System.out.println("área preenchida = " + Math.round(area_preenchida * 100) + "%");
        System.out.println("número de iterações = " + numero_iteracoes_total);
        System.out.println("número de formas = " + formas);
        Area.instancia().revalidate();
        Area.instancia().repaint();
    }
    
    public static void preencheQuadrado(int formas_max, int iteracoes_max, double preenchimento_max, double c) {

        //final double c_max = 1.48;
        // função exponencial da área
        double  teste_raio, area_preenchida,
                //c = 1.1 + Math.random() * 0.38, // função exponencial da área (usada pra determinar o primeiro círculo)
                exp_u = 0.5 * c; // metade desse valor
        
        
        int     formas = 1,
                numero_iteracoes_total = 0,
                numero_iteracoes,
                valor_n = 2,
                nmax = formas_max + 1;

        double  valor_zeta = funcaoZeta(c, valor_n), // o valor que vai determinar a porcentagem. ex: 4 = 25%
                
                area_razao = 1.0 / valor_zeta, // ex: valor_zeta = 4, area_razao = 1/4 = 25%
        
                // raio gerado multiplicado por uma porcentagem de controle. quanto maior c, menor o valor multiplicado
                // então menor será o raio de fato, que não será um círculo gigante preenchendo 25% da tela, mas
                // um pouco menor
                raio_forma = Quadrado.raioGeradoEstatico(area_razao) * valorControle(valor_n, exp_u),
        
                raio_original_primeiro = Quadrado.raioGeradoEstatico(area_razao);

        boolean teste;
        
        System.out.println("c = " + c + " | zeta = " + valor_zeta + " | razão = " + area_razao
        + "| raio = " + raio_forma);
        
        double x = (int) ( Math.random() * (Area.instancia().getLargura() - 2 * raio_forma));
        double y = (int) (Math.random() * (Area.instancia().getAltura() - 2 * raio_forma));
        
        Area.instancia().getFormas().add(new Quadrado((int) x, (int) y, (int) raio_forma));

        double area_total = Area.instancia().getFormas().get(0).getArea();
        do { // loop no número de círculos
        
            numero_iteracoes = 0;
            teste_raio = raio_original_primeiro * valorControle(formas + valor_n, exp_u);
            do { // busca aleatória
            
                x = Math.random() * (Area.instancia().getLargura() - 2 * teste_raio);
                y = Math.random() * (Area.instancia().getAltura() - 2 * teste_raio);
                numero_iteracoes++;
                teste = true;
                Quadrado obj_teste = new Quadrado((int) x, (int) y, (int) teste_raio);
                for (int k = 0; k < formas; k++) {//loop over old placements
                    teste = obj_teste.teste(Area.instancia().getFormas().get(k));
                    if (!teste) break;
                } // próximo k
            } while (!teste); // repetir se ficou muito perto de um círculo
            numero_iteracoes_total += numero_iteracoes;
            
            synchronized(Area.instancia().getFormas()){
                Area.instancia().getFormas().add(new Quadrado((int) x, (int) y, (int) teste_raio));
            }
            
            area_total += Area.instancia().getFormas().get(formas).getArea();
            area_preenchida = area_total / (Area.instancia().getArea());
            formas++;
        } while (numero_iteracoes_total < iteracoes_max && formas < nmax && area_preenchida < preenchimento_max);
        System.out.println("área preenchida = " + area_preenchida + "%");
        System.out.println("número de iterações = " + numero_iteracoes_total);
        System.out.println("número de formas = " + formas);
        Area.instancia().revalidate();
        Area.instancia().repaint();
    }
    
    public static void preencheApolonio(int formas_max, int iteracoes_max, double preenchimento_max, double c) {

        //final double c_max = 1.48;
        // função exponencial da área
        double  teste_raio, area_preenchida,
                //c = 1.1 + Math.random() * 0.38, // função exponencial da área (usada pra determinar o primeiro círculo)
                exp_u = 0.5 * c; // metade desse valor
        
        
        int     formas = 1,
                numero_iteracoes_total = 0,
                numero_iteracoes,
                valor_n = 2,
                nmax = formas_max + 1;

        double  valor_zeta = funcaoZeta(c, valor_n), // o valor que vai determinar a porcentagem. ex: 4 = 25%
                
                area_razao = 1.0 / valor_zeta, // ex: valor_zeta = 4, area_razao = 1/4 = 25%
        
                // raio gerado multiplicado por uma porcentagem de controle. quanto maior c, menor o valor multiplicado
                // então menor será o raio de fato, que não será um círculo gigante preenchendo 25% da tela, mas
                // um pouco menor
                raio_forma = Circulo.raioGeradoEstatico(area_razao) * valorControle(valor_n, exp_u),
        
                raio_original_primeiro = Circulo.raioGeradoEstatico(area_razao);

        boolean teste;
        
        System.out.println("c = " + c + " | zeta = " + valor_zeta + " | razão = " + area_razao
        + "| raio = " + raio_forma);
        
        
        double raio_primeiro = Area.instancia().getLargura()/2;
        
        int i = Area.instancia().getLargura()/2, j = Area.instancia().getAltura()/2;
        Area.instancia().getFormas().add(new Circulo(i, j, (int) raio_primeiro));
        
        double a = 1 + 2 / Math.sqrt(3);  
        
        double raio_menor = raio_primeiro / a;
        
        double altura = raio_menor * Math.sqrt(3);
        
        double centro_a = raio_primeiro + (2 * raio_primeiro - raio_menor) * 1;
        double centro_b = (raio_primeiro + raio_menor) + (2 * raio_primeiro - raio_menor - altura) * 1;
        double centro_c = (raio_primeiro - raio_menor) + (2 * raio_primeiro - raio_menor - altura) * 1;
        System.out.println("centro a: " + centro_a);
        System.out.println("centro b: " + centro_b);
        System.out.println("centro c: " + centro_c);
        
        Area.instancia().getFormas().add(new Circulo((int) centro_c/2, (int) centro_c/2, (int) raio_menor));
        
        double radius_sum = 0.0;
        double square_radius_sum = 0.0;

        
        /*
        double area_total = Area.instancia().getFormas().get(0).getArea();
        do { // loop no número de círculos
        
            numero_iteracoes = 0;
            numero_iteracoes++;

            numero_iteracoes_total += numero_iteracoes;
            synchronized(Area.instancia().getFormas()){
                Area.instancia().getFormas().add(new Circulo((int) x, (int) y, (int) teste_raio));
            }
            
            area_total += Area.instancia().getFormas().get(formas).getArea();
            area_preenchida = area_total / (Area.instancia().getArea());
            formas++;
        } while (numero_iteracoes_total < iteracoes_max && formas < nmax && area_preenchida < preenchimento_max);
        System.out.println("área preenchida = " + Math.round(area_preenchida * 100) + "%");
        System.out.println("número de iterações = " + numero_iteracoes_total);
        System.out.println("número de formas = " + formas);
        */
        Area.instancia().revalidate();
        Area.instancia().repaint();
    }

    public static double funcaoZeta(double c, int N) {
        double soma = 0;
        int NEXP = 100000;
        for (double i = N; i < NEXP; i++) {
            soma += Math.pow(i, -c);
        }
        return soma + soma_estimada(c, NEXP);
    }

    public static double soma_estimada(double c, double n) {
        return (1.0 / (c - 1)) * Math.pow(n, 1 - c);
    }
    
    public static double valorControle(double valor_n, double exp_u){
        return Math.pow(valor_n, -exp_u);
    }

}
