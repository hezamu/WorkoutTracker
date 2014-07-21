package org.vaadin.hezamu.workouttracker;

/**
 * Bogus rules to calculating 1-5 star rating for a workout.
 */
public class WorkoutRatingLogic {
	public static int calculateRating(String activity, int duration,
			int calories, double avgHr, double maxHr, String comment) {
		int score = activityScore(activity) + durationScore(duration)
				+ calScore(calories) + hrScore(avgHr, maxHr)
				+ commentScore(comment);

		if (score < 150)
			return 1;
		else if (score <= 220)
			return 2;
		else if (score <= 290)
			return 3;
		else if (score <= 360)
			return 4;
		else
			return 5;
	}

	private static int activityScore(String activity) {
		return WorkoutPresenter.activityScore(activity);
	}

	private static int durationScore(int duration) {
		return duration * 3;
	}

	private static int calScore(int calories) {
		return 20 + calories / 10;
	}

	private static int hrScore(double avgHr, double maxHr) {
		return (int) (20 + Math.max(0, avgHr - 75) + 20 + Math.max(0,
				maxHr - 75));
	}

	private static int commentScore(String comment) {
		return (comment != null && !comment.isEmpty()) ? 5 : 0;
	}
}