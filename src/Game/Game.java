package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import GUIControls.Window;
import Models.Direction;
import Models.Pellet;
import Models.Snake;

public class Game extends JPanel {
	private Timer timer;
	private Snake snake;
	private Pellet pellet;
	private final int GRID_SIZE = 20;
	private Direction nextDirection = Direction.UP;
	private int numberOfRows;
	private int numberOfColumns;
	private final int MOVEMENT_DELAY = 7; // higher value == snake moves slower
	private int movementDelayCounter;

	public Game() {
		Window.setTitle("Snake");
		setDoubleBuffered(true);

		snake = new Snake(180, 300, GRID_SIZE);

		pellet = new Pellet();

		// game loop
		timer = new Timer(1, new ActionListener() {

			// this game loop method will continuously run over and over again
			@Override
			public void actionPerformed(ActionEvent e) {
				// if enough time has gone by, snake will move one grid space in a direction based on the key pressed
				if (movementDelayCounter >= MOVEMENT_DELAY) {
					// change snake direction
					if (nextDirection != null) {
						snake.setDirection(nextDirection);
					}
					
					// move snake
					snake.move();

					movementDelayCounter = 0;
					nextDirection = null;
				}
				
				// if snake hits screen boundaries, game over
				if (snake.getXLocation() < 0 || snake.getXLocation() > getWidth() || snake.getYLocation() < 0 || snake.getYLocation() > getHeight()) {
					System.exit(1);
				}
				
				// if snake hits its own tail, game over
				if (snake.hasCollidedWithTail()) {
					System.exit(1);
				}

				// if snake gets pellet, move pellet to new random location and add new segment on to snake's tail
				if (snake.getXLocation() == pellet.getXLocation() && snake.getYLocation() == pellet.getYLocation()) {
					Point newPelletLocation = getRandomGridCoords();
					pellet.setXLocation(newPelletLocation.x * GRID_SIZE);
					pellet.setYLocation(newPelletLocation.y * GRID_SIZE);
					snake.addSegment();
				}

				movementDelayCounter++;

				repaint();
			}
		});

		// detects keyboard key presses
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (nextDirection == null) {
					// if A Key was pressed
					if (e.getKeyCode() == KeyEvent.VK_A) {
						if (snake.getDirection() == Direction.UP || snake.getDirection() == Direction.DOWN) {
							nextDirection = Direction.LEFT;
						}
					}
					 // if D Key was pressed
					if (e.getKeyCode() == KeyEvent.VK_D) {
						if (snake.getDirection() == Direction.UP || snake.getDirection() == Direction.DOWN) {
							nextDirection = Direction.RIGHT;
						}
					}
					// if W Key was pressed
					if (e.getKeyCode() == KeyEvent.VK_W) {
						if (snake.getDirection() == Direction.LEFT || snake.getDirection() == Direction.RIGHT) {
							nextDirection = Direction.UP;
						}
					}
					// if S Key was pressed
					if (e.getKeyCode() == KeyEvent.VK_S) {
						if (snake.getDirection() == Direction.LEFT || snake.getDirection() == Direction.RIGHT) {
							nextDirection = Direction.DOWN;
						}
					}
				}

			}

		});

		this.setFocusable(true);
	}

	// initializes game and starts game loop timer
	public void startGame() {
		// sets grid size based on width/height of the window
		numberOfRows = this.getHeight() / GRID_SIZE;
		numberOfColumns = this.getWidth() / GRID_SIZE;

		// places pellet in a random location to start
		Point pelletStartLocation = getRandomGridCoords();
		pellet.setXLocation(pelletStartLocation.x * GRID_SIZE);
		pellet.setYLocation(pelletStartLocation.y * GRID_SIZE);

		// game loop starts iterating here
		timer.start();
		
		this.grabFocus();
	}
	
	// gets a random location in the grid
	private Point getRandomGridCoords() {
		Random random = new Random();
		return new Point(random.nextInt(numberOfColumns), random.nextInt(numberOfRows));
	}

	// graphics are painted to the screen here
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// create a Graphics2D brush to allow for painting grpahics to the screen
		Graphics2D brush = (Graphics2D) g;

		drawGrid(brush);
		snake.draw(brush);
		pellet.draw(brush);

		// draw text "Created by" in bottom right corner
		brush.setColor(Color.black);
		brush.setFont(new Font("Arial", 0, 10));
		brush.drawString("Created by: ARTech Industries", getWidth() - 146, getHeight() - 5);
	}

	// paints the grid graphics to the screen
	private void drawGrid(Graphics2D g) {
		Color oldColor = g.getColor();
		int incrementTracker = 0;
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				Color gridColor = j % 2 == incrementTracker ? new Color(53, 94, 59) : Color.green;
				g.setColor(gridColor);
				g.fillRect(j * GRID_SIZE, i * GRID_SIZE, GRID_SIZE, GRID_SIZE);
			}
			incrementTracker = incrementTracker == 0 ? 1 : 0;
		}
		g.setColor(oldColor);
	}
}
