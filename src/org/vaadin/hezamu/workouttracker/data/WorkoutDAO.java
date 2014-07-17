package org.vaadin.hezamu.workouttracker.data;

import java.time.LocalDate;
import java.util.stream.Stream;

public interface WorkoutDAO {
	/**
	 * Get all workouts.
	 */
	Stream<Workout> findAll();

	/**
	 * Get the monthly kcal totals for the workouts in the X previous months.
	 * The result is sorted by age, and the current month is included as the
	 * last element.
	 */
	Number[] getTotalKCal(int maxMonths);

	/**
	 * Get the monthly heart rate averages for the workouts in the X previous
	 * months. The result is sorted by age, and the current month is included as
	 * the last element.
	 */
	Number[] getAverageHR(int maxMonths);

	/**
	 * Add a new workout.
	 */
	void add(String activity, int minutes, LocalDate date, int calories,
			double avgHR, double maxHR);
}
