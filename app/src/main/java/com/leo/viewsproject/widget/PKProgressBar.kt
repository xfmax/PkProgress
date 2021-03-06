package com.leo.viewsproject.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import com.leo.viewsproject.R
import kotlin.math.max
import kotlin.math.min


/**
 * Describe : 对战积分对比
 * Created by Leo on 2019/2/20 on 9:10.
 */
class PKProgressBar : View {
    var textBgColor: Int = 0

    /**
     * 绘制跟随进度条移动的数字
     */
    private lateinit var paintNumber: Paint
    private var bitmapPaint = Paint()
    private var paintBar: Paint? = null
    private var paintOtherBar: Paint? = null
    private var paintLight: Paint? = null
    private lateinit var leftTextPaintBg: Paint
    private val rightTextPaintBg: Paint? = null
    private var backGroundColor = Color.GRAY
    private var barColor = Color.RED
    private var leftDrawable: Drawable? = null
    private var rightDrawable: Drawable? = null
    private var rightDrawableOne: Drawable? = null
    private var rightDrawableTwo: Drawable? = null
    private var rightDrawableThree: Drawable? = null
    private var onProgressChangeListener: OnProgressChangeListener? = null
    private var halfDrawableWidth = 0
    private var halfDrawableHeight = 0
    private var barPadding = 0
    private var isRound = true
    private var progress = 50f
    private var max = 100f
    private var progressPercentage = 0.5
    private var viewWidth = 0f
    private var viewHeight = 0
    private var progressWidth = 100
    private val rectFBG = RectF()
    private val rectFPB = RectF()
    private val rectFPBO = RectF()
    private val barRoundPath = Path()
    private val barRoundPathOther = Path()
    private val rectLeftText = Rect()
    private val rectRightText = Rect()
    private val rectLeftFollowText = Rect()
    private val rectRightFollowText = Rect()
    private var paintRightText: Paint? = null
    private var paintLeftText: Paint? = null
    private var paintBackGround: Paint? = null

    //是否使用颜色渐变器
    private var isGradient = false

    //颜色渐变器
    private var linearGradient: LinearGradient? = null
    private var linearGradientOther: LinearGradient? = null
    var gradientStartColor = Color.RED
    var gradientEndColor = Color.YELLOW
    var otherGradientStartColor = Color.RED
    var otherGradientEndColor = Color.YELLOW
    private var barStartWidth = 0
    private var barEndWidth // 进度条最大绘制位置与最小绘制位置
            = 0
    private var barRadioSize // 圆角大小
            = 0f
    private var textColor = Color.WHITE
    private var textSize = 12
    private var textIsBold = true
    private var leftTextStr = "蓝方"
    private var rightTextStr = "红方"
    var leftTeamName = ""
    var leftTeamCount = ""
    var rightTeamName = ""
    var rightTeamCount = ""
    var leftFollowTextStr = ""
    var rightFollowTextStr = ""
    private var lightBitmap: Bitmap? = null
    private var lightDrawableId = 0

    private var leftTextStrWidth = 0f
    private var leftTextStrHeight = 0f
    private var leftTextStrMargin = 21f.dp
    private var commonMargin = 6f.dp
    private var leftTextStrBg = 57f.dp

    private var rightTextStrWidth = 0f
    private var rightTextStrHeight = 0f
    private var rightTextStrMargin = 21f.dp

    private var leftFollowWidth = 0f
    private var leftFollowHeight = 0f
    private var rightFollowWidth = 0f
    private var rightFollowHeight = 0f
    private var followMargin = 16f.dp
    /**
     * 左侧队名显示状态
     */
    private var leftTeamNameVisible = true

    /**
     * 左侧团队人数的显示状态
     */
    private var leftTeamCountVisible = true

    /**
     * 右侧队名显示状态
     */
    private var rightTeamNameVisible = true

    /**
     * 右侧团队人数的显示状态
     */
    private var rightTeamCountVisible = true

    /**
     * 左侧的队名
     */
    private var leftTeamNameWidth = 0f

    /**
     * 左侧的团队人数
     */
    private var leftTeamCountWidth = 0f

    /**
     * 右侧的队名的宽度
     */
    private var rightTeamNameWidth = 0f

    /**
     * 右侧的团队人数宽度
     */
    private var rightTeamCountWidth = 0f

    /**
     * 右侧用户头像边距
     */
    private val rightAvatarMargin = 10f.dp

