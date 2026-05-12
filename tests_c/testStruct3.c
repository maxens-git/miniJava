typedef struct Point {int x; int y;} Point;
typedef struct Segment {Point ext1; Point ext2;} Segment;

void main() {
    Segment s = {{0,1},{2,3}};
    
    print s.ext1.x;
}