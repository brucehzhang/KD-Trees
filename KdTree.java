import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stack;

public class KdTree {
    private Node root;
    private int count = 0;

    private class Node {
        private Point2D point;
        private double xMax;
        private double xMin;
        private double yMax;
        private double yMin;
        private Node left;
        private Node right;
        private int depth;
        private boolean vertical;
        private RectHV rect;

        private Node(Point2D p, int depth, Node previous) {
            this.point = p;
            this.left = null;
            this.right = null;
            this.depth = depth;
            count++;
            if (depth % 2 == 0) {
                vertical = false;
            } else {
                vertical = true;
            }
            if (previous == null) {
                xMax = 1;
                xMin = 0;
                yMax = 1;
                yMin = 0;
                this.rect = new RectHV(xMin, yMin, xMax, yMax);
            } else if (vertical) {
                xMax = previous.xMax;
                xMin = previous.xMin;
                yMax = previous.yMax;
                yMin = previous.yMin;
                if (p.y() < previous.point.y()) {
                    yMax = previous.point.y();
                } else {
                    yMin = previous.point.y();
                }
                this.rect = new RectHV(xMin, yMin, xMax, yMax);
            } else {
                xMax = previous.xMax;
                xMin = previous.xMin;
                yMax = previous.yMax;
                yMin = previous.yMin;
                if (p.x() < previous.point.x()) {
                    xMax = previous.point.x();
                } else {
                    xMin = previous.point.x();
                }
                this.rect = new RectHV(xMin, yMin, xMax, yMax);
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
        return count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        if (root == null) {
            root = new Node(p, 1, null);
        } else {
            insertTree(p, root);
        }
    }

    private void insertTree(Point2D p, Node n) {
        if (p.x() == n.point.x() && p.y() == n.point.y()) {
            return;
        }
        if (n.vertical) {
            if (p.x() < n.point.x()) {
                if (n.left == null) {
                    n.left = new Node(p, n.depth + 1, n);
                } else {
                    insertTree(p, n.left);
                }
            } else {
                if (n.right == null) {
                    n.right = new Node(p, n.depth + 1, n);
                } else {
                    insertTree(p, n.right);
                }
            }
        } else {
            if (p.y() < n.point.y()) {
                if (n.left == null) {
                    n.left = new Node(p, n.depth + 1, n);
                } else {
                    insertTree(p, n.left);
                }
            } else {
                if (n.right == null) {
                    n.right = new Node(p, n.depth + 1, n);
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
        } else if (root == null) {
            return false;
        }
        return search(p, root);
    }

    private boolean search(Point2D p, Node n) {
        if (n == null) {
            return false;
        }
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
        drawNodes(root);
    }

    private void drawNodes(Node n) {
        if (n == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        n.point.draw();
        if (n.vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            if (n == root) {
                StdDraw.line(n.point.x(), 0, n.point.x(), 1);
            } else {
                StdDraw.line(n.point.x(), n.rect.ymax(), n.point.x(), n.rect.ymin());
            }
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmax(), n.point.y(), n.rect.xmin(), n.point.y());
        }
        drawNodes(n.left);
        drawNodes(n.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Argument is null");
        } else if (root == null) {
            return null;
        }
        Stack<Point2D> pointStack = new Stack<>();
        findPoints(root, pointStack, rect);
        return pointStack;
    }

    private void findPoints(Node n, Stack<Point2D> pointStack, RectHV rect) {
        if (n == null) {
            return;
        }
        if (rect.intersects(n.rect)) {
            if (rect.contains(n.point)) {
                pointStack.push(n.point);
            }
            findPoints(n.left, pointStack, rect);
            findPoints(n.right, pointStack, rect);

        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument is null");
        } else if (root == null) {
            return null;
        }
        Point2D closestPoint = findNearest(root, p, root.point);
        return closestPoint;
    }

    private Point2D findNearest(Node n, Point2D p, Point2D closestPoint) {
        if (n == null) {
            return closestPoint;
        }
        if (n.rect.distanceSquaredTo(p) < closestPoint.distanceSquaredTo(p)) {
            if (n.point.distanceSquaredTo(p) < closestPoint.distanceSquaredTo(p)) {
                closestPoint = n.point;
            }
            if (n.vertical) {
                if (n.point.x() < p.x()) {
                    closestPoint = findNearest(n.left, p, closestPoint);
                    closestPoint = findNearest(n.right, p, closestPoint);
                } else {
                    closestPoint = findNearest(n.right, p, closestPoint);
                    closestPoint = findNearest(n.left, p, closestPoint);
                }
            } else {
                if (n.point.y() < p.y()) {
                    closestPoint = findNearest(n.left, p, closestPoint);
                    closestPoint = findNearest(n.right, p, closestPoint);
                } else {
                    closestPoint = findNearest(n.right, p, closestPoint);
                    closestPoint = findNearest(n.left, p, closestPoint);
                }
            }
        }
        return closestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
