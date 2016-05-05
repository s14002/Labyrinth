package jp.ac.it_college.std.s14002.android.labyrinth;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by s14002 on 15/10/29.
 */
public class Ball {

    private static final Paint PAINT = new Paint();
    private final Rect rect;
    private final Rect srcRect;
    private Bitmap ballBitmap;
    private OnMoveListener listener;

    public Ball(Bitmap bmp, Map.Block startBlock, float scale) {
        this(bmp, startBlock.rect.left, startBlock.rect.top, scale);
    }


    public Ball(Bitmap bmp, int left, int top, float scale) {
        ballBitmap = bmp;
        int right = left + Math.round(bmp.getWidth() * scale);
        int bottom = top + Math.round(bmp.getHeight() * scale);
        rect = new Rect(left, top, right, bottom);

        srcRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public void setOnMoveListener(OnMoveListener l) {
        listener = l;
    }

    void draw(Canvas canvas) {
        canvas.drawBitmap(ballBitmap, srcRect, rect, PAINT);
    }

    void move(float xOffset, float yOffset) {
        int align = yOffset >= 0 ? 1 : -1;
        while (!tryMoveVertical(yOffset)) {
            yOffset -= align;
        }

        align = xOffset >= 0 ? 1 : -1;
        while (!tryMoveHorizontal(xOffset)) {
            xOffset -= align;
        }
    }

    private boolean tryMoveHorizontal(float xOffset) {
        int left = rect.left + Math.round(xOffset);
        int right = left + rect.width();

        if (!listener.canMove(left, rect.top, right, rect.bottom)) {
            return false;
        }

        rect.left = left;
        rect.right = right;
        return true;
    }

    private boolean tryMoveVertical(float yOffset) {
        int top = rect.top + Math.round(yOffset);
        int bottom = top + rect.height();

        if (!listener.canMove(rect.left, top, rect.right, bottom)) {
            return false;
        }

        rect.top = top;
        rect.bottom = bottom;
        return true;
    }

    public interface OnMoveListener {
        public boolean canMove(int left, int top, int right, int bottom);
    }
}
