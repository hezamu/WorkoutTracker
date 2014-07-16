package org.vaadin.hezamu.workouttracker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vaadin.hezamu.workouttracker.data.DummyWorkoutDAOImpl;
import org.vaadin.hezamu.workouttracker.data.WorkoutDAO;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class WorkoutPresenter {
	final WorkoutEditorView editor;
	final WorkoutGraphView graph;
	private final WorkoutDAO dao;

	public final static String[] ACTIVITIES = { "Cycling", "Walking",
			"Running", "Gym", "Other" };

	public WorkoutPresenter() {
		dao = new DummyWorkoutDAOImpl();

		graph = new WorkoutGraphView();

		editor = new WorkoutEditorView();

		setupEditorListeners();

		graph.update(dao.getTotalKCal(12), dao.getAverageHR(12));

		updateRating();
	}

	private void setupEditorListeners() {
		editor.date.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				updateRating();
			}
		});

		editor.activity
				.addValueChangeListener(new Property.ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						updateRating();
					}
				});

		editor.duration
				.addValueChangeListener(new Property.ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						updateRating();
					}
				});

		editor.calories
				.addValueChangeListener(new Property.ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						updateRating();
					}
				});

		editor.avgHR.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				updateRating();
			}
		});

		editor.maxHR.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				updateRating();
			}
		});

		editor.add.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (getInvalidInputNames().isEmpty()) {
					dao.add(editor.activity.getValue().toString(),
							Integer.parseInt(editor.duration.getValue()),
							editor.date.getValue(), editor.getCalories(),
							editor.getAvgHR(), editor.getMaxHR());

					graph.update(dao.getTotalKCal(12), dao.getAverageHR(12));

					editor.clearValues();
				}

				updateRating();
			}
		});

		editor.clear.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				editor.clearValues();
				updateRating();
			}
		});
	}

	private void updateRating() {
		List<String> invalidInputs = getInvalidInputNames();

		if (!invalidInputs.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < invalidInputs.size(); ++i) {
				sb.append(invalidInputs.get(i));
				if (i < invalidInputs.size() - 1)
					sb.append(", ");
			}

			editor.rating.setValue("Missing/invalid: " + sb.toString());
		} else {
			editor.rating.setValue("Rating: " + calculateRating());
		}

		editor.add.setEnabled(invalidInputs.isEmpty());
	}

	private int calculateRating() {
		if (getInvalidInputNames().isEmpty()) {
			return 1;
		}

		return 0;
	}

	@SuppressWarnings("rawtypes")
	private List<String> getInvalidInputNames() {
		List<String> result = new ArrayList<>();

		Component c = null;
		for (Iterator<Component> iter = editor.iterator(); iter.hasNext(); c = iter
				.next()) {
			if (c instanceof Field) {
				Field field = (Field) c;

				try {
					field.validate();
				} catch (InvalidValueException ive) {
					result.add(field.getCaption());
				}
			}
		}

		return result;
	}
}
