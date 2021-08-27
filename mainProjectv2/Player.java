package mainProjectv2;

public class Player implements Comparable<Player> {
	private int points = 0;
	private String name;
	
	public Player(){
		
	}
	
	public Player(String name) {
		this.name = name;
	}
	
	public void addPoints(int points) {
		this.points += points;
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public int compareTo(Player p) {
		int comparePoints = ((Player) p).getPoints();
		return comparePoints - this.points;
	}

}
