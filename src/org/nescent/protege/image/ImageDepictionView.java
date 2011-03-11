package org.nescent.protege.image;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import org.protege.editor.owl.ui.view.AbstractOWLSelectionViewComponent;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;

@SuppressWarnings("serial")
public class ImageDepictionView extends AbstractOWLSelectionViewComponent {

    private ImageDepictionModel model;
    private ImageDepictionComponent component;

    @Override
    public void initialiseView() {
        this.setLayout(new BorderLayout());
        this.model = new ImageDepictionModel(this.getOWLModelManager());
        this.component = new ImageDepictionComponent(this.model);
        final JScrollPane scroller = new JScrollPane(this.component);
        this.add(scroller, BorderLayout.CENTER);
    }

    @Override
    public void disposeView() {
        this.model.dispose();
    }

    @Override
    protected OWLObject updateView() {
        final OWLEntity selectedEntity = this.getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
        this.component.setSubject(selectedEntity);
        return selectedEntity;
    }

    @Override
    protected boolean isOWLClassView() {
        return true;
    }


    @Override
    protected boolean isOWLObjectPropertyView() {
        return false;
    }


    @Override
    protected boolean isOWLDataPropertyView() {
        return false;
    }


    @Override
    protected boolean isOWLIndividualView() {
        return true;
    }


    @Override
    protected boolean isOWLDatatypeView() {
        return false;
    }


    @Override
    protected boolean isOWLAnnotationPropertyView() {
        return false;
    }
}
