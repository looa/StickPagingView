package org.looa.ndkencode;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranxiangwei on 2017/2/21.
 */

public class StickyListPageView extends LinearLayout implements Animator.AnimatorListener {

    private Context context;
    /**
     * 头指针，始终用来标记第一个页面
     */
    private View header;
    private View page1;
    private View page2;
    private SpringView svInfo;//商品的信息页
    private SpringView svInfo2;//商品的web详情页

    private boolean isFinishAnim = true;
    private int viewHeight;
    private int position = 3;
    private List<StickyPageInfo> list;

    private DynamicFillManager manager;
    private TextView tvContent, tvContent2;

    public StickyListPageView(Context context) {
        this(context, null);
    }

    public StickyListPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyListPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        manager = new DynamicFillManager();
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        page1 = layoutInflater.inflate(R.layout.sticky_paging_info, null);
        page2 = layoutInflater.inflate(R.layout.sticky_paging_info, null);

        tvContent = (TextView) page1.findViewById(R.id.tv_content);
        tvContent2 = (TextView) page2.findViewById(R.id.tv_content);

        manager.setParent(this);
        manager.setFillPage(page1);
        manager.setFreePage(page2);

        list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(new StickyPageInfo("page" + i, ""));
        }

        StickyHeaderFooterView infoHeader = new StickyHeaderFooterView(StickyHeaderFooterView.HEADER);
        StickyHeaderFooterView infoFooter = new StickyHeaderFooterView(StickyHeaderFooterView.FOOTER);

        StickyHeaderFooterView detailHeader = new StickyHeaderFooterView(StickyHeaderFooterView.HEADER);
        StickyHeaderFooterView detailFooter = new StickyHeaderFooterView(StickyHeaderFooterView.FOOTER);

        header = svInfo = (SpringView) page1.findViewById(R.id.sv_info);
        svInfo2 = (SpringView) page2.findViewById(R.id.sv_info);

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
        infoHeader.setOnReachLimitListener(new OnReachLimitListener() {
            @Override
            public void onReached() {
                moveUp();
            }
        });
        infoFooter.setOnReachLimitListener(new OnReachLimitListener() {
            @Override
            public void onReached() {
                moveDown();
            }
        });

        svInfo2.setHeader(detailHeader);
        svInfo2.setFooter(detailFooter);
        svInfo2.setType(SpringView.Type.FOLLOW);
        svInfo2.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                svInfo2.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                svInfo2.onFinishFreshAndLoad();
            }
        });
        detailHeader.setOnReachLimitListener(new OnReachLimitListener() {
            @Override
            public void onReached() {
                moveUp();
            }
        });
        detailFooter.setOnReachLimitListener(new OnReachLimitListener() {
            @Override
            public void onReached() {
                moveDown();
            }
        });
        addView(page1);
        addView(page2);
    }

    /**
     * 设置自定义的layout，建议该layout的高度为wrap_content
     *
     * @param layoutId R.layout.your_page
     */
    public void setPage1Layout(int layoutId) {
        View scrollView = svInfo.getChildAt(0);
        if (scrollView instanceof ScrollView) {
            ((ScrollView) scrollView).removeAllViews();
            ((ScrollView) scrollView).addView(LayoutInflater.from(context).inflate(layoutId, null));
        }
    }

    public void addAllData(List<StickyPageInfo> list) {
        this.list.addAll(list);
    }

    public void addData(StickyPageInfo data) {
        list.add(data);
    }

    public void moveDown() {
        if (position + 1 >= list.size()) return;
        position++;
        move(true);
        manager.fill(Position.DOWN);
    }

    public void moveUp() {
        if (position - 1 < 0) return;
        position--;
        move(false);
        manager.fill(Position.UP);
    }

    private void move(boolean isNext) {
        if (!isFinishAnim) return;
        if (manager.getFreePage() == page1) tvContent.setText(list.get(position).getName());
        else tvContent2.setText(list.get(position).getName());
        LayoutParams params;
        if (isNext) params = (LayoutParams) manager.getFillPage().getLayoutParams();
        else params = (LayoutParams) manager.getFreePage().getLayoutParams();
        LayoutParamsWarpper warpper = new LayoutParamsWarpper(params);
        int topMargin = params.topMargin;
        if (topMargin == 0 && isNext) {
            ObjectAnimator animator = ObjectAnimator.ofInt(warpper, "marginTop", 0, -viewHeight);
            animator.setDuration(300);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(this);
            animator.start();
        } else if (topMargin != 0 && !isNext) {
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
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        isFinishAnim = true;
        manager.clear();
    }

    @Override
    public void onAnimationCancel(Animator animation) {
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
     * 动态装填view的manager
     */
    private class DynamicFillManager {
        private View fillPage;
        private View freePage;
        private StickyListPageView parent;

        public void setFillPage(View fillPage) {
            this.fillPage = fillPage;
        }

        public void setFreePage(View freePage) {
            this.freePage = freePage;
        }

        public StickyListPageView getParent() {
            return parent;
        }

        public void setParent(StickyListPageView parent) {
            this.parent = parent;
        }

        public View getFillPage() {
            return fillPage;
        }

        public View getFreePage() {
            return freePage;
        }

        /**
         * 自动装填到相应的位置
         *
         * @param position
         */
        public void fill(Position position) {
            View freePage = getFreePage();
            if (freePage.getParent() != null) return;
            LayoutParams params = (LayoutParams) freePage.getLayoutParams();
            if (StickyListPageView.this.position % 2 == 0) freePage.setBackgroundColor(Color.CYAN);
            else freePage.setBackgroundColor(Color.WHITE);
            if (position == Position.DOWN) {
                params.topMargin = 0;
                getParent().attachViewToParent(freePage, -1, params);
            } else {
                params.topMargin = -viewHeight;
                getParent().attachViewToParent(freePage, 0, params);
            }
        }

        /**
         * 移除上一次装填view，重置layoutParams等参数；
         * 在新装填的视图完全显示在parent里面之后调用；
         * 本例中，在onAnimationEnd中调用
         */
        public void clear() {
            View page1 = getFillPage();
            View page2 = getFreePage();
            getParent().detachViewFromParent(page1);//移除pre fill page
            LayoutParams params = (LayoutParams) page2.getLayoutParams();
            params.topMargin = 0;
            page2.setLayoutParams(params);//重置free的marginTop
            setFillPage(page2);//交换free、fill的指针
            setFreePage(page1);
        }
    }

    public enum Position {
        DOWN,
        UP
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
