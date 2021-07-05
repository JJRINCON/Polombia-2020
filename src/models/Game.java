package models;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import persistence.FileManager;

public class Game {
	
	private static final String PATH_PRODUCTS_IMG = "src/resources/homeAppliancesImg/";
	private static final String PATH_GAME_WON_SOUND = "/resources/Sound/Himno Nacional de la República de Colombia(MP3_160K).mp3";
	private static final String PATH_GAME_OVER_SOUND = "/resources/Sound/doblaron las campanas.mp3";
	private static final String PATH_INITIAL_SOUND = "/resources/Sound/el parrandon.mp3";
	private static final String PATH_GAME_SOUND = "/resources/Sound/la plata.mp3";
	private static final String SCREEN_SHOTS_PATH = "dataBase/screenshots/";
	private Character character;
	private Sound gameSound;
	private Sound initialSound;
	private Sound gameOverSound;
	private Sound gameWonSound;
	private ConcurrentHashMap<Integer, Enemy> enemies;
	private ConcurrentHashMap<Integer, Shoot> shoots;
	private ConcurrentHashMap<Integer, Antenna> antennas;
	private ConcurrentHashMap<Integer, Wave> waves;
	private ConcurrentHashMap<String, BufferedImage> productsImages;
	private Rectangle2D home;
	private Rectangle2D alkosto;
	private int shootKey;
	private int waveKey;
	private int enemyKey;
	private boolean gameWon;
	private boolean stupidControls;
	private boolean showHistory;
	private int screenShotNumber;
	private String selectedProduct;
	private MyThread entitiesThread;
	private String screeenShotsFile;
	private HealingItem healingItem;
	
	public Game() throws IOException {
		character = new Character(20,(int)(Constants.WINDOW_HEIGHT * 0.7));
		enemies = new ConcurrentHashMap<Integer, Enemy>();
		generateEnemies();
		shoots = new ConcurrentHashMap<Integer, Shoot>();
		antennas = new ConcurrentHashMap<Integer, Antenna>();
		generateAntennas();
		waves = new ConcurrentHashMap<Integer, Wave>();
		generateInitialWaves();
		home = new Rectangle2D.Double( 0, (int)(Constants.WINDOW_HEIGHT * 0.3), (int)(Constants.WINDOW_WIDTH * 0.2),
				(int)(Constants.WINDOW_HEIGHT * 0.3));
		alkosto = new Rectangle2D.Double((int)(Constants.WINDOW_WIDTH * 0.8), (int)(Constants.WINDOW_HEIGHT * 0.3), 
				(int)(Constants.WINDOW_WIDTH * 0.2), (int)(Constants.WINDOW_HEIGHT * 0.3));
		readProductsImages();
		showHistory = false;
		screeenShotsFile = new File(SCREEN_SHOTS_PATH + "/Partida(" + getActualDate() + ")").toString();
		}
	
		public Game(Character character, ConcurrentHashMap<Integer, Enemy> enemies, ConcurrentHashMap<Integer, Shoot> shoots,
			ConcurrentHashMap<Integer, Wave> waves, ConcurrentHashMap<Integer, String> images,String selectProduct, long initialTime,
			String screenshotFile, Long screenshoNumber)
					throws IOException, URISyntaxException {
		this.character = character;
		this.enemies = enemies;
		this.shoots = shoots;
		this.waves = waves;
		shootKey = shoots.size();
		waveKey = waves.size();
		antennas = new ConcurrentHashMap<Integer, Antenna>();
		generateAntennas();
		home = new Rectangle2D.Double( 0, (int)(Constants.WINDOW_HEIGHT * 0.3), (int)(Constants.WINDOW_WIDTH * 0.2),
				(int)(Constants.WINDOW_HEIGHT * 0.3));
		alkosto = new Rectangle2D.Double((int)(Constants.WINDOW_WIDTH * 0.8), (int)(Constants.WINDOW_HEIGHT * 0.3), 
				(int)(Constants.WINDOW_WIDTH * 0.2), (int)(Constants.WINDOW_HEIGHT * 0.3));
		readProductsImages();
		loadImages(images);
		showHistory = false;
		this.selectedProduct = selectProduct;
		gameSound = new Sound(PATH_GAME_SOUND, initialTime, true);
		gameWon = false;
		this.screeenShotsFile = screenshotFile;
		this.screenShotNumber = screenshoNumber.intValue() + 1;
	}
	
