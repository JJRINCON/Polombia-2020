package persistence;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import models.Character;
import models.Enemy;
import models.Game;
import models.Shoot;
import models.Wave;

public class FileManager {
	
	private static final String TAG_GAME = "GAME";
	private static final String DB_PATH_DATA = "dataBase/data/data.json";
	private static final String TAG_POSITION_X = "pisition-x";
	private static final String TAG_POSITION_Y = "pisition-y";
	private static final String TAG_CHARACTER = "hero";
	private static final String TAG_RIGHT = "right";
	private static final String TAG_LEFT = "left";
	private static final String TAG_UP = "up";
	private static final String TAG_DOWN = "down";
	private static final String TAG_HEALTH = "health";
	private static final String TAG_ENEMIES = "enemies";
	private static final String TAG_SHOOTS = "shoots";
	private static final String TAG_SPEEDX = "speedX";
	private static final String TAG_SPEEDY = "speedY";
	private static final String TAG_WAVE_START = "start";
	private static final String TAG_WAVE_EXTENT = "extent";
	private static final String TAG_WAVE_LOCATION = "location";
	private static final String TAG_WAVES = "waves";
	private static final String TAG_IMAGE = "productImage";
	private static final String TAG_IMAGES = "productsImages";
	private static final String TAG_SELECTED_PRODUCT = "selectedProduct";
	private static final String PATH_HISTORY_FILE = "src/resources/history.txt";
	private static final String PATH_STATUS_FILE = "src/resources/status.txt";
	private static final String TAG_GAME_SOUND_TIME = "game-sound-time";
	private static final String TAG_SCREENSHOTS_FILE = "screenshots-file";
	private static final String TAG_NUMBER_SCREENSHOT = "screenshot-number";

	@SuppressWarnings("unchecked")
	public static void writeData(Game game) {
		final JSONObject jsonFile = new JSONObject();
		jsonFile.put(TAG_GAME, createJsonGame(game));
		try(FileWriter file = new FileWriter(DB_PATH_DATA, false)){
	        file.write(jsonFile.toJSONString());
	    }catch(final IOException e){
	    	e.printStackTrace();
	    }
	}
	
