
fn main() {
    #[cfg(not(any(target_os = "android", target_os = "ios")))]
    rust_application_codebase::main();
}
