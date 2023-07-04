/* TRAVELLING SALESMAN PROBLEM
 * 
 * This program will provide a solution to the Travelling Salesman Problem given a variable number of points.
 * @authors Sorino, Cabana, Dingcon, Mendinueta, Opina, Rafal, Ruffy, Tolentino
 */

import java.util.*;

public class Path {
    private Vector<Point> coordList = new Vector<Point>();
    private Vector<Point> lineOrderList = new Vector<Point>();
    private double totalFitness = 0.0;
    String pathName;

    // Copy constructor
    Path(Path other, String name) {
        this.pathName = name;
        this.lineOrderList = (Vector<Point>) other.getList().clone();
        calculateFitness();
    }

    Path(String name) {
        pathName = name;

        // Initialize coords
        coordList.add(new Point("A", 78, 57));
        coordList.add(new Point("B", 42, 13));
        coordList.add(new Point("C", -118.18, -67.89));
        coordList.add(new Point("D", 0.78, 6.9));
        coordList.add(new Point("E", 50, -20));
        coordList.add(new Point("F", 69, -12));
        coordList.add(new Point("G", -36, 67));
        coordList.add(new Point("H", 60, 90));
        coordList.add(new Point("I", -44, -44));
        coordList.add(new Point("J", -9, 9));
        coordList.add(new Point("K", -30, 27));
        coordList.add(new Point("L", -51.55, -5));
        coordList.add(new Point("M", 80, -47));
        coordList.add(new Point("N", -99, 0));
        coordList.add(new Point("O", 50, 40));


        // Initialize starting pseudo-pair
        Point leftComponent = null;
        Point rightComponent = getRandomPoint(coordList.size());

        // Initialize fitness variables
        double pairFitness = 0.0;

        // Generate list of parents until coordinate list is empty
        while (coordList.size() > 0) {
            // Initialize new pair utilizing previously stored pair
            leftComponent = rightComponent;
            rightComponent = getRandomPoint(coordList.size());

            // Get the fitness
            pairFitness = getFitness(leftComponent, rightComponent);
            totalFitness += pairFitness;

            // Display details
            // System.out.println("Pair: " + leftComponent.getName() + rightComponent.getName());
            // System.out.println("Fitness: " + pairFitness + "\n");
        }

        // System.out.println(pathName + " - Total fitness: " + totalFitness);
    }

    void setName(String name) {
        this.pathName = name;
    }

    /* This method recalculates the total fitness of a path */
    void calculateFitness() {
        this.totalFitness = 0.0;
        
        for (int i = 1; i < lineOrderList.size(); i++) {
            this.totalFitness += getFitness(getPoint(i - 1), getPoint(i));
        }
    }

    /* This function will retrieve a random Point from the global vector coordList */
    Point getRandomPoint(int max) {
        int min = 0;

        int parentID = -1;

        // Use random number function to get ID
        parentID = getRandomNumber(min, max);
        Point tempParent = this.coordList.get(parentID);

        // Add to new list to define point order and lines
        this.lineOrderList.add(coordList.get(parentID));

        //  Remove parent ID from list, so it cannot be selected again
        this.coordList.remove(parentID);

        return tempParent;
    }

    /* This is a general function that returns the distance between 2 points */
    static double getFitness(Point a, Point b) {
        return Math.sqrt(Math.pow((b.getY() - a.getY()), 2) + Math.pow((b.getX() - a.getX()), 2));
    }

    /* This is a general function that returns a random number in a given range */
    static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    Point getPoint(int index) {
        return this.lineOrderList.get(index);
    }

    String getName() {
        return pathName;
    }

    Vector<Point> getList() {
        return this.lineOrderList;
    }

    double getTotalFitness() {
        return this.totalFitness;
    }
}

class Point {
    String name;
    private double x, y;

    // Constructor
    Point(String text, double xVal, double yVal) {
        name = text;
        x = xVal;
        y = yVal;
    }

    // Getters
    String getName() {
        return name;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }
}