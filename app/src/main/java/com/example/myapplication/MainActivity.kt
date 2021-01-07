package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.db.UserViewModel
import com.example.myapplication.socket.SocketManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnWebView -> {
                startActivity(Intent(this, WebViewActivity::class.java))
            }
            R.id.btnSubmit -> {
//                tilEmail.error = "Enter valid email"
//                tilEmail.boxStrokeErrorColor =
//                    ContextCompat.getColorStateList(this, R.color.colorRed)
//                tilEmail.errorIconDrawable =
//                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_cancel_24)
//                val user = User(0, etName.text.toString().trim(), etAge.text.toString().trim())
//                viewModel.insert(user)
//
//                etAge.setText("")
//                etName.setText("")

            }
        }
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setClickListener()
        setObservers()

        val (words, lines) = listOf("a", "a b", "c", "d e").partitionTo(
            ArrayList<String>(),
            ArrayList()
        ) { s -> !s.contains(" ") }
    }

    fun <T, C : MutableCollection<T>> Collection<T>.partitionTo(
        first: C,
        second: C,
        predicate: (T) -> Boolean
    ): Pair<C, C> {
        for (element in this) {
            if (predicate(element)) {
                first.add(element)
            } else
                second.add(element)
        }

        return Pair(first, second)
    }

    fun partitionLettersAndOtherSymbols() {
        val (letters, other) = setOf('a', '%', 'r', '}').partitionTo(
            HashSet<Char>(),
            HashSet()
        ) { c -> c in 'a'..'z' || c in 'A'..'Z' }
        letters == setOf('a', 'r')
        other == setOf('%', '}')
    }

    private fun init() {
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        fun task(): List<Boolean> {
            val isEven: Int.() -> Boolean = {
                this % 2 == 0
            }
            val isOdd: Int.() -> Boolean = {
                this % 2 != 0
            }

            return listOf(42.isOdd(), 239.isOdd(), 294823098.isEven())
        }
        initSocket()
    }

    private fun initSocket() {
        val socketManager =
            SocketManager.getInstance("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTc3LCJpYXQiOjE2MDk5MjA4ODF9.32O94YxESAW3fFLrWeBtsLZphQxAE1fq22gJ1vRaq-0")
//        socketManager.on(SocketManager.EVENT_DELETE_MESSAGE, deleteMessageListener)
//        socketManager.on(SocketManager.EVENT_NEW_MESSAGE, newMessageListener)
        socketManager.connect()
    }

    private fun setClickListener() {
        btnSubmit.setOnClickListener(this)
        btnWebView.setOnClickListener(this)
        etEmail.afterTextChanged(tilEmail) {

        }

        etEmail.addTextChangedListener {
            Log.d("addTextChangedLis->", it.toString())
            if (isValidEmail(it.toString())) {
                tilEmail.showError(this)
            } else {
                tilEmail.showError(this, true, "Please enter email")

            }
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
            .matches()
    }

    private fun setObservers() {
        viewModel.allUsers?.observe(this, Observer {
            val size = it.size

            for (i in 0 until size) {
                Log.d("name", it[i].userId.toString().plus("-> ").plus(it[i].name ?: ""))
            }
        })
    }
}