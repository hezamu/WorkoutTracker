package org.vaadin.hezamu.workouttracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.vaadin.hezamu.workouttracker.data.DummyWorkoutDAOImpl;
import org.vaadin.hezamu.workouttracker.data.WorkoutDAO;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

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
		editor.date.addValueChangeListener(this::updateRating);
		editor.activity.addValueChangeListener(this::updateRating);
		editor.duration.addValueChangeListener(this::updateRating);
		editor.calories.addValueChangeListener(this::updateRating);
		editor.avgHR.addValueChangeListener(this::updateRating);
		editor.maxHR.addValueChangeListener(this::updateRating);

		editor.add.addClickListener(event -> {
			if (getInvalidInputNames().isEmpty()) {
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
		List<String> invalidInputs = getInvalidInputNames();

		if (!invalidInputs.isEmpty()) {
			editor.rating.setValue("Missing/invalid: "
					+ invalidInputs.stream().collect(Collectors.joining(", ")));
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

	/* @formatter:off */
	private List<String> getInvalidInputNames() {
		Stream<String> s = StreamSupport.stream(editor.spliterator(), true)
				.filter(c -> fieldNotValidating(c))
				.map(c -> c.getCaption());

		return Arrays.asList(s.toArray(String[]::new));
	}
	/* @formatter:on */

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