package net.izom.rust_application_codebase

import android.content.Intent
import android.os.Bundle

class AndroidActivity : android.app.Activity() {
    companion object {
        init {
            System.loadLibrary("rust_application_codebase")
        }
    }

    private external fun onFirstActivityCreate()

    private external fun onCreateActivity(activity: AndroidActivity)

    private external fun onStartActivity(activity: AndroidActivity)

    private external fun onResumeActivity(activity: AndroidActivity)

    private external fun onPauseActivity(activity: AndroidActivity)

    private external fun onStopActivity(activity: AndroidActivity)

    private external fun onDestroyActivity(activity: AndroidActivity)

    private external fun onWindowFocusChangedActivity(activity: AndroidActivity, focus: Int)

    private external fun onLowMemory()

    private external fun onNewIntentActivity(intent: Intent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onFirstActivityCreate()
        onCreateActivity(this)
    }

    override fun onStart() {
        super.onStart()
        onStartActivity(this)
    }

    override fun onResume() {
        super.onResume()
        onResumeActivity(this)
    }

    override fun onPause() {
        super.onPause()
        onPauseActivity(this)
    }

    override fun onStop() {
        super.onStop()
        onStopActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyActivity(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        onWindowFocusChangedActivity(this, if (hasFocus) 1 else 0)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        onLowMemoryAct()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        onNewIntentActivity(intent)
    }
}
