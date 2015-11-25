/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
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
package com.viewpagerindicator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.yhpl.news.R;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements PageIndicator {
	/** Title text used when no title is provided by the adapter. */
	private static final CharSequence EMPTY_TITLE = "";
	private int mTxtSly;

	/**
	 * Interface for a callback when the selected tab has been reselected.
	 */
	public interface OnTabReselectedListener {
		/**
		 * Callback when the selected tab has been reselected.
		 * 
		 * @param position
		 *            Position of the current center item.
		 */
		void onTabReselected(int position);

		boolean isTabResSelectAble(int postion);
	}

	private Runnable mTabSelector;

	private final OnClickListener mTabClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			TabView tabView = (TabView) view;
			final int oldSelected = mViewPager.getCurrentItem();
			final int newSelected = tabView.getIndex();
			if (mTabReselectedListener != null) {
				boolean isSelectAble = mTabReselectedListener.isTabResSelectAble(newSelected);
				if (!isSelectAble) {
					return;
				}
			}
			mViewPager.setCurrentItem(newSelected);
			if (oldSelected == newSelected && mTabReselectedListener != null) {
				mTabReselectedListener.onTabReselected(newSelected);
			}
		}
	};

	private final IcsLinearLayout mTabLayout;

	private ViewPager mViewPager;
	private ViewPager.OnPageChangeListener mListener;

	private int mMaxTabWidth;
	private int mSelectedTabIndex;

	private OnTabReselectedListener mTabReselectedListener;

	public TabPageIndicator(Context context) {
		this(context, null);
	}

	public TabPageIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.vpiTabPageIndicatorStyle);
	}

	public TabPageIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setHorizontalScrollBarEnabled(false);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabPageIndicator, defStyle, 0);
		mTxtSly = a.getResourceId(R.styleable.TabPageIndicator_tabPageIndicatorTxtSly, R.attr.vpiTabPageIndicatorStyle);
		a.recycle();
		mTabLayout = new IcsLinearLayout(context, mTxtSly);
		addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
	}

	public void setOnTabReselectedListener(OnTabReselectedListener listener) {
		mTabReselectedListener = listener;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();

		if (lockedExpanded && oldWidth != newWidth && mViewPager != null) {
			// Recenter the tab display if we're at a new (scrollable) size.
			setCurrentItem(mSelectedTabIndex);
		}
	}

	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			@Override
			public void run() {
				final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}

	private void addTab(int index, CharSequence text, int iconResId, int bgResid, int iconDefaultId,
			String imageHttpUrl) {
		final TabView tabView = new TabView(getContext());
		tabView.setBackgroundResource(bgResid);
		tabView.mIndex = index;
		tabView.setFocusable(true);
		// tabView.setSingleLine(false);
		// tabView.setMaxLines(2);
		tabView.setEllipsize(TruncateAt.END);
		tabView.setOnClickListener(mTabClickListener);
		tabView.setText(text);
		if (iconResId != 0) {
			tabView.setCompoundDrawablesWithIntrinsicBounds(0, iconResId, 0, 0);
		} else if (!TextUtils.isEmpty(imageHttpUrl)) {

			// ImageLoaderUtil.getIntance(getContext()).loadImageFromUrl(iconDefaultId,
			// iconDefaultId, null, imageHttpUrl,
			// new ImageLoadingListener() {
			// @Override
			// public void onLoadingStarted(String arg0, View arg1) {
			// }
			//
			// @Override
			// public void onLoadingFailed(String arg0, View arg1, FailReason
			// arg2) {
			// }
			//
			// @Override
			// public void onLoadingComplete(String arg0, View arg1, Bitmap
			// bitmap) {
			//
			// Drawable drawable = new BitmapDrawable(bitmap);
			// tabView.setCompoundDrawablesWithIntrinsicBounds(null, drawable,
			// null, null);
			// }
			//
			// @Override
			// public void onLoadingCancelled(String arg0, View arg1) {
			// }
			// });
		}
		mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
	}

	private void addTab(int index, CharSequence text, int iconResId, int iconDefaultId, String imageHttpUrl) {
		final TabView tabView = new TabView(getContext());
		// tabView.setBackgroundResource(bgResid);
		tabView.mIndex = index;
		tabView.setFocusable(true);
		tabView.setOnClickListener(mTabClickListener);
		tabView.setText(text);
		if (iconResId != 0) {
			tabView.setCompoundDrawablesWithIntrinsicBounds(0, iconResId, 0, 0);
		} else if (!TextUtils.isEmpty(imageHttpUrl)) {
			//
			// ImageLoaderUtil.getIntance(getContext()).loadImageFromUrl(iconDefaultId,
			// iconDefaultId, null, imageHttpUrl,
			// new ImageLoadingListener() {
			// @Override
			// public void onLoadingStarted(String arg0, View arg1) {
			// }
			//
			// @Override
			// public void onLoadingFailed(String arg0, View arg1, FailReason
			// arg2) {
			// }
			//
			// @Override
			// public void onLoadingComplete(String arg0, View arg1, Bitmap
			// bitmap) {
			//
			// Drawable drawable = new BitmapDrawable(bitmap);
			// tabView.setCompoundDrawablesWithIntrinsicBounds(null, drawable,
			// null, null);
			// }
			//
			// @Override
			// public void onLoadingCancelled(String arg0, View arg1) {
			// }
			// });
		}
		mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (mListener != null) {
			mListener.onPageScrollStateChanged(arg0);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (mListener != null) {
			mListener.onPageScrolled(arg0, arg1, arg2);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentItem(arg0);
		if (mListener != null) {
			mListener.onPageSelected(arg0);
		}
	}

	@Override
	public void setViewPager(ViewPager view) {
		if (mViewPager == view) {
			return;
		}
		if (mViewPager != null) {
			mViewPager.setOnPageChangeListener(null);
		}
		final PagerAdapter adapter = view.getAdapter();
		if (adapter == null) {
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}
		mViewPager = view;
		view.setOnPageChangeListener(this);
		notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetChanged() {
		mTabLayout.removeAllViews();
		PagerAdapter adapter = mViewPager.getAdapter();
		IconPagerAdapter iconAdapter = null;
		if (adapter instanceof IconPagerAdapter) {
			iconAdapter = (IconPagerAdapter) adapter;
		}
		final int count = adapter.getCount();
		int mBackGroud = -1;
		if (iconAdapter != null) {
			mBackGroud = iconAdapter.getBackGroud();
		}
		for (int i = 0; i < count; i++) {
			CharSequence title = adapter.getPageTitle(i);
			if (title == null) {
				title = EMPTY_TITLE;
			}
			int iconResId = 0;
			if (iconAdapter != null) {
				iconResId = iconAdapter.getIconResId(i);
			}
			if (mBackGroud > 0) {
				// addTab(i, title, iconResId, mBackGroud);
				addTab(i, title, iconResId, mBackGroud, iconAdapter.getDefaultIconResId(i), iconAdapter.getImageUrl(i));
			} else {
				// addTab(i, title, iconResId);
				addTab(i, title, iconResId, iconAdapter.getDefaultIconResId(i), iconAdapter.getImageUrl(i));
			}
		}
		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
	}

	@Override
	public void setViewPager(ViewPager view, int initialPosition) {
		setViewPager(view);
		setCurrentItem(initialPosition);
	}

	public void clearViewPager() {
		if (mViewPager != null) {
			mViewPager.setOnPageChangeListener(null);
			mViewPager = null;
		}
	}

	@Override
	public void setCurrentItem(int item) {
		if (mViewPager == null) {
			throw new IllegalStateException("ViewPager has not been bound.");
		}
		mSelectedTabIndex = item;
		mViewPager.setCurrentItem(item);

		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				animateToTab(item);
			}
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mListener = listener;
	}

	private class TabView extends TextView {
		private int mIndex;

		public TabView(Context context) {
			super(context, null, mTxtSly);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			// Re-measure if we went beyond our maximum size.
			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			Drawable[] drawables = getCompoundDrawables();
			Drawable drawableTop = drawables[1];
			if (drawableTop != null) {
				float txtHeight = getPaint().descent() - getPaint().ascent();
				int drawablePadding = getCompoundDrawablePadding();
				int height = 0;
				height = drawableTop.getIntrinsicHeight();
				float contentHeight = txtHeight + height + drawablePadding;
				setPadding(0, (int) (getHeight() - contentHeight) / 2, 0, 0);
				// canvas.translate(0, (getHeight() - contentHeight) / 2);
			}
			super.onDraw(canvas);
		}

		public int getIndex() {
			return mIndex;
		}
	}
}
