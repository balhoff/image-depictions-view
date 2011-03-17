package org.nescent.protege.image;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;

@SuppressWarnings("serial")
public class ImageDepictionTile extends JComponent {
    
    private final IRI imageIRI;
    
    public ImageDepictionTile(IRI imageIRI) {
        this.imageIRI = imageIRI;
        this.setLayout(new BorderLayout());
        try {
            final BufferedImage image = ImageIO.read(this.imageIRI.toURI().toURL());
            final JLabel label = new JLabel(new ImageIcon(image));
            this.add(label);
        } catch (MalformedURLException e) {
            this.add(new JLabel("Invalid image URL: " + this.imageIRI));
            log().error("Invalid image URL: " + this.imageIRI, e);
        } catch (IOException e) {
            this.add(new JLabel("Problem reading image from URL: " + this.imageIRI));
            log().error("Problem reading image from URL: " + this.imageIRI, e);
        }
    }
    
    private Logger log() {
        return Logger.getLogger(this.getClass());
    }
    
}
