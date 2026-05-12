typedef int Entier;
typedef <Entier, Entier> PaireEntier;

void main() {

    Entier i = 99;

    PaireEntier p = <i,42>;

    print fst (p);
}