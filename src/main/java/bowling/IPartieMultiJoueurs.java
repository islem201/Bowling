package bowling;

/**
 * Interface pour une partie de bowling pour plusieurs joueurs
 */
public interface IPartieMultiJoueurs {

    public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException;

    public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException;

    public int scorePour(String nomDuJoueur) throws IllegalArgumentException;

}
