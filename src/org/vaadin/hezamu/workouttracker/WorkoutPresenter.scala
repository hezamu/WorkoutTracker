package org.vaadin.hezamu.workouttracker

import org.vaadin.hezamu.workouttracker.data.DummyWorkoutDAOImpl
import java.time.LocalDate
import vaadin.scala._
import java.time.LocalDateTime
import java.time.ZoneId
import com.vaadin.ui.themes.ValoTheme

object WorkoutPresenter {
  val Activities = Array("Cycling", "Walking", "Running", "Gym", "Other");
}

class WorkoutPresenter {
  val graph = new WorkoutGraphView
  val editor = new WorkoutEditorView
  private val dao = new DummyWorkoutDAOImpl

  setupEditorListeners

  graph.update(dao.getTotalKCal(12), dao.getAverageHR(12))

  updateRating

  def setupEditorListeners {
    editor.date.valueChangeListeners += updateRating
    editor.activity.valueChangeListeners += updateRating
    editor.duration.valueChangeListeners += updateRating
    editor.calories.valueChangeListeners += updateRating
    editor.avgHR.valueChangeListeners += updateRating
    editor.maxHR.valueChangeListeners += updateRating

    editor.addButton.clickListeners += {
      if (getInvalidInputNames.isEmpty) {
        val date = LocalDateTime.ofInstant(
          editor.date.value.get.toInstant,
          ZoneId.systemDefault).toLocalDate

        dao.add(editor.activity.value.get.toString,
          editor.getDuration, date,
          editor.getCalories, editor.getAvgHR,
          editor.getMaxHR);

        graph.update(dao.getTotalKCal(12), dao.getAverageHR(12));

        editor.clearFields
      }

      updateRating
    }

    editor.clearButton.clickListeners += {
      editor.clearFields
      updateRating
    }
  }

  private def updateRating {
    val invalidInputs = getInvalidInputNames

    editor.rating.value =
      if (invalidInputs.nonEmpty) "Needed: " + invalidInputs.mkString(", ")
      else "Rating: " + calculateRating

    editor.addButton.enabled = invalidInputs.isEmpty
  }

  // TODO!
  def calculateRating =
    if (getInvalidInputNames.isEmpty) 1 else 0

  private def getInvalidInputNames =
    editor.components.filter(fieldNotValidating).flatMap(_.caption)

  def fieldNotValidating(c: Component) = c match {
    case f: Validatable => !f.valid
    case _ => false
  }
}