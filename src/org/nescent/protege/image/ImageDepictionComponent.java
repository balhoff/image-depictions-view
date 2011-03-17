package org.nescent.protege.image;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridy = 0;
        //this.add(new JLabel("<html>Direct: " + this.model.getDirectImageDepictions().toString() + "</html>"), gbc);
        this.add(new JLabel("Direct image annotations"), gbc);
        for (IRI iri : this.model.getDirectImageDepictions()) {
            gbc.gridy += 1;
            this.add(new ImageDepictionTile(iri), gbc);
            //final BufferedImage image = ImageIO.read(iri.toURI().toURL());
            //final BufferedImage image = ImageIO.read(new URL("http://images.apple.com/v20110310162107/startpage/images/promo_ipad_takeover_black20110308.jpg"));
            //final ImageIcon icon = new ImageIcon(new URL("http://images.apple.com/v20110310162107/startpage/images/promo_ipad_takeover_black20110308.jpg"));
            //final URLConnection connection = iri.toURI().toURL().openConnection();
            //                final URLConnection connection = new URL("http://images.apple.com/v20110310162107/startpage/images/promo_ipad_takeover_black20110308.jpg").openConnection();
            //                connection.connect();
            //                final Object content = connection.getContent();
            //this.add(new JLabel(content.getClass().toString()), gbc);

            //this.add(new JLabel(icon), gbc);
            //this.add(new JLabel(new ImageIcon(image)), gbc);
        }
        gbc.gridy += 1;
        this.add(new JLabel("Inferred image annotations"), gbc);
        for (IRI iri : this.model.getInferredImageDepictions()) {
            gbc.gridy += 1;
            this.add(new ImageDepictionTile(iri), gbc);
        }
        this.getParent().validate();
        this.repaint();
    }
    
    @SuppressWarnings("unused")
    private Logger log() {
        return Logger.getLogger(this.getClass());
    }
    
}
