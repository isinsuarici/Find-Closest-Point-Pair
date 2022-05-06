package HW_2;

import java.awt.*;
import java.awt.geom.Point2D;

public class FindClosest {

    private PointPair closestPointPair;
    private final QuickSort quicksort = new QuickSort();

    /** Constructor
     *
     * @param points --> point array
     */
    public FindClosest(Point2D.Double[] points)
    {
        //Sort points by X coordinate
        quicksort.sort(points, 0, points.length - 1, "compareX");
        this.closestPointPair = calculateClosestPointPair(points, 0, points.length - 1);
        //*********************************do nothing***************************************//
    }

    /** Get closest Point Pair
     *
     * @return closestPointPair
     */
    public PointPair getClosestPointPair()
    {
        return this.closestPointPair;
    }

    /** Main method for calculate and return closest point pair
     *
     * @param p --> point array
     * @param startIndex --> First index of p[]
     * @param lastIndex --> last index of p[]
     * @return
     */
    private PointPair calculateClosestPointPair(Point2D.Double[] p, int startIndex, int lastIndex) {
       PointPair ret;
        if((lastIndex-startIndex)<2){ //  if there are less than 3 elements
            Point2D.Double p1 = new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);
            Point2D.Double p2 = new Point2D.Double(0, 0);
            ret = new PointPair(p1, p2);
            for (int i = 0; i < lastIndex; ++i) {
                PointPair cur_min=new PointPair(p[i], p[i+1]);
                    if (cur_min.getDistance() < ret.getDistance()) {
                        ret=cur_min;
                }
            }
            return ret;
        }
        else if(lastIndex-startIndex==2){ // base case: if there are 3 elements
            return getClosestPointPair(p[startIndex], p[startIndex + 1], p[lastIndex]);
        }

        int mid = ( (lastIndex + startIndex) / 2);

        PointPair dist_l = calculateClosestPointPair(p, startIndex, mid);
        PointPair dist_r = calculateClosestPointPair(p, mid+1, lastIndex);
        double dist;

        if(dist_l.getDistance()<dist_r.getDistance()){
            dist= dist_l.getDistance();
            ret=dist_l;
        }
        else{
            dist=dist_r.getDistance();
            ret=dist_r;
        }
        Point2D.Double strip[]=new Point2D.Double[lastIndex+1];
        int j = 0;
        for (int i = startIndex; i <= lastIndex; i++) {
            if (Math.abs(p[i].x - p[mid].x) < dist) {
                strip[j] = p[i];
                j++;
            }
        }
        /*I split the array into left and right and recursively check both sides and
        find the two pairs of points with the shortest distance on the left and right sides, but there is one thing I missed, which is:
        The distance between an element on the left and an element on the right can be shorter.
        Therefore, I must send the elements I added to the strip array to the stripClosest() method and check these elements as well.*/

        if(stripClosest(strip,j,ret).getDistance()<dist){
            ret= stripClosest(strip,j,ret);
        }

        return ret;

    }

    /** calculate and return closest point pair from 3 points
     *
     * @param p1 --> point 1
     * @param p2 --> point 2
     * @param p3 --> point 3
     * @return
     */
    // It takes three points, takes two of them, makes a PointPair,
    // and calls getClosestPointPair inside and returns the one with the shortest distance between PointPairs.
    private PointPair getClosestPointPair(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        PointPair pair1= new PointPair(p1,p2);
        PointPair pair2= new PointPair(p1,p3);
        PointPair pair3= new PointPair(p2,p3);
        PointPair close=getClosestPointPair(pair1,pair2);
        close=getClosestPointPair(close,pair3);
        return close;
    }
    // Returns the PointPair with the shortest distance from two PointPairs.
    private PointPair getClosestPointPair(PointPair p1, PointPair p2){
        if (p1.getDistance()<p2.getDistance())
            return p1;
        return p2;
    }

    /**
     * A utility function to find the distance between the closest points of
     * strip of given size. All points in strip[] are sorted according to
     * y coordinate. They all have an upper bound on minimum distance as d.
     * Note that this method seems to be a O(n^2) method, but it's a O(n)
     * method as the inner loop runs at most 6 times
     *
     * @param strip --> point array
     * @param size --> strip array element count
     * @param shortestLine --> shortest line calculated so far
     * @return --> new shortest line
     */
    private PointPair stripClosest(Point2D.Double strip[], int size, PointPair shortestLine) {
        quicksort.sort(strip, 0, size-1, "compareY");
        for (int i = 0; i < size; ++i) {
            for (int j = i + 1; j < size && (strip[j].y - strip[i].y) < shortestLine.getDistance(); ++j) {
                PointPair cur_shortest = new PointPair(strip[i], strip[j]);
                if (cur_shortest.getDistance() < shortestLine.getDistance())
                    shortestLine = cur_shortest;
            }
        }
       return shortestLine;
    }

}