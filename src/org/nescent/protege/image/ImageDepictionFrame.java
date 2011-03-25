package org.nescent.protege.image;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.AbstractOWLFrame;
import org.semanticweb.owlapi.model.OWLClassExpression;

public class ImageDepictionFrame extends AbstractOWLFrame<OWLClassExpression> {

	public ImageDepictionFrame(OWLEditorKit editorKit) {
        super(editorKit.getModelManager().getOWLOntologyManager());
        addSection(new ImageDepictionFrameSection(editorKit, this));
    }

}
