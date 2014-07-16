package org.vaadin.hezamu.workouttracker

import vaadin.scala._
import vaadin.scala.server.FontAwesome
import com.vaadin.ui.themes.ValoTheme

class WorkoutTrackerUI extends UI(theme = "valo") {
  content = new VerticalLayout {
    margin = true
    spacing = true
    sizeFull

    // Setup the app title
    add(new Label {
      value = "Workout Tracker " + FontAwesome.Signal.html
      contentMode = Label.ContentMode.Html
      sizeUndefined
      styleNames += ValoTheme.LABEL_H1
    }, alignment = Alignment.TopCenter)

    // Setup the layout that will contain the views
    add(new HorizontalLayout {
      spacing = true

      val presenter = new WorkoutPresenter

      add(presenter.editor)
      add(presenter.graph, ratio = 1)
    }, alignment = Alignment.TopCenter, ratio = 1)
  }
}