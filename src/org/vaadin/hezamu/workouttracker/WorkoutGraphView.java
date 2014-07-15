package org.vaadin.hezamu.workouttracker;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.ZoomType;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class WorkoutGraphView extends VerticalLayout {
	private final Chart chart;

	private final DataSeries kcalSerie, hrSerie;

	public WorkoutGraphView() {
		setSpacing(true);
		setMargin(false);

		addComponent(chart = new Chart());
		chart.setHeight("580px");
		chart.setWidth("700px");

		Configuration conf = chart.getConfiguration();

		conf.getChart().setZoomType(ZoomType.XY);

		conf.setTitle("Progress");

		XAxis x = new XAxis();
		x.setCategories("Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar",
				"Apr", "May", "Jun", "Jul");
		conf.addxAxis(x);

		YAxis primary = new YAxis();
		primary.setTitle("Average HR");
		Style style = new Style();
		style.setColor(new SolidColor("#89AA4E"));
		primary.getTitle().setStyle(style);
		conf.addyAxis(primary);

		YAxis snd = new YAxis();
		snd.setTitle("Burned calories");
		snd.setOpposite(true);
		style = new Style();
		style.setColor(new SolidColor("#4572A7"));
		snd.getTitle().setStyle(style);
		conf.addyAxis(snd);

		Tooltip tooltip = new Tooltip();
		tooltip.setFormatter("this.x +': '+ this.y + (this.series.name == 'Burned calories' ? ' calories' : ' BPM')");
		conf.setTooltip(tooltip);

		kcalSerie = new DataSeries();
		kcalSerie.setPlotOptions(new PlotOptionsColumn());
		kcalSerie.setName("Burned calories");
		kcalSerie.setyAxis(1);
		conf.addSeries(kcalSerie);

		hrSerie = new DataSeries();
		PlotOptionsSpline plotOptions = new PlotOptionsSpline();
		hrSerie.setPlotOptions(plotOptions);
		hrSerie.setName("Average HR");
		plotOptions.setColor(new SolidColor("#89AA4E"));
		conf.addSeries(hrSerie);
	}

	public void update(Double[] kcalData, Double[] hrData) {
		kcalSerie.setData(kcalData);
		hrSerie.setData(hrData);
		chart.drawChart();
	}
}
