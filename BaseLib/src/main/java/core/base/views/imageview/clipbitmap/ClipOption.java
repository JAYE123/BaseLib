package core.base.views.imageview.clipbitmap;

public class ClipOption {
	/**
	 *圆角半径
	 */
	private int cornerLength;
	/**
	 * 尖角的底部长度
	 */
	private int triangleBottomLength;
	/**
	 * 尖角高度
	 */
	private int triangleHeight;
	/**
	 * 尖角与顶部的距离
	 */
	private int triangleMaginTop;
	
	/**
	 * 尖角与左边的距离
	 */
	private int triangleMaginLeft;

	/**
	 * 尖角的位置，必段设置，否则出错
	 */
	private TriAngleDirection triAngleDirection;

	public ClipOption() {
		super();
	}

	public ClipOption(int cornerLength, int triangleHight, int triangleWidth,
			int triangleMaginTop, int triangleMaginLeft,
			TriAngleDirection triAngleDirection) {
		super();
		this.cornerLength = cornerLength;
		this.triangleBottomLength = triangleHight;
		this.triangleHeight = triangleWidth;
		this.triangleMaginTop = triangleMaginTop;
		this.triangleMaginLeft = triangleMaginLeft;
		this.triAngleDirection = triAngleDirection;
	}

	public TriAngleDirection getTriAngleDirection() {
		return triAngleDirection;
	}

	public ClipOption setTriAngleDirection(TriAngleDirection triAngleDirection) {
		this.triAngleDirection = triAngleDirection;
		return this;
	}

	public int getTriangleMaginTop() {
		return triangleMaginTop;
	}

	public ClipOption setTriangleMaginTop(int triangleMaginTop) {
		this.triangleMaginTop = triangleMaginTop;
		return this;
	}

	public int getTriangleMaginLeft() {
		return triangleMaginLeft;
	}

	public ClipOption setTriangleMaginLeft(int triangleMaginLeft) {
		this.triangleMaginLeft = triangleMaginLeft;
		return this;
	}

	public int getCornerDip() {
		return cornerLength;
	}

	public ClipOption setCornerDip(int cornerDip) {
		this.cornerLength = cornerDip;
		return this;
	}

	public int getTriangleBottomLength() {
		return triangleBottomLength;
	}

	public ClipOption setTriangleBottomLength(int triangleHight) {
		this.triangleBottomLength = triangleHight;
		return this;
	}

	public int getTriangleHeight() {
		return triangleHeight;
	}

	public ClipOption setTriangeHeight(int triangleHeight) {
		this.triangleHeight = triangleHeight;
		return this;
	}

	public ClipOption check(int width, int height) {
		if (triAngleDirection == null)throw new RuntimeException("必须传入三角形的位置");
		if (triAngleDirection == TriAngleDirection.LEFT|| triAngleDirection == TriAngleDirection.RIGHT) {
			if (triangleMaginTop < cornerLength||triangleMaginTop+triangleBottomLength+cornerLength>height || triangleHeight>=width){
				setDefaultOption(width, height);
			}
		}
		if (triAngleDirection == TriAngleDirection.TOP|| triAngleDirection == TriAngleDirection.BOTTOM) {
			if (triangleMaginLeft < cornerLength||triangleMaginLeft+triangleBottomLength+cornerLength>width || triangleHeight >=height){
				setDefaultOption(width, height);
			}
		}
		return this;
	}
	
	
	public ClipOption setDefaultOption(int width, int height){
		
		int cornerLength=(int) Math.min((width*0.1f),(height*0.1f));
		int triangleWidth=0;;
		int triangleHight=0;
		int triAngleMarginTop=0;
		int triAngleMargintLeft=0;
		if (triAngleDirection == TriAngleDirection.LEFT|| triAngleDirection == TriAngleDirection.RIGHT) {
			triangleWidth=(int) (width*0.1f);
			triangleHight=(int) (height*0.1f);	
			triAngleMarginTop=(int) (height*0.1f);
			setTriangleMaginTop(triAngleMarginTop);
		}
		if (triAngleDirection == TriAngleDirection.TOP|| triAngleDirection == TriAngleDirection.BOTTOM) {
			triangleWidth=(int) (height*0.1f);
			triangleHight=(int) (width*0.1f);	
			triAngleMargintLeft=(int) (width*0.1f);
			setTriangleMaginLeft(triAngleMargintLeft);
		}
		setCornerDip(cornerLength);
		setTriangeHeight(triangleWidth);
		setTriangleBottomLength(triangleHight);
		
		
		return this;
	}
	public static enum TriAngleDirection {
		LEFT, TOP, RIGHT, BOTTOM;
	}	
}
