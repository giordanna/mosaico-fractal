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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2DIOException;

import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;

/**
 * A classe <code>SalvaImagem</code> cuida de rasterizar a tela gerada pelo 
 * programa para PNG, ou salvando como um documento SVG. Esta classe possui 
 * dependências com a toolkit <code>Batik</code> para converter os elementos 
 * gráficos de Java2D para SVG, e disso salvar o documento.
 * 
 * @author Giordanna De Gregoriis
 * @see Tela
 */
public class SalvaImagem {
    
    /**
     * Variáveis para definir o número do árquivo que será salvo.
     */
    private static int numeroImagem = 0, numeroVetor = 0;
    
    /**
     * Função para salvar os elementos da tela em PNG.
     */
    public static void salvarPNG() {
        File file = new File("imagens_salvas");
        if (!file.exists()) {
            file.mkdirs();
        }
        
        BufferedImage bImg = new BufferedImage(Tela.LARGURA, Tela.ALTURA, BufferedImage.TYPE_INT_ARGB);
        Graphics2D cg = bImg.createGraphics();
        
        Tela.tela.renderizador.setOpaque(false);
        
        Tela.tela.renderizador.paintAll(cg);
        
        try {
            File salva = new File("./imagens_salvas/imagem_"+numeroImagem+".png");
            while (salva.exists()){
                numeroImagem++;
                salva = new File("./imagens_salvas/imagem_"+numeroImagem+".png");
            }
            
            if (ImageIO.write(bImg, "png", new File("./imagens_salvas/imagem_"+numeroImagem+".png"))){
                numeroImagem++;
            }
        }
        catch (IOException e) {}
    }
    
    /**
     * Função para salvar os elementos da tela em SVG.
     */
    public static void salvarSVG() {
        File file = new File("vetores_salvos");
        if (!file.exists()) {
            file.mkdirs();
        }
        
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgNamespaceURI = "http://www.w3.org/2000/svg";

        Document document = domImpl.createDocument(svgNamespaceURI, "svg", null);
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        Tela.tela.renderizador.setOpaque(false);
        Tela.tela.renderizador.paintAll(svgGenerator);

        boolean useCSS = true;
        Writer out;
        try {
            File salva = new File("./vetores_salvos/vetor_"+numeroVetor+".svg");
            while (salva.exists()){
                numeroVetor++;
                salva = new File("./vetores_salvos/vetor_"+numeroVetor+".svg");
            }
            
            out = new OutputStreamWriter(new FileOutputStream(salva), "UTF-8");
            svgGenerator.stream(out, useCSS);
        } catch (UnsupportedEncodingException | SVGGraphics2DIOException | FileNotFoundException ex) {
            Logger.getLogger(SalvaImagem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
