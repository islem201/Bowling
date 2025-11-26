package bowling;

/**
 * Cette classe a pour but d'enregistrer le nombre de quilles abattues lors des
 * lancers successifs d'<b>un seul et même</b> joueur, et de calculer le score
 * final de ce joueur
 */
public class PartieMonoJoueur {

	/**
	 * Constructeur
	 */
	public PartieMonoJoueur() {
	}

	/**
	 * Cette méthode doit être appelée à chaque lancer de boule
	 *
	 * @param nombreDeQuillesAbattues le nombre de quilles abattues lors de ce lancer
	 * @throws IllegalStateException si la partie est terminée
	 * @return vrai si le joueur doit lancer à nouveau pour continuer son tour, faux sinon	
	 */
	public boolean enregistreLancer(int nombreDeQuillesAbattues) {
		if (estTerminee()) {
			throw new IllegalStateException("La partie est terminée");
		}
		rolls[currentRoll++] = nombreDeQuillesAbattues;
		// after recording, determine next ball: if next ball is still in same frame, return true
		int nextFrame = computeCurrentFrame(currentRoll);
		if (nextFrame == 0) {
			return false;
		}
		int nextBall = computeNextBall(currentRoll);
		return nextBall != 1;
	}

	/**
	 * Cette méthode donne le score du joueur.
	 * Si la partie n'est pas terminée, on considère que les lancers restants
	 * abattent 0 quille.
	 * @return Le score du joueur
	 */
	public int score() {
		int score = 0;
		int rollIndex = 0;
		for (int frame = 0; frame < 10; frame++) {
			if (isStrike(rollIndex)) { // strike
				score += 10 + safeRoll(rollIndex + 1) + safeRoll(rollIndex + 2);
				rollIndex += 1;
			} else if (isSpare(rollIndex)) {
				score += 10 + safeRoll(rollIndex + 2);
				rollIndex += 2;
			} else {
				score += safeRoll(rollIndex) + safeRoll(rollIndex + 1);
				rollIndex += 2;
			}
		}
		return score;
	}

	/**
	 * @return vrai si la partie est terminée pour ce joueur, faux sinon
	 */
	public boolean estTerminee() {
		return computeCurrentFrame(currentRoll) == 0;
	}


	/**
	 * @return Le numéro du tour courant [1..10], ou 0 si le jeu est fini
	 */
	public int numeroTourCourant() {
		return computeCurrentFrame(currentRoll);
	}

	/**
	 * @return Le numéro du prochain lancer pour tour courant [1..3], ou 0 si le jeu
	 *         est fini
	 */
	public int numeroProchainLancer() {
		if (estTerminee()) {
			return 0;
		}
		return computeNextBall(currentRoll);
	}

	// ----- Implementation details -----
	private int[] rolls = new int[21 + 3]; // extra space for final bonuses
	private int currentRoll = 0;

	private int safeRoll(int index) {
		if (index < 0 || index >= currentRoll) {
			return 0;
		}
		if (index >= rolls.length) {
			return 0;
		}
		return rolls[index];
	}

	private boolean isStrike(int rollIndex) {
		return rollIndex < currentRoll && rolls[rollIndex] == 10;
	}

	private boolean isSpare(int rollIndex) {
		return rollIndex + 1 < currentRoll && (rolls[rollIndex] + rolls[rollIndex + 1] == 10);
	}

	/**
	 * Compute the current frame number (1..10) for a given roll count. Returns 0 when game finished.
	 */
	private int computeCurrentFrame(int uptoRoll) {
		int rollIndex = 0;
		for (int frame = 1; frame <= 9; frame++) {
			if (rollIndex >= uptoRoll) {
				return frame;
			}
			// at least one roll in this frame recorded
			int first = rolls[rollIndex];
			if (first == 10) {
				// strike consumes one roll
				rollIndex += 1;
				continue;
			}
			// not a strike: check if only first ball recorded
			if (rollIndex + 1 >= uptoRoll) {
				return frame;
			}
			rollIndex += 2;
		}
		// 10th frame
		// count how many rolls recorded in 10th frame so far
		int recordedInTenth = Math.max(0, uptoRoll - rollIndex);
		if (recordedInTenth == 0) {
			return 10;
		}
		// if there are at least 2 rolls and they are not spare/strike combination that gives bonus, need to check termination
		if (recordedInTenth == 1) {
			return 10;
		}
		if (recordedInTenth == 2) {
			int first = rolls[rollIndex];
			int second = rolls[rollIndex + 1];
			if (first == 10 || first + second == 10) {
				return 10; // still in 10th with bonus roll pending
			} else {
				return 0; // finished
			}
		}
		// recordedInTenth >=3
		return 0;
	}

	/**
	 * Compute next ball number (1..3) in current frame for given roll count.
	 */
	private int computeNextBall(int uptoRoll) {
		int rollIndex = 0;
		for (int frame = 1; frame <= 9; frame++) {
			if (rollIndex >= uptoRoll) {
				return 1;
			}
			int first = rolls[rollIndex];
			if (first == 10) {
				// strike: frame ends, next roll is frame+1 first ball
				rollIndex += 1;
				continue;
			}
			// not a strike
			if (rollIndex + 1 >= uptoRoll) {
				return 2;
			}
			rollIndex += 2;
		}
		// 10th frame
		int recordedInTenth = Math.max(0, uptoRoll - rollIndex);
		if (recordedInTenth == 0) {
			return 1;
		}
		if (recordedInTenth == 1) {
			return 2;
		}
		if (recordedInTenth == 2) {
			int first = rolls[rollIndex];
			int second = rolls[rollIndex + 1];
			if (first == 10 || first + second == 10) {
				return 3;
			} else {
				return 1; // game finished but higher-level will map to 0
			}
		}
		return 1;
	}

}