    /**
     * 右侧用户头像宽度
     */
    private val rightAvatarwidth = 58f.dp

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.PKProgressBar_pk, 0, 0)
        backGroundColor =
            typedArray.getColor(R.styleable.PKProgressBar_pk_backGroundColor, Color.GRAY)
        barColor = typedArray.getColor(R.styleable.PKProgressBar_pk_barColor, Color.RED)
        leftDrawable = typedArray.getDrawable(R.styleable.PKProgressBar_pk_leftDrawableBg)
        rightDrawable = typedArray.getDrawable(R.styleable.PKProgressBar_pk_rightDrawableBg)
        halfDrawableWidth =
            typedArray.getDimensionPixelSize(R.styleable.PKProgressBar_pk_halfDrawableWidth, 1)
        halfDrawableHeight =
            typedArray.getDimensionPixelSize(R.styleable.PKProgressBar_pk_halfDrawableHeight, 1)
        barPadding = typedArray.getDimensionPixelSize(R.styleable.PKProgressBar_pk_barPadding, 0)
        isRound = typedArray.getBoolean(R.styleable.PKProgressBar_pk_isRound, true)
        max = typedArray.getInt(R.styleable.PKProgressBar_pk_max, 100).toFloat()
        lightDrawableId = typedArray.getResourceId(R.styleable.PKProgressBar_pk_lightDrawable, 0)
        textColor = typedArray.getColor(R.styleable.PKProgressBar_pk_textColor, Color.BLACK)
        textSize = typedArray.getDimensionPixelSize(R.styleable.PKProgressBar_pk_textSize, 13)
        textIsBold = typedArray.getBoolean(R.styleable.PKProgressBar_pk_textIsBold, false)
        isGradient = typedArray.getBoolean(R.styleable.PKProgressBar_pk_isGradient, false)
        gradientStartColor =
            typedArray.getColor(R.styleable.PKProgressBar_pk_gradientStartColor, Color.RED)
        gradientEndColor =
            typedArray.getColor(R.styleable.PKProgressBar_pk_gradientEndColor, Color.YELLOW)
        otherGradientStartColor =
            typedArray.getColor(R.styleable.PKProgressBar_pk_otherGradientStartColor, Color.RED)
        otherGradientEndColor =
            typedArray.getColor(R.styleable.PKProgressBar_pk_otherGradientEndColor, Color.YELLOW)
        typedArray.recycle()
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {

        // 底部边框背景
        paintBackGround = Paint()
        paintBackGround!!.color = backGroundColor
        paintBackGround!!.isAntiAlias = true
        paintLight = Paint()
        paintLight!!.isAntiAlias = true

        // 左半部分进度条
        paintBar = Paint()
        paintBar!!.color = barColor
        paintBar!!.isAntiAlias = true
        // 右半部分进度条
        paintOtherBar = Paint()
        paintOtherBar!!.color = barColor
        paintOtherBar!!.isAntiAlias = true

        // 左半部分文字
        val plain = Typeface.createFromAsset(context.assets, "font/Roboto-BlackItalic.ttf")
        paintLeftText = Paint()
        paintLeftText!!.typeface = plain
        paintLeftText!!.style = Paint.Style.FILL
        paintLeftText!!.color = textColor
        paintLeftText!!.textSize = textSize.toFloat()

        leftTextStrWidth = paintLeftText?.measureText(leftTextStr).orZero()
        leftTextStrHeight = paintLeftText?.descent().orZero() - paintLeftText?.ascent().orZero()

        // 团队数字
        val plainNumber = Typeface.createFromAsset(context.assets, "font/Roboto-BlackItalic.ttf")
        paintNumber = Paint().apply {
            typeface = plainNumber
            style = Paint.Style.FILL
            color = textColor
            textSize = 24f.dp
        }

        // 左半部文字的背景
        leftTextPaintBg = Paint().apply {
            style = Paint.Style.FILL
            color = textBgColor
            isAntiAlias = true
        }

        // 右半部分文字
        paintRightText = Paint()
        paintRightText!!.style = Paint.Style.FILL
        paintRightText!!.color = textColor
        paintRightText!!.textSize = textSize.toFloat()
        paintRightText!!.isFakeBoldText = textIsBold
        paintRightText!!.isAntiAlias = true
        paintRightText!!.textSkewX = -0.25f
        if (lightDrawableId != 0) {
            lightBitmap = BitmapFactory.decodeResource(resources, lightDrawableId)
        }

        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                //是否需要渐变器
                if (isGradient) {
                    linearGradient = LinearGradient(
                        0f,
                        viewHeight / 2f,
                        progressWidth.toFloat(),
                        viewHeight / 2f,
                        gradientStartColor,
                        gradientEndColor,
                        Shader.TileMode.CLAMP
                    )
                    linearGradientOther = LinearGradient(
                        0f,
                        viewHeight / 2f,
                        progressWidth.toFloat(),
                        viewHeight / 2f,
                        otherGradientStartColor,
                        otherGradientEndColor,
                        Shader.TileMode.CLAMP
                    )
                    paintBar!!.shader = linearGradient
                    paintOtherBar!!.shader = linearGradientOther
                }
                return false
            }
        })
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 光标水平位置
        viewWidth = ((progressWidth - 3 * barPadding) * progressPercentage + barPadding).toFloat()
        viewWidth = max(viewWidth, leftTextStrBg + leftTextStrMargin + leftFollowWidth)
        viewWidth = min(viewWidth, progressWidth - commonMargin - barPadding - rightAvatarMargin - rightAvatarwidth - rightFollowWidth - followMargin)
        viewHeight = height
        // 圆角大小，为实际进度条高度一半
        if (isRound) {
            barRadioSize = (viewHeight - barPadding * 2) / 2f
        }

        // 绘制底部边框
        drawBackground(canvas)
        // 绘制左半部分进度条
        drawLeftBar(canvas)
        // 绘制右半部分进度条
        drawRightBar(canvas)
        // 绘制透明图层
        drawPicture(canvas)
        // 绘制横向半透明光柱
        drawLight(canvas)
        // 绘制跟随进度条移动的文字
        drawFollowText(canvas)
        // 绘制队伍信息
        drawTeamInfoText(canvas)
    }

    private fun drawFollowText(canvas: Canvas) {
        val leftFollowStart = boundaryPosition - leftFollowWidth - halfDrawableWidth - followMargin
        val rightFollowStart = boundaryPosition + rightFollowWidth + halfDrawableWidth + followMargin
        canvas.drawText(
            leftFollowTextStr,
            boundaryPosition - leftFollowWidth - halfDrawableWidth - followMargin,
            viewHeight / 2f + leftFollowHeight / 3,
            paintNumber
        )
        leftTeamCountVisible =
            leftFollowStart > leftTextStrBg + leftTeamNameWidth + leftTextStrMargin * 2 + leftTeamCountWidth
        leftTeamNameVisible =
            leftFollowStart > leftTextStrBg + commonMargin + leftTeamNameWidth
        rightTeamCountVisible =
            rightFollowStart < (progressWidth - commonMargin * 2 - barPadding - rightAvatarMargin - rightAvatarwidth - rightTeamNameWidth - rightTeamCountWidth)
        rightTeamNameVisible =
            rightFollowStart < (progressWidth - commonMargin - rightTeamNameWidth - barPadding - rightAvatarMargin - rightAvatarwidth)
        canvas.drawText(
            rightFollowTextStr,
            boundaryPosition + followMargin,
            viewHeight / 2f + rightFollowHeight / 3,
            paintNumber
        )
    }

    private fun drawLight(canvas: Canvas) {
        if (lightBitmap == null) {
            return
        }
        canvas.save()

        // 将画布坐标系移动到画布中央
        canvas.translate(0f, barPadding.toFloat())
        val src = Rect(barStartWidth, 0, barEndWidth, (viewHeight - barPadding) / 2)
        canvas.drawBitmap(lightBitmap, src, src, paintLight)
        canvas.restore()
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.save()
        rectFBG[0f, 0f, progressWidth.toFloat()] = viewHeight.toFloat()
        if (isRound) {
//            canvas.drawRoundRect(rectFBG, y / 2f, y / 2f, paintBackGround);
            val path = Path()
            path.addRoundRect(
                RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat()), viewHeight / 2f,
                viewHeight / 2f, Path.Direction.CW
            )
            // 先对canvas进行裁剪
            canvas.clipPath(path, Region.Op.INTERSECT)
        } else {
            canvas.drawRect(rectFBG, paintBackGround)
        }
        canvas.restore()
    }

    private fun drawLeftBar(canvas: Canvas) {
        val right = boundaryPosition
        val radios = floatArrayOf(
            barRadioSize, barRadioSize, 0f, 0f, 0f, 0f, barRadioSize, barRadioSize
        )
//        if (progressPercentage == 1.0) {
//            radios[2] = barRadioSize
//            radios[3] = barRadioSize
//            radios[4] = barRadioSize
//            radios[5] = barRadioSize
//        }
        canvas.save()
        rectFPB[barPadding.toFloat(), barPadding.toFloat(), right - halfDrawableWidth] =
            (viewHeight - barPadding).toFloat()
        barRoundPath.reset()
        barRoundPath.addRoundRect(rectFPB, radios, Path.Direction.CCW)
        barRoundPath.close()
        if (isRound) {
            canvas.drawPath(barRoundPath, paintBar)
        } else {
            canvas.drawRect(rectFPB, paintBar)
        }
        canvas.restore()
    }

    private fun drawRightBar(canvas: Canvas) {
        val left = boundaryPosition
        val radios = floatArrayOf(
            0f, 0f,
            barRadioSize, barRadioSize,
            barRadioSize, barRadioSize, 0f, 0f
        )
        // 去掉有进度条的左侧圆角
//         (progressPercentage == 0.0) {
//            radios[0] = barRadioSize
//            radios[1] = barRadioSize
//            radios[6] = barRadioSize
//            radios[7] = barRadioSize
//        }
        canvas.save()
        rectFPBO[left + halfDrawableWidth, barPadding.toFloat(), barEndWidth.toFloat()] =
            (viewHeight - barPadding).toFloat()
        barRoundPathOther.reset()
        barRoundPathOther.addRoundRect(rectFPBO, radios, Path.Direction.CCW)
        barRoundPathOther.close()
        if (isRound) {
            canvas.drawPath(barRoundPathOther, paintOtherBar)
        } else {
            canvas.drawRect(rectFPBO, paintOtherBar)
        }
        canvas.restore()
    }

    private fun drawTeamInfoText(canvas: Canvas) {
        //左侧
        // 绘制文字的背景
        leftTextPaintBg.color = textBgColor
        canvas.drawRoundRect(
            13f.dp,
            13f.dp,
            57f.dp,
            viewHeight - 13f.dp,
            viewHeight / 2f,
            viewHeight / 2f,
            leftTextPaintBg
        )
        canvas.drawText(
            leftTextStr,
            leftTextStrMargin,
            viewHeight / 2f + leftTextStrHeight / 3.0f,
            paintLeftText
        )
        if (leftTeamNameVisible) {
            // 队名
            canvas.drawText(
                leftTeamName,
                leftTextStrBg + commonMargin,
                viewHeight / 2f + leftTextStrHeight / 3.0f,
                paintLeftText
            )
        }
        if (leftTeamCountVisible) {
            // 队伍人数
            canvas.drawText(
                leftTeamCount,
                leftTextStrBg + leftTeamNameWidth + commonMargin * 2,
                viewHeight / 2f + leftTextStrHeight / 3.0f,
                paintLeftText
            )
        }
        // 右侧
        if (rightTeamNameVisible) {
            // 右侧队名
            canvas.drawText(
                rightTeamName,
                progressWidth - commonMargin - rightTeamNameWidth - barPadding - rightAvatarMargin - rightAvatarwidth,
                viewHeight / 2f + leftTextStrHeight / 3.0f,
                paintRightText
            )
        }
        if (rightTeamCountVisible) {
            // 右侧团队人数
            canvas.drawText(
                rightTeamCount,
                progressWidth - commonMargin * 2 - barPadding - rightAvatarMargin - rightAvatarwidth - rightTeamNameWidth - rightTeamCountWidth,
                viewHeight / 2f + leftTextStrHeight / 3.0f,
                paintRightText
            )
        }
    }

    /**
     * 获取极限位置进度条的绘制位置
     * 1. 最左边
     * 2. 最左边到圆角区段位置
     * 3. 中间正常位置
     * 4. 最右边圆角区段位置
     * 5. 最右边
     */
    private val boundaryPosition: Float
        private get() {
            // 默认为计算的比例位置
            var boundaryPos = viewWidth
            if (progressPercentage == 0.0 || viewWidth == barStartWidth.toFloat()) {
                // 光标位于最左边
                boundaryPos =
                    barPadding.toFloat() + leftTextStrBg + leftTextStrMargin + leftFollowWidth
            } else if (progressPercentage == 1.0 || viewWidth == barEndWidth.toFloat()) {
                // 光标位于最右边
                boundaryPos = barEndWidth.toFloat() - rightAvatarwidth - rightAvatarMargin
            } else if ((viewWidth - barStartWidth < barRadioSize || viewWidth - barStartWidth == barRadioSize)
                && viewWidth > barStartWidth
            ) {
                boundaryPos = Math.max(viewWidth, barRadioSize + barStartWidth)
            } else if ((viewWidth > barEndWidth - barRadioSize || viewWidth == barEndWidth - barRadioSize)
                && viewWidth < barEndWidth
            ) {
                // 光标位于进度条右侧弧形区域
                boundaryPos = Math.min(viewWidth, barEndWidth - barRadioSize)
            }
            return boundaryPos
        }

    private fun drawPicture(canvas: Canvas) {
        leftDrawable?.let {
            it.setBounds(0, 0, 180.dp, viewHeight)
            it.draw(canvas)

        }
        rightDrawable?.let {
            it.setBounds(
                rectFPBO.right.toInt() - 180.dp,
                rectFPBO.top.toInt(),
                rectFPBO.right.toInt(),
                rectFPBO.bottom.toInt()
            )
            it.draw(canvas)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = MeasureSpec.getSize(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = halfDrawableWidth * 2
        }
        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = halfDrawableHeight * 2
        }

        // 记录进度条总长度
        progressWidth = width
        // 记录进度条起始位置
        barStartWidth = barPadding
        // 记录进度条结束位置
        barEndWidth = progressWidth - barPadding
        setMeasuredDimension(width, height)

        leftTextStrWidth = paintRightText?.measureText(leftTextStr).orZero()
        leftTextStrHeight = paintLeftText?.descent().orZero() - paintLeftText?.ascent().orZero()

        rightTextStrWidth = paintRightText?.measureText(rightTextStr).orZero()
        rightTextStrHeight = paintRightText?.descent().orZero() - paintRightText?.ascent().orZero()

        leftFollowWidth = paintNumber.measureText(leftFollowTextStr).orZero()
        leftFollowHeight = paintNumber.descent().orZero() - paintNumber.ascent().orZero()

        rightFollowWidth = paintNumber.measureText(rightFollowTextStr).orZero()
        rightFollowHeight = paintNumber.descent().orZero() - paintNumber.ascent().orZero()

        rightTeamNameWidth = paintLeftText?.measureText(rightTeamName).orZero()
        rightTeamCountWidth = paintLeftText?.measureText(rightTeamCount).orZero()

        leftTeamNameWidth = paintLeftText?.measureText(leftTeamName).orZero()
        leftTeamCountWidth = paintLeftText?.measureText(leftTeamCount).orZero()
    }

    interface OnProgressChangeListener {
        fun onOnProgressChange(progress: Int)
        fun onOnProgressFinish()
    }

    /************************public method */
    @Synchronized
    fun setAnimProgress(leftValue: Long, rightValue: Long) {
        leftFollowTextStr = "${leftValue}m"
        rightFollowTextStr = "${rightValue}m"
        if (leftValue + rightValue == 0L) {
            setAnimProgress(50f)
        } else {
            setAnimProgress(100f * leftValue / (leftValue + rightValue))
        }
    }

    /**
     * 传入百分比值
     *
     * @param progressValue 百分比值
     */
    fun setAnimProgress(progressValue: Float) {
        if (progressValue < 0 || progressValue > 100) {
            return
        }
        val animator = ValueAnimator.ofFloat(progress, progressValue)
        animator.addUpdateListener { valueAnimator ->
            setProgress(
                valueAnimator.animatedValue.toString().toFloat()
            )
        }
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = (Math.abs(progress - progressValue) * 20).toLong()
        animator.start()
    }

    fun setProgress(progressValue: Float) {
        if (progress <= max) {
            progress = progressValue
        } else if (progress < 0) {
            progress = 0f
        } else {
            progress = max
        }
        progressPercentage = (progress / (max * 1.0f)).toDouble()
        doProgressRefresh()
        invalidate()
    }

    @Synchronized
    private fun doProgressRefresh() {
        if (onProgressChangeListener != null) {
            onProgressChangeListener!!.onOnProgressChange(progress.toInt())
            if (progress >= max) {
                onProgressChangeListener!!.onOnProgressFinish()
            }
        }
    }

    //设置颜色渐变器
    fun setLinearGradient(linearGradient: LinearGradient?) {
        this.linearGradient = linearGradient
    }

    //设置进度监听器
    fun setOnProgressChangeListener(onProgressChangeListener: OnProgressChangeListener?) {
        this.onProgressChangeListener = onProgressChangeListener
    }
}
