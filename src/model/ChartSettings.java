package model;

public class ChartSettings {

	String title;
	String subTitle;
	String xAxisTitle;
	String yAxisTitle;
	public ChartSettings(String title, String subTitle, String xAxisTitle, String yAxisTitle) {
		super();
		this.title = title;
		this.subTitle = subTitle;
		this.xAxisTitle = xAxisTitle;
		this.yAxisTitle = yAxisTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getxAxisTitle() {
		return xAxisTitle;
	}
	public void setxAxisTitle(String xAxisTitle) {
		this.xAxisTitle = xAxisTitle;
	}
	public String getyAxisTitle() {
		return yAxisTitle;
	}
	public void setyAxisTitle(String yAxisTitle) {
		this.yAxisTitle = yAxisTitle;
	}
	@Override
	public String toString() {
		return "ChartSettings [title=" + title + ", subTitle=" + subTitle + ", xAxisTitle=" + xAxisTitle
				+ ", yAxisTitle=" + yAxisTitle + "]";
	}
	
	
	
	
	
	
}
