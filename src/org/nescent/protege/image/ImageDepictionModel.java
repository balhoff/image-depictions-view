package org.nescent.protege.image;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;

public class ImageDepictionModel {
    
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
    
    /**
     * Temporary method for proof of concept.
     */
    public IRI getSubjectIRI() {
        return this.subject != null ? this.subject.getIRI() : null; 
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
