package testefractal;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @author Giordanna De Gregoriis
 * código original em C: http://john-art.com/circle_fractal_demo.c 
 */

public class TesteFractal {

    public static void main(String[] args) {
        Area.instancia();
        BufferedImage bImg = new BufferedImage(Area.instancia().getWidth(), Area.instancia().getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        preencheCirculo(90000, 400000, 0.99);
        
        Area.instancia().renderizador.paintAll(cg);
        try {
            if (ImageIO.write(bImg, "png", new File("./output_image.png")))
            {
                System.out.println("-- saved");
            }
        } catch (IOException e) {
        }
    }

    public static void preencheCirculo(int formas_max, int iteracoes_max, double preenchimento_max) {

        //final double c_max = 1.48;
        // função exponencial da área
        double  teste_raio, area_preenchida,
                c = 1.1 + Math.random() * 0.38, // função exponencial da área (usada pra determinar o primeiro círculo)
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

        Area.instancia().revalidate();
        Area.instancia().repaint();
    }
    
    public static void preencheQuadrado(int formas_max, int iteracoes_max, double preenchimento_max) {

        //final double c_max = 1.48;
        // função exponencial da área
        double  teste_raio, area_preenchida,
                c = 1.1 + Math.random() * 0.38, // função exponencial da área (usada pra determinar o primeiro círculo)
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
        Area.instancia().revalidate();
        Area.instancia().repaint();
    }

    public static double funcaoZeta(double c, int N) {
        double soma = 0;
        int NEXP = 100000;
        for (double i = N; i < NEXP; i++) {
            soma += Math.pow(i, -c);
        }
        System.out.println("soma="+soma);

        return soma + soma_estimada(c, NEXP);
    }

    public static double soma_estimada(double c, double n) {
        System.out.println("soma_estimada="+(1.0 / (c - 1)) * Math.pow(n, 1 - c));
        return (1.0 / (c - 1)) * Math.pow(n, 1 - c);
    }
    
    public static double valorControle(double valor_n, double exp_u){
        return Math.pow(valor_n, -exp_u);
    }

}
