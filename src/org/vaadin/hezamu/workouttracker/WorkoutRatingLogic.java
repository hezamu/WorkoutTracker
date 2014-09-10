package org.vaadin.hezamu.workouttracker;

import java.util.Date;

import rx.Observable;

/**
 * Bogus rules to calculating 1-5 star rating for a workout.
 */
public class WorkoutRatingLogic {
	public static Observable<Integer> ratings(Observable<String> activities,
			Observable<String> durations, Observable<Date> dates,
			Observable<String> calories, Observable<String> avgHRs,
			Observable<String> maxHRs, Observable<String> comments) {

		Observable<Input> inputs = Observable.combineLatest(activities,
				durations, dates, calories, avgHRs, maxHRs, comments,
				WorkoutRatingLogic::combineInputs);

		return inputs.map(i -> i.rating());
	}

	private static Input combineInputs(String activity, String duration,
			Date date, String calories, String avgHRs, String maxHRs,
			String comment) {
		return new WorkoutRatingLogic().new Input(activity, duration, date,
				calories, avgHRs, maxHRs, comment);
	}

	// Unfortunately Java 8 doesn't have tuples
	class Input {
		String activity, comment;
		Integer duration, calories, avgHR, maxHR;
		Date date;

		public Input(String activity, String duration, Date date,
				String calories, String avgHR, String maxHR, String comment) {
			this.activity = activity;
			this.duration = parseOrZero(duration);
			this.date = date;
			this.calories = parseOrZero(calories);
			this.avgHR = parseOrZero(avgHR);
			this.maxHR = parseOrZero(maxHR);
			this.comment = comment;
		}

		public Integer rating() {
			if (activity == null || duration == 0 || date == null) {
				return null;
			}

			int activityScore = WorkoutPresenter.ACTIVITIES.get(activity);
			int durationScore = duration * 3;
			int calScore = 20 + calories / 10;
			int hrScore = (int) (20 + Math.max(0, avgHR - 75) + 20 + Math.max(
					0, maxHR - 75));
			int commentScore = (comment != null && !comment.isEmpty()) ? 5 : 0;

			int total = activityScore + durationScore + calScore + hrScore
					+ commentScore;

			if (total < 150)
				return 1;
			else if (total <= 220)
				return 2;
			else if (total <= 290)
				return 3;
			else if (total <= 360)
				return 4;
			else
				return 5;
		}

		private int parseOrZero(String input) {
			try {
				return Integer.parseInt(input);
			} catch (NumberFormatException nfe) {
				return 0;
			}
		}
	}
}