void main() {
    int a = 221;
    int b = 782;
    while (b != 0) {
        int r = a % b;
        a = b;
        b = r;
    }
    print a;
}
// Attendu : 221, 782 -> 17