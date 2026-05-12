void main() {
    boolean t = true;
    boolean f = false;
    if (!(t && f) && (t || f)) { 
        print 1;
    }
    if (!t || !f) { 
        print 2; 
    }
    if (!(!t)) {
        print 3;
    }
}
// Attendu : 1 2 3