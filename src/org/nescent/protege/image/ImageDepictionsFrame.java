package org.nescent.protege.image;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.AbstractOWLFrame;
import org.semanticweb.owlapi.model.OWLClassExpression;

public class ImageDepictionsFrame extends AbstractOWLFrame<OWLClassExpression> {

	public ImageDepictionsFrame(OWLEditorKit editorKit) {
        super(editorKit.getModelManager().getOWLOntologyManager());
        addSection(new ImageDepictionsFrameSection(editorKit, this));
    }

}
