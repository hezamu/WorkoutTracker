package org.vaadin.hezamu.workouttracker

import vaadin.scala._
import com.vaadin.addon.charts.Chart
import com.vaadin.addon.charts.model._
import com.vaadin.addon.charts.model.style._
import com.vaadin.ui.themes.ValoTheme
import com.vaadin.data.Validator.InvalidValueException
import java.util.Date
import vaadin.scala.Validator._

class WorkoutEditorView extends GridLayout {
  columns = 2
  rows = 7
  spacing = true

  add(new Label {
    caption = "New Workout"
    styleName += (ValoTheme.LABEL_H3, ValoTheme.LABEL_BOLD)
    sizeUndefined()
  }, 0, 0, 1, 0, alignment = Alignment.TopCenter)

  val activity = add(new ComboBox {
    caption = "Activity"
    required = true
    inputPrompt = "Required"
    WorkoutPresenter.Activities foreach addItem
  })

  val duration = add(new ValidatedTextField("Duration", 1, 300))
  duration.required = true

  val date = add(new DateField {
    caption = "Date"
    required = true
    value = new Date
    dateFormat = "dd.MM.yyyy"
    sizeFull

    validators += { v =>
      v match {
        case Some(date: Date) if new Date().before(date) =>
          Invalid(List("Date can't be in the future"))
        case _ => Valid
      }
    }
  })

  val calories = add(new ValidatedTextField("Calories", 0, 5000))
  val avgHR = add(new ValidatedTextField("Average HR", 60, 200))
  val maxHR = add(new ValidatedTextField("Max HR", 60, 200))

  val comment = add(new TextArea { sizeFull }, 0, 5, 1, 6)

  val rating = add(new TextField {
    sizeFull
    styleName = ValoTheme.TEXTFIELD_BORDERLESS
    enabled = false
  }, 0, 4, 1, 4)

  val addButton = add(new Button {
    caption = "Add"
    styleName = ValoTheme.BUTTON_PRIMARY
    sizeFull
  })

  val clearButton = add(new Button {
    caption = "Clear"
    sizeFull
  })

  def clearFields {
    activity.value = None
    duration.value = ""
    date.value = new Date()
    calories.value = ""
    avgHR.value = ""
    maxHR.value = ""
    comment.value = ""
  }

  def getDuration = getIntFieldValue(duration)
  def getCalories = getIntFieldValue(calories)
  def getAvgHR = getIntFieldValue(avgHR)
  def getMaxHR = getIntFieldValue(maxHR)

  private def getIntFieldValue(field: TextField) = {
    try {
      Integer.parseInt(field.value.getOrElse(""))
    } catch {
      case nfe: NumberFormatException => 0
    }
  }
}

class ValidatedTextField(txt: String, min: Int, max: Int) extends TextField {
  caption = txt

  validators += { v =>
    v match {
      case Some(s: String) if s.nonEmpty =>
        try {
          val number = Integer.parseInt(s);
          if (number < min || number > max) {
            Invalid(List(s"Must be between $min and $max"))
          } else Valid
        } catch {
          case _: NumberFormatException => Invalid(List("Not an integer"))
        }
      case _ => Valid // Empty values are caught with setRequired as needed
    }
  }
}