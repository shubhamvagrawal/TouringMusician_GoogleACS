

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList mainList = new CircularLinkedList();
    private CircularLinkedList secondList = new CircularLinkedList();
    private CircularLinkedList thirdList = new CircularLinkedList();
    private String insertMode = "Add";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.RED);

        List<Point> points = new ArrayList<>();
        List<Point> secondPoints = new ArrayList<>();
        List<Point> thirdPoints = new ArrayList<>();
        populatePoints(secondPoints, secondList);
        populatePoints(thirdPoints, thirdList);
        for (Point p : mainList) {
            points.add(p);

            canvas.drawCircle(p.x, p.y, 20, pointPaint);
        }

        for (int i = 0; i < points.size(); i++) {
            if (i == points.size() - 1) {
                canvas.drawLine(points.get(i).x, points.get(i).y, points.get(0).x, points.get(0).y, pointPaint);
            } else {
                canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, pointPaint);
            }
        }

        if (insertMode.equals("Compare all")) {

            pointPaint.setColor(Color.BLACK);
            for (int i = 0; i < secondPoints.size(); i++) {
                if (i == secondPoints.size() - 1) {
                    canvas.drawLine(secondPoints.get(i).x, secondPoints.get(i).y, secondPoints.get(0).x, secondPoints.get(0).y, pointPaint);
                } else {
                    canvas.drawLine(secondPoints.get(i).x, secondPoints.get(i).y, secondPoints.get(i + 1).x, secondPoints.get(i + 1).y, pointPaint);
                }
            }

            pointPaint.setColor(Color.GREEN);
            for (int i = 0; i < thirdPoints.size(); i++) {
                if (i == thirdPoints.size() - 1) {
                    canvas.drawLine(thirdPoints.get(i).x, thirdPoints.get(i).y, thirdPoints.get(0).x, thirdPoints.get(0).y, pointPaint);
                } else {
                    canvas.drawLine(thirdPoints.get(i).x, thirdPoints.get(i).y, thirdPoints.get(i + 1).x, thirdPoints.get(i + 1).y, pointPaint);
                }
            }

        }

    }

    private void populatePoints(List<Point> points, CircularLinkedList mainList) {
        for (Point p : mainList) {
            points.add(p);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int) event.getY());
                if (insertMode.equals("Closest")) {
                    mainList.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    mainList.insertSmallest(p);
                } else if (insertMode.equals("Beginning")) {
                    mainList.insertBeginning(p);
                } else {
                    mainList.insertNearest(p);
                    secondList.insertSmallest(p);
                    thirdList.insertBeginning(p);

                }
                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                if (message != null) {
                    message.setText(String.format("Tour length is now %.2f", mainList.totalDistance(true)));
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        mainList.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
