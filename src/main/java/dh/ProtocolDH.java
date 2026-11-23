package dh;

import java.util.Scanner;

/**
 * Programa principal amb un menú per simular, per parelles,
 * l'intercanvi de clau Diffie-Hellman entre client i servidor.
 *
 * <p>Cada opció del menú demana els números per teclat i crida els mètodes
 * de la classe {@link EinesDH}. El client i el servidor fan servir el mateix
 * programa: el que canvia són els números que hi introdueixen.</p>
 *
 * @author Programació 0485
 * @version 1.0
 */
public class ProtocolDH {

    /**
     * Constructor privat: aquesta classe només conté mètodes static.
     */
    private ProtocolDH() {
    }

    /**
     * Punt d'entrada del programa. Mostra el menú fins que l'usuari tria 0.
     *
     * @param args no s'utilitzen
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcio;
        do {
            mostrarMenu();
            opcio = sc.nextInt();
            switch (opcio) {
                case 1 -> opcioPrimers(sc);
                case 2 -> opcioGeneradors(sc);
                case 3 -> opcioNumeroPublic(sc);
                case 4 -> opcioClauSessio(sc);
                case 5 -> opcioEspia(sc);
                case 6 -> opcioMissatge(sc, true);
                case 7 -> opcioMissatge(sc, false);
                case 0 -> System.out.println("Fins aviat!");
                default -> System.out.println("Opció no vàlida.");
            }
        } while (opcio != 0);
        sc.close();
    }

    /**
     * Mostra les opcions del menú principal.
     */
    public static void mostrarMenu() {
        System.out.println();
        System.out.println("=== SIMULADOR DIFFIE-HELLMAN ===");
        System.out.println("1. Veure primers d'un interval");
        System.out.println("2. Veure generadors d'un primer p");
        System.out.println("3. Calcular el meu número públic");
        System.out.println("4. Calcular la clau de sessió");
        System.out.println("5. Espia: trobar un secret per força bruta");
        System.out.println("6. Xifrar un missatge amb la clau de sessió");
        System.out.println("7. Desxifrar un missatge amb la clau de sessió");
        System.out.println("0. Sortir");
        System.out.print("Opció: ");
    }

    /**
     * Opció 1: demana un interval i mostra els primers que conté.
     *
     * @param sc el {@link Scanner} per llegir del teclat
     */
    public static void opcioPrimers(Scanner sc) {
        System.out.print("Inici: ");
        int inici = sc.nextInt();
        System.out.print("Fi: ");
        int fi = sc.nextInt();
        EinesDH.mostrarPrimersEntre(inici, fi);
    }

    /**
     * Opció 2: demana un primer p i mostra els seus generadors.
     * Si p no és primer, mostra un missatge d'error.
     *
     * @param sc el {@link Scanner} per llegir del teclat
     */
    public static void opcioGeneradors(Scanner sc) {
        System.out.print("Primer p: ");
        int p = sc.nextInt();
        if (!EinesDH.esPrimer(p)) {
            System.out.println("Error: " + p + " no és primer.");
            return;
        }
        EinesDH.mostrarGeneradors(p);
    }

    /**
     * Opció 3: demana p, g i el secret propi, i mostra el número públic
     * que cal escriure al cable (A per al client, B per al servidor).
     *
     * @param sc el {@link Scanner} per llegir del teclat
     */
    public static void opcioNumeroPublic(Scanner sc) {
        System.out.print("p: ");
        int p = sc.nextInt();
        System.out.print("g: ");
        int g = sc.nextInt();
        System.out.print("El meu secret (entre 2 i p-2): ");
        int secret = sc.nextInt();
        System.out.println("Número públic a enviar: " + EinesDH.calcularNumeroPublic(g, secret, p));
    }

    /**
     * Opció 4: demana p, el número públic rebut i el secret propi,
     * i mostra la clau de sessió K.
     *
     * @param sc el {@link Scanner} per llegir del teclat
     */
    public static void opcioClauSessio(Scanner sc) {
        System.out.print("p: ");
        int p = sc.nextInt();
        System.out.print("Número públic rebut: ");
        int rebut = sc.nextInt();
        System.out.print("El meu secret: ");
        int secret = sc.nextInt();
        System.out.println("Clau de sessió K = " + EinesDH.calcularClauSessio(rebut, secret, p));
    }

    /**
     * Opció 5 (espia): demana p, g i un número públic interceptat,
     * i intenta trobar el secret per força bruta.
     *
     * @param sc el {@link Scanner} per llegir del teclat
     */
    public static void opcioEspia(Scanner sc) {
        System.out.print("p: ");
        int p = sc.nextInt();
        System.out.print("g: ");
        int g = sc.nextInt();
        System.out.print("Número públic interceptat: ");
        int numeroPublic = sc.nextInt();
        int secret = EinesDH.trobarSecret(g, numeroPublic, p);
        if (secret == -1) {
            System.out.println("No s'ha trobat cap secret.");
        } else {
            System.out.println("Secret trobat: " + secret);
        }
    }

    /**
     * Opcions 6 i 7: xifra o desxifra un text amb Cèsar fent servir
     * la clau de sessió.
     *
     * @param sc     el {@link Scanner} per llegir del teclat
     * @param xifrar {@code true} per xifrar; {@code false} per desxifrar
     */
    public static void opcioMissatge(Scanner sc, boolean xifrar) {
        System.out.print("Clau de sessió K: ");
        int clau = sc.nextInt();
        sc.nextLine();
        System.out.print("Text (MAJÚSCULES): ");
        String text = sc.nextLine();
        if (xifrar) {
            System.out.println("Xifrat: " + EinesDH.xifrarCesar(text, clau));
        } else {
            System.out.println("Desxifrat: " + EinesDH.desxifrarCesar(text, clau));
        }
    }
}
