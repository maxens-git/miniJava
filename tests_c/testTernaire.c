void main() {
    int i = 5;
    int a = (i > 5) ? 1 : 0;
    int b = (i < 5) ? 0 : 1;
    int c = (i < 0) ? 0 : (i == 0 ? 0 : (i < 10 ? 1 : 0));
    print c;
}