	public String getActualDate() {
		String actualDate = "";
		actualDate += LocalDate.now() + "--";
		actualDate += LocalTime.now().getHour() + "-";
		actualDate += LocalTime.now().getMinute() + "-";
		actualDate += LocalTime.now().getSecond();
		return actualDate;
	}
		
	public void moveEntities() {
		entitiesThread = new MyThread(Constants.ENTITY_PAUSE_TIME) {
			
			@Override
			void executeTask() {
				walkEnemies();
				moveWaves();
				moveShoots();
				verifyEnemyCollisions();
				verifyCharacterHealth();
				verifyWaveCollisions();
				verifyShootsCollisions();
				verifyHealtingItemCollisions();
			}
		};
		entitiesThread.start();
	}
	
	public void pauseEntities() {
		entitiesThread.pause();
	}
	
	public void stopEntities() {
		entitiesThread.stop();
	}
	
	public void resumeEntities() {
		entitiesThread.resume();
	}
	
	private void loadImages(ConcurrentHashMap<Integer, String> images) {
		for(Integer imagesKey : images.keySet()) {
			for(String key : productsImages.keySet()) {
				if(!key.equals(images.get(imagesKey))) {
					images.remove(imagesKey);
				}
			}
		}
	}
	
	public void verifyCharacterHealth() {
		if(character.getHealth() <= 0) {
			character.dead();
		}
	}
	
	public void verifyEnemyCollisions() {
		for(Integer key : enemies.keySet()) {
			Rectangle enemy = new Rectangle(enemies.get(key).getX(), enemies.get(key).getY() + (int)(Enemy.HEIGHT * 0.4), 
					Enemy.WIDTH , (int)(Enemy.HEIGHT * 0.6));
			Rectangle hero = new Rectangle(character.getX(), character.getY() + (int)(Character.HEIGHT * 0.4),
					Character.WIDTH, (int)(Character.HEIGHT * 0.6));
			if(enemy.intersects(hero)) {
				character.hurt(enemies.get(key).getRight());
			}
		}
	}

	public void verifyShootsCollisions() {
		verifyLimitsShoots();
		for(Integer key : shoots.keySet()) {
			Rectangle shoot = new Rectangle(shoots.get(key).getX(), shoots.get(key).getY(), Shoot.WIDTH, Shoot.HEIGHT);
			for(Integer enemyKey : enemies.keySet()) {
				Rectangle enemy = new Rectangle(enemies.get(enemyKey).getX(), enemies.get(enemyKey).getY(), Enemy.WIDTH, Enemy.HEIGHT);
				if(shoot.intersects(enemy)) {
					enemies.get(enemyKey).setHurt();
					enemies.get(enemyKey).hurt();
					shoots.remove(key);
					break;
				}
				verifyEnemyHealth(enemyKey);
			}
		}
	}
	
	public void verifyLimitsShoots() {
		for(Integer key : shoots.keySet()) {
			if(shoots.get(key).getX() <= 0 || shoots.get(key).getX() >= Constants.WINDOW_WIDTH) {
				shoots.remove(key);
			}
		}
	}
	
	private void verifyEnemyHealth(Integer enemyKey) {
		if(enemies.get(enemyKey).getHealth() <= 0) {
			enemies.remove(enemyKey);
		}
	}
	
	public void generateEnemies() throws IOException {
		int x = (int)(Math.random() * (Constants.WINDOW_WIDTH * 0.8)) + (int)(Constants.WINDOW_WIDTH * 0.2);
		int y = (int)(Constants.WINDOW_HEIGHT * 0.5);
		for (int i = 0; i < 4; i++) {
			Enemy enemy = new Enemy(x,y);
			x =  (int)(Math.random() * (Constants.WINDOW_WIDTH * 0.8)) + (int)(Constants.WINDOW_WIDTH * 0.2);
			y += 100;
			enemies.put(enemyKey, enemy);
			enemyKey++;
		}
	}
	
