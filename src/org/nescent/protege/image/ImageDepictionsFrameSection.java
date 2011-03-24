package org.nescent.protege.image;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSection;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.OWLFrameSectionRow;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.NodeSet;

public class ImageDepictionsFrameSection extends AbstractOWLFrameSection<OWLClassExpression, OWLIndividualAxiom, OWLNamedIndividual> {

	public static final String LABEL = "Image depictions";

	private Set<OWLIndividual> added = new HashSet<OWLIndividual>();


	public ImageDepictionsFrameSection(OWLEditorKit editorKit, OWLFrame<? extends OWLClassExpression> frame) {
		super(editorKit, LABEL, "Image depiction", frame);
	}

	@Override
	protected void clear() {
		for (OWLFrameSectionRow<OWLClassExpression, OWLIndividualAxiom, OWLNamedIndividual> row : this.getRows()) {
			if (row instanceof ImageDepictionsFrameSectionRow) {
				((ImageDepictionsFrameSectionRow)row).dispose();
			}
		}
		this.added.clear();
	}

	@Override
	protected void refill(OWLOntology ontology) {
		final OWLClassExpression classOfDepictions = this.getRootObject();
		if (classOfDepictions instanceof OWLObjectHasValue) {
			final OWLObjectHasValue hasValue = (OWLObjectHasValue)classOfDepictions;
			final Set<OWLObjectPropertyAssertionAxiom> objectPropertyAssertionAxioms = ontology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION, true);
			for (OWLObjectPropertyAssertionAxiom axiom : objectPropertyAssertionAxioms) {
				if ((axiom.getProperty().equals(hasValue.getProperty()) && (axiom.getObject().equals(hasValue.getValue())) && (axiom.getSubject() instanceof OWLNamedIndividual))) {
					this.added.add(axiom.getSubject());
					this.addRow(new ImageDepictionsFrameSectionRow(getOWLEditorKit(), this, ontology, getRootObject(), axiom));
				}
			}
		}
		final Set<OWLClassAssertionAxiom> axioms = ontology.getAxioms(AxiomType.CLASS_ASSERTION, true);
            for (OWLClassAssertionAxiom axiom : axioms) {
                if ((axiom.getIndividual() instanceof OWLNamedIndividual) && (axiom.getClassExpression().equals(classOfDepictions))) {
                	final OWLIndividual depiction = axiom.getIndividual();
                	if (!this.added.contains(depiction)) {
                		this.added.add(axiom.getIndividual());
                        this.addRow(new ImageDepictionsFrameSectionRow(getOWLEditorKit(), this, ontology, getRootObject(), axiom));	
            		}
                }
            }
	}

	@Override
	protected void refillInferred() {
		final OWLClassExpression classOfDepictions = this.getRootObject();
         final NodeSet<OWLNamedIndividual> depictions = this.getReasoner().getInstances(classOfDepictions, false);
         for (OWLNamedIndividual depiction : depictions.getFlattened()) {
        	 if (!this.added.contains(depiction)) {
        		 final OWLClassAssertionAxiom axiom = this.getOWLDataFactory().getOWLClassAssertionAxiom(classOfDepictions, depiction);
                 this.added.add(depiction);
                 this.addRow(new ImageDepictionsFrameSectionRow(getOWLEditorKit(), this, null, getRootObject(), axiom));
        	 }
         }
	}

	@Override
	protected OWLClassAssertionAxiom createAxiom(OWLNamedIndividual object) {
		return null; // canAdd() = false
	}

	@Override
	public OWLObjectEditor<OWLNamedIndividual> getObjectEditor() {
		return null; // canAdd() = false
	}

	public Comparator<OWLFrameSectionRow<OWLClassExpression, OWLIndividualAxiom, OWLNamedIndividual>> getRowComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canAdd() {
		return false;
	}


}
