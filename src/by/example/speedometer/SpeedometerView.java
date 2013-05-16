package by.example.speedometer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

public class SpeedometerView extends android.view.View {

	private Paint paintRect;
	private Paint paintText;
	private Paint paintFill;
	private Paint paintSmallSpeed;
	private Paint paintLargeSpeed;

	private int speedSteps;
	private static String TAG = "SIMPLEDROID";

	public SpeedometerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		paintFill = new Paint();
		paintFill.setStyle(Style.FILL);

		paintText = new Paint();
		paintText.setStyle(Style.FILL);
		paintText.setColor(Color.YELLOW);

		paintSmallSpeed = new Paint();
		paintSmallSpeed.setStyle(Style.STROKE);
		paintSmallSpeed.setColor(Color.YELLOW);
		paintSmallSpeed.setStrokeWidth(5);

		paintLargeSpeed = new Paint();
		paintLargeSpeed.setStyle(Style.STROKE);
		paintLargeSpeed.setColor(Color.YELLOW);
		paintLargeSpeed.setStrokeWidth(10);

		paintRect = new Paint();
		paintRect.setStyle(Style.FILL);
		paintRect.setColor(Color.RED);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.v(TAG, "ON DRAW");
		super.onDraw(canvas);
		Path path = new Path();
		Path speedSmallPath = new Path();
		Path speedLargePath = new Path();

		RectF ovalRectOUT = new RectF(0, 0, 50, 50);
		RectF speedRect = new RectF(0, 0, 50, 50);

		float semicircle = getSemicircle(10, 100, 200, 100, ovalRectOUT,
				Side.LEFT);
		float semicircleSpeed = getSemicircle(15, 100, 195, 100, speedRect,
				Side.LEFT);

		path.addArc(ovalRectOUT, semicircle, 180);

		speedSmallPath.addArc(speedRect, semicircleSpeed, 180);
		speedLargePath.addArc(speedRect, semicircleSpeed, 180);

		PathMeasure pm = new PathMeasure(path, false);
		float aCoordinates[] = { 0f, 0f };
		// get point from the middle
		boolean posTan = pm
				.getPosTan(pm.getLength() * 0.5f, aCoordinates, null);
		Log.v(TAG, "COORDS" + aCoordinates[0] + " " + aCoordinates[1]);
		Log.v(TAG, "RECT " + ovalRectOUT);

		canvas.drawRect(ovalRectOUT, paintRect);
		canvas.drawPath(path, paintFill);

		int speedSpot = 0;
		int vOffsetStep = 0;
		for (int i = 0; i < 13; i++) {
			canvas.drawTextOnPath(Integer.toString(speedSpot), path,
					vOffsetStep, 20, paintText);
			speedSpot += 10;
			vOffsetStep += 20;
		}

		float[] pathEffSmall = {5,5}; // 5 - width ,30 - padding
		float[] pathEffLarge = {5,15}; // 5 - width ,30 - padding
		paintLargeSpeed.setPathEffect(new DashPathEffect(pathEffLarge,0));
		paintSmallSpeed.setPathEffect(new DashPathEffect(pathEffSmall,0));
		canvas.drawPath(speedSmallPath, paintSmallSpeed);
		canvas.drawPath(speedLargePath, paintLargeSpeed);		

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.v(TAG, "onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public static Path getEquilateralTriangle(Point p1, int width,
			Direction direction) {
		Point p2 = null, p3 = null;

		if (direction == Direction.NORTH) {
			p2 = new Point(p1.x + width, p1.y);
			p3 = new Point(p1.x + (width / 2), p1.y - width);
		} else if (direction == Direction.SOUTH) {
			p2 = new Point(p1.x + width, p1.y);
			p3 = new Point(p1.x + (width / 2), p1.y + width);
		} else if (direction == Direction.EAST) {
			p2 = new Point(p1.x, p1.y + width);
			p3 = new Point(p1.x - width, p1.y + (width / 2));
		} else if (direction == Direction.WEST) {
			p2 = new Point(p1.x, p1.y + width);
			p3 = new Point(p1.x + width, p1.y + (width / 2));
		}

		Path path = new Path();
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);

		return path;
	}

	public static float getSemicircle(float xStart, float yStart, float xEnd,
			float yEnd, RectF ovalRectOUT, Side direction) {

		float centerX = xStart + ((xEnd - xStart) / 2);
		float centerY = yStart + ((yEnd - yStart) / 2);

		double xLen = (xEnd - xStart);
		double yLen = (yEnd - yStart);
		float radius = (float) (Math.sqrt(xLen * xLen + yLen * yLen) / 2);

		RectF oval = new RectF((float) (centerX - radius),
				(float) (centerY - radius), (float) (centerX + radius),
				(float) (centerY + radius));

		ovalRectOUT.set(oval);

		double radStartAngle = 0;
		if (direction == Side.LEFT) {
			radStartAngle = Math.atan2(yStart - centerY, xStart - centerX);
		} else {
			radStartAngle = Math.atan2(yEnd - centerY, xEnd - centerX);
		}
		float startAngle = (float) Math.toDegrees(radStartAngle);

		return startAngle;

	}

	enum Direction {
		NORTH, SOUTH, EAST, WEST;
	}

	enum Side {
		LEFT, RIGHT
	}
}
