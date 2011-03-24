package org.nescent.protege.image;

import java.util.Arrays;
import java.util.List;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSectionRow;
import org.protege.editor.owl.ui.frame.OWLFrameSection;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

public class ImageDepictionsFrameSectionRow extends AbstractOWLFrameSectionRow<OWLClassExpression, OWLIndividualAxiom, OWLNamedIndividual> {

	protected ImageDepictionsFrameSectionRow(OWLEditorKit owlEditorKit, OWLFrameSection<OWLClassExpression, OWLIndividualAxiom, OWLNamedIndividual> section, OWLOntology ontology, OWLClassExpression rootObject, OWLIndividualAxiom axiom) {
		super(owlEditorKit, section, ontology, rootObject, axiom);
		System.err.println("Created ImageDepictionsFrameSectionRow");
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

}
