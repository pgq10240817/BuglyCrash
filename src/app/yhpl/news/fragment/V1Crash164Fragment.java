package app.yhpl.news.fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import app.yhpl.news.R;

public class V1Crash164Fragment extends V1NormalFragment implements SurfaceTextureListener {

	private TextureView mTextureView;
	private Surface mSurface;
	private Rect mRect;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initViews(View root) {
		super.initViews(root);
		mTextureView = (TextureView) root.findViewById(R.id.surfaceview);
		mTextureView.setSurfaceTextureListener(this);

	}

	@Override
	public int getLayoutRes() {
		return R.layout.v1_fragment_crash_164;
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		mSurface = new Surface(surface);
		mRect = new Rect(0, 0, width, height);
		Canvas c = mSurface.lockCanvas(mRect);
		c.drawColor(Color.RED);
		c = mSurface.lockCanvas(mRect);

	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		return false;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {

	}
}
