package com.example.capstone.ui.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.capstone.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    init {
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    setError(context.getString(R.string.warning_password), null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Do Nothing
            }
        })
    }
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val drawableEndX = width - paddingEnd
            val drawableStartX = drawableEndX - compoundDrawables[2].intrinsicWidth

            if (event.x > drawableStartX && event.x < drawableEndX){
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}