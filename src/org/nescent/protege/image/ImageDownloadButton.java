package org.nescent.protege.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;

import org.protege.editor.core.ui.list.MListButton;

public class ImageDownloadButton extends MListButton {

	private static final Color ROLL_OVER_COLOR = new Color(51, 204, 51);
	private static final String ARROW = String.format("%c", '\u2713');

	public ImageDownloadButton(ActionListener actionListener) {
		super("Download image", ROLL_OVER_COLOR, actionListener);
	}

	@Override
	public void paintButtonContent(Graphics2D graphics) {
		final Font font = graphics.getFont();
		graphics.setFont(font.deriveFont(Font.BOLD));
		int stringWidth = graphics.getFontMetrics().getStringBounds(ARROW, graphics).getBounds().width;
		int w = getBounds().width;
		int h = getBounds().height;
		graphics.drawString(ARROW,
				getBounds().x + w / 2 - stringWidth / 2,
				getBounds().y + graphics.getFontMetrics().getAscent() / 2 + h / 2);
		graphics.setFont(font);
	}

}