	public void generateEnemy() throws IOException {
		if(enemies.size() < 4) {
			int x = (int)(Math.random() * (Constants.WINDOW_WIDTH * 0.8)) + (int)(Constants.WINDOW_WIDTH * 0.2);
			int y = (int)(Math.random() * (Constants.WINDOW_HEIGHT * 0.45)) + (int)(Constants.WINDOW_HEIGHT * 0.5);
			Enemy enemy = new Enemy(x, y);
			enemies.put(enemyKey, enemy);
			enemyKey++;
		}
	}
	
	public void generateShoots() {
		if(selectedProduct == null){
			if(character.getRight()) {
				Shoot shoot = new Shoot(character.getX() + Character.WIDTH, character.getY() + 60);
				shoot.setRight();
				shoots.put(shootKey, shoot);
				shootKey++;
			}else {
				Shoot shoot = new Shoot(character.getX() - Character.WIDTH, character.getY() + 60);
				shoot.setLeft();
				shoots.put(shootKey, shoot);
				shootKey++;
			}
		}
	}
	
	public void moveShoots() {
		if(!shoots.isEmpty()) {
		for(Integer keyShoot : shoots.keySet()) {
			if(shoots.get(keyShoot).getRight()) {
				shoots.get(keyShoot).moveRight();
			}else {
				shoots.get(keyShoot).moveLeft();
			}
		}
		}
	}
	
	public void walkEnemies() {
		for(Integer key : enemies.keySet()) {
			enemies.get(key).walk();
		}
	}
	
	public void initGameSound() throws URISyntaxException {
		gameSound = new Sound(PATH_GAME_SOUND, true);
		gameSound.playSong();
	}
	
	public void stopGameSound() {
		gameSound.stopSong();
	}
	
	public void pauseGameSound() {
		gameSound.pauseSong();
	}
	
	public void resumeGameSound() {
		gameSound.playSong();
	}
	
	public void initInitialSound() throws URISyntaxException {
		initialSound = new Sound(PATH_INITIAL_SOUND, true);
		initialSound.playSong();
	}
	
	public void stopInitialSound() {
		initialSound.stopSong();
	}
	
	public void resumeInitialSound() {
		initialSound.playSong();
	}
	
	public void pauseInitialSound() {
		initialSound.pauseSong();
	}
	
	public void initGameOverSound() throws URISyntaxException {
		gameOverSound = new Sound(PATH_GAME_OVER_SOUND, true);
		gameOverSound.playSong();
	}
	
	public void stopGameOverSound() {
		gameOverSound.stopSong();
	}
	
	public void initGameWonSound() throws URISyntaxException {
		gameWonSound = new Sound(PATH_GAME_WON_SOUND, true);
		gameWonSound.playSong();
	}
	
	public void stopGameWonSound() {
		gameWonSound.stopSong();
	}
	
	public void generateAntennas() {
		Antenna leftAntenna = new Antenna( (int)(Constants.WINDOW_WIDTH * 0.35), (int)(Constants.WINDOW_HEIGHT * 0.24));
		antennas.put(1, leftAntenna);
		Antenna rightAntenna = new Antenna( (int)(Constants.WINDOW_WIDTH * 0.6), (int)(Constants.WINDOW_HEIGHT * 0.24));
		antennas.put(2, rightAntenna);
	}
	
	public void generateInitialWaves() {
		Wave left = new Wave((int)(Constants.WINDOW_WIDTH * 0.35), (int)(Constants.WINDOW_HEIGHT * 0.24), 270, 90, false);
		waves.put(waveKey, left);
		waveKey++;
		Wave right = new Wave((int)(Constants.WINDOW_WIDTH * 0.6), (int)(Constants.WINDOW_HEIGHT * 0.24), 180, 90, true);
		waves.put(waveKey, right);
		waveKey++;
	}
	
	public void moveWaves() {
		if(waves.isEmpty()) {
			generateWave();
		}else {
			for(Integer key : waves.keySet()) {
				if(waves.get(key).getArc2d().getMaxY() <= Constants.WINDOW_HEIGHT ) {
					waves.get(key).move();
				}else {
					waves.remove(key);
					generateWave();
				}
			}
		}
	}
	
