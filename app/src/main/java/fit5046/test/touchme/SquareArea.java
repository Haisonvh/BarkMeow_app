package fit5046.test.touchme;


public class SquareArea {
    private TouchPoint topLeft,rightBottom;

    public SquareArea(TouchPoint topLeft, TouchPoint rightBottom) {
        this.topLeft = topLeft;
        this.rightBottom = rightBottom;
    }

    public TouchPoint getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(TouchPoint topLeft) {
        this.topLeft = topLeft;
    }

    public TouchPoint getRightBottom() {
        return rightBottom;
    }

    public void setRightBottom(TouchPoint rightBottom) {
        this.rightBottom = rightBottom;
    }
    public boolean checkIncludePoint(TouchPoint point){
        if (topLeft.x <= point.x && rightBottom.x>=point.x && topLeft.y <= point.y && rightBottom.y>=point.y)
            return true;
        else
            return false;
    }

    public static class TouchPoint{
        public float x,y;
        private int originalPx = 750;

        public TouchPoint(float x, float y,float currentPx) {
            this.x = x * (currentPx/originalPx);
            this.y = y * (currentPx/originalPx);
            //this.x = x;
            //this.y = y;
        }
        public TouchPoint(float x, float y) {
            this.x = x;
            this.y = y;
            //this.x = x;
            //this.y = y;
        }
    }
}
