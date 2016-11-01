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

public class SalvaImagem {
    private static int i = 0, v = 0;
    
    public static void salvarPNG() {
        File file = new File("imagens_salvas");
        if (!file.exists()) {
            file.mkdirs();
        }
        
        BufferedImage bImg = new BufferedImage(Area.area.LARGURA, Area.area.ALTURA, BufferedImage.TYPE_INT_ARGB);
        Graphics2D cg = bImg.createGraphics();
        Area.area.revalidate();
        Area.area.paintAll(cg);
        
        try {
            File salva = new File("./imagens_salvas/imagem_"+i+".png");
            while (salva.exists()){
                i++;
                salva = new File("./imagens_salvas/imagem_"+i+".png");
            }
            
            if (ImageIO.write(bImg, "png", new File("./imagens_salvas/imagem_"+i+".png"))){
                i++;
            }
        }
        catch (IOException e) {}
    }
    
    public static void salvarSVG() {
        File file = new File("vetores_salvos");
        if (!file.exists()) {
            file.mkdirs();
        }
        Area.area.revalidate();
        // Get a DOMImplementation
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgNamespaceURI = "http://www.w3.org/2000/svg";

        // Create an instance of org.w3c.dom.Document
        Document document = domImpl.createDocument(svgNamespaceURI, "svg", null);

        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // Render into the SVG Graphics2D implementation
        Area.area.paintAll(svgGenerator);

        // Finally, stream out SVG to the standard output using UTF-8
        // character to byte encoding
        boolean useCSS = true; // we want to use CSS style attribute
        Writer out;
        try {
            File salva = new File("./vetores_salvos/vetor_"+v+".svg");
            while (salva.exists()){
                v++;
                salva = new File("./vetores_salvos/vetor_"+v+".svg");
            }
            
            out = new OutputStreamWriter(new FileOutputStream(salva), "UTF-8");
            svgGenerator.stream(out, useCSS);
        } catch (UnsupportedEncodingException | SVGGraphics2DIOException | FileNotFoundException ex) {
            Logger.getLogger(SalvaImagem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
