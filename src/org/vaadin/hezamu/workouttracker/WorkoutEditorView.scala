package org.vaadin.hezamu.workouttracker

import vaadin.scala._
import com.vaadin.addon.charts.Chart
import com.vaadin.addon.charts.model._
import com.vaadin.addon.charts.model.style._
import com.vaadin.ui.themes.ValoTheme
import com.vaadin.data.Validator.InvalidValueException
import java.util.Date
import vaadin.scala.Validator._
import scala.util.Try

class WorkoutEditorView extends GridLayout {
  columns = 2
  rows = 6
  spacing = true

  val title = add(Label("New Workout", undefSize = true, styles = Vector(ValoTheme.LABEL_H3, ValoTheme.LABEL_BOLD)),
    0, 0, 1, 0, alignment = Alignment.TopCenter)
  title.contentMode = Label.ContentMode.Html

  val activity = add(new ComboBox {
    caption = "Activity"
    required = true
    inputPrompt = "Required"
    WorkoutPresenter.activities.foreach(addItem)
  })

  val duration = add(new ValidatedTextField("Duration", 1, 600, true))

  val date = add(DateField("Date", fullSize = true, req = true,
    initialValue = Some(new Date), format = Some("dd.MM.yyyy")))

  date.validators += { v: Option[Any] =>
    v match {
      case Some(date: Date) if new Date().before(date) => Invalid(List("Date can't be in the future"))
      case _ => Valid
    }
  }

  val calories = add(new ValidatedTextField("Calories", 0, 5000))
  val avgHR = add(new ValidatedTextField("Average HR", 50, 250))
  val maxHR = add(new ValidatedTextField("Max HR", 50, 250))

  val comment = add(TextArea("Comment", fullSize = true), 0, 4, 1, 4)

  val addButton = add(Button("Add", true, style = Some(ValoTheme.BUTTON_PRIMARY)))
  val clearButton = add(Button("Clear", true))

  def clearFields {
    activity.value = None
    duration.value = ""
    date.value = new Date()
    calories.value = ""
    avgHR.value = ""
    maxHR.value = ""
    comment.value = ""
  }

  def getDuration = Try(Integer.parseInt(duration.value.getOrElse(""))) getOrElse 0
  def getCalories = Try(Integer.parseInt(calories.value.getOrElse(""))) getOrElse 0
  def getAvgHR = Try(Integer.parseInt(avgHR.value.getOrElse(""))) getOrElse 0
  def getMaxHR = Try(Integer.parseInt(maxHR.value.getOrElse(""))) getOrElse 0
}

class ValidatedTextField(_caption: String, min: Int, max: Int, _required: Boolean = false) extends TextField {
  this.caption = _caption
  this.required = _required

  validators += { v =>
    v match {
      case Some(s: String) if s.nonEmpty =>
        Try(Integer.parseInt(s)) map {
          case num if num >= min && num <= max => Valid
          case _ => Invalid(List(s"Must be between $min and $max"))
        } getOrElse Invalid(List("Not an integer"))
      case _ => Valid // Empty values are caught with setRequired as needed
    }
  }
}