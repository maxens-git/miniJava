void main() {
    int i = 0;
    int j = 20;
    while ((i < j) && (i + j < 30)) {
        i = i + 2;
        j = j - 1;
        print i;
        print j;
    }
}
// Attendu : 2 19 4 18 6 17 8 16 10 15 12 14 14 13