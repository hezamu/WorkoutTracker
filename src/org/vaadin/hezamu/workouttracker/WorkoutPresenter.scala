package org.vaadin.hezamu.workouttracker

import org.vaadin.hezamu.workouttracker.data.DummyWorkoutDAOImpl
import java.time.LocalDate
import vaadin.scala._
import java.time.LocalDateTime
import java.time.ZoneId
import com.vaadin.ui.themes.ValoTheme
import vaadin.scala.server.FontAwesome

object WorkoutPresenter {
  private val Activities: Map[String, Integer] = Map(("Cycling", 50), ("Walking", 25), ("Running", 100), ("Gym", 40), ("Other", 25));
  def activityScore(activity: String) = Activities.getOrElse(activity, 25).asInstanceOf[Integer]
  def activities = Activities.keys.toArray[String]
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

    if (invalidInputs.nonEmpty) editor.title.value = "New Workout"
    else {
      val rating = WorkoutRatingLogic.calculateRating(editor.activity.value.get.toString,
        editor.getDuration, editor.getCalories, editor.getAvgHR, editor.getMaxHR,
        editor.comment.value.get)

      val stars = for (i <- 1 to rating) yield FontAwesome.Star.html

      editor.title.value = "New Workout: " + stars.mkString
    }

    editor.addButton.enabled = invalidInputs.isEmpty
  }

  private def getInvalidInputNames =
    editor.components
      .filter(fieldNotValidating)
      .flatMap(_.caption)

  def fieldNotValidating(c: Component) = c match {
    case f: Validatable => !f.valid
    case _ => false
  }
}