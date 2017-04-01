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

package mosaicofractal.elementos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * A classe <code>Estampa</code> é usada para encapsular as funções relacionada 
 * a estampas que são manipuladas na {@link mosaicofractal.tela.Tela}. Ela cuida de verificar 
 * intersecção com outra estampa, e se a mesma se encontra totalmente dentro 
 * da tela personalizada.
 * 
 * @author      Giordanna De Gregoriis
 * @see         mosaicofractal.tela.Tela
 * @see         Preenchimento
 */
public class Estampa {
    
    /**
     * Forma geométrica a ser renderizada.
     */
    private final Shape estampa;
    
    /**
     * Formas a serem usadas como "parceiros", caso a forma principal ultrapasse os 
     * limites da tela.
     */
    private Shape [] apoio = {null, null, null};
    
    /**
     * Preenchimento utilizado na estampa, podendo ser textura ou cor.
     */
    private final Preenchimento preenchimento;
    
    /**
     * Valor da área calculada desta forma.
     */
    private final double area;
    
    
    /**
     * Cria uma estampa de acordo com os parâmetros passados. Também cria 
     * formas de apoio caso a posição da estampa original ultrapassem a tela, 
     * assim dando a sensação de que a tela é ininterrupta.
     * 
     * @param estampa forma a ser renderizada
     * @param preenchimento usado para preencher a forma
     * @param x posição da coordenada X da estampa
     * @param y posição da coordenada Y da estampa
     */
    public Estampa(Shape estampa, Preenchimento preenchimento, double x, double y) {
        this.preenchimento = preenchimento;
        
        AffineTransform translada = AffineTransform.getTranslateInstance(x, y);
        this.estampa = translada.createTransformedShape(estampa);
        this.area = calculaArea(this.estampa);
        if (x + estampa.getBounds2D().getWidth() > mosaicofractal.tela.Tela.LARGURA){
            translada = AffineTransform.getTranslateInstance(-mosaicofractal.tela.Tela.LARGURA, 0);
            this.apoio[0] = translada.createTransformedShape(this.estampa);
        }
        if (y + estampa.getBounds2D().getHeight() > mosaicofractal.tela.Tela.ALTURA){
            if (this.apoio[0] != null){
                translada = AffineTransform.getTranslateInstance(0, -mosaicofractal.tela.Tela.ALTURA);
                this.apoio[1] = translada.createTransformedShape(this.estampa);
                translada = AffineTransform.getTranslateInstance(-mosaicofractal.tela.Tela.LARGURA, -mosaicofractal.tela.Tela.ALTURA);
                this.apoio[2] = translada.createTransformedShape(this.estampa);
            }
            else{
                translada = AffineTransform.getTranslateInstance(0, -mosaicofractal.tela.Tela.ALTURA);
                this.apoio[0] = translada.createTransformedShape(this.estampa);
            }
        }
    }
    
    /**
     * Cria uma cópia da estampa passada.
     * 
     * @param copia estampa a ser copiada
     */
    public Estampa(Estampa copia) {
        final AffineTransform copiadora = AffineTransform.getTranslateInstance(0, 0);
        this.preenchimento = new Preenchimento(copia.preenchimento);
        this.estampa = copiadora.createTransformedShape(copia.estampa);
        if (copia.apoio[0] != null){
            this.apoio[0] = copiadora.createTransformedShape(copia.apoio[0]);
            
            if (copia.apoio[1] != null){
                this.apoio[1] = copiadora.createTransformedShape(copia.apoio[1]);
                this.apoio[2] = copiadora.createTransformedShape(copia.apoio[2]);
            }
        }
        
        this.area = copia.area;
    }
    
    /**
     * Função de encapsulamento da variável <code>area</code> da estampa.
     * 
     * @return a área da estampa
     */
    public double getArea() {
        return area;
    }
    
