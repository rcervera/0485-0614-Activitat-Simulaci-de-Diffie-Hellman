package dh;

/**
 * Biblioteca de mètodes static per simular l'intercanvi de clau Diffie-Hellman
 * entre un client (navegador) i un servidor web.
 *
 * <p>Tots els mètodes són {@code static}: es criden amb el nom de la classe,
 * per exemple {@code EinesDH.esPrimer(23)}, sense crear cap objecte.</p>
 *
 * <p>Resum del protocol:</p>
 * <ol>
 *   <li>Es fixen dos números públics: un primer {@code p} i un generador {@code g}.</li>
 *   <li>El client tria un secret {@code a} i el servidor un secret {@code b}.</li>
 *   <li>El client envia {@code A = g^a mod p} i el servidor {@code B = g^b mod p}.</li>
 *   <li>El client calcula {@code K = B^a mod p} i el servidor {@code K = A^b mod p}:
 *       tots dos obtenen la mateixa clau de sessió.</li>
 * </ol>
 *
 * <p>Els números d'aquesta activitat són petits (primers de fins a 500) perquè
 * es puguin seguir a mà. A la realitat són de centenars de xifres.</p>
 *
 * @author Programació 0485
 * @version 1.0
 */
public class EinesDH {

    /**
     * Constructor privat: aquesta classe només conté mètodes static
     * i no té sentit crear-ne objectes.
     */
    private EinesDH() {
    }

