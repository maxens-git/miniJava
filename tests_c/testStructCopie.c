typedef struct Point {int x; int y;} Point;

void main() {
    Point p = {3, 7};
    Point q = {0, 0};
    q = p;
    print q.x;
    print q.y;
}
// Attendu : 3 7
