package org.vaadin.hezamu.workouttracker

import vaadin.scala._
import com.vaadin.addon.charts.Chart
import com.vaadin.addon.charts.model._
import com.vaadin.addon.charts.model.style._
import com.vaadin.server.Sizeable.Unit._
import com.vaadin.server.Sizeable.Unit

class WorkoutGraphView extends VerticalLayout {
  val chart = new Chart {
    setHeight(506, Unit.PIXELS)
    setWidth(700, Unit.PIXELS)
  }

  p.addComponent(chart)

  val conf = chart.getConfiguration
  conf.getChart.setZoomType(ZoomType.XY)
  conf.setTitle("Heart rate vs. burned calories")

  val tooltip = new Tooltip
  tooltip.setFormatter("this.x +': '+ this.y.toFixed(1) + (this.series.name == 'Burned calories' ? ' kcal' : ' BPM')")
  conf.setTooltip(tooltip)

  val x = new XAxis
  x.setCategories("Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar",
    "Apr", "May", "Jun", "Jul", "Aug")
  conf.addxAxis(x)

  val left = new YAxis
  left.setTitle("Average HR")

  val xStyle = new Style
  xStyle.setColor(new SolidColor("#89AA4E"))
  left.getTitle.setStyle(xStyle)
  conf.addyAxis(left)

  val right = new YAxis
  right.setTitle("Burned calories")
  right.setOpposite(true)

  val yStyle = new Style
  yStyle.setColor(new SolidColor("#4572A7"))
  right.getTitle.setStyle(yStyle)
  conf.addyAxis(right)

  val kcalSerie = new DataSeries
  kcalSerie.setPlotOptions(new PlotOptionsColumn)
  kcalSerie.setName("Burned calories")
  kcalSerie.setyAxis(1)
  conf.addSeries(kcalSerie)

  val hrSerie = new DataSeries
  val plotOptions = new PlotOptionsSpline
  hrSerie.setPlotOptions(plotOptions)
  hrSerie.setName("Average HR")
  plotOptions.setColor(new SolidColor("#89AA4E"))
  conf.addSeries(hrSerie)

  def update(kcalData: Array[Number], hrData: Array[Number]) {
    kcalSerie.setData(kcalData: _*)
    hrSerie.setData(hrData: _*)
    chart.drawChart()
  }
}