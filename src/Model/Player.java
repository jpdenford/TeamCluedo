package model;

import java.util.*;

/**
 * A class representing a Player in the game Cluedo
 * 
 * @author Kieran Mckay
 *
 */
public class Player {
	private Token myCharacter;
	private Set<Card> hand;
	private Set<Card> suspects;

	/**
	 * Create a new player card.
	 *
	 * @param - Token the character belonging to this player.
	 */
	public Player(Token myCharacter) {
		this.myCharacter = myCharacter;
		this.hand = new HashSet<Card>();
		this.suspects = new HashSet<Card>();

		//TODO populate suspects with all possible weapons, rooms and characters
	}

	
	public Tile getPosition(){
		return myCharacter.getLocation();
	}
	
	/**
	 * Returns a set representing this players hand of cards.
	 *
	 * @return hand - Set of type Card.
	 */
	public Set<Card> getHand() {
		return hand;
	}

	/**
	 * Adds a card to this players hand.
	 *
	 * @param card
	 *            - Card.
	 */
	public void addCard(Card card) {
		hand.add(card);
	}
	
	/**
	 * Removes a given card from a players hand
	 * @param card
	 */
	public void removeCard(Card card){
		//not sure if this method is required yet
		hand.remove(card);
	}

	/**
	 * Get this players Token
	 * 
	 * @return the myCharacter
	 */
	public Token getMyCharacter() {
		return myCharacter;
	}
	
	
}