package org.vaadin.hezamu.workouttracker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.hezamu.workouttracker.data.DummyWorkoutDAOImpl;
import org.vaadin.hezamu.workouttracker.data.WorkoutDAO;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class WorkoutPresenter {
	final WorkoutEditorView editor;
	final WorkoutGraphView graph;
	private final WorkoutDAO dao;

	public static final Map<String, Integer> ACTIVITIES = new LinkedHashMap<String, Integer>() {
		{
			put("Cycling", 15);
			put("Walking", 10);
			put("Running", 20);
			put("Gym", 15);
			put("Other", 10);
		}
	};

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
			editor.title.setValue("New Workout");
		} else {
			int rating = WorkoutRatingLogic.calculateRating(editor.activity
					.getValue().toString(), editor.getDuration(), editor
					.getCalories(), editor.getAvgHR(), editor.getMaxHR(),
					editor.comment.getValue());

			StringBuilder stars = new StringBuilder("New Workout: ");
			for (int i = 0; i < rating; ++i) {
				stars.append(FontAwesome.STAR.getHtml());
			}

			editor.title.setValue(stars.toString());
		}

		editor.add.setEnabled(invalidInputs.isEmpty());
	}

	private List<String> getInvalidInputNames() {
		List<String> result = new ArrayList<>();

		Component c = null;
		for (Iterator<Component> iter = editor.iterator(); iter.hasNext(); c = iter
				.next()) {
			if (fieldNotValidating(c))
				result.add(c.getCaption());
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	private boolean fieldNotValidating(Component c) {
		if (c instanceof Field) {
			Field field = (Field) c;

			try {
				field.validate();
			} catch (InvalidValueException ive) {
				return true;
			}
		}

		return false; // Validates, or not a Field
	}
}