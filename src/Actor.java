import java.awt.Color;
import java.awt.Graphics2D;

/**
 * 
 * Inheritance changes: Actor is a Game Objet
 * 	- Actor has a Transform or RigiBody.
 * 	- Actor cannot move
 *  - Rigid bodies and transforms can move.
 *  -Actor moves based on transform. When transform is called to move, the actor's locaiton is updated?
 */

public class Actor extends GameObject{
	
	public Sprite actor_sprite;
	private Color sprite_color = Color.PINK;
	public Graphics2D actor_graphics;
	
	private Actor instanceOfActorInTable;
	private int actor_index = GameWorld.actor_list.size();
	private int actor_width = 10;
	private int actor_height = 10;
	private int actor_avg_width = actor_width / 2;
	private int actor_avg_height = actor_height / 2;
	private int converted_pos_x = 0;
	private int converted_pos_y = 0;
	
	private boolean has_collision = false;
	public boolean is_visible = false;
	
	
	/**
	 * Constructs a Actor object with 'debug' position and sprite.
	 * The default 'Sprite' is 10x10 box with the color Pink
	 * @param The String name of the actor. Used for searching through the Array list of actors.
	 */
	
	Actor(String name) {
		super(name);
		actor_sprite = null;
		GameWorld.actor_list.add(this);
	}
	
	
	/**
	 * Constructs an Actor object with the specified position.
	 * @param x The X position of the Actor.
	 * @param y The Y position of the Actor.
	 * @param name the name of the Actor.
	 */
	
	Actor(double x, double y, String name) {
		super(x,y,name);
		GameWorld.game_obj_table.put(this.name, this);

	}
	
	
	/**
	 * Constructs an Actor object with a specified transform.
	 * @param transform The transform of the Actor. Through which the Actor's velocity and position 
	 * @param name The string name of the actor. Used for searching through the Array list of actors.
	 * is manipulated and retrieved.
	 */
	
	Actor(Transform transform, String name){
		super(name, transform);
		transform.setMentor(this);
		transform.setX(transform.getX() - actor_avg_width);
		transform.setY(transform.getY() - actor_avg_height);
		
		this.transform = transform;
		actor_sprite = null;
		GameWorld.actor_list.add(this);
	}
	
	
	/**
	 * Constructs an Actor object with
	 * @param transform The transform of the Actor. Through which the Actor's velocity and position 
	 * is manipulated and retrieved.
	 * @param sprite The Image of the sprite.
	 * @param name The String name of the actor. Used for searching through the Array list of actors.
	 * @param width The width of the Actor, and consequently, the sprite.
	 * @param height The height of the Actor, and consequently, the sprite.
	 */
	
	Actor(Transform transform, Sprite sprite, int width, int height, String name) {
		
		
		super(name, transform);
		actor_sprite = sprite;
	
		actor_width = width;
		actor_height = height;
		sprite_color = sprite.getColor();
		
		actor_avg_width = actor_width / 2;
		actor_avg_height = actor_height / 2;
		
		
		transform.setX(transform.getX() - actor_avg_width);
		transform.setY(transform.getY() - actor_avg_height);
		this.transform = transform;
		GameWorld.game_obj_table.put(this.name, this);

	}
	
	
	/**
	 * Constructs an Actor object with a transform, color, width, and height.
	 * The actor will appear as a box on screen.
	 * @param transform The Transform of the Actor. Through which the velocity and position are manipulated
	 * and retrieved.
	 * @param name The String name of the actor. Used for searching through the Array list of actors.
	 * @param color The color of the Actor. 
	 * @param width The width of the Actor and box.
	 * @param height The height of the Actor and box.
	 */
	
	Actor(Transform transform, Color color, int width, int height, String name) {
	
		super(name, transform);
		
		actor_sprite = null;
		sprite_color = color;
		
		actor_width = width;
		actor_height = height;
		actor_avg_width = actor_width / 2;
		actor_avg_height = actor_height / 2;
		
		
		transform.setX(transform.getX() - actor_avg_width);
		transform.setY(transform.getY() - actor_avg_height);
		this.transform = transform;
		GameWorld.game_obj_table.put(this.name, this);
	}
	
	
	/**
	 * Sets the bounds in the world coordinate array located in GameWorld.
	 * May need to use calculus for this problem...
	 * @param x 
	 * @param y
	 * @return
	 */
	public boolean setWorldCoordinates(int x, int y) {
		
		return true;
	}
	
	
	/**
	 * Sets the Actor's size.
	 * @param width The width of the Actor.
	 * @param height The height of the Actor.
	 * @return Returns true as long as width and height are >= 0. Otherwise, it returns false.
	 */
	
