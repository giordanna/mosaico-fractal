package testefractal.dependencias;

import java.util.ArrayList;
import testefractal.formas.CirculoPolonio;


public class Polonio {
    public static double tamanho_minimo = 0.001;
    
    public static class Tupla{
        public CirculoPolonio posicao_a;
        public CirculoPolonio posicao_b;
        public CirculoPolonio posicao_c;
        
        public Tupla(CirculoPolonio posicao_a, CirculoPolonio posicao_b, CirculoPolonio posicao_c){
            this.posicao_a = posicao_a;
            this.posicao_b = posicao_b;
            this.posicao_c = posicao_c;
        }
        
        public Tupla(Tupla copia){
            this.posicao_a = copia.posicao_a;
            this.posicao_b = copia.posicao_b;
            this.posicao_c = copia.posicao_c;
        }
    }
    
    // ordena os pontos
    public static Tupla sentidoHorario(Tupla tupla){
        double x1 = tupla.posicao_a.x - tupla.posicao_b.x;
        double x2 = tupla.posicao_c.x - tupla.posicao_b.x;
        double y1 = tupla.posicao_a.y - tupla.posicao_b.y;
        double y2 = tupla.posicao_c.y - tupla.posicao_b.y;
        
        if ( (x1 * y2 - y1 * x2) >= 0) return new Tupla(tupla);
        else return new Tupla(tupla.posicao_a, tupla.posicao_c, tupla.posicao_b);
    }
    
    // encontra o 4º círculo que encosta em 3 circulos
    public static ArrayList<CirculoPolonio> encostado(CirculoPolonio a, CirculoPolonio b, CirculoPolonio c, boolean inicial) {
        double k1 = 1 / (a.raio * 1.0);
        Complexo z1 = new Complexo(a.x, a.y);
        Complexo kz1 = Complexo.multiplicacao(new Complexo(k1,0), z1);

        double k2 = 1 / (b.raio * 1.0);
        Complexo z2 = new Complexo(b.x, b.y);
        Complexo kz2 = Complexo.multiplicacao(new Complexo(k2,0), z2);

        double k3 = 1 / (c.raio * 1.0);
        Complexo z3 = new Complexo(c.x, c.y);
        Complexo kz3 = Complexo.multiplicacao(new Complexo(k3,0),z3);

        double k4p = k1 + k2 + k3 + 2*Math.sqrt(k1*k2 + k2*k3 + k3*k1);
        double k4m = k1 + k2 + k3 - 2*Math.sqrt(k1*k2 + k2*k3 + k3*k1);
        
        Complexo kz4p = Complexo.soma(Complexo.soma(Complexo.soma(kz1,kz2),kz3), Complexo.multiplicacao(new Complexo(2,0),
          Complexo.raizq(Complexo.soma(Complexo.soma(Complexo.multiplicacao(kz1,kz2), Complexo.multiplicacao(kz2,kz3)), Complexo.multiplicacao(kz3,kz1)))));
        
        Complexo kz4m = Complexo.subtracao(Complexo.soma(Complexo.soma(kz1,kz2),kz3),Complexo.multiplicacao(new Complexo(2,0),
          Complexo.raizq(Complexo.soma(Complexo.soma(Complexo.multiplicacao(kz1,kz2),Complexo.multiplicacao(kz2,kz3)),Complexo.multiplicacao(kz3,kz1)))));
        
        double k4;
        Complexo kz4;
        double k4b;
        Complexo kz4b;
        ArrayList<CirculoPolonio> cs = new ArrayList<>();
        
        if (k4p > k4m){
            k4 = k4p;
            kz4 = kz4p;
            k4b = k4m;
            kz4b = kz4m;
        }
        else {
            k4 = k4m;
            kz4 = kz4m;
            k4b = k4p;
            kz4b = kz4p;
        }
        
        CirculoPolonio cc = new CirculoPolonio(kz4.real/k4, kz4.imaginaria/k4, Math.abs(1/k4));
        double dx = a.x - cc.x;
        double dy = a.y - cc.y;
        double dr = a.raio + cc.raio;
        
        if (Math.abs(dx * dx + dy *dy - dr * dr) > 0.0001) {
            cc = new CirculoPolonio(kz4b.real/k4, kz4b.imaginaria/k4, Math.abs(1/k4));
        }
        
        cs.add(cc);
        
        if (inicial) {
            cc = new CirculoPolonio(kz4b.real/k4b, kz4b.imaginaria/k4b, Math.abs(1/k4b));
            cs.add(cc);
        }
        
        return cs;
    }
    
    public static void iniciaFractal(){
        CirculoPolonio b = new CirculoPolonio(0, 0, -1);

        // insert two randomly positioned touching circles
        double tr = 1-Math.random()/2;
        double pa = Math.PI/2 - Math.asin(Math.random()*(1-tr)/tr);
        double px = tr * Math.cos(pa);
        double py = tr * Math.sin(pa);
        double pr = 1 - tr;
        double qr = (1 - pr) * (1 - Math.cos(pa + Math.PI/2))
               / (1 + pr - (1 - pr) * Math.cos(pa + Math.PI/2));
        double qx = 0;
        double qy = qr - 1;
        CirculoPolonio p = new CirculoPolonio(px, py, pr);
        CirculoPolonio q = new CirculoPolonio(qx, qy, qr);

        // fila para conter trincas de círculos que se encostam
        ArrayList<Tupla> queue = new ArrayList<>();
        ArrayList<CirculoPolonio> cs = encostado(b,p,q, true);
        queue.add(new Tupla(b, p, cs.get(0)));
        queue.add(new Tupla(b, cs.get(0), q));
        queue.add(new Tupla(cs.get(0), p, q));
        queue.add(new Tupla(b, p, cs.get(1)));
        queue.add(new Tupla(b, cs.get(1), q));
        queue.add(new Tupla(cs.get(1), p, q));

        // fila que contém circulos para desenhar
        ArrayList<CirculoPolonio> draw = new ArrayList<>();
        draw.add(b);
        draw.add(p);
        draw.add(q);
        draw.add(cs.get(0));
        draw.add(cs.get(1));

        // adiciona 10000 mais círculos na fila
        // adiciona 3 trincas para computar a fila
        int c;
        for (c = 0; c < Math.min(queue.size(), 10000); c = c + 1) {
            CirculoPolonio c0 = queue.get(c).posicao_a;
            CirculoPolonio c1 = queue.get(c).posicao_b;
            CirculoPolonio c2 = queue.get(c).posicao_c;
            ArrayList<CirculoPolonio> ncs = encostado(c0, c1, c2, false);
            CirculoPolonio nc = ncs.get(0);
            if (nc.raio > tamanho_minimo) {
                queue.add(new Tupla(nc, c1, c2));
                queue.add(new Tupla(c0, nc, c2));
                queue.add(new Tupla(c0, c1, nc));
                draw.add(nc);
            }
        }
    }
}
