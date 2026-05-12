void main() {
    int i = 1;
    int s = 0;
    while (i <= 20) {
        if (i % 2 == 0) {
            s = s + i;
        }
        i = i + 1;
    }
    print s;
}
// Attendu : 110