package com.coder.kzxt.utils;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * @author Administrator
 * 录音工具类
 */
public  class SoundMeter {
	static final private double EMA_FILTER = 0.6;
	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;

	public void start(Context context, String name) {
		if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(Constants.RECORD+"//"+name);
			try {
				mRecorder.prepare();
				mRecorder.start();
				mEMA = 0.0;
			} catch (IllegalStateException e) {
				Log.v("tangcy", "startRecord",e);
				Toast.makeText(context, "录音失败，请检查后台是否运行其他录音软件。", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				Log.v("tangcy", "startRecord",e);
			}catch (Exception e){
				Log.v("tangcy", "startRecord",e);
				Toast.makeText(context, "录音失败，请检查后台是否运行其他录音软件。", Toast.LENGTH_LONG).show();
			}
		}
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.setOnErrorListener(null);
	         try {
	        	 mRecorder.stop();
	            } catch (IllegalStateException e) {
	                Log.w("tangcy", "stopRecord", e);
	            } catch (RuntimeException e) {
	                Log.w("tangcy", "stopRecord", e);
	            } catch (Exception e) {
	                Log.w("tangcy", "stopRecord", e);
	            }
			
			mRecorder.release();
			mRecorder = null;
		}
	}

	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}

	public void start() {
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;
	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}
}
