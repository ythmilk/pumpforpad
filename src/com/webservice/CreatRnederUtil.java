package com.webservice;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;

public class CreatRnederUtil {
	public static XYMultipleSeriesRenderer getdemorenderer() {
		Log.i("getRender", "ffff");
		XYMultipleSeriesRenderer render;
		render = new XYMultipleSeriesRenderer();
		render.setBackgroundColor(Color.parseColor("#ffffff"));// 设置背景颜色
		render.setApplyBackgroundColor(true);// 背景颜色生效
		render.setMarginsColor(Color.parseColor("#efefef"));// 设置边框外侧颜色
		render.setChartTitleTextSize(20);// 设置整个图表标题文字的大小
		render.setAxisTitleTextSize(25);// 设置轴标题文字的大小
		render.setAxesColor(Color.BLACK);// 设置坐标轴的颜色
		render.setXTitle("时间");// 设置坐标轴的Titile

		render.setLabelsTextSize(25f);// 设置轴刻度文字的大小

		render.setLabelsColor(Color.BLACK);
		render.setXLabelsColor(Color.BLACK);
		render.setYLabelsColor(0, Color.BLACK);
		render.setLegendTextSize(25);// 设置图例文字大小
		render.setXLabelsAngle(45);
		// render.setShowLegend(false);//显示不显示在这里设置，非常完美
		render.setPointSize(10f);
		render.setYLabelsAlign(Align.RIGHT);// 刻度值相对于刻度的位置
		render.setShowGrid(true);// 显示网格
		render.setGridColor(Color.parseColor("#eeeeee"));

		render.setInScroll(true);
		render.setZoomEnabled(false,false);
		render.setPanEnabled(false, false);// 禁止报表的拖动
		render.setPointSize(5f);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
		render.setMargins(new int[] { 45, 55, 90, 10 }); // 设置图形四周的留白
		render.setMarginsColor(Color.WHITE);
		render.setXLabels(0);// 取消X坐标的数字zjk,只有自己定义横坐标是才设为此值

		XYSeriesRenderer r = new XYSeriesRenderer();// 设置颜色和点类型
		r.setColor(Color.parseColor("#007aa4"));
		r.setPointStyle(PointStyle.CIRCLE);

		r.setFillPoints(true);
		r.setDisplayChartValues(true);
		r.setLineWidth(2f);
		r.setChartValuesSpacing(15);// 设置数值和点的距离
		r.setChartValuesTextSize(18f);
		r.setGradientStart(0, Color.parseColor("#007aa4"));
		render.addSeriesRenderer(r);
		return render;
	}

}
