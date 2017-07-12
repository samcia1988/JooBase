package org.theta.joobase.josql;

/**
 * 
 * @author ranger
 * @date Jul 12, 2017
 *
 */
public class DataObject {

	private int pr1;
	private Integer pr2;
	public int pr3;
	public Integer pr4;
	public String pr5;

	public DataObject(int pr1, Integer pr2, int pr3, Integer pr4, String pr5) {
		this.pr1 = pr1;
		this.pr2 = pr2;
		this.pr3 = pr3;
		this.pr4 = pr4;
		this.pr5 = pr5;
	}

	public int getPr1() {
		return pr1;
	}

	public void setPr1(int pr1) {
		this.pr1 = pr1;
	}

	public Integer getPr2() {
		return pr2;
	}

	public void setPr2(Integer pr2) {
		this.pr2 = pr2;
	}

	public String flag() {
		return "flag";
	}

}
