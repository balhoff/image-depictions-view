package org.nescent.protege.image;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class ImageDepictionModel {
    
    private final String DEPICTS = "http://xmlns.com/foaf/0.1/depicts";
    private final OWLObjectProperty depicts;
    private final OWLModelManager manager;
    private final Set<ImageDepictionModelListener> listeners = new HashSet<ImageDepictionModelListener>();
    private final OWLOntologyChangeListener ontologyChangeListener = new OWLOntologyChangeListener() {
        @Override
        public void ontologiesChanged(List<? extends OWLOntologyChange> list) throws OWLException {
            handleOntologyChanges(list);
        }
    };
    private OWLEntity subject;
    
    public ImageDepictionModel(OWLModelManager manager) {
        this.manager = manager;
        this.manager.addOntologyChangeListener(ontologyChangeListener);
        this.depicts = this.manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(DEPICTS));
    }

    public void addModelListener(ImageDepictionModelListener modelListener) {
        listeners.add(modelListener);
    }

    public void removeModelListener(ImageDepictionModelListener modelListener){
        listeners.remove(modelListener);
    }
    
    public void dispose() {
        this.manager.removeOntologyChangeListener(this.ontologyChangeListener);
        listeners.clear();
    }
    
    private void handleOntologyChanges(List<? extends OWLOntologyChange> changes) {
        //TODO handle if depicts are changed - we would need to check what we're displaying
//        for (OWLOntologyChange change : changes){
//            if (change.isAxiomChange() &&
//                change.getAxiom().isOfType(AxiomType.ANNOTATION_ASSERTION) &&
//                ((OWLAnnotationAssertionAxiom)change.getAxiom()).getSubject().equals(subject)){
//                refresh();
//                return;
//            }
//        }
    }

    public void setSubject(OWLEntity newSubject) {
        if (newSubject != null) {
            if ((newSubject instanceof OWLClass) || (newSubject instanceof OWLNamedIndividual)) {
                this.subject = newSubject;
            }
        } else {
            this.subject = null;
        }
        notifyStructureChanged();
    }
    
    public OWLEntity getSubject() {
        return this.subject;
    }
    
    public Set<IRI> getDirectImageDepictions() {
        final OWLDataFactory factory = this.manager.getOWLDataFactory();
        final Set<IRI> depictionIRIs = new HashSet<IRI>();
        if (this.subject instanceof OWLClass) {
            final OWLClass subjectClass = (OWLClass)this.subject;
            final OWLClassExpression classOfDepictions = factory.getOWLObjectSomeValuesFrom(depicts, subjectClass);
            final Set<OWLClassAssertionAxiom> axioms = this.manager.getActiveOntology().getAxioms(AxiomType.CLASS_ASSERTION, true);
            for (OWLClassAssertionAxiom axiom : axioms) {
                if ((axiom.getIndividual() instanceof OWLNamedIndividual) && (axiom.getClassExpression().equals(classOfDepictions))) {
                    depictionIRIs.add(((OWLNamedIndividual)axiom.getIndividual()).getIRI());
                }
            }
        } else if (this.subject instanceof OWLNamedIndividual) {
            //TODO
        }
        return depictionIRIs;
    }
    
    public Set<IRI> getInferredImageDepictions() {
        final OWLDataFactory factory = this.manager.getOWLDataFactory();
        final OWLReasoner reasoner = this.manager.getOWLReasonerManager().getCurrentReasoner();
        final Set<IRI> depictionIRIs = new HashSet<IRI>();
        if (this.subject instanceof OWLClass) {
            final OWLClass subjectClass = (OWLClass)this.subject;
            final OWLClassExpression classOfDepictions = factory.getOWLObjectSomeValuesFrom(depicts, subjectClass);
            final NodeSet<OWLNamedIndividual> depictions = reasoner.getInstances(classOfDepictions, false);
            for (OWLNamedIndividual depiction : depictions.getFlattened()) {
                depictionIRIs.add(depiction.getIRI());
            }
        } else if (this.subject instanceof OWLNamedIndividual) {
            //TODO
        }
        depictionIRIs.removeAll(this.getDirectImageDepictions());
        return depictionIRIs;
    }
    
    private void notifyStructureChanged() {
        for (ImageDepictionModelListener listener : this.listeners){
            listener.modelChanged();
        }
    }
    
    private Logger log() {
        return Logger.getLogger(this.getClass());
    }

}
