#![cfg(any(target_os = "android", target_os = "ios"))]
pub mod app;
pub mod platform;
pub mod utils;
pub mod entry_point;

#[cfg(target_os = "android")]
use std::sync::{Condvar, Mutex};

#[cfg(target_os = "android")]
static ACTIVITY_READY: Mutex<bool> = Mutex::new(false);
#[cfg(target_os = "android")]
static ACTIVITY_CONDVAR: Condvar = Condvar::new();

fn stop_unwind<F: FnOnce() -> T, T>(f: F) -> T {
    match std::panic::catch_unwind(std::panic::AssertUnwindSafe(f)) {
        Ok(t) => t,
        Err(err) => {
            eprintln!("{}", err
                .downcast_ref::<&'static str>()
                .map(|s| (*s).to_string())
                .or_else(|| err.downcast_ref::<String>().cloned())
                .unwrap_or_else(|| "".to_string()));
            std::process::abort()
        }
    }
}

fn _start_app() {
    stop_unwind(|| {
        #[cfg(target_os = "android")]
        {
            let mut activity_ready = ACTIVITY_READY.lock().unwrap();
            while !*activity_ready {
                activity_ready = ACTIVITY_CONDVAR.wait(activity_ready).unwrap();
            }
        }
        entry_point::init();
    });
}

#[cfg(target_os = "android")]
unsafe fn _on_activity_create(
    _activity_name: &str,
    _env: tao::platform::android::prelude::JNIEnv,
    _thread_looper: &tao::platform::android::prelude::ndk::looper::ThreadLooper,
    _saved_state: tao::platform::android::prelude::GlobalRef,
) {
    *ACTIVITY_READY.lock().unwrap() = true;
    ACTIVITY_CONDVAR.notify_one();
}

#[cfg(target_os = "android")]
const _: () = {
    ::tao::android_binding!(
        net_izom,
        rust_application_codebase,
        AndroidActivity,
        _on_activity_create,
        _start_app,
        ::tao
    );
};

#[cfg(not(target_os = "android"))]
#[unsafe(no_mangle)]
#[inline(never)]
pub extern "C" fn start_app() {
    _start_app()
}