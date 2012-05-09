package com.hakka.pluto.example;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.examples.adt.card.Card;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import com.hakka.pluto.example.module.Puzzle2;

import android.os.Bundle;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga
 * 
 * @author Nicolas Gramlich
 * @since 15:44:58 - 05.11.2010
 */
public class PradaPuzzleGameScene extends SimpleBaseGameActivity implements
		IOnSceneTouchListener, IScrollDetectorListener,
		IPinchZoomDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private ZoomCamera mZoomCamera;
	private BitmapTextureAtlas mCardDeckTexture;

	private Scene mScene;

	private HashMap<Puzzle2, ITextureRegion> mCardTotextureRegionMap;
	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mPausedTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mZoomCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), this.mZoomCamera);

		if (MultiTouch.isSupported(this)) {
			if (MultiTouch.isSupportedDistinct(this)) {
				Toast.makeText(
						this,
						"MultiTouch detected --> Both controls will work properly!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						this,
						"MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(
					this,
					"Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.",
					Toast.LENGTH_LONG).show();
		}

		
		// init the puzzle object 
		Bundle bundle = this.getIntent().getExtras();
		try {
			Class.forName(bundle.getString("puzzleClass"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return engineOptions;
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mCardDeckTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 1024, 512, TextureOptions.BILINEAR);
		BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mCardDeckTexture, this, Puzzle2.getResourceName(), 0, 0);
		this.mCardDeckTexture.load();

		this.mCardTotextureRegionMap = new HashMap<Puzzle2, ITextureRegion>();
		
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mPausedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "paused.png", 0, 0);
		this.mBitmapTextureAtlas.load();

		/* Extract the TextureRegion of each card in the whole deck. */
		for (final Puzzle2 puzzle : Puzzle2.values()) {
			final ITextureRegion cardTextureRegion = TextureRegionFactory
					.extractFromTexture(this.mCardDeckTexture,
							puzzle.getTexturePositionX(),
							puzzle.getTexturePositionY(), Puzzle2.PUZZLE_WIDTH,
							Puzzle2.PUZZLE_HEIGHT);
			this.mCardTotextureRegionMap.put(puzzle, cardTextureRegion);
		}
	}

	private Random random = new Random();
	private Vector<PuzzleSprite> puzzles = new Vector<PuzzleSprite>();
	private Rectangle rootMap;
	private Rectangle finishedPuzzle;
	private CameraScene mPauseScene;
	private Enumeration<PuzzleSprite> tmpEnumer;

	
	private void finish(IShape puzzle){
		this.mScene.detachChild(puzzle);
		this.mScene.unregisterTouchArea(puzzle);
		this.finishedPuzzle.attachChild(puzzle);
	}
	
	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mPauseScene = new CameraScene(this.mZoomCamera);
		/* Make the 'PAUSED'-label centered on the camera. */
		final float centerX = (CAMERA_WIDTH - this.mPausedTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mPausedTextureRegion.getHeight()) / 2;
		final Sprite pausedSprite = new Sprite(centerX, centerY, this.mPausedTextureRegion, this.getVertexBufferObjectManager());
		this.mPauseScene.attachChild(pausedSprite);
		/* Makes the paused Game look through. */
		this.mPauseScene.setBackgroundEnabled(false);

		this.mScene = new Scene();
		this.rootMap = new Rectangle(0, 0, 1,1,
				getVertexBufferObjectManager());
	    this.finishedPuzzle = new Rectangle(0, 0, 1,1,
				getVertexBufferObjectManager());
		
		mScene.attachChild(rootMap);
		mScene.attachChild(finishedPuzzle);
		this.mScene.setOnAreaTouchTraversalFrontToBack();
		Rectangle r;

		// int count ;
		for (final Puzzle2 puzzle : Puzzle2.values()) {

			r = new Rectangle(puzzle.getTexturePositionX(),
					puzzle.getTexturePositionY(), Puzzle2.PUZZLE_WIDTH - 1,
					Puzzle2.PUZZLE_HEIGHT - 1,
					this.getVertexBufferObjectManager());
			rootMap.attachChild(r);
			puzzle.setAnswer(r);
			PuzzleSprite ps = this.addPuzzle(puzzle, random.nextInt(500),
					random.nextInt(500));
			puzzles.add(ps);
		}

		this.mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mPinchZoomDetector = new PinchZoomDetector(this);

		this.mScene.setOnSceneTouchListener(this);
		this.mScene.setTouchAreaBindingOnActionDownEnabled(true);

		/* The actual collision-checking. */
		mScene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {
			}

