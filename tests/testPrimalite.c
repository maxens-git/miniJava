void main() {
    int n = 2301;
    int d = 2;
    boolean prime = (n > 1);
    while ((d * d <= n) && prime) {
        if (n % d == 0) { 
            prime = false; 
        }
        d = d + 1;
    }
    if (prime) { 
        print 1; 
    } else { 
        print 0; 
    }
}
// Attendu : 3853 -> 1, 3203 -> 1, 2301 -> 0