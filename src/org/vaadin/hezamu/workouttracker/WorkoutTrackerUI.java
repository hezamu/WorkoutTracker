package org.vaadin.hezamu.workouttracker;

import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("valo")
public class WorkoutTrackerUI extends UI {
	@Override
	protected void init(VaadinRequest request) {
		// Setup the root layout of the UI
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		setContent(layout);

		// Setup the app title
		final Label title = new Label("Workout Tracker "
				+ FontAwesome.SIGNAL.getHtml(), ContentMode.HTML);
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		layout.addComponent(title);
		layout.setComponentAlignment(title, Alignment.TOP_CENTER);

		// Setup the layout that will contain the views
		final HorizontalLayout view = new HorizontalLayout();
		view.setSpacing(true);
		layout.addComponent(view);
		layout.setExpandRatio(view, 1);
		layout.setComponentAlignment(view, Alignment.TOP_CENTER);

		// Create a presenter and setup the views
		final WorkoutPresenter presenter = new WorkoutPresenter();

		view.addComponent(presenter.editor);

		view.addComponent(presenter.graph);
		view.setExpandRatio(presenter.graph, 1);
	}
}