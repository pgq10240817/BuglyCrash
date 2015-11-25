package com.viewpagerindicator;

public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);

    // From PagerAdapter
    int getCount();

    int getBackGroud();

    /**
     * 
     * @Title: getDefaultIconResId
     * @Description: ���Ĭ��ͼƬ
     * @date 2014-9-10
     * @version 1.0
     */
    int getDefaultIconResId(int index);

    /**
     * 
     * @Title: getDefaultIconResId
     * @Description: ���ͼƬ��url
     * @date 2014-9-10
     * @version 1.0
     */
    String getImageUrl(int index);
}
