package ja.burhanrashid52.photoeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <p>
 * This is custom drawing view used to do painting on user touch events it it will paint on canvas
 * as per attributes provided to the paint
 * </p>
 *
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.1
 * @since 12/1/18
 */
public class DrawingView extends BrushDrawingView {

  protected float mStartX;
  protected float mStartY;
  private DrawingTool drawingTool = DrawingTool.PEN;

  public DrawingView(Context context) {
    super(context);
  }

  public DrawingView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DrawingView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  void setBrushDrawingMode(boolean brushDrawMode, DrawingTool drawingTool) {
    super.setBrushDrawingMode(brushDrawMode);
    this.drawingTool = drawingTool;
  }

  /**
   * Handle touch event to draw paint on canvas i.e brush drawing
   *
   * @param event points having touch info
   * @return true if handling touch events
   */
  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(@NonNull MotionEvent event) {
    if (mBrushDrawMode) {
      float touchX = event.getX();
      float touchY = event.getY();
      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          touchStart(touchX, touchY);
          break;
        case MotionEvent.ACTION_MOVE:
          touchMove(touchX, touchY);
          break;
        case MotionEvent.ACTION_UP:
          touchUp();
          break;
      }
      invalidate();
      return true;
    } else {
      return false;
    }
  }

  private void touchStart(float x, float y) {
    mRedoPaths.clear();
    mPath.reset();
    mPath.moveTo(x, y);
    mTouchX = x;
    mTouchY = y;
    mStartX = mTouchX;
    mStartY = mTouchY;
    if (mBrushViewChangeListener != null) {
      mBrushViewChangeListener.onStartDrawing();
    }
  }

  protected void touchMove(float x, float y) {
    switch (drawingTool) {
      case PEN:
        float dx = Math.abs(x - mTouchX);
        float dy = Math.abs(y - mTouchY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
          mPath.quadTo(mTouchX, mTouchY, (x + mTouchX) / 2, (y + mTouchY) / 2);
          mTouchX = x;
          mTouchY = y;
        }
        break;

      case LINE:
        break;

      case ARROW:
        break;

      case DOUBLE_ARROW:
        break;

      case SQUARE:
        mPath.reset();
        mPath.addRect(new RectF(mStartX, mStartY, x, y), Path.Direction.CW);
        break;

      case CIRCLE:
        mPath.reset();
        mPath.addOval(new RectF(mStartX, mStartY, x, y), Path.Direction.CW);
        break;

      case TRIANGLE:
        break;

      case STAR:
        mPath.reset();
        int numOfPt = 5;
        float radius = Math.max(Math.abs(x-mStartX), Math.abs(y-mStartY));
        float innerRadius = radius/2;
        double section = 2.0 * Math.PI/numOfPt;;
        mPath.moveTo(
            (float)(x + radius * Math.cos(0)),
            (float)(y + radius * Math.sin(0)));
        mPath.lineTo(
            (float)(x + innerRadius * Math.cos(0 + section/2.0)),
            (float)(y + innerRadius * Math.sin(0 + section/2.0)));

        for(int i=1; i<numOfPt; i++){
          mPath.lineTo(
              (float)(x + radius * Math.cos(section * i)),
              (float)(y + radius * Math.sin(section * i)));
          mPath.lineTo(
              (float)(x + innerRadius * Math.cos(section * i + section/2.0)),
              (float)(y + innerRadius * Math.sin(section * i + section/2.0)));
        }

        mPath.close();

        break;

      case PENTAGONE:
        break;
    }
  }
}


  //float mid = Math.abs(x-mStartX) / 2;
  //float min = Math.min(Math.abs(x-mStartX), Math.abs(y-mStartY));
  //float fat = min / 17;
  //float half = min / 2;
  //float rad = half - fat;
  //      mid = mid - half;
  //
  //          // top left
  //          mPath.moveTo(mid + half * 0.5f, half * 0.84f);
  //          // top right
  //          mPath.lineTo(mid + half * 1.5f, half * 0.84f);
  //          // bottom left
  //          mPath.lineTo(mid + half * 0.68f, half * 1.45f);
  //          // top tip
  //          mPath.lineTo(mid + half * 1.0f, half * 0.5f);
  //          // bottom right
  //          mPath.lineTo(mid + half * 1.32f, half * 1.45f);
  //          // top left
  //          mPath.lineTo(mid + half * 0.5f, half * 0.84f);
  //          mPath.close();