package presenters;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.Timer;

import models.Constants;
import models.Game;
import persistence.FileManager;
import views.AlkostoDilalog;
import views.GameOverDialog;
import views.GameWinDialog;
import views.InitialFrame;
import views.PauseDialog;
import views.ScreeenShotsDialog;
import views.Window;

public class Presenter implements KeyListener, ActionListener{
	
	private Game game;
	private Window window;
	private InitialFrame initialFrame;
	private GameOverDialog gameOverDialog;
	private PauseDialog pauseDialog;
	private AlkostoDilalog alkostoDialog;
	private GameWinDialog gameWinDialog;
	private ScreeenShotsDialog screeenShotsDialog;
	private Timer gameLoop;
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean pause;
	private volatile boolean buying;
	private long time;
	private long screenshotTime;
	private Timer historyTimer;
	private Timer tutorialTimer;
	
	public Presenter() {
		try {
			game = new Game();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			window = new Window(game, this);
		} catch (AWTException | IOException e1) {
			e1.printStackTrace();
		}
		initInitialFrame();
	}

	private void initInitialFrame() {
		try {
			initialFrame = new InitialFrame(this);
			initialFrame.initInitialPanel();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		initialFrame.setVisible(true);
		try {
			game.initInitialSound();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void initGameOverDialog() {
		try {
			gameOverDialog = new GameOverDialog(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameOverDialog.setVisible(true);
	}
	
	private void initGame() {
		pause = false;
		window.showGamePanel(game, this);
		window.setVisible(true);
		game.moveEntities();
		gameLoop = new Timer(Constants.REFRESHMENT_TIME, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!pause) {
					window.update(game);
					verifyAlkostoDialog();
					try {
						game.verifyHome();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					verifyControls();
					calculateTakeScreeshotTime();
					calculateHideScreenshotTime();
					verifyAlkosto();
					verifyGameOverStatus();
					verifyGameWinStatus();
				}
			}
		});
		gameLoop.start();
		time = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime());
		screenshotTime = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime()); 
		try {
			game.initGameSound();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}
	
	public void verifyGameOverStatus() {
		if(!game.getCharacter().getAlive()) {
			game.stopGameSound();
			game.stopEntities();
			gameLoop.stop();
			try {
				game.initGameOverSound();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			initGameOverDialog();
		}
	}
	
	public void verifyGameWinStatus() {
		if(game.getGameWon()) {
			game.stopGameSound();
			game.stopEntities();
			gameLoop.stop();
			try {
				game.initGameWonSound();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			initGameWinDialog();
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyChar()) {
			case 'x':
				game.generateShoots();
				break;
			case '1':
				selectProduct(Integer.parseInt(String.valueOf(e.getKeyChar())));
				break;
			case '2':
				selectProduct(Integer.parseInt(String.valueOf(e.getKeyChar())));
				break;
			case '3':
				selectProduct(Integer.parseInt(String.valueOf(e.getKeyChar())));
				break;
			case '4':
				selectProduct(Integer.parseInt(String.valueOf(e.getKeyChar())));
				break;
			case '5':
				selectProduct(Integer.parseInt(String.valueOf(e.getKeyChar())));
				break;
			case '6':
				selectProduct(Integer.parseInt(String.valueOf(e.getKeyChar())));
				break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			right = true;
			break;
		case KeyEvent.VK_LEFT:
			left = true;
			break;
		case KeyEvent.VK_UP:
			up = true;
			break;
		case KeyEvent.VK_DOWN:
			down = true;
			break;
		case KeyEvent.VK_ESCAPE:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_UP:
			up = false;
			break;
		case KeyEvent.VK_DOWN:
			down = false;
			break;
		case KeyEvent.VK_ESCAPE:
			pauseGame();
			break;
		}
	}
	
	public void verifyControls() {
		if(game.getStupidControls()) {
			verifyStupidControls();
		}else {
			verifyNormalControls();
		}
	}
	
	private void verifyNormalControls() {
		if(up) {
			game.getCharacter().walkUp();
		}else if(down) {
			game.getCharacter().walkDown();
		}else if(left) {
			game.getCharacter().walkLeft();
		}else if(right) {
			game.getCharacter().walkRight();
		}
	}
	
	private void verifyStupidControls() {
		if(up) {
			game.getCharacter().walkDown();
		}else if(down) {
			game.getCharacter().walkUp();
		}else if(left) {
			game.getCharacter().walkRight();
		}else if(right) {
			game.getCharacter().walkLeft();
		}
	}
	
	private void calculateTakeScreeshotTime() {
		long actualTime = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime());
		if((actualTime - time) == 10) {
			screenshotTime = actualTime;
			game.takeScreenShot(window.screenShot());
			window.setIsCapturing(true);
			time = actualTime;
			game.saveGame(game);
		}
	}
	
	private void calculateHideScreenshotTime() {
		long actualTime = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime());
			if((actualTime - screenshotTime) == 3) {
				window.setIsCapturing(false);
			}
	}
	
	private void pauseGame() {
		if(!pause) {
			pause = true;
			game.pauseEntities();
			initPauseDialog();
		}
	}
	
	private void initPauseDialog() {
		try {
			pauseDialog = new PauseDialog(this, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		pauseDialog.setVisible(true);
	}
	
	public void verifyAlkosto() {
		if(game.verifyAlkostoCoordinates() && game.getSelectedProduct() == null) {
			try {
				buying = true;
				alkostoDialog = new AlkostoDilalog(game.getProductsImages(), this);
				alkostoDialog.setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}


	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Commands.valueOf(e.getActionCommand())) {
		case START:
			verifyInitialPanelActions(e);
			break;
		case GAME_OVER_OPTION:
			verifyGameOverOptions(e);
			break;
		case PAUSED_OPTION:
			verifyPausedGameOptions(e);
			break;
		case TUTORIAL_OPTIONS:
			verifyTutorialOptions(e);
			break;
		case GAME_WIN_OPTION:
			verifyGameWinOptions(e);
			break;
		case SCREENSHOTS_OPTION:
			screeenShotsDialog.dispose();
			break;
		}
	}
	
	private void selectProduct(int selectedProduct) {
		if(buying) {
			game.setSelectedProduct(selectedProduct);
			game.setCharacterSpeedX(6);
			game.setCharacterSpeedY(5);
			buying = false;
			alkostoDialog.setVisible(false);
		}
	}
	
	private void verifyInitialPanelActions(ActionEvent e) {
		String btnName = ((JButton)(e.getSource())).getName();
		switch (btnName) {
		case Constants.TXT_START_BTN:
			startGame();
			break;
		case Constants.TXT_LOAD_GAME_BTN:
			loadGame();
			break;
		case Constants.TXT_EXIT_BTN:
			System.exit(0);
			break;
		case Constants.TXT_PLAY_SOUND_BTN:
			game.resumeInitialSound();
			break;
		case Constants.TXT_SILENCE_SOUND_BTN:
			game.pauseInitialSound();
			break;
		}
	}
	
	private void startGame() {
		game.stopInitialSound();
		try {
			if(!game.getShowHistory()) {
				verifyHistory();
			}else{
				initialFrame.initOptionsPanel();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void verifyHistory() throws IOException {
		initialFrame.initPanelHistory(FileManager.readHistory());
		historyTimer = new Timer(100, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(initialFrame.getFinish()) {
					historyTimer.stop();
					initialFrame.initOptionsPanel();
				}
			}
		});
		historyTimer.start();	
	}
	
	private void loadGame() {
		game.stopInitialSound();
		try {
			game = FileManager.readData();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		initGame();
	}
	
	private void verifyGameOverOptions(ActionEvent e) {
		String btnName = ((JButton)(e.getSource())).getName();
		switch (btnName) {
		case Constants.TXT_RESTART_BTN:
			restartGame();
			hideGameOverDialog();
			break;
		case Constants.TXT_START_PANEL_BTN:
			goToInitialPanel();
			hideGameOverDialog();
			break;
		case Constants.TXT_SHOW_SCRERNSHOT_BTN:
			showScreenshotsDialog();
			break;
		case Constants.TXT_EXIT_GAME_BTN:
			System.exit(0);
			break;
		}
	}
	
	private void hideGameOverDialog() {
		gameOverDialog.dispose();
		game.stopGameOverSound();
	}
	
	private void restartGame() {
		stopGameLoop();
		game.stopEntities();
		game.stopGameSound();
		pause = false;
		try {
			game.restartGame();
			game.initGameSound();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		gameLoop.start();
		game.moveEntities();
	}
	
	private void goToInitialPanel() {
		stopGameLoop();
		game.stopEntities();
		game.stopGameSound();
		window.setVisible(false);
		initialFrame.initInitialPanel();
		initialFrame.setVisible(true);
		try {
			game.initInitialSound();
			game.restartGame();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void showScreenshotsDialog() {
		try {
			screeenShotsDialog = new ScreeenShotsDialog(game.getScreenshots(), this);
			screeenShotsDialog.setVisible(true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void verifyPausedGameOptions(ActionEvent e) {
		String btnName = ((JButton)(e.getSource())).getName();
		switch (btnName) {
		case Constants.TXT_RESTART_BTN_PAUSE_PANEL:
			restartGame();
			pauseDialog.dispose();
			break;
		case Constants.TXT_START_BTN:
			goToInitialPanel();
			pauseDialog.dispose();
			break;
		case Constants.TXT_LOAD_BTN_PAUSE_PANEL:
			loadGameFromPauseDialog();
			break;
		case Constants.TXT_PLAY_SOUND_BTN:
			game.resumeGameSound();
			break;
		case Constants.TXT_SILENCE_SOUND_BTN:
			game.pauseGameSound();
			break;
		case Constants.TXT_CONTINUE_BTN_PAUSE_PANEL:
			resumeGame();
			break;
		}
	}
	
	private void resumeGame() {
		if(pause) {
			pauseDialog.dispose();
			pause = false;
			game.resumeEntities();
		}
	}
	
	private void loadGameFromPauseDialog() {
		stopGameLoop();
		game.stopEntities();
		game.stopGameSound();
		pauseDialog.dispose();
		window.dispose();
		try {
			this.game = FileManager.readData();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		initGame();
	}
	
	private void verifyTutorialOptions(ActionEvent e) {
		String btnName = ((JButton)(e.getSource())).getName();
		switch (btnName) {
			case Constants.TXT_TUTORIAL_BTN:
				verifyTutorial();
				break;
			case Constants.TXT_CONTINUE_BTN_TUTORIAL:
				initialFrame.setVisible(false);
				initGame();
				break;
		}
	}
	
	private void verifyTutorial() {
		initialFrame.initTutorialPanel();
		tutorialTimer = new Timer(100, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(initialFrame.getFinishTutorial()) {
					tutorialTimer.stop();
					initialFrame.setVisible(false);
					initGame();
				}
			}
		});
		tutorialTimer.start();
	}
	
	private void verifyGameWinOptions(ActionEvent e) {
		String btnName = ((JButton)(e.getSource())).getName();
		switch (btnName) {
			case Constants.TXT_PLAY_AGAIN_BTN:
				restartGame();
				hideGameWonDialog();
				break;
			case Constants.TXT_START_PANEL_BTN:
				goToInitialPanel();
				hideGameWonDialog();
				break;
			case Constants.TXT_SHOW_SCRERNSHOT_BTN:
				showScreenshotsDialog();
				break;
			case Constants.TXT_EXIT_GAME_BTN:
				System.exit(0);
				break;
		}
	}
	
	public void initGameWinDialog() {
		try {
			gameWinDialog = new GameWinDialog(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameWinDialog.setVisible(true);
	}
	
	public void hideGameWonDialog() {
		gameWinDialog.dispose();
		game.stopGameWonSound();
	}
	
	private void stopGameLoop() {
		if(gameLoop != null) {
			gameLoop.stop();
		}
	}
	
	private void verifyAlkostoDialog() {
		if(!buying && alkostoDialog != null) {
			alkostoDialog.setVisible(false);
		}
	}
}