    /**
     * Indica si un número és primer.
     *
     * <p>Un número és primer si és més gran que 1 i només es pot dividir
     * exactament per 1 i per ell mateix.</p>
     *
     * @param n el número que es vol comprovar
     * @return {@code true} si {@code n} és primer; {@code false} en cas contrari
     *         (inclosos 0, 1 i els negatius)
     */
    public static boolean esPrimer(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Mostra per pantalla, en una sola línia i separats per espais,
     * tots els números primers de l'interval [inici, fi].
     *
     * <p>Exemple: {@code mostrarPrimersEntre(10, 30)} mostra
     * {@code 11 13 17 19 23 29}.</p>
     *
     * @param inici primer valor de l'interval (inclòs)
     * @param fi    últim valor de l'interval (inclòs)
     */
    public static void mostrarPrimersEntre(int inici, int fi) {
        for (int i = inici; i <= fi; i++) {
            if (esPrimer(i)) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    /**
     * Calcula {@code base^exponent mod modul} sense desbordar l'{@code int}.
     *
     * <p>En lloc de calcular primer la potència sencera (que seria enorme),
     * aplica el mòdul després de cada multiplicació.</p>
     *
     * <p>Exemple: {@code potenciaModular(5, 4, 23)} retorna 4,
     * perquè 5^4 = 625 i 625 mod 23 = 4.</p>
     *
     * @param base     el número que s'eleva
     * @param exponent quantes vegades es multiplica la base (ha de ser &ge; 0)
     * @param modul    el mòdul (ha de ser &gt; 0)
     * @return el valor de {@code base^exponent mod modul}, entre 0 i {@code modul - 1}
     */
    public static int potenciaModular(int base, int exponent, int modul) {
        long resultat = 1 % modul;
        for (int i = 0; i < exponent; i++) {
            resultat = (resultat * base) % modul;
        }
        return (int) resultat;
    }

    /**
     * Indica si {@code g} és un generador del primer {@code p}.
     *
     * <p>{@code g} és generador si les potències {@code g^1, g^2, ..., g^(p-1)}
     * (mod p) donen tots els valors de 1 a p - 1. És equivalent a comprovar que
     * cap potència {@code g^k mod p} amb k entre 1 i p - 2 val 1.</p>
     *
     * <p>Exemples: {@code esGenerador(5, 23)} retorna {@code true};
     * {@code esGenerador(2, 23)} retorna {@code false} perquè 2^11 mod 23 = 1.</p>
     *
     * @param g el candidat a generador
     * @param p un número primer
     * @return {@code true} si {@code g} és generador de {@code p}
     */
    public static boolean esGenerador(int g, int p) {
        for (int k = 1; k < p - 1; k++) {
            if (potenciaModular(g, k, p) == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Mostra per pantalla, en una sola línia, tots els generadors
     * del primer {@code p} entre 2 i p - 2.
     *
     * <p>Exemple: {@code mostrarGeneradors(23)} mostra
     * {@code 5 7 10 11 14 15 17 19 20 21}.</p>
     *
     * @param p un número primer
     */
    public static void mostrarGeneradors(int p) {
        for (int g = 2; g < p - 1; g++) {
            if (esGenerador(g, p)) {
                System.out.print(g + " ");
            }
        }
        System.out.println();
    }

    /**
     * Calcula el número públic que s'envia per la xarxa:
     * {@code g^secret mod p}.
     *
     * <p>El client l'anomena A (amb el seu secret a) i el servidor B
     * (amb el seu secret b).</p>
     *
     * @param g      el generador públic
     * @param secret el número secret propi (entre 2 i p - 2)
     * @param p      el primer públic
     * @return el número públic que cal enviar
     */
    public static int calcularNumeroPublic(int g, int secret, int p) {
        return potenciaModular(g, secret, p);
    }

    /**
     * Calcula la clau de sessió compartida:
     * {@code numeroRebut^secret mod p}.
     *
     * <p>El client fa servir el B rebut i el seu secret a; el servidor fa
     * servir l'A rebut i el seu secret b. Tots dos obtenen el mateix resultat.</p>
     *
     * @param numeroRebut el número públic rebut de l'altra part
     * @param secret      el número secret propi
     * @param p           el primer públic
     * @return la clau de sessió K
     */
    public static int calcularClauSessio(int numeroRebut, int secret, int p) {
        return potenciaModular(numeroRebut, secret, p);
    }

    /**
     * Simula l'atac d'un espia: busca per força bruta l'exponent {@code x}
     * tal que {@code g^x mod p} sigui igual al número públic interceptat
     * (problema del logaritme discret).
     *
     * <p>Prova x = 1, 2, 3, ... fins a p - 2. Amb primers petits ho troba de
     * seguida; amb primers reals de centenars de xifres seria impracticable.</p>
     *
     * @param g            el generador públic
     * @param numeroPublic el número públic interceptat (A o B)
     * @param p            el primer públic
     * @return el secret trobat, o -1 si no n'hi ha cap
     */
    public static int trobarSecret(int g, int numeroPublic, int p) {
        for (int x = 1; x < p - 1; x++) {
            if (potenciaModular(g, x, p) == numeroPublic) {
                return x;
            }
        }
        return -1;
    }

    /**
     * Xifra un text amb el xifratge de Cèsar fent servir la clau de sessió.
     *
     * <p>Cada lletra majúscula (A-Z) es desplaça {@code clau % 26} posicions;
     * els altres caràcters (espais, signes, minúscules) no es modifiquen.</p>
     *
     * <p>Exemple: {@code xifrarCesar("HOLA", 18)} retorna {@code "ZGDS"}.</p>
     *
     * @param text el text original, en majúscules
     * @param clau la clau de sessió (&ge; 0)
     * @return el text xifrat
     */
    public static String xifrarCesar(String text, int clau) {
        String resultat = "";
        int desplacament = clau % 26;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c = (char) ('A' + (c - 'A' + desplacament) % 26);
            }
            resultat += c;
        }
        return resultat;
    }

    /**
     * Desxifra un text xifrat amb {@link #xifrarCesar(String, int)}.
     *
     * <p>Desplaçar enrere {@code clau % 26} posicions és el mateix que
     * desplaçar endavant {@code 26 - clau % 26}, així que reutilitza
     * el mètode de xifrar.</p>
     *
     * @param text el text xifrat
     * @param clau la mateixa clau de sessió que s'ha fet servir per xifrar
     * @return el text original
     */
    public static String desxifrarCesar(String text, int clau) {
        return xifrarCesar(text, 26 - clau % 26);
    }
}
