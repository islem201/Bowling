package bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MultiPlayerGameTest {

    private IPartieMultiJoueurs partie;

    @BeforeEach
    void setUp() {
        partie = new PartieMultiJoueurs();
    }

    @Test
    void examplePlay() {
        String[] players = { "Pierre", "Paul" };
        String s = partie.demarreNouvellePartie(players);
        assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 1", s);

        s = partie.enregistreLancer(5);
        assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2", s);

        s = partie.enregistreLancer(3);
        assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 1", s);

        s = partie.enregistreLancer(10);
        assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", s);

        s = partie.enregistreLancer(7);
        assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 2", s);

        s = partie.enregistreLancer(3);
        assertEquals("Prochain tir : joueur Paul, tour n° 2, boule n° 1", s);

        assertEquals(18, partie.scorePour("Pierre"));
        assertEquals(10, partie.scorePour("Paul"));
        assertThrows(IllegalArgumentException.class, () -> partie.scorePour("Jacques"));
    }

    @Test
    void invalidStart() {
        assertThrows(IllegalArgumentException.class, () -> partie.demarreNouvellePartie(new String[0]));
        assertThrows(IllegalArgumentException.class, () -> partie.demarreNouvellePartie(null));
    }

}
