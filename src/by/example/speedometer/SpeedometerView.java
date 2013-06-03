package by.example.speedometer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
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

	Path path;
	Path speedSmallPath;
	Path speedLargePath;
	private Bitmap bitmap;
	private Drawable speedPointer;

	private static RectF ovalRectOUT = new RectF(0, 0, 200, 250);
	private static RectF speedRect = new RectF(0, 0, 50, 50);
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

		paintLargeSpeed = new Paint();
		paintLargeSpeed.setStyle(Style.STROKE);
		paintLargeSpeed.setColor(Color.YELLOW);
		paintLargeSpeed.setStrokeWidth(10);

		paintRect = new Paint();
		paintRect.setStyle(Style.FILL);
		paintRect.setColor(Color.GREEN);

		Resources res = getResources();
		speedPointer = res.getDrawable(R.drawable.clockgoog_minute);
		bitmap = BitmapFactory.decodeResource(res, R.drawable.clockgoog_minute);
	}
	
	@Override
	protected void onAttachedToWindow() {
		Log.i(TAG, "onAttachedToWindow");
		super.onAttachedToWindow();
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.i(TAG, "onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		Log.i(TAG, "onLayout");
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	public void requestLayout() {
		Log.i(TAG, "requestLayout");
		super.requestLayout();
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		Log.i(TAG, "ON DRAW");
		super.onDraw(canvas);

		float radius = 84f;
		float shortRadius = radius * 0.9f;
		float longRadius = radius * 1.1f;
		
		double min = 1000;
		double max = 7890;

		double angleMin = 300;
		double angleMax = 60;

		canvas.drawRect(ovalRectOUT, paintFill);
		// drawSpeedHand

		double NULL_VALUE = Double.MAX_VALUE;
		double minorTicks = NULL_VALUE, majorTicks = NULL_VALUE;

		if (minorTicks == NULL_VALUE) {
			minorTicks = (max - min) / 30;
		}
		if (majorTicks == NULL_VALUE) {
			majorTicks = (max - min) / 10;
		}

		int intrinsicWidth = speedPointer.getIntrinsicWidth();
		int intrinsicHeight = speedPointer.getIntrinsicHeight();

		float percent = (float) ((4600 - min) / (max - min));
		float current = 240 * percent;
		
		canvas.save();
		
		int start = (int) (240 + current);
		float currentPoint = start ;

		canvas.rotate(currentPoint, 100, 100);

		speedPointer.setBounds(100 - (intrinsicWidth / 2),
				110 - (intrinsicHeight / 2), 100 + (intrinsicWidth / 2),
				60 + (intrinsicHeight / 2));
		speedPointer.draw(canvas);
		canvas.restore();
		
		drawTicks(canvas, min, max, angleMin, angleMax, 100, 100, longRadius,
				radius, minorTicks, paintSmallSpeed, false);
		drawTicks(canvas, min, max, angleMin, angleMax, 100, 100, longRadius,
				shortRadius, majorTicks, paintSmallSpeed, true);
	}

	/**
	 * Returns the angle for a specific chart value.
	 * 
	 * @param value
	 *            the chart value
	 * @param minAngle
	 *            the minimum chart angle value
	 * @param maxAngle
	 *            the maximum chart angle value
	 * @param min
	 *            the minimum chart value
	 * @param max
	 *            the maximum chart value
	 * @return the angle
	 */
	private double getAngleForValue(double value, double minAngle,
			double maxAngle, double min, double max) {
		double angleDiff = maxAngle - minAngle;
		double diff = max - min;
		return Math.toRadians(minAngle + (value - min) * angleDiff / diff);
	}

	/**
	 * Draws the chart tick lines.
	 * 
	 * @param canvas
	 *            the canvas
	 * @param min
	 *            the minimum chart value
	 * @param max
	 *            the maximum chart value
	 * @param minAngle
	 *            the minimum chart angle value
	 * @param maxAngle
	 *            the maximum chart angle value
	 * @param centerX
	 *            the center x value
	 * @param centerY
	 *            the center y value
	 * @param longRadius
	 *            the long radius
	 * @param shortRadius
	 *            the short radius
	 * @param ticks
	 *            the tick spacing
	 * @param paint
	 *            the paint settings
	 * @param labels
	 *            paint the labels
	 * @return the angle
	 */
	private void drawTicks(Canvas canvas, double min, double max,
			double minAngle, double maxAngle, int centerX, int centerY,
			double longRadius, double shortRadius, double ticks, Paint paint,
			boolean labels) {
		// TODO count labels;
		for (double i = min; i <= max; i += ticks) {
			double angle = getAngleForValue(i, minAngle, maxAngle, min, max);
			double sinValue = Math.sin(angle);
			double cosValue = Math.cos(angle);
			int x1 = Math.round(centerX + (float) (shortRadius * sinValue));
			int y1 = Math.round(centerY + (float) (shortRadius * cosValue));
			int x2 = Math.round(centerX + (float) (longRadius * sinValue));
			int y2 = Math.round(centerY + (float) (longRadius * cosValue));
			canvas.drawLine(x1, y1, x2, y2, paint);
			if (labels) {
				paint.setTextAlign(Align.LEFT);
				if (x1 <= x2) {
					paint.setTextAlign(Align.RIGHT);
				}
				String text = i + "";
				if (Math.round(i) == (long) i) {
					text = (long) i + "";
				}
				canvas.drawText(text, x1, y1, paint);
			}
		}
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
		Log.v(TAG, "RAD" + radStartAngle);
		float startAngle = (float) Math.toDegrees(radStartAngle);
		Log.v(TAG, "Degrees" + startAngle);
		return startAngle;

	}

	enum Direction {
		NORTH, SOUTH, EAST, WEST;
	}

	enum Side {
		LEFT, RIGHT
	}
}
