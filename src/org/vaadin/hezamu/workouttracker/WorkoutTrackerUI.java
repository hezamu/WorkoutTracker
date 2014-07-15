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
	private WorkoutPresenter presenter;

	@Override
	protected void init(VaadinRequest request) {
		presenter = new WorkoutPresenter();

		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		setContent(layout);

		final Label title = new Label("Workout Tracker "
				+ FontAwesome.SIGNAL.getHtml(), ContentMode.HTML);
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		layout.addComponent(title);
		layout.setComponentAlignment(title, Alignment.TOP_CENTER);

		final HorizontalLayout result = new HorizontalLayout();
		result.setSpacing(true);

		result.addComponent(presenter.getEditor());

		result.addComponent(presenter.getGraph());
		result.setExpandRatio(presenter.getGraph(), 1);

		layout.addComponent(result);
		layout.setExpandRatio(result, 1);
		layout.setComponentAlignment(result, Alignment.TOP_CENTER);
	}
}