    /**
     * Função que verifica se há intersecção entre duas estampas. Esta função 
     * também verifica a intersecção com a estampa e as formas de apoio da 
     * outra, caso a variável de apoio_1 de uma não seja null.
     * 
     * @param a uma das estampas
     * @param b uma das estampas
     * @return se houve intersecção ou não
     */
    public static boolean intersecta(Estampa a, Estampa b) {
        Area areaA = new Area(a.estampa);
        areaA.intersect(new Area(b.estampa));
        if (!areaA.isEmpty()) return true;
        
        if (a.apoio[0] != null){
            areaA = new Area(a.apoio[0]);
            areaA.intersect(new Area(b.estampa));
            if (!areaA.isEmpty()) return true;
            
            if (a.apoio[1] != null){
                areaA = new Area(a.apoio[1]);
                areaA.intersect(new Area(b.estampa));
                if (!areaA.isEmpty()) return true;
                
                areaA = new Area(a.apoio[2]);
                areaA.intersect(new Area(b.estampa));
                if (!areaA.isEmpty()) return true;
            }
        }
        
        if (b.apoio[0] != null){
            areaA = new Area(b.apoio[0]);
            areaA.intersect(new Area(a.estampa));
            if (!areaA.isEmpty()) return true;
            
            if (b.apoio[1] != null){
                areaA = new Area(b.apoio[1]);
                areaA.intersect(new Area(a.estampa));
                if (!areaA.isEmpty()) return true;
                
                areaA = new Area(b.apoio[2]);
                areaA.intersect(new Area(a.estampa));
                if (!areaA.isEmpty()) return true;
            }
        }

        return false;
    }
    
    /**
     * Função que verifica se a estampa se encontra dentro da estampa maior. 
     * É utilizada para verificar se a posição da forma se encontra dentro 
     * dos devidos limites de uma tela personalizada.
     * 
     * @param x posição da coordenada X da forma
     * @param y posição da coordenada Y da forma
     * @param tela a forma da tela personalizada
     * @param forma a forma a qual deseja saber se está dentro da tela
     * @return se a forma se encontra dentro ou não da tela
     */
    public static boolean estaDentro(double x, double y, Shape tela, Shape forma) {
        final AffineTransform translada = AffineTransform.getTranslateInstance(x, y);
        Shape teste = translada.createTransformedShape(forma);
        
        Area areaA = new Area(tela);
        Area areaB = new Area(teste);
        
        areaA.intersect(areaB);
        areaB.subtract(areaA);
        return !areaB.isEmpty();
    }
    
    /**
     * Função gráfica para renderizar a estampa. Esta função renderiza também 
     * as demais formas de apoio, caso existam.
     * 
     * @param g2 objeto usado para realizar as operações de pintura
     */
    public void desenha(Graphics2D g2){
        if (preenchimento.isTextura()) {
            TexturePaint tp = new TexturePaint(preenchimento.getTextura(), estampa.getBounds());
            g2.setPaint(tp);
        }
        else {
            g2.setColor(preenchimento.getCor());
        }
        
        g2.fill(estampa);
        
        if (apoio[0] != null){
            g2.fill(apoio[0]);
            
            if (apoio[1] != null){
                g2.fill(apoio[1]);
                g2.fill(apoio[2]);
            }
        }
    }
    
    /**
     * Função que calcula a área de uma forma. É realizada uma operação 
     * similar a uma integral, onde conta-se os pixels preenchidos ao 
     * rasterizar a forma.
     * 
     * @param forma forma na qual deseja-se calcular sua área
     * @return a área da forma passada
     */
    public static double calculaArea(Shape forma){
        int largura = forma.getBounds().width, altura = forma.getBounds().height;
        
        BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagem.createGraphics();
        g2.setColor(Color.BLACK);
        
        AffineTransform transforma = AffineTransform.getTranslateInstance(-forma.getBounds2D().getX(), -forma.getBounds2D().getY());
        Shape formaIntegral = transforma.createTransformedShape(forma);
        g2.fill(formaIntegral);
        
        int count = 0;
        for (int x = 0 ; x < largura ; x++){
            for (int y = 0 ; y < altura ; y++){
                int alpha = ( imagem.getRGB(x, y) >>24 ) & 0xff;
                if (alpha > 0)
                    count++; 
            }
        }
        
        return count;
    }    
}