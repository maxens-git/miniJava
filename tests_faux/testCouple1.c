void main() {
    <<int, int>, int> couple = <<10, 20>, 30>;

    print fst (snd (couple)); // snd (couple) n'est pas un couple
}