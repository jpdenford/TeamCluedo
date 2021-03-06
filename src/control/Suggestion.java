package control;

import game.*;

public class Suggestion {
	private Player player;
	private Envelope guessEnvelope;

	public Suggestion(Player player, Envelope guessEnvelope) {
		this.player = player;
		this.guessEnvelope = guessEnvelope;
	}

	public Envelope getGuess(){
		return guessEnvelope;
	}

	public String getSuggestedCharacter(){
		return guessEnvelope.characterToString();
	}

	public String getSuggestedWeapon(){
		return guessEnvelope.weaponToString();
	}

	public String getSuggestedRoom(){
		return guessEnvelope.roomToString();
	}

	/**
	 * Returns whether this is an accusation or not
	 * @return false - because this is parent class Suggestion
	 */
	public boolean isAccusation(){
		return false;
	}
}
