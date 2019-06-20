/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chromostat;

//stores a point using cartisean coordinates
public class Point {

    private int x;
    private int y;

    //pre: intergs to set the values of x and y to
    //post: sets x and y to passed values
    public Point(int x, int y) {

        this.x = x;
        this.y = y;

    }

    //post: returns current x value
    public int getX() {
        return this.x;
    }

    //post: returns current y value
    public int getY() {
        return this.y;
    }

    //pre: integer to set x to
    //post: sets x to passed value
    public void setX(int newX) {

        this.x = newX;
    }

    //pre: integer to set y to
    //post: sets y to passed value
    public void setY(int newY) {
        this.y = newY;
    }

    @Override
    //post: return comma seperated x and y value
    public String toString() {

        String point = "" + x + "," + y;

        return point;

    }

}