//			private Enumeration<PuzzleSprite> tmpEnumer;

			@Override
			public void onUpdate(final float pSecondsElapsed) {
//				tmpEnumer = puzzles.elements();
//				PuzzleSprite tmp;
//				while (tmpEnumer.hasMoreElements()) {
//					tmp = tmpEnumer.nextElement();
//					if (tmp.isCollide()) {
//						tmp.bgClick();
//					} else {
//						tmp.bgUnClick();
//					}
//				}
				
				
				/** check the puzzle game is success */
//				checkTheGameState();
				
					
			}

			
		});
		return this.mScene;
	}

	private void checkTheGameState() {
		tmpEnumer = puzzles.elements();
		PuzzleSprite tmp;
		boolean isSuccess = true;
		while (tmpEnumer.hasMoreElements()) {
			tmp = tmpEnumer.nextElement();
			if (tmp.isCollide()==false) {
				isSuccess = false;
				break;
			}
		}
		if(isSuccess){
			mScene.setChildScene(mPauseScene, false, true, true);
			mEngine.stop();
		}
	}
	
	@Override
	public void onScrollStarted(final ScrollDetector pScollDetector,
			final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY
				/ zoomFactor);
	}

	@Override
	public void onScroll(final ScrollDetector pScollDetector,
			final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY
				/ zoomFactor);
	}

	@Override
	public void onScrollFinished(final ScrollDetector pScollDetector,
			final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY
				/ zoomFactor);
	}

	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector,
			final TouchEvent pTouchEvent) {
		this.mPinchZoomStartedCameraZoomFactor = this.mZoomCamera
				.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector,
			final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor
				* pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector,
			final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor
				* pZoomFactor);
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

		if (this.mPinchZoomDetector.isZooming()) {
			this.mScrollDetector.setEnabled(false);
		} else {
			if (pSceneTouchEvent.isActionDown()) {
				this.mScrollDetector.setEnabled(true);
			}
			this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}

		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private PuzzleSprite addPuzzle(final Puzzle2 pCard, final int pX,
			final int pY) {
		final PuzzleSprite sprite = new PuzzleSprite(pX, pY,
				this.mCardTotextureRegionMap.get(pCard),
				this.getVertexBufferObjectManager()) {
			boolean mGrabbed = false;
			boolean isFinish = false;

			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (this.isFinish == false) {
					switch (pSceneTouchEvent.getAction()) {
					case TouchEvent.ACTION_DOWN:
						this.setScale(1.25f);
						this.mGrabbed = true;
						break;
					case TouchEvent.ACTION_MOVE:
						if (this.mGrabbed) {
							this.setPosition(pSceneTouchEvent.getX()
									- Card.CARD_WIDTH / 2,
									pSceneTouchEvent.getY() - Card.CARD_HEIGHT
											/ 2);
						}
						break;
					case TouchEvent.ACTION_UP:
						if (this.mGrabbed) {
							this.mGrabbed = false;
							this.setScale(1.0f);
							if(this.isCollide()){
								this.setPosition(this.getPuzzle().getAnswer());
								finish(this);
								this.isFinish = true;
								checkTheGameState();
							}
						}
						break;
					}
				}
				return true;
			}
		};
		sprite.setPuzzle(pCard);
		this.mScene.attachChild(sprite);
		this.mScene.registerTouchArea(sprite);
		return sprite;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
