package core.base.utils;

import java.io.File;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import core.base.log.L;
import core.base.utils.mp3recorder.MP3Recorder;

/**
 * @author: 杨强辉
 * @类 说 明:录制语音消息
 * @version 1.0
 * @创建时间：2014-11-7 下午9:13:51
 * 
 */
public class VoiceRecorder {
	private MP3Recorder recorder;
	private boolean isRecording = false;
	private long startTime;
	private String voiceFilePath = null;
	private String voiceFileName = null;
	private File file;
	private long pressToSpeakTime;

	public String startRecording(String userid, Context context) {
		pressToSpeakTime = System.currentTimeMillis();
		L.e(getClass().getName(), "ACTION_DOWN==pressToSpeakTime=" + pressToSpeakTime);
		this.file = null;
		try {
			this.voiceFileName = getVoiceFileName(userid);
			this.voiceFilePath = getVoiceFilePath();
			this.file = new File(this.voiceFilePath);
			this.recorder = new MP3Recorder(file);
			this.isRecording = true;
			this.recorder.start();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("voice", "prepare() failed");
		}
		this.startTime = new Date().getTime();
		Log.e("voice", "start voice recording to file:" + this.file.getAbsolutePath());
		return this.file == null ? null : this.file.getAbsolutePath();
	}

	public void discardRecording() {
		long time = System.currentTimeMillis() - pressToSpeakTime;
        L.e(getClass().getName(), "ACTION_UP--time>200=" + time);
		if (this.recorder != null) {
			try {
				this.recorder.stop();
				// this.recorder.release();
				this.recorder = null;
				if ((this.file != null) && (this.file.exists()) && (!this.file.isDirectory()))
					this.file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.isRecording = false;
		}
	}

	public int stopRecoding() {
		if (this.recorder != null) {
			this.isRecording = false;
			this.recorder.stop();
			// this.recorder.release();
			this.recorder = null;
			int i = (int) (new Date().getTime() - this.startTime) / 1000;
			Log.e("voice", "voice recording finished. seconds:" + i + " file length:" + new File(this.voiceFilePath).length());
			return i;
		}
		return 0;
	}

	protected void finalize() throws Throwable {
		super.finalize();
		if (this.recorder != null) {
		}
		// this.recorder.release();
	}

	public String getVoiceFileName(String userid) {
		Time localTime = new Time();
		localTime.setToNow();
		return userid + localTime.toString().substring(0, 15) + ".mp3";
	}

	public boolean isRecording() {
		return this.isRecording;
	}

	public String getVoiceFilePath() {
		return Environment.getExternalStorageDirectory() + "/" + this.voiceFileName;
	}

	public MP3Recorder getMp3Recorder() {
		return recorder;
	}
}
