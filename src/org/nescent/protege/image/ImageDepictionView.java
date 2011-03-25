package org.nescent.protege.image;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import org.protege.editor.owl.ui.framelist.OWLFrameList;
import org.protege.editor.owl.ui.view.AbstractOWLSelectionViewComponent;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import com.volantis.map.ics.imageio.SVGActivator;

@SuppressWarnings("serial")
public class ImageDepictionView extends AbstractOWLSelectionViewComponent {

	private static final SVGActivator svgActivator = new SVGActivator();
	static {
		svgActivator.activate();
	}

	private OWLFrameList<OWLClassExpression> list;
	private static final String DEPICTS = "http://xmlns.com/foaf/0.1/depicts";
	private OWLObjectProperty depicts;

	@Override
	public void initialiseView() {
		this.depicts = this.getOWLModelManager().getOWLDataFactory().getOWLObjectProperty(IRI.create(DEPICTS));
		this.list = new OWLFrameList<OWLClassExpression>(this.getOWLEditorKit(), new ImageDepictionsFrame(getOWLEditorKit()));
		this.list.setCellRenderer(new ImageDepictionCellRenderer(this.getOWLEditorKit()));
		setLayout(new BorderLayout());
		add(new JScrollPane(this.list));
	}

	@Override
	public void disposeView() {
		this.list.dispose();
	}

	@Override
	protected OWLObject updateView() {
		final OWLEntity selectedEntity = this.getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
		if (selectedEntity instanceof OWLClass) {
			final OWLClass subjectClass = (OWLClass)selectedEntity;
			final OWLClassExpression classOfDepictions = this.getOWLModelManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(depicts, subjectClass);
			this.list.setRootObject(classOfDepictions);
		} else if (selectedEntity instanceof OWLNamedIndividual) {
			final OWLNamedIndividual individual = (OWLNamedIndividual)selectedEntity;
			final OWLClassExpression classOfDepictions = this.getOWLModelManager().getOWLDataFactory().getOWLObjectHasValue(depicts, individual);
			this.list.setRootObject(classOfDepictions);
		}
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
