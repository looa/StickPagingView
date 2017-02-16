package org.looa.ndkencode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liaoinstan.springview.container.BaseHeader;

/**
 * Created by ranxiangwei on 2017/2/16.
 */

public class StickySideView extends BaseHeader {

    private OnReachLimitListener listener;

    public final static int HEADER = 1;
    public final static int FOOTER = 2;

    private int type;

    public StickySideView(int type) {
        this.type = type;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = null;
        if (type == HEADER) view = inflater.inflate(R.layout.sticky_paging_header, viewGroup, true);
        else if (type == FOOTER) view = inflater.inflate(R.layout.sticky_paging_footer, viewGroup, true);
        return view;
    }

    public void setOnReachLimitListener(OnReachLimitListener listener) {
        this.listener = listener;
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
        if (listener != null) listener.onReached();
    }

    @Override
    public void onFinishAnim() {

    }


}