	public void verifyWaveCollisions() {
		for(Integer key : waves.keySet()) {
			Rectangle hero = new Rectangle(character.getX(), character.getY(),
					Character.WIDTH, Character.HEIGHT);
			if(waves.get(key).getArc2d().intersects(hero)) {
				if(!stupidControls) {
					waves.remove(key);
					stupidControls = true;
				}else {
					waves.remove(key);
					character.waveHurt();
				}
			}
		}
	}
	
	public void generateWave() {
		if(waves.isEmpty()) {
			generateInitialWaves();
		}else if(waves.size() == 1) {
			for(Integer key : waves.keySet()) {
				if(waves.get(key).getLocation()) {
					Wave left = new Wave((int)(Constants.WINDOW_WIDTH * 0.35), (int)(Constants.WINDOW_HEIGHT * 0.24), 270, 90, false);
					waves.put(waveKey, left);
					waveKey++;
				}else {
					Wave right = new Wave((int)(Constants.WINDOW_WIDTH * 0.6), (int)(Constants.WINDOW_HEIGHT * 0.24), 180, 90, true);
					waves.put(waveKey, right);
					waveKey++;
				}
			}
		}
	}
	
	public boolean verifyHomeCoordinates() {
		Rectangle hero = new Rectangle(character.getX(), character.getY() + Character.HEIGHT - 10, Character.WIDTH , 10);
		if(hero.intersects(home)) {
			if(character.getY() <= Constants.WINDOW_HEIGHT * 0.6 - Character.HEIGHT) {
				character.setX((int)(Constants.WINDOW_WIDTH * 0.2 + 2));
				return false;
			}else if(character.getY() >= Constants.WINDOW_HEIGHT * 0.60 - Character.HEIGHT){
				character.setY((int)(Constants.WINDOW_HEIGHT * 0.5));
				return true;
			}
		}
		return false;
	}
	
	public void verifyHome() throws IOException {
		if(verifyHomeCoordinates() && selectedProduct != null) {
			generateEnemy();
			removeProduct(selectedProduct);
			setSelectedProduct(0);
			setCharacterSpeedX(6);
			setCharacterSpeedY(5);
			generateHealtingItem();
			if(productsImages.isEmpty()) {
				gameWon = true;
			}
		}
	}
	
	public boolean verifyAlkostoCoordinates() {
		Rectangle hero = new Rectangle(character.getX(), character.getY() + Character.HEIGHT - 10, Character.WIDTH , 10);
		if(hero.intersects(alkosto)) {
			if(character.getY() <= Constants.WINDOW_HEIGHT * 0.6 - Character.HEIGHT) {
				character.setX((int)(Constants.WINDOW_WIDTH * 0.8 - Character.WIDTH));
				return false;
			}else if(character.getY() >= Constants.WINDOW_HEIGHT * 0.60 - Character.HEIGHT) {
				character.setY((int)(Constants.WINDOW_HEIGHT * 0.5));
				return true;
			}
		}
		return false;
	}
	
