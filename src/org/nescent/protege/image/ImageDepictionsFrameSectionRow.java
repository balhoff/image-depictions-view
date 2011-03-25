package org.nescent.protege.image;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.protege.editor.core.ui.list.MListButton;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSectionRow;
import org.protege.editor.owl.ui.frame.OWLFrameSection;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

public class ImageDepictionsFrameSectionRow extends AbstractOWLFrameSectionRow<OWLClassExpression, OWLIndividualAxiom, OWLNamedIndividual> {

	private final SwingWorker<Image, Object> worker;
	private Image image;
	private boolean loading = false;

	protected ImageDepictionsFrameSectionRow(OWLEditorKit owlEditorKit, OWLFrameSection<OWLClassExpression, OWLIndividualAxiom, OWLNamedIndividual> section, OWLOntology ontology, OWLClassExpression rootObject, OWLIndividualAxiom axiom) {
		super(owlEditorKit, section, ontology, rootObject, axiom);
		this.worker = new SwingWorker<Image, Object>() {
			@Override
			protected Image doInBackground() throws MalformedURLException, IOException {
				return ImageIO.read(getImageIRI().toURI().toURL());
			}
			@Override
			protected void done() {
				if (!this.isCancelled()) {
					try {
						loading = false;
						image = this.get();
						getFrameSection().getFrame().fireContentChanged();
					} catch (InterruptedException e) {
						log().error("Image retrieval interrupted: " + getImageIRI(), e);
					} catch (ExecutionException e) {
						log().error("Problem reading image from URL: " + getImageIRI(), e);
					}
				}                    
			}
			private Logger log() {
				return Logger.getLogger(this.getClass());
			}
		};
	}

	public List<? extends OWLObject> getManipulatableObjects() {
		if (this.getAxiom() instanceof OWLObjectPropertyAssertionAxiom) {
			final OWLObjectPropertyAssertionAxiom axiom = (OWLObjectPropertyAssertionAxiom)(this.getAxiom());
			return Arrays.asList(axiom.getSubject());
		} else if (this.getAxiom() instanceof OWLClassAssertionAxiom) {
			final OWLClassAssertionAxiom axiom = (OWLClassAssertionAxiom)(this.getAxiom());
			return Arrays.asList(axiom.getIndividual());
		} else {
			return null;
		}
	}

	public Image getImage() {
		return this.image;
	}

	public void dispose() {
		this.worker.cancel(true);
	}
	
	public void downloadImage() {
		this.loading = true;
		getFrameSection().getFrame().fireContentChanged();
		this.worker.execute();
	}
	
	public boolean isLoading() {
		return this.loading;
	}

	private IRI getImageIRI() {
		final OWLIndividual subject;
		if (this.getAxiom() instanceof OWLObjectPropertyAssertionAxiom) {
			final OWLObjectPropertyAssertionAxiom axiom = (OWLObjectPropertyAssertionAxiom)(this.getAxiom());
			subject = axiom.getSubject();
		} else if (this.getAxiom() instanceof OWLClassAssertionAxiom) {
			final OWLClassAssertionAxiom axiom = (OWLClassAssertionAxiom)(this.getAxiom());
			subject = axiom.getIndividual();
		} else {
			subject = null;
		}
		if (subject instanceof OWLNamedIndividual) {
			return ((OWLNamedIndividual)subject).getIRI();
		} else {
			return null;
		}
	}

	@Override
	protected OWLIndividualAxiom createAxiom(OWLNamedIndividual individual) {
		return this.getAxiom();
	}

	@Override
	protected OWLObjectEditor<OWLNamedIndividual> getObjectEditor() {
		return null;
	}

	@Override
	public boolean isDeleteable() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public List<MListButton> getAdditionalButtons() {
		if ((this.image == null) && (this.loading == false)) {
			return Arrays.asList((MListButton)(new ImageDownloadButton(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					downloadImage();
				}
			})));
		} else {
			return Collections.emptyList();
		}
	}

}
