void main() {
    int n = 0;
    int i = -3;
    while (i <= 3) {
        if (i < 0) {
            n = n - 1;
        } else {
            if (i == 0) {
                n = n + 0;
            } else {
                n = n + 1;
            }
        }
        i = i + 1;
    }
    print n;
}
// Attentu : 0