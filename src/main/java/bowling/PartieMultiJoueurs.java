package bowling;

import java.util.ArrayList;
import java.util.List;

public class PartieMultiJoueurs implements IPartieMultiJoueurs {

    private String[] playerNames;
    private List<PartieMonoJoueur> games;
    private int currentPlayer = 0;
    private boolean started = false;

    @Override
    public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException {
        if (nomsDesJoueurs == null || nomsDesJoueurs.length == 0) {
            throw new IllegalArgumentException("Liste de joueurs vide");
        }
        this.playerNames = nomsDesJoueurs.clone();
        this.games = new ArrayList<>();
        for (int i = 0; i < playerNames.length; i++) {
            games.add(new PartieMonoJoueur());
        }
        currentPlayer = 0;
        started = true;
        return formatNextTurnOrFinished();
    }

    @Override
    public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {
        if (!started) {
            throw new IllegalStateException("La partie n'est pas démarrée");
        }
        // if all finished
        if (allFinished()) {
            return "Partie terminée";
        }

        PartieMonoJoueur current = games.get(currentPlayer);
        boolean continues = current.enregistreLancer(nombreDeQuillesAbattues);

        // if player's frame finished after this shot, move to next non-finished player
        if (!continues || current.estTerminee()) {
            advanceToNextPlayer();
        }

        if (allFinished()) {
            return "Partie terminée";
        }
        return formatNextTurnOrFinished();
    }

    @Override
    public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
        if (playerNames == null) {
            throw new IllegalArgumentException("Partie non initialisée");
        }
        for (int i = 0; i < playerNames.length; i++) {
            if (playerNames[i].equals(nomDuJoueur)) {
                return games.get(i).score();
            }
        }
        throw new IllegalArgumentException("Joueur inconnu");
    }

    private boolean allFinished() {
        for (PartieMonoJoueur p : games) {
            if (!p.estTerminee()) {
                return false;
            }
        }
        return true;
    }

    private void advanceToNextPlayer() {
        int attempts = 0;
        do {
            currentPlayer = (currentPlayer + 1) % games.size();
            attempts++;
        } while (attempts <= games.size() && games.get(currentPlayer).estTerminee());
    }

    private String formatNextTurnOrFinished() {
        if (allFinished()) {
            return "Partie terminée";
        }
        PartieMonoJoueur p = games.get(currentPlayer);
        String name = playerNames[currentPlayer];
        int tour = p.numeroTourCourant();
        int boule = p.numeroProchainLancer();
        return String.format("Prochain tir : joueur %s, tour n° %d, boule n° %d", name, tour, boule);
    }

}
