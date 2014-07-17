package org.vaadin.hezamu.workouttracker

import vaadin.scala._
import vaadin.scala.server.FontAwesome
import com.vaadin.ui.themes.ValoTheme

class WorkoutTrackerUI extends UI(theme = "valo") {
  val presenter = new WorkoutPresenter

  content = new VerticalLayout {
    margin = true
    spacing = true
    sizeFull

    // Setup the app title
    add(new Label {
      value = "Workout Tracker " + FontAwesome.Signal.html
      contentMode = Label.ContentMode.Html
      sizeUndefined
      styleName = ValoTheme.LABEL_H1
    }, alignment = Alignment.TopCenter)

    // Setup the layout that will contain the views
    add(new HorizontalLayout {
      margin = false
      spacing = true

      add(presenter.editor)
      add(presenter.graph, ratio = 1)
    }, alignment = Alignment.TopCenter, ratio = 1)
  }
}