	public boolean setActorSize(int width, int height) {
		if (width < 0 && height < 0)
			return false; //invalid size.
		else {
			GameWorld.actor_list.get(actor_index).actor_width = width;
			GameWorld.actor_list.get(actor_index).actor_height = height;
			return true;
		}		
	}
	
	
	/**
	 * Loads a new sprite onto the actor.
	 * @param sprite The new sprite to load in the actor.
	 */
	
	public void setSprite(Sprite sprite) {
		Actor temp = (Actor) GameWorld.game_obj_table.get(name);
		temp.actor_sprite = sprite;
	}
	
	
	/**
	 * Sets the actor to render a rectangle instead of a sprite.
	 * @param width The width of the actor.
	 * @param height The height of the actor.
	 * @param color The color of the actor.
	 */
	
	public void setRect(int width, int height, Color color) {
		Actor temp = (Actor) GameWorld.game_obj_table.get(name);
		temp.actor_sprite = null;
		temp.actor_width = width;
		temp.actor_height = height;
		temp.sprite_color = color;
		
	}
	
	
	/**
	 * Sets the actor to render a rectangle instead of a sprite.
	 * @param color The color of the rectangle.
	 */
	
	public void setRect(Color color) {
		Actor temp = (Actor) GameWorld.game_obj_table.get(name);

		temp.actor_sprite = null;
		temp.sprite_color = color;
		
	}
	
	
	/**
	 * Renders an actor on screen if set to visible.
	 */
	public void renderActor() {
		Actor temp = (Actor) GameWorld.game_obj_table.get(name);

		converted_pos_x = (int) (transform.getX() + 0.5f);
		converted_pos_y = (int) (transform.getY() + 0.5f);
		
		double actor_dir = transform.getDirection();
		
		if (is_visible) {
			//actor_graphics = Render.bi.createGraphics();
			if (actor_sprite != null) {
				temp.actor_graphics.drawImage(actor_sprite.getImage(), converted_pos_x, converted_pos_y, actor_sprite.getWidth(), actor_sprite.getHeight(), null);
				temp.actor_graphics.rotate(actor_dir);
			} else {

				temp.actor_graphics.setColor(sprite_color);
				temp.actor_graphics.fillRect(converted_pos_x, converted_pos_y, actor_width, actor_height);
				temp.actor_graphics.rotate(actor_dir);
				
			}
		} else {
			//if (actor_sprite != null) {
				
			//} else {
			//	actor_graphics.dispose();
			//	actor_graphics.clearRect(x_position, y_position, actor_width, actor_height);
		//	}
			//unrender the actor.
		}
	}
	
	
	/**
	 * Sets an actor visible on the scene.
	 * @param enable Set to true if you want the actor to be visible and in the jframe/scene.
	 */
	
	public void setVisible(boolean enable) {
		Actor temp = (Actor) GameWorld.game_obj_table.get(name);

		temp.is_visible = enable;
		
	}
	
	/**
	 * Sets the Actor name.
	 * @param name The new name of the actor.
	 * @return Returns true all the time. In the future it will if and only if there does not exist an Actor with the same name.
	 */
	
	public boolean setActorName(String name) {
		Actor temp = (Actor) GameWorld.game_obj_table.get(name);

		temp.name = name;
		return true;
	}
	
	
	/**
	 * Removes an Actor object from the GameWorld Actor list. Please use this other than the remove function from the array list.
	 */
	
	public void remove() {
		
		int list_size = GameWorld.actor_list.size();
		if (actor_index < list_size - 1) { //Ensures that the current object being removed is not at the end of the list.
			
			for (int i = this.actor_index + 1; i < list_size; ++i) 
				GameWorld.actor_list.get(i).actor_index = i - 1;
			
			GameWorld.actor_list.remove(actor_index);
			
		} else {
			GameWorld.actor_list.remove(actor_index); // If this is the last elements, then we can just remove if from the array list without having to re-index the others.
		}
	}
	
	
	/**
	 * If the actor has been set to visible, this function returns true.
	 * @return Returns true is suppose to be rendered onto the scene.
	 */
	
	public boolean isVisible() {
		return is_visible;
	}
	
	
	/**
	 * Get function for the actor's name. Use this to search through the Array string of actors.
	 * @return Returns the name of the actor.
	 */
	
	public String getActorName() {
		return name;
	}
	
	
	/**
	 * Get function for the actor's index.
	 * @return Returns the index of the actor.
	 */
	
	public int getActorIndex() {
		return actor_index;
	}
	
	/**
	 * Retrieves the Actor's width
	 * @return Returns the width of the Actor's sprite.
	 */
	
	public int getActorWidth() {
		return actor_width;
	}
	
	
	/**
	 * Returns the Actor's height.
	 * @return Returns the height of the Actor's sprite.
	 */
	
	public int getActorHeight() {
		return actor_height;
	}
	
	
	/**
	 * Determines if an object is collidable.
	 * @return Returns true if the object can be collided with.
	 */
	public boolean isCollidable() {
		return has_collision;
	}
	
	
}
