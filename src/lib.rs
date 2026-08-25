#![cfg(any(target_os = "android", target_os = "ios"))]
pub mod app;
pub mod platform;
pub mod utils;
pub mod entry_point;

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
    stop_unwind(|| entry_point::init());
}

#[cfg(target_os = "android")]
unsafe fn _on_activity_create(
    _activity_name: &str,
    _env: tao::platform::android::prelude::JNIEnv,
    _looper: &tao::platform::android::prelude::ndk::looper::ThreadLooper,
    _saved_state: tao::platform::android::prelude::GlobalRef,
) {}

#[cfg(target_os = "android")]
tao::android_binding!(
    net_izom,
    rust_application_codebase,
    AndroidActivity,
    _on_activity_create,
    _start_app,
    ::tao
);

#[cfg(not(target_os = "android"))]
#[unsafe(no_mangle)]
#[inline(never)]
pub extern "C" fn start_app() {
    _start_app()
}