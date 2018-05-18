package com.wq.common.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.wq.common.util.visible
import com.wq.config.R


/**
 * @author Administrator
 *
 * 自定义TiemView
 */
class ItemView(internal var mContext: Context, attrs: AttributeSet) : RelativeLayout(mContext, attrs) {
    private lateinit var mItemLeftText: TextView // 标题,右边文字按钮
    private lateinit var mItemRightText: TextView // 标题,右边文字按钮
    private lateinit var mItemRightIcon: ImageView // 标题,右边文字按钮
    private lateinit var mItemLeftIcon: ImageView // 标题,右边文字按钮

    init {
        val ta = mContext.obtainStyledAttributes(attrs, R.styleable.ItemView)
        val leftIcon = ta.getDrawable(R.styleable.ItemView_iv_leftIcon)
        val  leftText = ta.getString(R.styleable.ItemView_iv_leftText)
        val leftTextColor = ta.getColor(R.styleable.ItemView_iv_leftText, 0)
        val leftTextSize = ta.getDimension(R.styleable.ItemView_iv_leftTextSize,14f)
        val rightTextSize=ta.getDimension(R.styleable.ItemView_iv_rightTextSize,14f)
        val  rightText = ta.getString(R.styleable.ItemView_iv_rightText)
        val  isRightIconShow = ta.getBoolean(R.styleable.ItemView_iv_rightIconShow, true)
        val rightIcon = ta.getDrawable(R.styleable.ItemView_iv_rightIcon)
        val  rightTextColor = ta.getColor(R.styleable.ItemView_iv_rightTextColor, 0)
        ta.recycle()
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.common_item_view, null)
        mItemLeftText = view.findViewById(R.id.item_left_text) as TextView
        mItemRightIcon = view.findViewById(R.id.item_right_img) as ImageView
        mItemRightText = view.findViewById(R.id.item_right_text) as TextView
        mItemLeftIcon =view.findViewById(R.id.item_left_icon) as ImageView
        addView(view)
        setRightText(rightText)
        setRightIconShow(isRightIconShow)
        setRightIcon(rightIcon)
        setLeftIcon(leftIcon)
        setRightTextColor(rightTextColor)
        setLeftTextColor(leftTextColor)
        setLeftTextSize(leftTextSize)
        setRightTextSize(rightTextSize)
        setLeftText(leftText)
    }

    private fun setLeftText( leftText: String?) {
        mItemLeftText.text=leftText
    }


    fun setRightTextSize(leftTextSize: Float) {
         mItemRightText.textSize=leftTextSize
    }

    fun setLeftTextSize(leftTextSize: Float) {
        mItemLeftText.textSize=leftTextSize
    }


    /**
     * 设置左侧text图标 位置
     *
     * @param drawable
     * @param itemString
     */
    fun setLeftIcon(drawable: Drawable? ) {
        mItemLeftIcon.setImageDrawable(drawable)
        mItemLeftIcon.visible(drawable!=null)
    }

    /**
     * 设置右侧text文字
     */
    fun setRightText(itemString: String?) {

        mItemRightText.visible(itemString != null)
        mItemRightText.text = itemString

    }

    /**
     * 设置右侧图标是否显示
     */
    fun setRightIconShow(isShow: Boolean) {
        mItemRightText.visible(isShow)
    }


    /**
     * 设置右侧图片
     */
    fun setRightIcon(drawable: Drawable?) {

        if (drawable != null) {
            mItemRightIcon.setImageDrawable(drawable)
        }

    }

    /**
     * 设置右侧字体的颜色
     * @param RightTextColor
     */

    fun setRightTextColor(RightTextColor: Int) {
        if (RightTextColor != 0) {
            mItemRightText.setTextColor(RightTextColor)
        }

    }

    fun setLeftTextColor(RightTextColor: Int) {
        if (RightTextColor != 0) {
            mItemLeftText.setTextColor(RightTextColor)
        }

    }

    /**
     * 设置字体左侧图标
     */
    fun setRightTexticon(drawable: Drawable?) {
        if (drawable != null) {
            // / 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            mItemRightText.setCompoundDrawables(drawable, null, null, null)
        } else {
            mItemRightText.setCompoundDrawables(null, null, null, null)
        }
    }

}

