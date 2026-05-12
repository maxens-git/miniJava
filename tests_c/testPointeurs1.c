void main() {
    int x = 10;
    int y = 20;
    
    int* ptr = &x;
    print *ptr;

    *ptr = 30;
    print x;
    
    ptr = &y;
    print *ptr;
    
}
// Attendu : 10 30 20