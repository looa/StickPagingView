package org.looa.ndkencode;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.liaoinstan.springview.widget.SpringView;

import org.looa.tabview.adapter.SimpleTabAdapter;
import org.looa.tabview.widget.TabView;

import java.util.ArrayList;
import java.util.List;

/**
 * 粘性下拉的商品详情页面。
 * <p>
 * Created by ranxiangwei on 2017/2/15.
 */

public class StickyPagingView extends LinearLayout implements Animator.AnimatorListener {

    private Context context;
    private View page1;
    private View page2;
    private SpringView svInfo;//商品的信息页
    private SpringView svDetail;//商品的web详情页
    private TabView tabView;
    private WebView webView;

    private StickySideView infoHeader, detailHeader;
    private StickySideView infoFooter, detailFooter;

    private boolean isFinishAnim = true;
    private int viewHeight;
    private SimpleTabAdapter adapter;
    private List<String> list;

    public StickyPagingView(Context context) {
        this(context, null);
    }

    public StickyPagingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyPagingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        page1 = layoutInflater.inflate(R.layout.sticky_paging_info, null);
        page2 = layoutInflater.inflate(R.layout.sticky_paging_detail, null);

        tabView = (TabView) page2.findViewById(R.id.tab_detail);
        tabView.setAutoFillParent(true);
        tabView.setSmooth(true);
        tabView.setBashLineColor(Color.LTGRAY);

        adapter = new SimpleTabAdapter(context);
        list = new ArrayList<>();
        adapter.setData(list);
        tabView.setAdapter(adapter);
        tabView.setTabCurPosition(0, false);

        webView = (WebView) page2.findViewById(R.id.wv_detail);

        infoHeader = new StickySideView(StickySideView.HEADER);
        infoFooter = new StickySideView(StickySideView.FOOTER);

        detailHeader = new StickySideView(StickySideView.HEADER);
        detailFooter = new StickySideView(StickySideView.FOOTER);

        svInfo = (SpringView) page1.findViewById(R.id.sv_info);
        svDetail = (SpringView) page2.findViewById(R.id.sv_detail);

        svInfo.setHeader(infoHeader);
        svInfo.setFooter(infoFooter);
        svInfo.setType(SpringView.Type.FOLLOW);
        svInfo.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                svInfo.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                svInfo.onFinishFreshAndLoad();
            }
        });
        infoFooter.setOnReachLimitListener(new OnReachLimitListener() {
            @Override
            public void onReached() {
                if (list.size() == 0) tempData();
                moveDown();
            }
        });

        svDetail.setHeader(detailHeader);
        svDetail.setFooter(detailFooter);
        svDetail.setType(SpringView.Type.FOLLOW);
        svDetail.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                svDetail.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                svDetail.onFinishFreshAndLoad();
            }
        });
        detailHeader.setOnReachLimitListener(new OnReachLimitListener() {
            @Override
            public void onReached() {
                moveUp();
            }
        });
        addView(page1);
        addView(page2);
    }

    private void tempData() {
        list.add("商品详情");
        list.add("规格参数");
        list.add("包装售后");
        adapter.notifyDataSetChanged();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("https://github.com");
    }

    public void moveDown() {
        move(true);
    }

    public void moveUp() {
        move(false);
    }

    private void move(boolean isNext) {
        if (!isFinishAnim) return;
        LayoutParams params = (LayoutParams) page1.getLayoutParams();
        LayoutParamsWarpper warpper = new LayoutParamsWarpper(params);
        int page1TopMargin = params.topMargin;
        if (page1TopMargin == 0 && isNext) {
            ObjectAnimator animator = ObjectAnimator.ofInt(warpper, "marginTop", 0, -viewHeight);
            animator.setDuration(300);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(this);
            animator.start();
        } else if (page1TopMargin != 0 && !isNext) {
            ObjectAnimator animator = ObjectAnimator.ofInt(warpper, "marginTop", -viewHeight, 0);
            animator.setDuration(300);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(this);
            animator.start();
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        isFinishAnim = false;
        if (page1.getParent() == null) {
            attachViewToParent(page1, 0, page1.getLayoutParams());
        }
    }

    private boolean isChanged = false;

    @Override
    public void onAnimationEnd(Animator animation) {
        isFinishAnim = true;
        resetPagePosition();
        if (!isChanged && page1.getParent() != null) {
            detachViewFromParent(page1);
            isChanged = true;
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        isFinishAnim = true;
        resetPagePosition();
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    /**
     * 发生过动画中断现象，很难复现，没搞定什么情况，在动画结束或者消失的时候做一个重置比较保险
     */
    private void resetPagePosition() {
        LayoutParams params = (LayoutParams) page1.getLayoutParams();
        int topMargin = params.topMargin;
        if (topMargin <= (-viewHeight / 2f)) {
            params.topMargin = -viewHeight;
        } else {
            params.topMargin = 0;
        }
        page1.setLayoutParams(params);
    }

    /**
     * 包装LayoutParams，便于ObjectAnimator调用
     */
    private class LayoutParamsWarpper {
        private LayoutParams params;

        LayoutParamsWarpper(LayoutParams params) {
            this.params = params;
        }

        public void setMarginTop(int marginTop) {
            params.topMargin = marginTop;
            page1.setLayoutParams(params);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if ((viewHeight = getHeight()) != 0) {
            page1.getLayoutParams().height = viewHeight;
            page2.getLayoutParams().height = viewHeight;
        }
    }
}
