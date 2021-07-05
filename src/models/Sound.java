package models;

import java.net.URISyntaxException;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Sound {
	
	private Media media;
	private MediaPlayer mediaPlayer;

	public Sound(String file, boolean repeat) throws URISyntaxException {
		@SuppressWarnings("unused")
		JFXPanel jfxPanel = new JFXPanel();
		media = new Media(getClass().getResource(file).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		if(repeat) {
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		}
	}	

	public Sound(String file, long initialTime ,boolean repeat) throws URISyntaxException {
		@SuppressWarnings("unused")
		JFXPanel jfxPanel = new JFXPanel();
		media = new Media(getClass().getResource(file).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setStartTime(new Duration(initialTime));
		if(repeat) {
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		}
	}
	
	public void playSong() {
		mediaPlayer.play();
	}

	public void stopSong() {
		mediaPlayer.stop();
	}

	public void pauseSong() {
		mediaPlayer.pause();
	}
	
	public void setVolume(double volume) {
		mediaPlayer.setVolume(volume);
	}
	
	public Duration getTime() {
		return mediaPlayer.getCurrentTime(); 
	}
}
