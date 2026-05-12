void main() {
    <<int, int>, int> couple = <<10, 20>, 30>;

    print snd (fst (couple));
    print snd (couple);
}