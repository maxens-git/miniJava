void main() {
    int j = 0;
    int i = 10;
    boolean a = (i == 0) && (j == 0);
    print a;
    boolean b = (i == 0) || (j == 0);
    print b;
    int k = ((i == 10) && (j == 0)) ? 1 : 0;
    print k;
    int l = ((i == 10) && (j < 0)) ? 1 : 0;
    print l;
}
// Attendu : 0 1 1 0