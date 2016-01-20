package com.webservice;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
public class TestUtil {
	public static View getLineChartView(Context context, List<String> listtime,List<String> listdata,double min,double max,int x,String title,String xTitle,String yTitle,int length) {
		// 构造显示用渲染图
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		// 曲线图的格式，包括颜色，值的范围，点和线的形状等等
		renderer.setBackgroundColor(Color.parseColor("#efefef"));// 背景色，灰白色
		renderer.setApplyBackgroundColor(true);// 设置背景颜色生效

		renderer.setMarginsColor(Color.parseColor("#efefef"));// // 边框外侧颜色
		renderer.setPanEnabled(true, false);// 设置X轴和Y轴是否可以滑动
		renderer.setXAxisMin(0);// 设置X轴起点
		renderer.setXAxisMax(20);// 设置X轴最大点
		renderer.setPanLimits(new double[] { 0, length, 0, 0, });// 坐标滑动上、下限,
		renderer.setLabelsTextSize(20f);// 图表标题字体大小：20
		renderer.setMargins(new int[] {55, 55, 40, 10});// 图形4边距
		renderer.setYAxisMin(min);// 设置Y轴起点
		renderer.setYAxisMax(max);
		renderer.setYLabels(x);// 
		renderer.setZoomEnabled(true, false);// 设置不可放大缩小
		renderer.setYTitle(yTitle);
		renderer.setAxisTitleTextSize(20f);//设置轴标题文字大小
		
//		renderer.setXLabels(50);
		
		renderer.setXTitle(xTitle);
		renderer.setShowGrid(true); // 设置网格显示
		renderer.setGridColor(Color.parseColor("#eeeeee"));
		renderer.setPointSize(8f);// 每个坐标点的大小
		Align align = renderer.getYAxisAlign(0);
		renderer.setYLabelsAlign(align);
		renderer.setYLabelsColor(0, Color.BLACK);
		renderer.setXLabelsColor(Color.BLACK);// 设置轴标签颜色

		renderer.setXLabelsAngle(45);
		renderer.setXRoundedLabels(true);
		renderer.setAxesColor(Color.BLACK);// 设置XY轴颜色
		renderer.setYLabelsAlign(Align.RIGHT);
		//设置Y轴的数据

		//设置x轴的数据
		for (int i = 0; i < listtime.size(); i++) {
			renderer.addXTextLabel(listtime.size()-1-i, listtime.get(i));
		}

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		
		// 保存点集数据 ，包括每条曲线的X，Y坐标
		XYSeries series = new XYSeries("");
		series.setTitle(title);
		for (int i = 0; i < listdata.size(); i++) {
			series.add(Double.valueOf(i + ""), Double.valueOf(listdata.get(listtime.size()-1-i)));
		}
		dataset.addSeries(series);

		
		
		XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
		xyRenderer.setColor(Color.parseColor("#007aa4"));// 深蓝色
		xyRenderer.setLineWidth(2f);// 设置线的宽度
		xyRenderer.setDisplayChartValues(true);// 在柱子顶端显示数值
		xyRenderer.setChartValuesTextSize(18f);
		xyRenderer.setDisplayChartValuesDistance(30);
		xyRenderer.setPointStyle(PointStyle.DIAMOND);// 设置点的样式
		xyRenderer.setFillBelowLine(true);
		xyRenderer.setFillBelowLineColor(Color.parseColor("#66FFB040"));
		xyRenderer.setFillPoints(true);

		renderer.addSeriesRenderer(xyRenderer);

		return ChartFactory.getCubeLineChartView(context, dataset, renderer, 0.33f);

	}
	public static View getLineChartViewTwo(Context context, List<String> listtime,List<String> listdata,String title,String xTitle,String yTitle,int length) {
		// 构造显示用渲染图
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		// 曲线图的格式，包括颜色，值的范围，点和线的形状等等
		renderer.setBackgroundColor(Color.parseColor("#efefef"));// 背景色，灰白色
		renderer.setApplyBackgroundColor(true);// 设置背景颜色生效

		renderer.setMarginsColor(Color.parseColor("#efefef"));// // 边框外侧颜色
		renderer.setPanEnabled(true, false);// 设置X轴和Y轴是否可以滑动
		renderer.setXAxisMin(0);// 设置X轴起点
		renderer.setXAxisMax(20);// 设置X轴最大点
		renderer.setPanLimits(new double[] { 0, length, 0, 0, });// 坐标滑动上、下限,
		renderer.setLabelsTextSize(20f);// 图表标题字体大小：20
		renderer.setMargins(new int[] {55, 55, 40, 10});// 图形4边距
//		renderer.setYAxisMin(min);// 设置Y轴起点
//		renderer.setYAxisMax(max);
//		renderer.setYLabels(x);// 
		renderer.setZoomEnabled(true, false);// 设置不可放大缩小
		renderer.setYTitle(yTitle);
		renderer.setAxisTitleTextSize(20f);//设置轴标题文字大小
		
//		renderer.setXLabels(50);
		
		renderer.setXTitle(xTitle);
		renderer.setShowGrid(true); // 设置网格显示
		renderer.setGridColor(Color.parseColor("#eeeeee"));
		renderer.setPointSize(8f);// 每个坐标点的大小
		Align align = renderer.getYAxisAlign(0);
		renderer.setYLabelsAlign(align);
		renderer.setYLabelsColor(0, Color.BLACK);
		renderer.setXLabelsColor(Color.BLACK);// 设置轴标签颜色

		renderer.setXLabelsAngle(45);
		renderer.setXRoundedLabels(true);
		renderer.setAxesColor(Color.BLACK);// 设置XY轴颜色
		renderer.setYLabelsAlign(Align.RIGHT);
		//设置Y轴的数据

		//设置x轴的数据
		for (int i = 0; i < listtime.size(); i++) {
			renderer.addXTextLabel(listtime.size()-1-i, listtime.get(i));
		}

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		
		// 保存点集数据 ，包括每条曲线的X，Y坐标
		XYSeries series = new XYSeries("");
		series.setTitle(title);
		for (int i = 0; i < listdata.size(); i++) {
			series.add(Double.valueOf(i + ""), Double.valueOf(listdata.get(listtime.size()-1-i)));
		}
		dataset.addSeries(series);

		
		
		XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
		xyRenderer.setColor(Color.parseColor("#007aa4"));// 深蓝色
		xyRenderer.setLineWidth(2f);// 设置线的宽度
		xyRenderer.setDisplayChartValues(true);// 在柱子顶端显示数值
		xyRenderer.setChartValuesTextSize(18f);
		xyRenderer.setDisplayChartValuesDistance(30);
		xyRenderer.setPointStyle(PointStyle.DIAMOND);// 设置点的样式
		xyRenderer.setFillBelowLine(true);
		xyRenderer.setFillBelowLineColor(Color.parseColor("#66FFB040"));
		xyRenderer.setFillPoints(true);

		renderer.addSeriesRenderer(xyRenderer);

		return ChartFactory.getCubeLineChartView(context, dataset, renderer, 0.33f);

	}
}
