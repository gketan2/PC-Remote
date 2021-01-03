package com.k10.control.ui.main.keyboard

import android.os.Bundle
import android.text.Editable
import android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
import android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.k10.control.R
import com.k10.control.request.Services
import com.k10.control.ui.main.MainActivityViewModel
import com.k10.control.utils.SpecialKeyCodes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_keyboard.*

@AndroidEntryPoint
class KeyboardFragment : Fragment(R.layout.fragment_keyboard) {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //remove suggestion otherwise previous text also come
        //because keyboard stores the typed word(for prediction) until space is pressed
        sendText.inputType = TYPE_TEXT_FLAG_NO_SUGGESTIONS or TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        sendText.addTextChangedListener(textWatcher)
        sendText.setOnKeyListener(keyListener)

    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                if (it.isNotEmpty()) {
                    previousSent.text = "\'$it\'"
                    viewModel.typeString(it.toString())
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.isNotEmpty()) {
                    it.delete(0, it.length)
                }
            }
        }
    }

    private val keyListener = View.OnKeyListener { _, keyCode, event ->
        when (event.action) {
            KeyEvent.ACTION_DOWN -> {
            }
            KeyEvent.ACTION_UP -> {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //press special key
                    viewModel.addInSpecialArray(Services.TYPE_SPECIAL, SpecialKeyCodes.backspace)
                    viewModel.sendSpecialKeys()
                }
            }
        }
        false
    }
}