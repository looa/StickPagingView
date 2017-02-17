package org.looa.ndkencode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liaoinstan.springview.container.BaseHeader;

/**
 * 上拉或者下拉头
 * <p>
 * Created by ranxiangwei on 2017/2/16.
 */

public class StickyEmptyHeaderFooterView extends BaseHeader {

    public StickyEmptyHeaderFooterView() {
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.sticky_paging_empty, viewGroup, true);
    }

    @Override
    public void onPreDrag(View rootView) {

    }

    @Override
    public void onDropAnim(View rootView, int dy) {

    }

    @Override
    public void onLimitDes(View rootView, boolean upORdown) {
    }

    @Override
    public void onStartAnim() {
    }

    @Override
    public void onFinishAnim() {

    }
}
