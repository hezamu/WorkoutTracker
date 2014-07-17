package org.vaadin.hezamu.workouttracker.data

import java.time.LocalDate
import java.time.Period

case class Workout(activity: String, date: LocalDate, duration: Int, avgHR: Double,
  maxHR: Double, calories: Int, comment: String) {
  def monthAge = Period.between(date, LocalDate.now).toTotalMonths
}