void main() {
    int tab[] = new int[5];
    int i = 0;
    while (i < 5) {
        tab[i] = i * i;
        i = i + 1;
    }

    i = 0;
    while (i < 5) {
        print tab[i];
        i = i + 1;
    }
}
// Attendu: 0 1 4 9 16