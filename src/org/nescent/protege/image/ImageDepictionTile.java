package org.nescent.protege.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;

@SuppressWarnings("serial")
public class ImageDepictionTile extends JComponent {

    private final IRI imageIRI;
    private Image image;
    private final SwingWorker<Image, Object> worker;
    private final ComponentListener componentListener = new ComponentListener() {
        @Override
        public void componentShown(ComponentEvent e) {}
        @Override
        public void componentResized(ComponentEvent e) {
            rebuildUI();
        }
        @Override
        public void componentMoved(ComponentEvent e) {}
        @Override
        public void componentHidden(ComponentEvent e) {}
    };

    public ImageDepictionTile(IRI iri) {
        this.imageIRI = iri;
        this.worker = new SwingWorker<Image, Object>() {
            @Override
            protected Image doInBackground() throws MalformedURLException, IOException {
                return ImageIO.read(imageIRI.toURI().toURL());
            }
            @Override
            protected void done() {
                if (!this.isCancelled()) {
                    try {
                        image = this.get();
                        rebuildUI();
                        SwingUtilities.invokeLater(new Runnable() {
                            // this is the only way I can get the images to immediately 
                            // display without resizing or obscuring the window
                            @Override
                            public void run() {
                                revalidate();
                                if (getParent() != null) {
                                    getParent().validate();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        log().error("Image retrieval interrupted: " + imageIRI, e);
                        rebuildUI(e.getLocalizedMessage());
                    } catch (ExecutionException e) {
                        log().error("Problem reading image from URL: " + imageIRI, e);
                        rebuildUI(e.getLocalizedMessage());
                    }
                }                    
            }
            private Logger log() {
                return Logger.getLogger(this.getClass());
            }
        };
        this.addComponentListener(this.componentListener);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.RED));
        this.add(new JLabel("Loading:" + this.imageIRI));
        this.worker.execute();
    }
    
    public void dispose() {
        this.removeComponentListener(this.componentListener);
        this.worker.cancel(true);
    }

    private void rebuildUI() {
        if ((this.image != null) && (this.getParent() != null)) {
            this.removeAll();
            final int imageWidth = this.image.getWidth(null);
            final int parentWidth = this.getParent().getWidth();
            final int resizeWidth = (imageWidth > parentWidth) ? parentWidth : imageWidth;
            final JLabel label = new JLabel(new ImageIcon(this.image.getScaledInstance(resizeWidth, -1, Image.SCALE_DEFAULT)));
            this.add(label);
        }
    }

    private void rebuildUI(String errorMessage) {
        this.removeAll();
        final JLabel label = new JLabel(errorMessage);
        this.add(label);
//        this.setMinimumSize(label.getPreferredSize());
//        this.setPreferredSize(label.getPreferredSize());
    }

    @SuppressWarnings("unused")
    private Logger log() {
        return Logger.getLogger(this.getClass());
    }

}
