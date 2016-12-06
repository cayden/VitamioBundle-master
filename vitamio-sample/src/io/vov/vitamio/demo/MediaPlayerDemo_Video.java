/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vov.vitamio.demo;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

public class MediaPlayerDemo_Video extends Activity implements OnInfoListener, OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

	private static final String TAG = "MediaPlayerDemo";
	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private String path;
	private Bundle extras;
	private static final String MEDIA = "media";
	private static final int LOCAL_AUDIO = 1;
	private static final int STREAM_AUDIO = 2;
	private static final int RESOURCES_AUDIO = 3;
	private static final int LOCAL_VIDEO = 4;
	private static final int STREAM_VIDEO = 5;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	String ROOT_SDCARD_DIR = Environment.getExternalStorageDirectory().getPath()+"/";
	//239.0.1.2
	String UDP_TS="udp://@224.0.1.2:6666";
	/**
	 * 
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.mediaplayer_2);
		mPreview = (SurfaceView) findViewById(R.id.surface);
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888); 
		extras = getIntent().getExtras();

	}

	private void playVideo(Integer Media) {
		doCleanUp();
		try {

			switch (Media) {
			case LOCAL_VIDEO:
				/*
				 * TODO: Set the path variable to a local media file path.
				 */
				path=ROOT_SDCARD_DIR+"xiyouji.mp4";
				if (path == "") {
					// Tell the user to provide a media file URL.
					Toast.makeText(MediaPlayerDemo_Video.this, "Please edit MediaPlayerDemo_Video Activity, " + "and set the path variable to your media file path." + " Your media file must be stored on sdcard.", Toast.LENGTH_LONG).show();
					return;
				}
				break;
			case STREAM_VIDEO:
				/*
				 * TODO: Set path variable to progressive streamable mp4 or
				 * 3gpp format URL. Http protocol should be used.
				 * Mediaplayer can only play "progressive streamable
				 * contents" which basically means: 1. the movie atom has to
				 * precede all the media data atoms. 2. The clip has to be
				 * reasonably interleaved.
				 * 
				 */
				path = UDP_TS;
				if (path == "") {
					// Tell the user to provide a media file URL.
					Toast.makeText(MediaPlayerDemo_Video.this, "Please edit MediaPlayerDemo_Video Activity," + " and set the path variable to your media file URL.", Toast.LENGTH_LONG).show();
					return;
				}

				break;

			}

			// Create a new media player and set the listeners
			mMediaPlayer = new MediaPlayer(this,true);

			mMediaPlayer.setDataSource(path);
//			mMediaPlayer.setLooping(true);
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepare();

			mMediaPlayer.setOnInfoListener(this);
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);

			setVolumeControlStream(AudioManager.STREAM_MUSIC);

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
		}
	}
	int count=-1;
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		Log.d(TAG,"what="+what+",extra="+extra);
		switch (what) {
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				// 开始缓冲，如果正在播放，则停止
				Log.d(TAG,"isplaying="+mMediaPlayer.isPlaying()+",count="+count);
//				if(count>0){
//					startVideoPlayback();
//				}
				if (mMediaPlayer.isPlaying()) {
//					mMediaPlayer.pause();
				}
//				mp.start();
//				startVideoPlayback();

				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				// 缓冲完毕，开始播放
				mMediaPlayer.start();
//				startVideoPlayback();
				break;
			case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
				// 下载的速率发生了改变
//				Log.i(TAG,"下载速度:"+extra+"kb/s");
				// setText("" + arg2 + "kb/s" + "  ");
				int progress= mMediaPlayer.getBufferProgress();
				Log.i(TAG,"下载速度:"+extra+"kb/s progress="+progress);
				if(progress==0){
					if(count>6){
						Log.i(TAG,"再次播放:");
//						mMediaPlayer.start();
						VitamioApplication.getInstance().killPid();
					}
					count++;
				}else{
					count=-1;
				}
				break;
		}
		return false;
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
//		 Log.d(TAG, "onBufferingUpdate percent:" + percent);
//		if(percent>=50){
//			mp.start();
//		}
//		else {
//			mp.pause();
//		}

	}

	public void onCompletion(MediaPlayer mp) {
		Log.d(TAG, "onCompletion called");
		try{
//			mMediaPlayer.start();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged called");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
			return;
		}
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void onPrepared(MediaPlayer mediaplayer) {
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		mediaplayer.setBufferSize(512*1024);
		mediaplayer.setAdaptiveStream(true);
		mediaplayer.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);

		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
		Log.d(TAG, "surfaceChanged called");

	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		Log.d(TAG, "surfaceDestroyed called");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated called");
//		playVideo(extras.getInt(MEDIA));
		playVideo(STREAM_VIDEO);
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaPlayer();
		doCleanUp();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		doCleanUp();
	}

	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	private void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
		holder.setFixedSize(mVideoWidth, mVideoHeight);
		mMediaPlayer.start();
	}
}
