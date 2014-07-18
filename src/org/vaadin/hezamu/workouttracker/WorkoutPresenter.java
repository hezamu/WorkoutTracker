package org.vaadin.hezamu.workouttracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.vaadin.hezamu.workouttracker.data.DummyWorkoutDAOImpl;
import org.vaadin.hezamu.workouttracker.data.WorkoutDAO;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.FontAwesome;
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
		editor.date.addValueChangeListener(this::updateRating);
		editor.activity.addValueChangeListener(this::updateRating);
		editor.duration.addValueChangeListener(this::updateRating);
		editor.calories.addValueChangeListener(this::updateRating);
		editor.avgHR.addValueChangeListener(this::updateRating);
		editor.maxHR.addValueChangeListener(this::updateRating);

		editor.add.addClickListener(event -> {
			if (areInputsValid()) {
				LocalDate date = LocalDateTime.ofInstant(
						editor.date.getValue().toInstant(),
						ZoneId.systemDefault()).toLocalDate();

				dao.add(editor.activity.getValue().toString(),
						Integer.parseInt(editor.duration.getValue()), date,
						editor.getCalories(), editor.getAvgHR(),
						editor.getMaxHR());

				graph.update(dao.getTotalKCal(12), dao.getAverageHR(12));

				editor.clearValues();
			}

			updateRating();
		});

		editor.clear.addClickListener(event -> {
			editor.clearValues();
			updateRating();
		});
	}

	private void updateRating(ValueChangeEvent ignored) {
		updateRating();
	}

	private void updateRating() {
		boolean validInputs = areInputsValid();

		if (validInputs) {
			int rating = WorkoutRatingLogic.calculateRating(editor.activity
					.getValue().toString(), editor.getDuration(), editor
					.getCalories(), editor.getAvgHR(), editor.getMaxHR(),
					editor.comment.getValue());

			StringBuilder stars = new StringBuilder("New Workout: ");
			for (int i = 0; i < rating; ++i) {
				stars.append(FontAwesome.STAR.getHtml());
			}

			editor.title.setValue(stars.toString());
		} else {
			editor.title.setValue("New Workout");
		}

		editor.add.setEnabled(validInputs);
	}

	/* @formatter:off */
	private boolean areInputsValid() {
		return StreamSupport.stream(editor.spliterator(), true)
				.filter(component -> fieldNotValidating(component))
				.count() > 0;
	}
	/* @formatter:on */

	@SuppressWarnings("rawtypes")
	private boolean fieldNotValidating(Component component) {
		if (component instanceof Field) {
			Field field = (Field) component;

			try {
				field.validate();
			} catch (InvalidValueException ive) {
				return true;
			}
		}

		return false; // Validates, or not a Field
	}
}