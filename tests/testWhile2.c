void main() {
    int i = 0;
    int j = 0;
    while (i < 10) {
        i = i + 1;
        while (j < 10) {
            j = j + 1;
            if (i % 2 == 0) {
                print i;
            }
        }
        j = 0;
    }
}
// Attendu : 2222222222 4444444444 6666666666 8888888888 10101010101010101010