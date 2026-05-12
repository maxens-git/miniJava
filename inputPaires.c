void main() {

    // Paire
    int j = 0;
    <int, int> p = < j+1, 2>;
    //<int, int> q = p + 1;
    int i = fst(p);
    int k = snd(p);

    print i;
    print k;

    p = <i + 1, k>;

    i = fst(p);
    k = snd(p);

    print i;
    print k;
}
