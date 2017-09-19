package com.wq.common.widget

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.IntDef
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView


import com.lizhuo.kotlinlearning.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.ref.WeakReference

/**
 */


open class TitleBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = -1) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var btnLeft: View
    private lateinit var imgLeft: ImageView
    private lateinit var txtLeft: TextView

    private lateinit var txtTitle: TextView

    private lateinit var btnRight: View
    private lateinit var imgRight: ImageView
    private lateinit var txtRight: TextView

    private lateinit var btnRight2: View
    private lateinit var imgRight2: ImageView
    private lateinit var txtRight2: TextView


    private lateinit var bottomLine: View

    init {
        inflateRootView()
        initViews()
        setAttrs(context, attrs)
        setTitleBarHeightToWrapStatusBar()
    }

    private fun setTitleBarHeightToWrapStatusBar() {
        if (fitStatusBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            post {
                val statusBarHeight = getStatusBarHeight(context)
                setPadding(0, statusBarHeight, 0, 0)
                val lp = layoutParams
                lp.height += statusBarHeight
                layoutParams = lp
            }
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, STATUS_HEIGHT_DP.toFloat(), context.resources.displayMetrics).toInt()
        }
        return 0
    }

    private var fitStatusBar: Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    private fun setAttrs(context: Context, attrs: AttributeSet?) {
        attrs.let {
            val defaultTextColor = context.resources.getColor(R.color.white)
            var att: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
            val typedArray = context.obtainStyledAttributes(intArrayOf(R.attr.colorPrimary))
            val c = typedArray.getColor(0, Color.WHITE)
            val titleText = att.getString(R.styleable.TitleBar_title)
            val titleColor = att.getColor(R.styleable.TitleBar_titleColor, defaultTextColor)
            val backgroundColor = att.getColor(R.styleable.TitleBar_backgroundColor, c)
            val backgroundRes = att.getDrawable(R.styleable.TitleBar_backgroundDrawable)
            if (backgroundRes != null) {
                setBackgroundDrawable(backgroundRes)
            } else {
                setBackgroundColor(backgroundColor)
            }

            fitStatusBar = att.getBoolean(R.styleable.TitleBar_fitStatusBar, true)

            setTitle(titleText, titleColor)


            val leftIcon = att.getResourceId(R.styleable.TitleBar_leftIcon, -1)
            val leftText = att.getString(R.styleable.TitleBar_leftText)
            val leftTextColor = att.getColor(R.styleable.TitleBar_leftTextColor, defaultTextColor)
            val leftAsFinish = att.getBoolean(R.styleable.TitleBar_leftAsFinish, false)
            val leftVisible = att.getString(R.styleable.TitleBar_leftVisible)
            setLeftVisible(if (TextUtils.equals("1", leftVisible)) View.VISIBLE else View.GONE)
            setLeft(leftIcon, leftText, leftTextColor, if (leftAsFinish && getContext() is Activity) FinishAction(getContext() as Activity) else null)
            val rightIcon = att.getResourceId(R.styleable.TitleBar_rightIcon, -1)
            val rightText = att.getString(R.styleable.TitleBar_rightText)
            val rightTextColor = att.getColor(R.styleable.TitleBar_rightTextColor, defaultTextColor)
            val rightVisible = att.getString(R.styleable.TitleBar_rightVisible)
            setRight(rightIcon, rightText, rightTextColor, null)

            val rightIcon2 = att.getResourceId(R.styleable.TitleBar_rightIcon2, -1)
            val rightText2 = att.getString(R.styleable.TitleBar_rightText2)
            val rightTextColor2 = att.getColor(R.styleable.TitleBar_rightTextColor2, defaultTextColor)
            val rightVisible2 = att.getString(R.styleable.TitleBar_rightVisible2)
            setRight2(rightIcon2, rightText2, rightTextColor2, null)


            val drawable = att.getDrawable(R.styleable.TitleBar_bottomDivider)
            val dividerHeight = att.getDimensionPixelSize(R.styleable.TitleBar_bottomDividerHeight, 2)

            if (drawable != null) {
                bottomLine.setBackgroundDrawable(drawable)
            }
            if (!isInEditMode) {
                var layoutParams: ViewGroup.LayoutParams? = bottomLine.layoutParams
                if (layoutParams == null) {
                    layoutParams = generateDefaultLayoutParams()
                }
                layoutParams?.height = dividerHeight
            }
            att.recycle()

            setRightVisible(if (TextUtils.equals("1", rightVisible)) View.VISIBLE else View.GONE)
            setRight2Visible(if (TextUtils.equals("1", rightVisible)) View.VISIBLE else View.GONE)
        }

    }

    private operator fun <T : View> get(@IdRes id: Int): T {
        return findViewById(id) as T
    }

    private fun initViews() {
        btnLeft = get<View>(R.id.btnLeft)
        imgLeft = get<ImageView>(R.id.imgLeft)
        txtLeft = get<TextView>(R.id.txtLeft)

        txtTitle = get<TextView>(R.id.txtTitle)

        btnRight = get<View>(R.id.btnRight)
        imgRight = get<ImageView>(R.id.imgRight)
        txtRight = get<TextView>(R.id.txtRight)

        btnRight2 = get<View>(R.id.btnRight2)
        imgRight2 = get<ImageView>(R.id.imgRight2)
        txtRight2 = get<TextView>(R.id.txtRight2)

        bottomLine = get<View>(R.id.bottomLine)
    }

    @JvmOverloads fun setTitle(title: String?, @ColorInt textColor: Int = Color.TRANSPARENT): TitleBar {
        if (textColor != Color.TRANSPARENT) {
            this.txtTitle.setTextColor(textColor)
        }
        this.txtTitle.text = title
        return this
    }

    fun setLeftVisible(@Visibility visibility: Int) {
        this.btnLeft.visibility = visibility
    }

    fun setRightVisible(@Visibility visibility: Int) {
        this.btnRight.visibility = visibility
    }


    fun setRight2Visible(@Visibility visibility: Int) {
        this.btnRight2.visibility = visibility
    }

    fun setLeftAction(click: View.OnClickListener?): TitleBar {
        if (click == null) return this
        btnLeft.visibility = View.VISIBLE
        this.btnLeft.setOnClickListener(click)
        return this
    }

    fun setLeftIcon(@DrawableRes icon: Int): TitleBar {
        if (icon == -1) {
            val layoutParams = txtLeft.layoutParams as ViewGroup.MarginLayoutParams
            if (layoutParams != null) {
                layoutParams.leftMargin = 0
            }

            btnLeft.visibility = View.GONE
            return this
        }
        btnLeft.visibility = View.VISIBLE
        this.imgLeft.setImageResource(icon)
        return this
    }

    fun setLeftText(leftText: String?): TitleBar {
        if (TextUtils.isEmpty(leftText)) return this
        btnLeft.visibility = View.VISIBLE
        this.txtLeft.text = leftText
        return this
    }

    fun setLeft(@DrawableRes icon: Int, text: String?, @ColorInt color: Int, click: View.OnClickListener?): TitleBar {
        return this.setLeftIcon(icon).setLeftText(text).setLeftTextColor(color).setLeftAction(click)
    }

    private fun setLeftTextColor(color: Int): TitleBar {
        this.txtLeft.setTextColor(color)
        return this
    }

    fun setRightAction2(click: View.OnClickListener?): TitleBar {
        if (click == null) return this
        btnRight2.visibility = View.VISIBLE
        this.btnRight2.setOnClickListener(click)
        return this
    }

    fun setRightAction(click: View.OnClickListener?): TitleBar {
        if (click == null) return this
        btnRight.visibility = View.VISIBLE
        this.btnRight.setOnClickListener(click)
        return this
    }


    fun setRightIcon2(@DrawableRes icon: Int): TitleBar {
        if (icon == -1) return this
        btnRight2.visibility = View.VISIBLE
        this.imgRight2.setImageResource(icon)
        return this
    }

    fun setRightIcon(@DrawableRes icon: Int): TitleBar {
        if (icon == -1) return this
        btnRight.visibility = View.VISIBLE
        this.imgRight.setImageResource(icon)
        return this
    }

    fun setRightText2(rightText: String?): TitleBar {
        if (TextUtils.isEmpty(rightText)) return this
        btnRight2.visibility = View.VISIBLE
        this.txtRight2.text = rightText
        return this
    }

    fun setRightText(rightText: String?): TitleBar {
        if (TextUtils.isEmpty(rightText)) return this
        btnRight.visibility = View.VISIBLE
        this.txtRight.text = rightText
        return this
    }

    fun setRight2(@DrawableRes icon: Int, text: String?, @ColorInt color: Int, click: View.OnClickListener?): TitleBar {
        return this.setRightIcon2(icon).setRightText2(text).setRightTextColor2(color).setRightAction2(click)
    }

    fun setRight(@DrawableRes icon: Int, text: String?, @ColorInt color: Int, click: View.OnClickListener?): TitleBar {
        return this.setRightIcon(icon).setRightText(text).setRightTextColor(color).setRightAction(click)
    }

    private fun setRightTextColor2(color: Int): TitleBar {
        this.txtRight2.setTextColor(color)
        return this
    }

    private fun setRightTextColor(color: Int): TitleBar {
        this.txtRight.setTextColor(color)
        return this
    }

    private fun inflateRootView() {
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.view_titlebar_simple, this, true)
    }

    fun build(context: Activity, title: String, hasLeftAction: Boolean): TitleBar {
        return build(context, title, -1, null, if (hasLeftAction) FinishAction(context) else null)
    }

    fun build(context: Activity, title: String, leftText: String): TitleBar {
        return build(context, title, -1, leftText, FinishAction(context))
    }

    @JvmOverloads fun build(context: Activity, title: String, @DrawableRes leftIcon: Int, leftText: String?, leftActoin: View.OnClickListener? = FinishAction(context)): TitleBar {
        if (!TextUtils.isEmpty(leftText)) {
            setLeftText(leftText)
        }
        if (leftIcon != -1) {
            setLeftAction(leftActoin)
        }
        if (leftActoin != null) {
            setLeftAction(leftActoin)
        }
        setTitle(title)
        return this
    }


    internal inner class FinishAction(act: Activity) : View.OnClickListener {

        private val context: WeakReference<Activity>

        init {
            this.context = WeakReference(act)
        }

        override fun onClick(v: View) {
            val activity = context.get()
            activity?.finish()
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    @IntDef(View.VISIBLE.toLong(), View.GONE.toLong())
    internal annotation class Visibility

    companion object {

        private val STATUS_HEIGHT_DP = 20
    }
}
