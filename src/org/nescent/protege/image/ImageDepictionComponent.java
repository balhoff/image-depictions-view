package org.nescent.protege.image;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Scrollable;

import org.apache.log4j.Logger;
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
        if (this.model.getSubjectIRI() != null) {
            this.add(new JLabel(this.model.getSubjectIRI().toString()));
        }
        this.getParent().validate();
        this.repaint();
    }
    
    private Logger log() {
        return Logger.getLogger(this.getClass());
    }
    
}
