package org.nescent.protege.image;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.framelist.OWLFrameListRenderer;

public class ImageDepictionCellRenderer extends OWLFrameListRenderer {
	
	public ImageDepictionCellRenderer(OWLEditorKit owlEditorKit) {
		super(owlEditorKit);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		final Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (component instanceof JComponent) {
			final JPanel panel = new JPanel(new GridBagLayout());
			final GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			panel.add(((JComponent)component), gbc);
			gbc.gridy = 1;
			gbc.weighty = 0;
			gbc.insets = new Insets(5, 0, 0, 0);
			JLabel label;
			try {
				label = new JLabel(new ImageIcon(ImageIO.read(new URL("http://upload.wikimedia.org/wikipedia/commons/2/22/Keulemans_Laughing_Owl.jpg")).getScaledInstance(300, -1, Image.SCALE_DEFAULT)));
			} catch (MalformedURLException e) {
				label = new JLabel(e.getLocalizedMessage());
			} catch (IOException e) {
				label = new JLabel(e.getLocalizedMessage());
			}
			panel.add(label, gbc);
			return panel;
		}
		return component;
//		final AbstractOWLFrameSectionRow row = (AbstractOWLFrameSectionRow) value;
//		final List<?> objects = row.getManipulatableObjects();
////		for (Object item : objects) {
////			if (item instanceof OWLNamedIndividual) {
////				return new ImageDepictionTile(((OWLNamedIndividual)item).getIRI());
////			}
////		}
//		
//		return new JLabel("HELLO");
	}
	
	

}
