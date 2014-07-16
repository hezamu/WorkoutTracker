package org.vaadin.hezamu.workouttracker

import vaadin.scala._
import com.vaadin.addon.charts.Chart
import com.vaadin.addon.charts.model._
import com.vaadin.addon.charts.model.style._
import com.vaadin.server.Sizeable.Unit._
import com.vaadin.server.Sizeable.Unit

class WorkoutGraphView extends VerticalLayout {
  spacing = true
  margin = true

  val chart = new Chart {
    height = 580.px
    width = 700.px
  }

  p.addComponent(chart)
  chart.setHeight(580, Unit.PIXELS)
  chart.setWidth(700, Unit.PIXELS)

  val (kcalSerie, hrSerie) = configureChart

  def configureChart: (DataSeries, DataSeries) = {
    val conf = chart.getConfiguration()

    conf.getChart().setZoomType(ZoomType.XY)

    conf.setTitle("Progress")

    val x = new XAxis()
    x.setCategories("Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar",
      "Apr", "May", "Jun", "Jul")
    conf.addxAxis(x)

    val primary = new YAxis()
    primary.setTitle("Average HR")

    val xStyle = new Style()
    xStyle.setColor(new SolidColor("#89AA4E"))
    primary.getTitle().setStyle(xStyle)
    conf.addyAxis(primary)

    val snd = new YAxis()
    snd.setTitle("Burned calories")
    snd.setOpposite(true)

    val yStyle = new Style()
    yStyle.setColor(new SolidColor("#4572A7"))
    snd.getTitle().setStyle(yStyle)
    conf.addyAxis(snd)

    val tooltip = new Tooltip()
    tooltip.setFormatter("this.x +': '+ this.y + (this.series.name == 'Burned calories' ? ' calories' : ' BPM')")
    conf.setTooltip(tooltip)

    val kcalSerie = new DataSeries()
    kcalSerie.setPlotOptions(new PlotOptionsColumn())
    kcalSerie.setName("Burned calories")
    kcalSerie.setyAxis(1)
    conf.addSeries(kcalSerie)

    val hrSerie = new DataSeries()
    val plotOptions = new PlotOptionsSpline()
    hrSerie.setPlotOptions(plotOptions)
    hrSerie.setName("Average HR")
    plotOptions.setColor(new SolidColor("#89AA4E"))
    conf.addSeries(hrSerie)

    (kcalSerie, hrSerie)
  }

  def update(kcalData: Array[Number], hrData: Array[Number]) {
    kcalSerie.setData(kcalData: _*)
    hrSerie.setData(hrData: _*)
    chart.drawChart()
  }
}