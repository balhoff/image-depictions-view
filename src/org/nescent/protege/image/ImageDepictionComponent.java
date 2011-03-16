package org.nescent.protege.image;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Scrollable;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

@SuppressWarnings("serial")
public class ImageDepictionComponent extends JComponent implements Scrollable {
    
    private final ImageDepictionModel model;
    private final ImageDepictionModelListener modelListener = new ImageDepictionModelListener() {
        @Override
        public void modelChanged() {
            rebuildUI();
        }
    };
    
    public ImageDepictionComponent(ImageDepictionModel model) {
        super();
        setLayout(new GridBagLayout());
        setVisible(true);
        this.model = model;
        model.addModelListener(this.modelListener);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(300, 200);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle rectangle, int i, int i1) {
        return getFontMetrics(getFont()).getHeight();
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle rectangle, int i, int i1) {
        return 100;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    public void setSubject(OWLEntity subject){
        this.model.setSubject(subject);
    }
    
    private void rebuildUI() {
        this.removeAll();
        final GridBagConstraints gbc = new GridBagConstraints();
        //this.add(new JLabel("<html>Direct: " + this.model.getDirectImageDepictions().toString() + "</html>"), gbc);
        for (IRI iri : this.model.getDirectImageDepictions()) {
            try {
                gbc.gridy++;
                final BufferedImage image = ImageIO.read(iri.toURI().toURL());
                //final BufferedImage image = ImageIO.read(new URL("http://images.apple.com/v20110310162107/startpage/images/promo_ipad_takeover_black20110308.jpg"));
                //final ImageIcon icon = new ImageIcon(new URL("http://images.apple.com/v20110310162107/startpage/images/promo_ipad_takeover_black20110308.jpg"));
                //final URLConnection connection = iri.toURI().toURL().openConnection();
//                final URLConnection connection = new URL("http://images.apple.com/v20110310162107/startpage/images/promo_ipad_takeover_black20110308.jpg").openConnection();
//                connection.connect();
//                final Object content = connection.getContent();
                //this.add(new JLabel(content.getClass().toString()), gbc);
                
                //this.add(new JLabel(icon), gbc);
                this.add(new JLabel(new ImageIcon(image)), gbc);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        gbc.gridy++;
        //this.add(new JLabel("<html>Inferred: " + this.model.getInferredImageDepictions().toString() + "</html>"), gbc);
        this.getParent().validate();
        this.repaint();
    }
    
    private Logger log() {
        return Logger.getLogger(this.getClass());
    }
    
}
