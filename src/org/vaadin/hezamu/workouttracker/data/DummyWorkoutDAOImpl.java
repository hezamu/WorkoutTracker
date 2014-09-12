package org.vaadin.hezamu.workouttracker.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

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
		for (Workout w : findByAge(maxMonths)) {
			if (w.monthAge() < md) {
				result.add(accu);
				accu = w.calories();
				md--;
			} else {
				accu += w.calories();
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
		for (Workout w : findByAge(maxMonths)) {
			if (w.avgHR() == 0)
				continue;

			if (w.monthAge() < md) {
				if (count == 0) {
					result.add(0D);
				} else {
					result.add(accu / count);
				}

				accu = w.avgHR();
				count = 1;
				md--;

			} else {
				accu += w.avgHR();
				count++;
			}
		}

		result.add(accu / count);

		return result.toArray(new Double[0]);
	}

	private List<Workout> findByAge(int maxMonths) {
		List<Workout> result = new ArrayList<>();

		for (Workout w : workouts) {
			if (w.monthAge() < maxMonths) {
				result.add(w);
			}
		}

		Collections.sort(result, new Comparator<Workout>() {
			@Override
			public int compare(Workout o1, Workout o2) {
				return o2.monthAge() - o1.monthAge();
			}
		});

		return result;
	}

	// @SuppressWarnings("deprecation")
	private List<Workout> generateDummyData() {
		List<Workout> result = new ArrayList<>();

		Object[] activities = WorkoutPresenter.activities();
		Random rnd = new Random();

		LocalDate now = LocalDate.now();

		/* @formatter:off */
		IntStream.range(now.getYear() - 2, now.getYear() + 1).forEach(year -> {
			IntStream.range(1, 13).forEach(month -> {
				IntStream.range(0, rnd.nextInt(7) + 3).forEach(count -> {
					int duration = rnd.nextInt(60) + 15;
					double avgHr = 110 + rnd.nextDouble() * 15;
					LocalDate date = LocalDate.of(year, month, 
						rnd.nextInt(LocalDate.of(year, month, 1).lengthOfMonth()) + 1);

					if(date.isBefore(now)) {
						result.add(new Workout(activities[rnd.nextInt(activities.length)].toString(),
							date, duration, avgHr, avgHr + rnd.nextDouble() * 5,
							(duration / 60) * rnd.nextInt(150) + 200, ""));
					}
				});
			});
		});
		/* @formatter:on */

		return result;
	}

	@Override
	public void add(String activity, int minutes, LocalDate date, int calories,
			double avgHR, double maxHR) {
		workouts.add(new Workout(activity, date, minutes, avgHR, maxHR,
				calories, ""));
	}
}