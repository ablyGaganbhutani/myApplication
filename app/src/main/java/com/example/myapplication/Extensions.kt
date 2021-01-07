package com.example.myapplication

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout

fun EditText.afterTextChanged(
    textInputLayout: TextInputLayout,
    afterTextChanged: (String) -> Unit
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun TextInputLayout.showError(context: Context, isError: Boolean = false, errorString: String? = "") {
    if (isError) {
        this.isErrorEnabled = true
        this.error = errorString
        this.boxStrokeErrorColor =
            ContextCompat.getColorStateList(context, R.color.colorRed)
        this.errorIconDrawable =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_cancel_24)
    } else {
        this.isErrorEnabled = false
        this.boxStrokeColor = ContextCompat.getColor(context, R.color.colorGreen)
        this.endIconDrawable =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_circle_24)
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}