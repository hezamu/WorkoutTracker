package org.vaadin.hezamu.workouttracker.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.vaadin.hezamu.workouttracker.WorkoutPresenter;

public class DummyWorkoutDAOImpl implements WorkoutDAO {
	private List<Workout> workouts;

	public DummyWorkoutDAOImpl() {
		workouts = new ArrayList<>();

		workouts.addAll(generateDummyData());
	}

	@Override
	public List<Workout> findAll() {
		return workouts;
	}

	// Just a fragile dummy implementation.
	@Override
	public Number[] getTotalKCal(int maxMonths) {
		List<Double> result = new ArrayList<>();

		double accu = 0;
		int md = 11;
		for (Workout w : findByAge(maxMonths).toArray(Workout[]::new)) {
			if (w.monthAge() < md) {
				result.add(accu);
				accu = w.getCalories();
				md--;
			} else {
				accu += w.getCalories();
			}
		}

		result.add(accu);

		return result.toArray(new Double[0]);
	}

	// Just a fragile dummy implementation.
	@Override
	public Number[] getAverageHR(int maxMonths) {
		List<Double> result = new ArrayList<>();

		int count = 0;
		double accu = 0;
		int md = 11;
		for (Workout w : findByAge(maxMonths).toArray(Workout[]::new)) {
			if (w.getAvgHR() == 0)
				continue;

			if (w.monthAge() < md) {
				if (count == 0) {
					result.add(0D);
				} else {
					result.add(accu / count);
				}

				accu = w.getAvgHR();
				count = 1;
				md--;

			} else {
				accu += w.getAvgHR();
				count++;
			}
		}

		result.add(accu / count);

		return result.toArray(new Double[0]);
	}

	/* @formatter:off */
	private Stream<Workout> findByAge(int maxMonths) {
		return workouts.stream()
			.filter(w -> w.monthAge() < maxMonths)
			.sorted(Comparator.comparing(Workout::monthAge).reversed());
	}
	/* @formatter:on */

	private List<Workout> generateDummyData() {
		List<Workout> result = new ArrayList<>();

		Random rnd = new Random();

		for (int year = 2012; year <= 2014; year++) {
			for (int month = 1; month <= 12; month++) {
				LocalDate date = LocalDate.of(year, month, rnd.nextInt(28) + 1);

				if (LocalDate.now().isBefore(date))
					break;

				int count = rnd.nextInt(7) + 3;
				for (int i = 0; i < count; ++i) {
					int duration = rnd.nextInt(60) + 15;
					double avgHr = 110 + rnd.nextDouble() * 15;

					result.add(new Workout(WorkoutPresenter.ACTIVITIES[rnd
							.nextInt(WorkoutPresenter.ACTIVITIES.length)],
							date, duration, avgHr,
							avgHr + rnd.nextDouble() * 5, (duration / 60)
									* rnd.nextInt(150) + 200, ""));
				}
			}
		}

		return result;
	}

	@Override
	public void add(String activity, int minutes, LocalDate date, int calories,
			double avgHR, double maxHR) {
		workouts.add(new Workout(activity, date, minutes, avgHR, maxHR,
				calories, ""));
	}
}