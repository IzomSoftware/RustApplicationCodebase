package net.izom.rust_application_codebase

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class AndroidActivity : Activity() {
    companion object {
        init {
            System.loadLibrary("rust_application_codebase")
        }
        private var initialized = false
    }

    private external fun create(activity: AndroidActivity)
    private external fun onActivityCreate(activity: AndroidActivity)
    private external fun start(activity: AndroidActivity)
    private external fun stop(activity: AndroidActivity)
    private external fun resume(activity: AndroidActivity)
    private external fun pause(activity: AndroidActivity)
    private external fun onActivitySaveInstanceState(activity: AndroidActivity)
    private external fun onActivityDestroy(activity: AndroidActivity)
    private external fun onActivityLowMemory(activity: AndroidActivity)
    private external fun onWindowFocusChanged(activity: AndroidActivity, hasFocus: Int)
    override external fun onNewIntent(intent: Intent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!initialized) {
            create(this)
            initialized = true
        }
        onActivityCreate(this)
    }

    override fun onStart() {
        super.onStart()
        start(this)
    }

    override fun onResume() {
        super.onResume()
        resume(this)
    }

    override fun onPause() {
        super.onPause()
        pause(this)
    }

    override fun onStop() {
        super.onStop()
        stop(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        onActivityDestroy(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        onWindowFocusChanged(this, if (hasFocus) 1 else 0)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        onActivityLowMemory(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        onActivitySaveInstanceState(this)
    }
}