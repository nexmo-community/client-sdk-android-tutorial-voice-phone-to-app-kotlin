package com.vonage.phonetoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nexmo.client.NexmoCall
import com.nexmo.client.NexmoClient
import com.nexmo.client.request_listener.NexmoApiError
import com.nexmo.client.request_listener.NexmoRequestListener
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {

    private var call: NexmoCall? = null
    private var incomingCall = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init Nexmo client
        val client: NexmoClient = NexmoClient.Builder().build(this)

        // set Connection status listener
        client.setConnectionListener { connectionStatus, _ ->
            runOnUiThread {
                connectionStatusTextView.text = connectionStatus.toString()
            }
        }

        // login client
        client.login("ALICE_TOKEN")

        // listen for incoming calls
        client.addIncomingCallListener { it ->
            call = it
            incomingCall = true
            updateUI()
        }

        // add buttons listeners
        answerCallButton.setOnClickListener {
            incomingCall = false
            updateUI()
            call?.answer(object : NexmoRequestListener<NexmoCall> {
                override fun onError(p0: NexmoApiError) {
                }

                override fun onSuccess(p0: NexmoCall?) {
                }
            })
        }
        rejectCallButton.setOnClickListener {
            incomingCall = false
            call = null
            updateUI()

            call?.hangup(object : NexmoRequestListener<NexmoCall> {
                override fun onError(p0: NexmoApiError) {
                }

                override fun onSuccess(p0: NexmoCall?) {
                }
            })
        }
        endCallButton.setOnClickListener {
            call?.hangup(object : NexmoRequestListener<NexmoCall> {
                override fun onError(p0: NexmoApiError) {
                }

                override fun onSuccess(p0: NexmoCall?) {
                }
            })
            call = null
            updateUI()
        }
    }

    private fun updateUI() {
        answerCallButton.visibility = View.GONE
        rejectCallButton.visibility = View.GONE
        endCallButton.visibility = View.GONE
        if (incomingCall) {
            answerCallButton.visibility = View.VISIBLE
            rejectCallButton.visibility = View.VISIBLE
        } else if (call != null) {
            endCallButton.visibility = View.VISIBLE
        }
    }
}