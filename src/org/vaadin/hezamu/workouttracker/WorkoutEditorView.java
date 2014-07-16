package org.vaadin.hezamu.workouttracker;

import java.util.Date;

import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WorkoutEditorView extends GridLayout {
	final ComboBox activity;
	final DateField date;
	final TextField duration, avgHR, maxHR, calories, rating;
	final TextArea comment;
	final Button add, clear;

	public WorkoutEditorView() {
		super(2, 7);

		setSpacing(true);

		Label title = new Label("New Workout");
		title.addStyleName(ValoTheme.LABEL_H3);
		title.addStyleName(ValoTheme.LABEL_BOLD);
		title.setSizeUndefined();
		addComponent(title, 0, 0, 1, 0);
		setComponentAlignment(title, Alignment.TOP_CENTER);

		addComponent(activity = new ComboBox("Activity"));
		activity.addItems(WorkoutPresenter.ACTIVITIES);
		activity.setRequired(true);
		activity.setInputPrompt("Required");

		addComponent(duration = new TextField("Duration"));
		duration.setRequired(true);
		duration.setInputPrompt("Required");
		duration.addValidator(new IntegerFieldValidator(1, 300));

		addComponent(date = new DateField("Date"));
		date.setValue(new Date());
		date.setRequired(true);
		date.setDateFormat("dd.MM.yyyy");
		date.addValidator(value -> {
			if (value instanceof Date && new Date().before((Date) value))
				throw new InvalidValueException("Date can't be in the future");
		});

		addComponent(calories = new TextField("Calories"));
		calories.addValidator(new IntegerFieldValidator(0, 5000));

		addComponent(avgHR = new TextField("Average HR"));
		avgHR.addValidator(new IntegerFieldValidator(60, 200));

		addComponent(maxHR = new TextField("Max HR"));
		maxHR.addValidator(new IntegerFieldValidator(60, 200));

		addComponent(comment = new TextArea("Comment"), 0, 5, 1, 6);
		comment.setSizeFull();

		addComponent(rating = new TextField("Rating"), 0, 4, 1, 4);
		rating.setSizeFull();
		rating.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rating.setEnabled(false);

		addComponent(add = new Button("Add"));
		add.addStyleName(ValoTheme.BUTTON_PRIMARY);
		add.setSizeFull();

		addComponent(clear = new Button("Clear"));
		clear.setSizeFull();
	}

	public void clearValues() {
		activity.setValue(null);
		duration.setValue("");
		date.setValue(new Date());
		calories.setValue("");
		avgHR.setValue("");
		maxHR.setValue("");
		comment.setValue("");
	}

	public int getCalories() {
		return getIntFieldValue(calories);
	}

	public double getAvgHR() {
		return getIntFieldValue(avgHR);
	}

	public double getMaxHR() {
		return getIntFieldValue(maxHR);
	}

	private int getIntFieldValue(TextField field) {
		try {
			return Integer.parseInt(field.getValue());
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	private class IntegerFieldValidator implements Validator {
		private final int min, max;

		public IntegerFieldValidator(int min, int max) {
			this.min = min;
			this.max = max;
		}

		@Override
		public void validate(Object value) throws InvalidValueException {
			if (value == null || value.toString().isEmpty())
				return; // Empty values are caught with setRequired as needed

			int number;
			try {
				number = Integer.parseInt(value.toString());
			} catch (Exception e) {
				throw new InvalidValueException("Not an integer");
			}

			if (number < min || number > max) {
				throw new InvalidValueException("Must be between " + min
						+ " and " + max);
			}
		}
	}
}