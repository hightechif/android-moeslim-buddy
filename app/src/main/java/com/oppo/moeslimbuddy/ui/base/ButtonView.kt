package com.oppo.moeslimbuddy.ui.base

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.oppo.moeslimbuddy.R

class ButtonView : AppCompatTextView {
    enum class ButtonSize {
        Medium, Small
    }

    enum class ButtonVariant {
        Primary, Secondary, Alternative, Outline, Custom
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    var size = ButtonSize.Medium
        set(value) {
            field = value

            val left =
                if (value == ButtonSize.Small) resources.getDimensionPixelSize(R.dimen.size_12dp)
                else resources.getDimensionPixelSize(R.dimen.size_16dp)
            val top =
                if (value == ButtonSize.Small) resources.getDimensionPixelSize(R.dimen.size_8dp)
                else resources.getDimensionPixelSize(R.dimen.size_12dp)

            setPadding(left, top, left, top)

            TextViewCompat.setTextAppearance(
                this, if (value == ButtonSize.Small) R.style.H4
                else R.style.H2
            )
        }

    var variant = ButtonVariant.Primary
        set(value) {
            field = value

            isSelected = variant == ButtonVariant.Secondary || variant == ButtonVariant.Outline
            isActivated = variant == ButtonVariant.Alternative || variant == ButtonVariant.Outline

        }

    private fun init(attrs: AttributeSet?) {
        var bg = 0
        var cl = 0
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ButtonView,
                0, 0
            )

            val v = a.getInt(R.styleable.ButtonView_variant, 0)
            variant = ButtonVariant.values()[v]

            val s = a.getInt(R.styleable.ButtonView_size, 0)
            size = ButtonSize.values()[s]

            val vBg = a.getResourceId(R.styleable.ButtonView_buttonBackground, 0)
            if (vBg != 0) {
                bg = vBg
            }

            val vCl = a.getResourceId(R.styleable.ButtonView_buttonTextColor, 0)
            if (vCl != 0) {
                cl = vCl
            }

            a.recycle()
        } else {
            size = ButtonSize.Medium
            variant = ButtonVariant.Primary
        }

        if (variant == ButtonVariant.Custom && bg != 0 && cl != 0) {
            setBackgroundResource(bg)
            setTextColor(ContextCompat.getColorStateList(context, cl))
        } else {
            setBackgroundResource(R.drawable.bg_button)
            setTextColor(ContextCompat.getColorStateList(context, R.color.color_button))
        }

        gravity = Gravity.CENTER
    }
}