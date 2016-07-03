package testefractal;

public class TesteFractal {

    public static void main(String[] args) {
        Area.instancia();
        preencheCirculo(90000, 400000, .99);

    }

    public static void preencheCirculo(int circulos_max, int iteracoes_max, double preenchimento_max) {
        int nmax = circulos_max + 1;

        //final double c_max = 1.48;
        // função exponencial da área
        double c = 1 + Math.random() * 0.48, valor_n = 2, exp_u = 0.5 * c;
        double teste_raio, area_preenchida, area_nova, q1, x1, y1, dx, dy, teste, soma_raios;
        int circulos = 1, numero_iteracoes_total = 0, numero_iteracoes, raio_k, x_k, y_k;
        
        System.out.println(c);
        
        double valor_zeta = funcaoZeta(c, (int) valor_n);
        double area_razao = 1.0 / valor_zeta;
        double q2 = Math.sqrt(area_razao / Math.PI);
        double raio_circulo = Math.pow(Area.instancia().getArea(), 0.5) * q2 * Math.pow(valor_n, -exp_u);
        double ffac = Math.pow(Area.instancia().getArea(), 0.5) * q2;
        double area_total = Math.PI * raio_circulo * raio_circulo;

        int x = (int) (raio_circulo + Math.random() * (Area.instancia().getLargura() - 2 * raio_circulo));
        int y = (int) (raio_circulo + Math.random() * (Area.instancia().getAltura() - 2 * raio_circulo));
        
        Area.instancia().getFormas().add(new Circulo(x, y, (int) raio_circulo));

        do //loop on circle number
        {
            numero_iteracoes = 0;   //iteration count init
            q1 = (double) circulos + valor_n;
            teste_raio = ffac * Math.pow(q1, -exp_u);
            do //loop on random search
            {
                x1 = teste_raio + Math.random() * (Area.instancia().getLargura() - 2 * teste_raio);
                y1 = teste_raio + Math.random() * (Area.instancia().getAltura() - 2 * teste_raio);
                numero_iteracoes++;
                teste = 1;
                for (int k = 0; k < circulos; k++) //loop over old placements
                {
                    raio_k = Area.instancia().getFormas().get(k).raio;
                    x_k = Area.instancia().getFormas().get(k).x;
                    y_k = Area.instancia().getFormas().get(k).y;
                    
                    soma_raios = teste_raio + raio_k;  //sum of radii
                    dx = x_k - x1;
                    if (dx < 0) {
                        dx = -dx;
                    }
                    if (dx < soma_raios) //coarse test x
                    {
                        dy = y_k - y1;
                        if (dy < 0.) {
                            dy = -dy;
                        }
                        if (dy < soma_raios) //coarse test y
                        {
                            q1 = Math.pow(dx, 2) + Math.pow(dy, 2);
                            q2 = Math.sqrt(q1);
                            if (q2 < soma_raios) //fine test
                            {
                                teste = 0;
                                break;
                            }
                        }  //if dy<
                    }  //if(dx<
                } //next k
                //System.out.println("passou interno interno");
            } while (teste == 0);	//repeat if too close to a circle

            numero_iteracoes_total = numero_iteracoes_total + numero_iteracoes;
            Area.instancia().getFormas().add(new Circulo((int) x1, (int) y1, (int) teste_raio));
            area_nova = Math.PI * Math.pow(teste_raio, 2);
            area_total += area_nova;
            area_preenchida = area_total / (Area.instancia().getArea());
            circulos++;
            //System.out.println("passou interno");
        } while (numero_iteracoes_total < iteracoes_max & circulos < nmax & area_preenchida < preenchimento_max);

        //System.out.println("passou");
        Area.instancia().revalidate();
        Area.instancia().repaint();
    }

    public static double funcaoZeta(double c, int N) {
        double soma = 0;

        for (double i = N; i < 100000; i++) {
            soma += Math.pow(i, -c);
        }

        return soma + soma_estimada(c, 100000.5);
    }

    public static double soma_estimada(double c, double n) {
        return (1.0 / (c - 1)) * Math.pow(n - 0.5, 1 - c);
    }

}