	@SuppressWarnings("unchecked")
	private static JSONObject createJsonGame(Game game) {
		JSONObject jsonGame = new JSONObject();
		jsonGame.put(TAG_CHARACTER, createJsonChracter(game.getCharacter()));
		jsonGame.put(TAG_ENEMIES, createJsonArrayEnemies(game.getEnemies()));
		jsonGame.put(TAG_SHOOTS, createShootsJsonArray(game.getShoots()));
		jsonGame.put(TAG_WAVES, createWavesJsonArray(game.getWaves()));
		jsonGame.put(TAG_IMAGES, createProductsImagesJsonArray(game.getProductsImages()));
		jsonGame.put(TAG_SELECTED_PRODUCT, game.getSelectedProduct());
		jsonGame.put(TAG_GAME_SOUND_TIME, game.getGameSoundTime());
		jsonGame.put(TAG_SCREENSHOTS_FILE, game.getScreeenShotsFile());
		jsonGame.put(TAG_NUMBER_SCREENSHOT, game.getScreenShotNumber());
		return jsonGame;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONObject createJsonChracter(Character hero) {
		JSONObject jsonHero = new JSONObject();
		jsonHero.put(TAG_POSITION_X, hero.getX());
		jsonHero.put(TAG_POSITION_Y, hero.getY());
		jsonHero.put(TAG_SPEEDX, hero.getSpeedX());
		jsonHero.put(TAG_SPEEDY, hero.getSpeedY());
		jsonHero.put(TAG_HEALTH, hero.getHealth());
		jsonHero.put(TAG_RIGHT, hero.getRight());
		jsonHero.put(TAG_LEFT, hero.getLeft());
		jsonHero.put(TAG_UP, hero.getUp());
		jsonHero.put(TAG_DOWN, hero.getDown());
		return jsonHero;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONArray createJsonArrayEnemies(ConcurrentHashMap<Integer, Enemy> enemies) {
		JSONArray jsonEnemies = new JSONArray();
		for(Integer enemyKey : enemies.keySet()) {
			JSONObject jsonEnemy = new JSONObject();
			jsonEnemy.put(TAG_POSITION_X, enemies.get(enemyKey).getX());
			jsonEnemy.put(TAG_POSITION_Y, enemies.get(enemyKey).getY());
			jsonEnemy.put(TAG_HEALTH, enemies.get(enemyKey).getHealth());
			jsonEnemy.put(TAG_RIGHT, enemies.get(enemyKey).getRight());
			jsonEnemy.put(TAG_LEFT, enemies.get(enemyKey).getLeft());
			jsonEnemies.add(jsonEnemy);
		}
		return jsonEnemies;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONArray createShootsJsonArray(ConcurrentHashMap<Integer, Shoot> shoots) {
		JSONArray jsonShoots = new JSONArray();
		for(Integer shootKey: shoots.keySet()) {
			JSONObject jsonShoot = new JSONObject();
			jsonShoot.put(TAG_POSITION_X, shoots.get(shootKey).getX());
			jsonShoot.put(TAG_POSITION_Y, shoots.get(shootKey).getY());
			jsonShoot.put(TAG_RIGHT, shoots.get(shootKey).getRight());
			jsonShoot.put(TAG_LEFT, shoots.get(shootKey).getLeft());
			jsonShoots.add(jsonShoot);
		}
		return jsonShoots;
 	}
	
	@SuppressWarnings("unchecked")
	private static JSONArray createWavesJsonArray(ConcurrentHashMap<Integer, Wave> waves) {
		JSONArray jsonWaves = new JSONArray();
		for(Integer waveKey : waves.keySet()) {
			JSONObject jsonWave = new JSONObject();
			jsonWave.put(TAG_POSITION_X, waves.get(waveKey).getX());
			jsonWave.put(TAG_POSITION_Y, waves.get(waveKey).getY());
			jsonWave.put(TAG_WAVE_START, waves.get(waveKey).getStart());
			jsonWave.put(TAG_WAVE_EXTENT, waves.get(waveKey).getExtent());
			jsonWave.put(TAG_WAVE_LOCATION, waves.get(waveKey).getLocation());
			jsonWaves.add(jsonWave);
		}
		return jsonWaves;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONArray createProductsImagesJsonArray(ConcurrentHashMap<String, BufferedImage> productsImages) {
		JSONArray jsonProductsImages = new JSONArray();
		for(String imageKey : productsImages.keySet()) {
			JSONObject jsonImage = new JSONObject();
			jsonImage.put(TAG_IMAGE, imageKey);
			jsonProductsImages.add(jsonImage);
		}
		return jsonProductsImages;
	}
	
	public static Game readData() throws URISyntaxException {
		Game game = null;
		JSONParser parser = new JSONParser();
		try (Reader reader = new FileReader(new File(DB_PATH_DATA));){
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			JSONObject jsonGame = (JSONObject)jsonObject.get(TAG_GAME);
			game = new Game(convertJsonCharacter((JSONObject)jsonGame.get(TAG_CHARACTER)), converEnemies((JSONArray)jsonGame.get(TAG_ENEMIES)),
					convertShoots((JSONArray)jsonGame.get(TAG_SHOOTS)), convertWaves((JSONArray) jsonGame.get(TAG_WAVES)), 
					convertProductsImages((JSONArray) jsonGame.get(TAG_IMAGES)), (String) jsonGame.get(TAG_SELECTED_PRODUCT), 
					(long)jsonGame.get(TAG_GAME_SOUND_TIME), (String) jsonGame.get(TAG_SCREENSHOTS_FILE), (long)jsonGame.get(TAG_NUMBER_SCREENSHOT));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return game;
	}
	
	private static Character convertJsonCharacter(JSONObject jsonCharacter) throws IOException {
		return new Character((long)jsonCharacter.get(TAG_POSITION_X), (long)jsonCharacter.get(TAG_POSITION_Y), (long)jsonCharacter.get(TAG_HEALTH),
				(long)jsonCharacter.get(TAG_SPEEDX), (long)jsonCharacter.get(TAG_SPEEDY), (boolean)jsonCharacter.get(TAG_RIGHT), 
				(boolean)jsonCharacter.get(TAG_LEFT),(boolean)jsonCharacter.get(TAG_UP), (boolean)jsonCharacter.get(TAG_DOWN));
	}
	
	@SuppressWarnings("unchecked")
	private static ConcurrentHashMap<Integer, Enemy> converEnemies(JSONArray jsonEnemies){
		int key = 0;
		ConcurrentHashMap<Integer, Enemy> enemies = new ConcurrentHashMap<Integer, Enemy>();
		Iterator<JSONObject> iterator = jsonEnemies.iterator();
		while(iterator.hasNext()) {
			JSONObject enemy = (JSONObject) iterator.next();
			try {
				enemies.put(key, new Enemy((long)enemy.get(TAG_POSITION_X),(long)enemy.get(TAG_POSITION_Y) , (long)enemy.get(TAG_HEALTH),
						(boolean)enemy.get(TAG_RIGHT), (boolean)enemy.get(TAG_RIGHT)));
				key++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return enemies;
	}
	
	@SuppressWarnings("unchecked")
	private static ConcurrentHashMap<Integer, Wave> convertWaves(JSONArray jsonWaves){
		int waveKey = 0;
		ConcurrentHashMap<Integer, Wave> waves = new ConcurrentHashMap<Integer, Wave>();
		Iterator<JSONObject> iterator = jsonWaves.iterator();
		while(iterator.hasNext()) {
			JSONObject wave = (JSONObject) iterator.next();
			waves.put(waveKey, new Wave((long)wave.get(TAG_POSITION_X), (long)wave.get(TAG_POSITION_Y), (long)wave.get(TAG_WAVE_START),
					(long)wave.get(TAG_WAVE_EXTENT), (boolean)wave.get(TAG_WAVE_LOCATION)));
			waveKey++;
		}
		return waves;
	}
	
	@SuppressWarnings("unchecked")
	private static ConcurrentHashMap<Integer, Shoot> convertShoots(JSONArray jsonShoots){
		int shootKey = 0;
		ConcurrentHashMap<Integer, Shoot> shoots = new ConcurrentHashMap<Integer, Shoot>();
		Iterator<JSONObject> iterator = jsonShoots.iterator();
		while(iterator.hasNext()) {
			JSONObject shoot = (JSONObject) iterator.next();
			shoots.put(shootKey, new Shoot((long)shoot.get(TAG_POSITION_X), (long)shoot.get(TAG_POSITION_Y)));
			shootKey++;
		}
		return shoots;
	}
	
	@SuppressWarnings("unchecked")
	private static ConcurrentHashMap<Integer, String> convertProductsImages(JSONArray productsImagesJsonArray){
		int imageKey = 0;
		ConcurrentHashMap<Integer, String> productsImages = new ConcurrentHashMap<Integer, String>();
		Iterator<JSONObject> iterator = productsImagesJsonArray.iterator();
		while(iterator.hasNext()) {
			JSONObject productImage = (JSONObject) iterator.next();
			productsImages.put(imageKey, (String)productImage.get(TAG_IMAGE));
			imageKey++;
		}
		return productsImages;
	}
	
	public static String[] readHistory() throws IOException{
		String[] history = null;
		StringBuilder txt = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(new File(PATH_HISTORY_FILE)));
		while(reader.read() != -1) {
			txt.append(reader.readLine());
		}
		reader.close();
		history = txt.toString().split(",");
		return history;
	}
	
	public void writeStatusFile(boolean status) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(PATH_STATUS_FILE)));
		writer.write(String.valueOf(status));
		writer.close();
	}
	
	public static boolean readStatusFile() throws IOException {
		boolean status = false;
		BufferedReader reader = new BufferedReader(new FileReader(new File(PATH_STATUS_FILE)));
		while(reader.read() != -1){
			status = Boolean.parseBoolean(reader.readLine());
		}
		reader.close();
		return status;
	}
}
