package org.nescent.protege.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
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
        public void modelChanged() {
            rebuildUI();
        }
    };
    private final List<ImageDepictionTile> tiles = new ArrayList<ImageDepictionTile>();
    
    public ImageDepictionComponent(ImageDepictionModel model) {
        super();
        setLayout(new GridBagLayout());
        setVisible(true);
        this.model = model;
        model.addModelListener(this.modelListener);
        this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    }

    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(300, 200);
    }

    public int getScrollableUnitIncrement(Rectangle rectangle, int i, int i1) {
        return getFontMetrics(getFont()).getHeight();
    }

    public int getScrollableBlockIncrement(Rectangle rectangle, int i, int i1) {
        return 100;
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    public void setSubject(OWLEntity subject){
        this.model.setSubject(subject);
    }
    
    public void dispose() {
        this.disposeTiles();
    }
    
    private void rebuildUI() {
        this.disposeTiles();
        this.removeAll();
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        final JLabel directLabel = new JLabel("Direct image annotations");
        this.add(directLabel, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        for (IRI iri : this.model.getDirectImageDepictions()) {
            gbc.gridy += 1;
            final ImageDepictionTile tile = new ImageDepictionTile(iri);
            this.tiles.add(tile);
            this.add(tile, gbc);
        }
        gbc.gridy += 1;
        gbc.anchor = GridBagConstraints.WEST;
        final JLabel inferredLabel = new JLabel("Inferred image annotations");
        this.add(inferredLabel, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        for (IRI iri : this.model.getInferredImageDepictions()) {
            gbc.gridy += 1;
            final ImageDepictionTile tile = new ImageDepictionTile(iri);
            this.tiles.add(tile);
            this.add(tile, gbc);
        }
    }
    
    private void disposeTiles() {
        for (ImageDepictionTile tile : this.tiles) {
            tile.dispose();
        }
        this.tiles.clear();
    }
    
    @SuppressWarnings("unused")
    private Logger log() {
        return Logger.getLogger(this.getClass());
    }
    
}
