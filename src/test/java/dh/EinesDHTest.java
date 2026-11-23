package dh;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Proves unitàries dels mètodes de {@link EinesDH} que retornen un valor.
 *
 * <p>Per executar-les: {@code mvn test}. Cada classe interna ({@code @Nested})
 * agrupa les proves d'un mètode. Si un mètode encara no està implementat,
 * les seves proves fallaran: és normal.</p>
 *
 * @author Programació 0485
 * @version 1.0
 */
@DisplayName("Proves d'EinesDH")
class EinesDHTest {

    /** Proves de {@link EinesDH#esPrimer(int)}. */
    @Nested
    @DisplayName("esPrimer")
    class EsPrimer {

        @ParameterizedTest(name = "{0} és primer")
        @ValueSource(ints = {2, 3, 5, 7, 11, 13, 23, 97, 101, 467, 499})
        @DisplayName("Reconeix nombres primers")
        void primers(int n) {
            assertTrue(EinesDH.esPrimer(n));
        }

        @ParameterizedTest(name = "{0} no és primer")
        @ValueSource(ints = {4, 9, 15, 21, 25, 49, 91, 100, 143, 221, 500})
        @DisplayName("Rebutja nombres compostos")
        void compostos(int n) {
            assertFalse(EinesDH.esPrimer(n));
        }

        @ParameterizedTest(name = "{0} no és primer")
        @ValueSource(ints = {-7, -1, 0, 1})
        @DisplayName("0, 1 i els negatius no són primers")
        void casosLimit(int n) {
            assertFalse(EinesDH.esPrimer(n));
        }

        @Test
        @DisplayName("Hi ha exactament 25 primers entre 1 i 100")
        void comptarPrimersFins100() {
            int comptador = 0;
            for (int i = 1; i <= 100; i++) {
                if (EinesDH.esPrimer(i)) {
                    comptador++;
                }
            }
            assertEquals(25, comptador);
        }
    }

    /** Proves de {@link EinesDH#potenciaModular(int, int, int)}. */
    @Nested
    @DisplayName("potenciaModular")
    class PotenciaModular {

        @ParameterizedTest(name = "{0}^{1} mod {2} = {3}")
        @CsvSource({
            "5, 4, 23, 4",
            "5, 3, 23, 10",
            "10, 4, 23, 18",
            "4, 3, 23, 18",
            "2, 10, 1000, 24",
            "3, 5, 7, 5",
            "42, 7, 143, 81"
        })
        @DisplayName("Calcula valors coneguts")
        void valorsConeguts(int base, int exponent, int modul, int esperat) {
            assertEquals(esperat, EinesDH.potenciaModular(base, exponent, modul));
        }

        @Test
        @DisplayName("Qualsevol número elevat a 0 dona 1")
        void exponentZero() {
            assertEquals(1, EinesDH.potenciaModular(7, 0, 13));
        }

        @Test
        @DisplayName("Si la base és múltiple del mòdul, dona 0")
        void baseMultiple() {
            assertEquals(0, EinesDH.potenciaModular(46, 5, 23));
        }

        @ParameterizedTest(name = "{0}^{1} mod {2} = {3}")
        @CsvSource({
            "2, 153, 467, 224",
            "466, 466, 467, 1",
            "499, 499, 500, 499",
            "81, 103, 143, 42"
        })
        @DisplayName("No desborda amb exponents grans")
        void senseDesbordament(int base, int exponent, int modul, int esperat) {
            assertEquals(esperat, EinesDH.potenciaModular(base, exponent, modul));
        }
    }

    /** Proves de {@link EinesDH#esGenerador(int, int)}. */
    @Nested
    @DisplayName("esGenerador")
    class EsGenerador {

        @ParameterizedTest(name = "{0} és generador de {1}")
        @CsvSource({"2, 11", "6, 11", "5, 23", "7, 23", "21, 23", "2, 467", "5, 467"})
        @DisplayName("Reconeix generadors")
        void generadors(int g, int p) {
            assertTrue(EinesDH.esGenerador(g, p));
        }

        @ParameterizedTest(name = "{0} no és generador de {1}")
        @CsvSource({"3, 11", "2, 23", "4, 23", "22, 23", "3, 467"})
        @DisplayName("Rebutja els que no són generadors")
        void noGeneradors(int g, int p) {
            assertFalse(EinesDH.esGenerador(g, p));
        }

        @Test
        @DisplayName("El primer 23 té exactament 10 generadors entre 2 i 21")
        void comptarGeneradors23() {
            int comptador = 0;
            for (int g = 2; g < 22; g++) {
                if (EinesDH.esGenerador(g, 23)) {
                    comptador++;
                }
            }
            assertEquals(10, comptador);
        }
    }

    /** Proves de {@link EinesDH#calcularNumeroPublic(int, int, int)}. */
    @Nested
    @DisplayName("calcularNumeroPublic")
    class CalcularNumeroPublic {

        @ParameterizedTest(name = "g={0}, secret={1}, p={2} -> {3}")
        @CsvSource({
            "5, 4, 23, 4",
            "5, 3, 23, 10",
            "2, 153, 467, 224",
            "2, 97, 467, 82"
        })
        @DisplayName("Calcula el número públic (A o B)")
        void valorsConeguts(int g, int secret, int p, int esperat) {
            assertEquals(esperat, EinesDH.calcularNumeroPublic(g, secret, p));
        }

