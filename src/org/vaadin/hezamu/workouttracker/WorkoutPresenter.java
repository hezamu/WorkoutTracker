package org.vaadin.hezamu.workouttracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.vaadin.hezamu.rx.RxVaadin;
import org.vaadin.hezamu.workouttracker.data.DummyWorkoutDAOImpl;
import org.vaadin.hezamu.workouttracker.data.WorkoutDAO;

import rx.Observable;

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
			put("Cycling", 50);
			put("Walking", 25);
			put("Running", 100);
			put("Gym", 40);
			put("Other", 25);
		}
	};

	public WorkoutPresenter() {
		dao = new DummyWorkoutDAOImpl();

		graph = new WorkoutGraphView();

		editor = new WorkoutEditorView();

		setupButtonListeners();

		setupObservableLogic();

		graph.update(dao.getTotalKCal(12), dao.getAverageHR(12));
	}

	private void setupButtonListeners() {
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
		});

		editor.clear.addClickListener(event -> {
			editor.clearValues();
		});
	}

	private void setupObservableLogic() {
		// First, convert input fields to Observables
		Observable<String> activities = RxVaadin.valuesWithDefault(
				editor.activity, null);
		Observable<String> durations = RxVaadin.valuesWithDefault(
				editor.duration, "");
		Observable<Date> dates = RxVaadin.valuesWithDefault(editor.date,
				editor.date.getValue());
		Observable<String> calories = RxVaadin.valuesWithDefault(
				editor.calories, "");
		Observable<String> avgHRs = RxVaadin
				.valuesWithDefault(editor.avgHR, "");
		Observable<String> maxHRs = RxVaadin
				.valuesWithDefault(editor.maxHR, "");
		Observable<String> comments = RxVaadin.valuesWithDefault(
				editor.comment, "");

		// Combine the values emitted by these Observables to a single new
		// Observable that emits Integer ratings based on them.
		Observable<Integer> ratings = WorkoutRatingLogic.ratings(activities,
				durations, dates, calories, avgHRs, maxHRs, comments);

		// Compose a new Observable that emits the font icon strings
		// corresponding to the the ratings
		Observable<String> ratingStrings = ratings.map(rating -> {
			if (rating == null) {
				return "New Workout"; // No stars if required fields not ok
			} else {
				return IntStream.range(0, rating)
						.mapToObj(i -> FontAwesome.STAR.getHtml())
						.collect(Collectors.joining("", "New Workout: ", ""));
			}
		});

		// Make the label set its value every time the string Observable
		// emits a value
		RxVaadin.follow(editor.title, ratingStrings);

		// Make the add button enabled or disabled based on if the rating
		// calculation was successful or not
		ratings.map(i -> i).subscribe(
				rating -> editor.add.setEnabled(rating != null));
	}

	/* @formatter:off */
	private boolean areInputsValid() {
		return !StreamSupport.stream(editor.spliterator(), true)
			.anyMatch(this::fieldNotValidating);
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