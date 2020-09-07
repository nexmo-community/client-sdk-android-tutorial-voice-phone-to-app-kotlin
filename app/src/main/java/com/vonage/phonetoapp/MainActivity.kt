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
        client.login("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1OTk0ODQ3NzEsImp0aSI6ImMzOTAwYTgwLWYxMGMtMTFlYS1iNTM2LWFkNjZjOTE3YzgzYyIsImV4cCI6MTU5OTUwNjM3MCwiYWNsIjp7InBhdGhzIjp7Ii8qL3VzZXJzLyoqIjp7fSwiLyovY29udmVyc2F0aW9ucy8qKiI6e30sIi8qL3Nlc3Npb25zLyoqIjp7fSwiLyovZGV2aWNlcy8qKiI6e30sIi8qL2ltYWdlLyoqIjp7fSwiLyovbWVkaWEvKioiOnt9LCIvKi9hcHBsaWNhdGlvbnMvKioiOnt9LCIvKi9wdXNoLyoqIjp7fSwiLyova25vY2tpbmcvKioiOnt9fX0sInN1YiI6IkFsaWNlIiwiYXBwbGljYXRpb25faWQiOiJjNWJmNmNmZi02N2ExLTQ4ZmQtYjM3Yi0zZWM5N2Q0MjY4NjkifQ.Nb5TfU5MoLcZdAeBqpUAWVqUejZm_0dJpnIqqh-YVr1CchJPmWfulUAiWtC-ze7Tbx9-kzl1keOx6ONq05fL6nfC_J7UlXPrV59UHIlQiuu6kA1yl3mjDjDGUevIKObFyz2dF-m11JG1RqsecpGyKV85oKg0t0gMTGPqeGxXoam1wwhBfLzxy4VLwhudG7H5F_HipS9BF1wm185xk556ewXF0fUW7Y2N5xBU3z5TU9W8vecI8pQk86w8x-1BXA3jItlTfJr-P5iOabZFb4DEjwvHBW87GE_ygYebjal6H-DchGPDF9w9QZS79LhFPreHy34mCMGSEjy7WD7CSvXvMg")

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