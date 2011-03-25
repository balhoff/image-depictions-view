package org.nescent.protege.image;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.framelist.OWLFrameListRenderer;

public class ImageDepictionCellRenderer extends OWLFrameListRenderer {

	private final JPanel panel = new JPanel(new GridBagLayout());
	private final JLabel imageLabel = new JLabel();
	private JComponent owlComponent;

	public ImageDepictionCellRenderer(OWLEditorKit owlEditorKit) {
		super(owlEditorKit);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		final Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		this.imageLabel.setIcon(null);
		this.imageLabel.setText(null);		
		if (component instanceof JComponent) {
			if (this.owlComponent == null) {
				this.owlComponent = (JComponent)component;
				this.addComponents();
			}
			if (value instanceof ImageDepictionsFrameSectionRow) {
				final ImageDepictionsFrameSectionRow row = (ImageDepictionsFrameSectionRow)value;
				final Image image = row.getImage();
				if (image != null) {
					final int imageWidth = image.getWidth(null);
		            final int parentWidth = Math.max(0, list.getWidth() - 20);
		            final int resizeWidth = (imageWidth > parentWidth) ? parentWidth : imageWidth;
					this.imageLabel.setIcon((new ImageIcon(image.getScaledInstance(resizeWidth, -1, Image.SCALE_DEFAULT))));
				} else {
					if (row.isLoading()) {
						this.imageLabel.setText("<HTML><I>Loading image...</I></HTML>");
					}
				}
			}
			return panel;
		}
		return component;
	}

	private void addComponents() {
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		this.panel.add(this.owlComponent, gbc);
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(3, 5, 5, 0);
		this.imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.panel.add(this.imageLabel, gbc);
	}

	@SuppressWarnings("unused")
	private Logger log() {
		return Logger.getLogger(this.getClass());
	}

}
