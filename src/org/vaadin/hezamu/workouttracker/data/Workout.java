package org.vaadin.hezamu.workouttracker.data;

import java.util.Date;

public class Workout {
	private String activity;
	private Date date;
	private int duration, calories;
	private double avgHR, maxHR;
	private String comment;

	public Workout(String activity, Date date, int time, double avgHR,
			double maxHR, int kcal, String comment) {
		this.activity = activity;
		this.date = date;
		this.duration = time;
		this.avgHR = avgHR;
		this.maxHR = maxHR;
		this.calories = kcal;
		this.comment = comment;
	}

	public Workout() {
		this.activity = "";
		this.date = new Date();
		this.duration = 0;
		this.avgHR = 0;
		this.maxHR = 0;
		this.calories = 0;
		this.comment = "";
	}

	public int monthAge() {
		return (new Date().getYear() - date.getYear()) * 12
				+ (new Date().getMonth() - date.getMonth());
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getAvgHR() {
		return avgHR;
	}

	public void setAvgHR(double avgHR) {
		this.avgHR = avgHR;
	}

	public double getMaxHR() {
		return maxHR;
	}

	public void setMaxHR(double maxHR) {
		this.maxHR = maxHR;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}