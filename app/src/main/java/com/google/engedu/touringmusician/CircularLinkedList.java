

package com.google.engedu.touringmusician;


import android.graphics.Point;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

    }

    Node head;
    Node newlyInsertedNode;
    float total;

    public void insertBeginning(Point p) {
        Node newNode = new Node();
        newNode.point = p;
        newlyInsertedNode = newNode;

        if (head == null) {//node is empty

            newNode.prev = newNode;
            newNode.next = newNode;
            head = newNode;

        } else {

            newNode.next = head.next;//new node
            head.next = newNode;//head points to new node

            newNode.prev = newNode.next.prev;//newnode's prev points to the last element
            newNode.prev.next = newNode; // last element points to new node

            newNode.next.prev = newNode;// the previous first's prev element now points to new node

        }

    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y - to.y, 2) + Math.pow(from.x - to.x, 2));
    }


    public float totalDistance(boolean compute) {


        if (compute) {
            if (head.next.equals(head.prev)) {
                total += (distanceBetween(newlyInsertedNode.point, newlyInsertedNode.prev.point));
            } else {
                total += (distanceBetween(newlyInsertedNode.point, newlyInsertedNode.prev.point) + distanceBetween(newlyInsertedNode.point, newlyInsertedNode.next.point)) - distanceBetween(newlyInsertedNode.next.point, newlyInsertedNode.prev.point);
            }
        }

        return total;
    }

    public void insertNearest(Point p) {
        Node newNode = new Node();
        newNode.point = p;
        newlyInsertedNode = newNode;

        if (head == null) {//node is empty

            newNode.prev = newNode;
            newNode.next = newNode;
            head = newNode;

        } else {

            Node currentNode = head;

            float smallestDistance = distanceBetween(currentNode.point, newNode.point);
            Node closestNode = currentNode;
            currentNode = currentNode.next;
            while (currentNode != head) {

                if (smallestDistance > distanceBetween(currentNode.point, newNode.point)) {
                    smallestDistance = distanceBetween(currentNode.point, newNode.point);
                    closestNode = currentNode;
                }
                currentNode = currentNode.next;


            }

            newNode.next = closestNode.next;//new node points to closet node's next
            closestNode.next = newNode;//head points to new node

            newNode.prev = newNode.next.prev;//newnode's prev points to the last element
            newNode.prev.next = newNode; // last element points to new node

            newNode.next.prev = newNode;// the previous first's prev element now points to new node


        }

    }

    public void insertSmallest(Point p) {

        Node newNode = new Node();
        newNode.point = p;
        newlyInsertedNode = newNode;


        if (head == null) {
            newNode.prev = newNode;
            newNode.next = newNode;
            head = newNode;

        } else {
            Node currentNode = head;
            newlyInsertedNode.next = currentNode.next;
            newlyInsertedNode.prev = currentNode;
            float minTotalDistance = totalDistance(true);
            Node minTotalDistanceNode = currentNode;

            currentNode = currentNode.next;

            decrementTotalDistanceBy(newlyInsertedNode);

            while (currentNode != head) {
                newlyInsertedNode.next = currentNode.next;
                newlyInsertedNode.prev = currentNode;
                if (totalDistance(true) < minTotalDistance) {
                    minTotalDistance = totalDistance(false);
                    minTotalDistanceNode = currentNode;
                }
                currentNode = currentNode.next;
                decrementTotalDistanceBy(newlyInsertedNode);
            }

            newNode.next = minTotalDistanceNode.next;//new node points to closet node's next
            minTotalDistanceNode.next = newNode;//head points to new node

            newlyInsertedNode = minTotalDistanceNode;
            newNode.prev = newNode.next.prev;//newnode's prev points to the last element
            newNode.prev.next = newNode; // last element points to new node

            newNode.next.prev = newNode;// the previous first's prev element now points to new node
            totalDistance(true);
        }


    }

    public void reset() {
        head = null;
    }

    private void decrementTotalDistanceBy(Node newNode) {
        if (newNode.next.equals(newNode.prev)) {
            total = 0;
        } else {
            total = totalDistance(false) - (distanceBetween(newNode.point, newNode.prev.point) + distanceBetween(newNode.point, newNode.next.point)) + distanceBetween(newNode.prev.point, newNode.next.point);
        }

    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
