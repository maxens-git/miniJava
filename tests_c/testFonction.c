int incr(int j) {
    int res = j + 1;
    return res;
}

int decr(int i) {
    return i - 1;
}

void main() {
    int i = 3;
    int a = incr(i);
    print a;
    print decr(i);
}