        @Test
        @DisplayName("El resultat sempre està entre 1 i p - 1")
        void dinsDelRang() {
            for (int secret = 2; secret <= 21; secret++) {
                int numero = EinesDH.calcularNumeroPublic(5, secret, 23);
                assertTrue(numero >= 1 && numero <= 22, "secret " + secret + " -> " + numero);
            }
        }
    }

    /** Proves de {@link EinesDH#calcularClauSessio(int, int, int)}. */
    @Nested
    @DisplayName("calcularClauSessio")
    class CalcularClauSessio {

        @ParameterizedTest(name = "rebut={0}, secret={1}, p={2} -> K={3}")
        @CsvSource({
            "10, 4, 23, 18",
            "4, 3, 23, 18",
            "82, 153, 467, 194",
            "224, 97, 467, 194"
        })
        @DisplayName("Calcula la clau de sessió")
        void valorsConeguts(int rebut, int secret, int p, int esperat) {
            assertEquals(esperat, EinesDH.calcularClauSessio(rebut, secret, p));
        }

        @Test
        @DisplayName("Client i servidor obtenen la mateixa K per a tots els secrets (p=23, g=5)")
        void mateixaClauSempre() {
            int p = 23;
            int g = 5;
            for (int a = 2; a <= p - 2; a++) {
                for (int b = 2; b <= p - 2; b++) {
                    int numA = EinesDH.calcularNumeroPublic(g, a, p);
                    int numB = EinesDH.calcularNumeroPublic(g, b, p);
                    int clauClient = EinesDH.calcularClauSessio(numB, a, p);
                    int clauServidor = EinesDH.calcularClauSessio(numA, b, p);
                    assertEquals(clauClient, clauServidor, "a=" + a + ", b=" + b);
                }
            }
        }
    }

    /** Proves de {@link EinesDH#trobarSecret(int, int, int)}. */
    @Nested
    @DisplayName("trobarSecret (espia)")
    class TrobarSecret {

        @ParameterizedTest(name = "g={0}, públic={1}, p={2} -> secret {3}")
        @CsvSource({
            "5, 4, 23, 4",
            "5, 10, 23, 3",
            "2, 224, 467, 153",
            "2, 82, 467, 97"
        })
        @DisplayName("Troba el secret a partir del número públic")
        void valorsConeguts(int g, int numeroPublic, int p, int esperat) {
            assertEquals(esperat, EinesDH.trobarSecret(g, numeroPublic, p));
        }

        @Test
        @DisplayName("Recupera qualsevol secret entre 1 i p - 2 (p=23, g=5)")
        void recuperaTotsElsSecrets() {
            for (int secret = 1; secret <= 21; secret++) {
                int numeroPublic = EinesDH.calcularNumeroPublic(5, secret, 23);
                assertEquals(secret, EinesDH.trobarSecret(5, numeroPublic, 23));
            }
        }

        @Test
        @DisplayName("Retorna -1 si cap potència dona aquest número")
        void noTrobat() {
            // Les potències de 2 mòdul 23 són 2, 4, 8, 16, 9, 18, 13, 3, 6, 12, 1: el 5 no hi és
            assertEquals(-1, EinesDH.trobarSecret(2, 5, 23));
        }
    }

    /** Proves de {@link EinesDH#xifrarCesar(String, int)}. */
    @Nested
    @DisplayName("xifrarCesar")
    class XifrarCesar {

        @ParameterizedTest(name = "\"{0}\" amb clau {1} -> \"{2}\"")
        @CsvSource({
            "HOLA, 18, ZGDS",
            "HOLA CLIENT, 18, ZGDS UDAWFL",
            "ABC, 1, BCD",
            "XYZ, 3, ABC",
            "ABC, 29, DEF",
            "ABC, 0, ABC",
            "ABC, 26, ABC"
        })
        @DisplayName("Desplaça les lletres majúscules")
        void valorsConeguts(String text, int clau, String esperat) {
            assertEquals(esperat, EinesDH.xifrarCesar(text, clau));
        }

        @Test
        @DisplayName("No modifica espais, signes ni minúscules")
        void altresCaracters() {
            assertEquals("IPMB, NPO! hola 123", EinesDH.xifrarCesar("HOLA, MON! hola 123", 1));
        }

        @Test
        @DisplayName("Un text buit continua buit")
        void textBuit() {
            assertEquals("", EinesDH.xifrarCesar("", 5));
        }
    }

    /** Proves de {@link EinesDH#desxifrarCesar(String, int)}. */
    @Nested
    @DisplayName("desxifrarCesar")
    class DesxifrarCesar {

        @ParameterizedTest(name = "\"{0}\" amb clau {1} -> \"{2}\"")
        @CsvSource({
            "ZGDS, 18, HOLA",
            "ZGDS UDAWFL, 18, HOLA CLIENT",
            "ABC, 3, XYZ",
            "DEF, 29, ABC",
            "ABC, 26, ABC"
        })
        @DisplayName("Recupera el text original")
        void valorsConeguts(String text, int clau, String esperat) {
            assertEquals(esperat, EinesDH.desxifrarCesar(text, clau));
        }

        @ParameterizedTest(name = "clau {0}")
        @ValueSource(ints = {0, 1, 13, 18, 25, 26, 194, 466})
        @DisplayName("Xifrar i desxifrar torna el text original")
        void anadaITornada(int clau) {
            String original = "EL SERVIDOR ESTA LLEST";
            String xifrat = EinesDH.xifrarCesar(original, clau);
            assertEquals(original, EinesDH.desxifrarCesar(xifrat, clau));
        }
    }
}
