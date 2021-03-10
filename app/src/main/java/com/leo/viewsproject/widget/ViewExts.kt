package com.leo.viewsproject.widget

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.view.ViewTreeObserver


/**
 * 对 View 类的扩展函数
 *
 * @author liuxu@gotokeep.com (Liu Xu)
 */

/**
 * Change view's visibility.
 * @param visible if visible or not， true by default.
 * @param invisible if invisible or gone when not visible,
 * INVISIBLE mode by default for render performance.
 *
 * Derivatives:
 * 1. Set view visible
 * View.setVisible(), View.setVisible(true), View.setVisible(true, true)
 * 2. Set view invisible
 * View.setInvisible(), View.setVisible(false), View.setVisible(false, true)
 * 3. Set view gone
 * View.setGone(), View.setVisible(false, false)
 */
fun View.setVisible(visible: Boolean = true, invisible: Boolean = true) {
    this.visibility = when {
        visible -> View.VISIBLE
        invisible -> View.INVISIBLE
        else -> GONE
    }
}

/**
 * Set view visible as shown, or gone as hidden.
 */
fun View.showOrHide(visible: Boolean) {
    this.visibility = when {
        visible -> View.VISIBLE
        else -> GONE
    }
}

/**
 * 设置一个 View 为不可见
 */
fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

/**
 * 设置一个 View 为隐藏
 */
fun View.setGone() {
    this.visibility = GONE
}

/**
 * 设置一个 view 为可见
 */
fun View.setVisible() {
    this.visibility = View.VISIBLE
}

/**
 * 判断一个 View 是否可见
 */
fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

/**
 * 在显示 | 隐藏之间切换
 */
fun View.toggleVisible() {
    visibility = if (visibility == View.VISIBLE) GONE else View.VISIBLE
}

/**
 * 设置宽度并重新布局
 */
fun View.setWidthWithRelayout(width: Int) {
    layoutParams.width = width
    requestLayout()
}

/**
 * 设置 View 的 isClickable 和 isFocusable
 * 目的是让 View 可以或不可以被点击
 */
fun View.setClickableAndFocusable(value: Boolean) {
    isClickable = value
    isFocusable = value
}


/**
 * 在动态设置 backgroundResource时，会重置view的padding，所以使用这个方法
 */
fun View.doPaddingAfterSetBackground(function: () -> Unit) {
    val paddingTop = this.paddingTop
    val paddingBottom = this.paddingBottom
    val paddingLeft = this.paddingLeft
    val paddingRight = this.paddingRight
    function.invoke()
    this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

/**
 * 设置内边距
 */
fun View.padding(
    left: Int = this.paddingLeft,
    top: Int = this.paddingTop,
    right: Int = this.paddingRight,
    bottom: Int = this.paddingBottom
) {
    this.setPadding(left, top, right, bottom)
}

/**
 * 重置内边距
 */
fun View.resetPadding() {
    val paddingTop = this.paddingTop
    val paddingBottom = this.paddingBottom
    val paddingLeft = this.paddingLeft
    val paddingRight = this.paddingRight
    this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

/**
 * 同时设置 select 和 focus
 */
fun View.selectAndFocus() {
    isSelected = true
    requestFocus()
}

/**
 * 同时设置 isEnabled、isFocusable 和 isFocusableInTouchMode
 */
fun View.setEnableAndFocusable(value: Boolean) {
    isEnabled = value
    isFocusable = value
    isFocusableInTouchMode = value
    if (!value) {
        clearFocus()
    }
}

/**
 * 根据当前宽度测量 View 的高度
 */
fun View.calculateHeight(): Int {
    val wSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
    measure(wSpec, View.MeasureSpec.UNSPECIFIED)
    return measuredHeight
}

/**
 * 返回 dp 对应的 px
 * in: dp
 * out: px
 */
val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

/**
 * 返回 dp 对应的 px
 * in: dp
 * out: px
 */
val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

/**
 * 返回 sp 对应的 px
 * in: sp
 * out: px
 */
val Float.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)

/**
 * 返回 sp 对应的 px
 * in: sp
 * out: px
 */
val Int.sp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

fun Int?.orZero(): Int {
    return this ?: 0
}

fun Long?.orZero(): Long {
    return this ?: 0L
}

fun Float?.orZero(): Float {
    return this ?: 0F
}

fun Double?.orZero(): Double {
    return this ?: 0.0
}

fun Byte?.orZero(): Byte {
    return this ?: 0.toByte()
}

fun Boolean?.orFalse(): Boolean {
    return this ?: false
}