	public void takeScreenShot(BufferedImage image) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					verifyScreenShotsFile(new File(screeenShotsFile));
					ImageIO.write(image, "png", new File(screeenShotsFile.toString() + "/screenshot"+ screenShotNumber + ".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				screenShotNumber++;
			}
		}).start();	
	}
	
	private void verifyScreenShotsFile(File file) {
		if(!file.exists()) {
			file.mkdir();
		}
	}
	
	public void saveGame(Game game) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				FileManager.writeData(game);
			}
		}).start();
	}
	
	private void readProductsImages() throws IOException {
		productsImages = new ConcurrentHashMap<String, BufferedImage>();
		File file = new File(PATH_PRODUCTS_IMG);
		for(File image : file.listFiles()) {
			BufferedImage productImage = ImageIO.read(image);
			productsImages.put(image.getName(), productImage);
		}
	}
	
	public void removeProduct(String productKey) {
		if(productKey != null) {
			productsImages.remove(productKey);
		}
	}
	
	public void restartGame() throws IOException {
		character = new Character(20,(int)(Constants.WINDOW_HEIGHT * 0.7));
		waves = new ConcurrentHashMap<Integer, Wave>();
		enemies = new ConcurrentHashMap<Integer, Enemy>();
		generateInitialWaves();
		generateAntennas();
		generateEnemies();
		readProductsImages();
		shootKey = 0;
		waveKey = 0;
		screenShotNumber = 0;
		stupidControls = false;
		selectedProduct = null;
		gameWon = false;
		healingItem = null;
	}

	public void setSelectedProduct(int selectedProduct) {
		int number = 1;
		if(selectedProduct == 0) {
			this.selectedProduct = null;
		}else {
			for(String imgKey : productsImages.keySet()) {
				if(number == selectedProduct) {
					this.selectedProduct = imgKey;
					System.out.println(this.selectedProduct);
					break;
				}else {
					number++;
				}
			}
		}
	}
	
	public static ArrayList<BufferedImage> readHistoryImages(File file) throws IOException{
		ArrayList<BufferedImage> historyImages = new ArrayList<BufferedImage>();
		BufferedImage image = null;
		for(File imageFile : file.listFiles()) {
			image = ImageIO.read(imageFile);
			historyImages.add(image);
		}
		return historyImages;
	}
	
	public static ArrayList<BufferedImage> readTutorialImages(File file) throws IOException{
		ArrayList<BufferedImage> tutorialImages = new ArrayList<BufferedImage>();
		BufferedImage image = null;
		for(File imageFile : file.listFiles()) {
			image = ImageIO.read(imageFile);
			tutorialImages.add(image);
		}
		return tutorialImages;
	}
	
	private void generateHealtingItem() throws IOException{
		if(stupidControls) {
			int x =  (int)(Math.random() * (Constants.WINDOW_WIDTH * 0.8)) + (int)(Constants.WINDOW_WIDTH * 0.2);
			int y = (int)(Math.random() * (Constants.WINDOW_HEIGHT * 0.45)) + (int)(Constants.WINDOW_HEIGHT * 0.5);
			healingItem = new HealingItem(x, y);
		}
	}
	
	private void verifyHealtingItemCollisions() {
		if(healingItem != null) {
			Rectangle hero = new Rectangle(character.getX(), character.getY() + (int)(Character.HEIGHT * 0.4),
					Character.WIDTH, (int)(Character.HEIGHT * 0.6));
			if(hero.intersects(healingItem.getRectangle())) {
				stupidControls = false;
				healingItem.stopThread();
				healingItem = null;
			}
		}
	}
	
	public Character getCharacter() {
		return character;
	}
	
	public ConcurrentHashMap<Integer, Enemy> getEnemies() {
		return enemies;
	}
	
	public ConcurrentHashMap<Integer, Shoot> getShoots() {
		return shoots;
	}
	
	public ConcurrentHashMap<Integer, Antenna> getAntennas() {
		return antennas;
	}
	
	public ConcurrentHashMap<Integer, Wave> getWaves() {
		return waves;
	}
	
	public boolean getStupidControls() {
		return stupidControls;
	}
	
	public ConcurrentHashMap<String, BufferedImage> getProductsImages() {
		return productsImages;
	}
	
	public void setCharacterSpeedX(int newSpeedX) {
		character.setSpeedX(newSpeedX);
	}
	
	public void setCharacterSpeedY(int newSpeedY) {
		character.setSpeedY(newSpeedY);
	}
	
	
	public String getSelectedProduct() {
		return selectedProduct;
	}

	public BufferedImage getImageSelectedProduct() {
		return productsImages.get(selectedProduct);
	}
	
	public boolean getShowHistory() {
		return showHistory;
	}
	
	public long getGameSoundTime() {
		Double time = gameSound.getTime().toMillis();
		return time.longValue();
	}
	
	public void setGameWon() {
		gameWon = true;
	}
	
	public boolean getGameWon() {
		return gameWon;
	}
	
	public ArrayList<BufferedImage> getScreenshots() throws IOException {
		ArrayList<BufferedImage> screenshoots = new ArrayList<BufferedImage>();
		BufferedImage image = null;
		File screenShots = new File(screeenShotsFile);
		for(File file : screenShots.listFiles()) {
			image = ImageIO.read(file);
			screenshoots.add(image);
		}
		return screenshoots;
	}
	
	public String getScreeenShotsFile() {
		return screeenShotsFile.toString();
	}
	
	public int getScreenShotNumber() {
		return screenShotNumber;
	}
	
	public HealingItem getHealingItem() {
		return healingItem;
	}
}
