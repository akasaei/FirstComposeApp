# Fix Composable Invocation Error and Improve CounterScreen

The user is encountering a compiler error: `@Composable invocations can only happen from the context of a @Composable function`. This is caused by calling `CounterScreen()` inside the `onClick` lambda of a `Button` in `GreetingScreen`.

## Proposed Changes

### [Component Name] app

#### [MODIFY] [MainActivity.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/MainActivity.kt)
- **Introduce Screen State**: Add a state variable in `MainActivity` or a top-level Composable to track which screen is currently visible.
- **Fix `GreetingScreen`**: Pass a callback to `GreetingScreen` instead of calling `CounterScreen()` directly in `onClick`.
- **Refactor `CounterScreen`**: Make it stateless by hoisting its state, and add a "Decrease" button and better styling as suggested previously.
- **Add Previews**: Add `@Preview` for both screens.

## Verification Plan

### Manual Verification
- Deploy the app to a device/emulator.
- Verify that clicking "Start" on the Greeting screen correctly switches to the Counter screen.
- Verify that the Counter screen's Increase, Decrease, and Reset buttons work as expected.
- Verify that the UI looks improved and matches Material 3 standards.
