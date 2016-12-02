package mosaicofractal.elementos;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * A classe <code>Preenchimento</code> cuida de encapsular valores relacionados 
 * ao elemento usado para preencher uma {@link Estampa}. Um preenchimento pode 
 * ser uma simples cor, ou uma textura, a qual é uma imagem rasterizada.
 * 
 * @author Giordanna De Gregoriis
 * @see Estampa
 */
public class Preenchimento {
    
    /**
     * Variável para informar se o preenchimento é uma textura ou não.
     */
    private boolean isTextura = false;
    
    /**
     * A cor armazenada.
     */
    private Color cor = null;
    
    /**
     * A textura armazenada.
     */
    private BufferedImage textura = null;
    
    /**
     * Construtor onde o preenchimento da estampa é definido por uma cor.
     * 
     * @param cor a cor a ser armazenada
     */
    public Preenchimento(Color cor){
        this.cor = cor;
    }
    
    /**
     * Construtor onde o preenchimento da estampa é definido por uma textura.
     * 
     * @param textura a textura a ser armazenada
     */
    public Preenchimento (BufferedImage textura){
        isTextura = true;
        this.textura = textura;
    }
    
    /**
     * Construtor para copiar um determinado preenchimento.
     * 
     * @param copia preenchimento a ser copiado
     */
    public Preenchimento (Preenchimento copia){
        this.isTextura = copia.isTextura;
        this.cor = copia.cor;
        this.textura = copia.textura;
    }
    
    /**
     * Encapsulamento da variável <code>isTextura</code>, informando se o 
     * preenchimento é do tipo textura ou não.
     * 
     * @return se é uma textura ou não
     */
    public boolean isTextura(){
        return isTextura;
    }
    
    /**
     * Encapsulamento da variável <code>textura</code>.
     * 
     * @return a textura da estampa
     */
    public BufferedImage getTextura(){
        return textura;
    }
    
    /**
     * Encapsulamento da variável <code>cor</code>.
     * 
     * @return a cor da estampa
     */
    public Color getCor(){
        return cor;
    }
}
