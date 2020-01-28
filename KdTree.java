import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class KdTree {
    private Node root;

    private class Node {
        private Point2D point;
        private Node left;
        private Node right;
        private int depth;
        private boolean vertical;

        private Node(Point2D p, int depth) {
            this.point = p;
            this.left = null;
            this.right = null;
            this.depth = depth;
            if (depth % 2 == 0) {
                vertical = false;
            } else {
                vertical = true;
            }
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        nodes(root);
    }

    private int nodes(Node current) {
        if (current == null) {
            return 0;
        } else {
            return 1 + nodes(current.left) + nodes(current.right);
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        if (root == null) {
            root = new Node(p, 1);
        } else {
            insertTree(p, root);
        }
    }

    private void insertTree(Point2D p, Node n) {
        if (n.vertical) {
            if (p.x() < n.point.x()) {
                if (n.left == null) {
                    n.left = new Node(p, n.depth + 1);
                } else {
                    insertTree(p, n.left)
                }
            } else {
                if (n.right == null) {
                    n.right = new Node(p, n.depth + 1);
                } else {
                    insertTree(p, n.right);
                }
            }
        } else {
            if (p.y() < n.point.y()) {
                if (n.left == null) {
                    n.left = new Node(p, n.depth + 1);
                } else {
                    insertTree(p, n.left);
                }
            } else {
                if (n.right == null) {
                    n.right = new Node(p, n.depth + 1);
                } else {
                    insertTree(p, n.right);
                }
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        return search(p, root);
    }

    private boolean search(Point2D p, Node n) {
        if (n.point.x() == p.x() && n.point.y() == p.y()) {
            return true;
        } else if (n.vertical) {
            if (p.x() < n.point.x()) {
                return search(p, n.left);
            } else {
                return search(p, n.right);
            }
        } else {
            if (p.y() < n.point.y()) {
                return search(p, n.left);
            } else {
                return search(p, n.right);
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        drawNodes(root, null);
    }

    private void drawNodes(Node n, Node previous) {
        if (n == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        n.point.draw()
        if (n.vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            if (n == root) {
                StdDraw.line(n.point.x(), 0, n.point.x(), 1);
            } else {
                StdDraw.line(n.point.x(), previous.point.y(), n.point.x(), )
            }
            drawNodes(n.left);
            drawNodes(n.right);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);

        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Argument is null");
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument is null");
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
