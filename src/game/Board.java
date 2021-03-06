package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

import control.Game;


/**
 * A class representing the Board in the game Cluedo
 *
 * @author Johnny Denford, Kieran Mckay
 *
 */
public class Board {

	private int BOARD_WIDTH;
	private int BOARD_HEIGHT;

	private Tile board[][];
	private List<Tile> spawns = new ArrayList<Tile>();

	/**
	 * constructs new board given csv file
	 * @param boardFile
	 */
	public Board() {
		try {
			Scanner sc = new Scanner(new File("src/board.csv"));
			parseBoard(csvToArray(sc));
		} catch (FileNotFoundException e) {
			System.err.println("Invalid Board File");
			e.printStackTrace();
		}
	}

	/**
	 * returns a Tile given the x and y coordinates
	 * null if no tile present
	 * @param x
	 * @param y
	 * @return
	 */
	public Tile getTile(int x, int y){
		if(x < BOARD_WIDTH && x > -1 && y < BOARD_HEIGHT && y > -1 ){
			return board[y][x];
		}
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Returns the next available starting point for a character.
	 *
	 * @return Tile - next spawn point for characters which is not taken
	 */
	public Tile getFreeSpawn(){
		for ( Tile t : spawns){
			if (t.hasSpace()){
				return t;
			}
		}
		return null;
	}

	/**
	 * Add a token to the given coordinate
	 * @param token Token to add
	 * @param x x position
	 * @param y y position
	 * @return true if token placed successfully
	 */
	public boolean placeToken(Token token,int x, int y){
		Tile tile = getTile(x,y);
		if(tile == null || token == null)return false;
		tile.addToken(token);
		token.setLocation(tile);
		return true;
	}

	/**
	 * Adds a tile puts a token on a tile
	 * @param tile Tile to add the token to
	 * @param token Token to be added to the tile
	 */
	public void addToken(Tile tile, Token token){
		token.setLocation(tile);
		tile.addToken(token);
	}

	void parseBoard(String[][] stringBoard){

		Map<String, Room> rooms = new HashMap<String, Room>();
		board = new Tile[BOARD_HEIGHT][BOARD_WIDTH];

		for (int i = 0; i < stringBoard.length; i++) {
			for (int j = 0; j < stringBoard[i].length; j++) {
				String stringVal = stringBoard[i][j];
				if(stringVal == null || stringVal.isEmpty()){
					continue;
				}
				if(stringVal.charAt(0) == 'l'){ //is a location
					board[i][j] = new Location(j,i-1);
					if(stringVal.contains("char"))spawns.add(board[i][j]); //is a character spawn point
				}
				else if(Pattern.matches("[a-z,A-Z, ]+-+\\d-*[a-z,A-Z, ]*",stringVal)){//has a digit, must be a room
				String[] roomDetails = stringVal.split("-");
					if(!rooms.containsKey(roomDetails[0])){ //the map doesn't contain this room
						rooms.put(roomDetails[0],Game.rooms.get(roomDetails[0]));
						if(roomDetails.length > 2){ //the room has a teleport room
							String teleRoom = roomDetails[2]; //the name of the room to teleport to
							if(!rooms.containsKey(teleRoom)){ //the teleport room doesnt exist yet

								//rooms.put(teleRoom,new Room(teleRoom,-1,-1)); //add the new room to the map //TODO remove this
								Game.rooms.put(teleRoom, Game.rooms.get(teleRoom));
							}
							Game.rooms.get(roomDetails[0]).addNeighbour("Teleport to "+ teleRoom, Game.rooms.get(teleRoom)); //connect this room to the teleroom
						}
					}
					Door newDoor = new Door(roomDetails[1],j,i-1);
					Room neighbourRoom = rooms.get(roomDetails[0]);
					newDoor.room = neighbourRoom;
					neighbourRoom.addNeighbour(roomDetails[1],newDoor); //add the door with the door number to the room //TODO add "door: to roomdetails arg
					board[i][j] = newDoor;
				}
			}
		}

		//Go through the board linking neighbouring tiles
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j] != null ){
					if(i > 0 && board[i-1][j] != null){ //dont go over the edge
						if((board[i][j] instanceof Door && !stringBoard[i-1][j].contains("@")&& !(board[i-1][j] instanceof Door))
								|| (board[i][j] instanceof Location && !stringBoard[i][j].contains("@") && board[i-1][j] instanceof Door)
									|| (board[i][j] instanceof Location && board[i-1][j] instanceof Location)){
							board[i][j].addNeighbour("North", board[i-1][j]);
						}
					}
					if(i < board.length-1 && (board[i+1][j] != null)){
						if((board[i][j] instanceof Door && !stringBoard[i+1][j].contains("@")&& !(board[i+1][j] instanceof Door))
								|| (board[i][j] instanceof Location && !stringBoard[i][j].contains("@") && board[i+1][j] instanceof Door)
									|| (board[i][j] instanceof Location && board[i+1][j] instanceof Location)){
							board[i][j].addNeighbour("South", board[i+1][j]);
						}
					}
					if(j > 0 && board[i][j-1] != null){ //dont go over the edge
						if((board[i][j] instanceof Door && !stringBoard[i][j-1].contains("@")&& !(board[i][j-1] instanceof Door))
								|| (board[i][j] instanceof Location && !stringBoard[i][j].contains("@") && board[i][j-1] instanceof Door)
									|| (board[i][j] instanceof Location && board[i][j-1] instanceof Location)){
							board[i][j].addNeighbour("West", board[i][j-1]);
						}
					}
					if(j < board[i].length-1 && board[i][j+1] != null){
						if((board[i][j] instanceof Door && !stringBoard[i][j+1].contains("@") && !(board[i][j+1] instanceof Door)) ||
								(board[i][j] instanceof Location && !stringBoard[i][j].contains("@") && board[i][j+1] instanceof Door)
									||(board[i][j] instanceof Location && board[i][j+1] instanceof Location)){
							board[i][j].addNeighbour("East", board[i][j+1]);
						}
					}
				}
			}
		}
	}


	private String stringArray(String[][] stringArray) {
		String string = "";
		for (int i = 0; i < stringArray.length; i++) {
			for (int j = 0; j < stringArray[i].length; j++) {
				if(!stringArray[i][j].equals("")){
					string+= stringArray[i][j];
				}else{
					string+= " ";
				}
				string+= '\t';
			}
			string+= '\n';
		}
		return string;
	}

	/**
	 * Pull csv board into 2D String Array
	 *
	 * @param sc
	 *            Scanner with csv file
	 * @return 2D string array from CSV
	 */
	private String[][] csvToArray(Scanner sc) {
		this.BOARD_WIDTH = sc.nextInt();
		this.BOARD_HEIGHT = sc.nextInt();

		String line = sc.nextLine();
		String initialBoard[][] = new String[BOARD_HEIGHT][BOARD_WIDTH];

		for (int i = 0; i < initialBoard.length; i++) { // scan lines vertically
			String lineTiles[] = line.split(",");
			initialBoard[i] = lineTiles;
			line = sc.nextLine();
		}
		return initialBoard;
	}



	public int width(){
		return BOARD_WIDTH;
	}

	public int height(){
		return BOARD_HEIGHT;
	}

	/**
	 * fail parsing
	 * @param row
	 *
	 * @param col
	 */
	private void fail(int row, int col, String data) {
		throw new RuntimeException("Parse Exception: " + data + "@ row: " + row
				+ " col: " + col);
	}
}