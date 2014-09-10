package org.vaadin.hezamu.workouttracker.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
		for (Workout w : findByAge(maxMonths)) {
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

	@SuppressWarnings("deprecation")
	private List<Workout> generateDummyData() {
		List<Workout> result = new ArrayList<>();

		Object[] activities = WorkoutPresenter.ACTIVITIES.keySet().toArray();
		Random rnd = new Random();

		for (int year = 112; year <= 114; year++) {
			for (int month = 0; month <= 11; month++) {
				if (year == 114 && month > 8)
					break;

				int count = rnd.nextInt(7) + 3;
				for (int i = 0; i < count; ++i) {
					int duration = rnd.nextInt(60) + 15;
					double avgHr = 110 + rnd.nextDouble() * 15;

					result.add(new Workout(activities[rnd
							.nextInt(activities.length)].toString(), new Date(
							year, month, rnd.nextInt(30) + 1), duration, avgHr,
							avgHr + rnd.nextDouble() * 5, (duration / 60)
									* rnd.nextInt(150) + 200, ""));
				}
			}
		}

		return result;
	}

	@Override
	public void add(String activity, int minutes, Date date, int calories,
			double avgHR, double maxHR) {
		workouts.add(new Workout(activity, date, minutes, avgHR, maxHR,
				calories, ""));
	}
}