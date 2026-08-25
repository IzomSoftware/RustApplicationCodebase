#![cfg(target_os = "linux")]

use tao::{platform::unix::WindowBuilderExtUnix, window::WindowBuilder};

use crate::platform::{DesktopSizeBuilder, PlatformBuilder};

pub struct LinuxPlatform;

impl PlatformBuilder for LinuxPlatform {
    fn setup(&self) {
        // PLATFORM SPECIFIC:
        // Use x11 backend EVEN IF RUNNING UNDER WAYLAND
        // This is because there would be less bugs &
        // compatibility issues
        unsafe {
            std::env::set_var("GDK_BACKEND", "x11");
        }
    }

    fn setup_window(&self) -> WindowBuilder {
        WindowBuilder::new()
            .with_desktop_default_size()
            .with_default_vbox(true)
    }
}
