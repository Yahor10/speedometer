package by.example.speedometer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
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

	private static RectF ovalRectOUT = new RectF(0, 0, 50, 50);
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
		paintSmallSpeed.setStrokeWidth(5);

		paintLargeSpeed = new Paint();
		paintLargeSpeed.setStyle(Style.STROKE);
		paintLargeSpeed.setColor(Color.YELLOW);
		paintLargeSpeed.setStrokeWidth(10);

		paintRect = new Paint();
		paintRect.setStyle(Style.FILL);
		paintRect.setColor(Color.GREEN);
		
		path = new Path();
		speedSmallPath = new Path();
		speedLargePath = new Path();

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

		getSemicircle(10, 100, 200, 100, ovalRectOUT,
				Side.LEFT);
		getSemicircle(15, 100, 195, 100, speedRect,
				Side.LEFT);

		int sweepAngle = 220;
		int startAngle = 160;
		path.addArc(ovalRectOUT, startAngle, sweepAngle);

		// TODO investigate correct sweep
		speedSmallPath.addArc(speedRect, startAngle, sweepAngle);
		speedLargePath.addArc(speedRect, startAngle, sweepAngle);

		// get point from the middle

		canvas.drawRect(ovalRectOUT, paintRect);
		canvas.drawPath(path, paintFill);

		int speedSpot = 0;
		int vOffsetStep = 0;
		for (int i = 0; i < 18; i++) {
			canvas.drawTextOnPath(Integer.toString(speedSpot), path,
					vOffsetStep, 20, paintText);
			speedSpot += 10;
			vOffsetStep += 20;
		}

		float[] pathEffSmall = { 5, 5 }; // 5 - width ,5 - padding
		float[] pathEffLarge = { 5, 15 }; // 5 - width ,15 - padding
		
		paintLargeSpeed.setPathEffect(new DashPathEffect(pathEffLarge, 0));
		paintSmallSpeed.setPathEffect(new DashPathEffect(pathEffSmall, 0));
		
		canvas.drawPath(speedSmallPath, paintSmallSpeed);
		canvas.drawPath(speedLargePath, paintLargeSpeed);
		
		int intrinsicWidth = speedPointer.getIntrinsicWidth();
		int intrinsicHeight = speedPointer.getIntrinsicHeight();
		
		Log.v(TAG,"width" + intrinsicWidth);
		Log.v(TAG,"height" + intrinsicHeight);
		
		canvas.save();
		canvas.rotate(360,100,100);// TODO calculate rotate
		speedPointer.setBounds(100 - (intrinsicWidth / 2), 110 - (intrinsicHeight / 2), 100 + (intrinsicWidth / 2), 60 + (intrinsicHeight / 2));
		speedPointer.draw(canvas);
		
		
		canvas.restore();
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
