# Fix Simulation Toggle Logic (Re-implementation)

The simulation toggle logic is currently broken because:
1. The error state is not cleared when a new fetch starts.
2. The UI event for toggling simulation updates a state variable but doesn't trigger a new fetch.
3. The state is split between `StateFlow` and `mutableStateOf`.

I will consolidate the state into `OrderUiState` using the user's chosen name `simulateFailure` and fix the ViewModel logic.

## Proposed Changes

### [Model]

#### [MODIFY] [OrderUiState.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/model/OrderUiState.kt)
- Ensure `simulateFailure` defaults to `true` to maintain existing behavior where the app starts in a failure state for demonstration.

### [ViewModel]

#### [MODIFY] [OrderViewModel.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/viewmodel/OrderViewModel.kt)
- Remove `simulateError` property.
- Update `onEvent` to update `uiState.simulateFailure` AND call `fetchOrders()`.
- Update `fetchOrders` to clear `error` when starting and use `uiState.value.simulateFailure`.

### [UI]

#### [MODIFY] [OrderSummaryScreen.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/ui/screens/OrderSummaryScreen.kt)
- Update `OrderSummaryScreen` to pass `uiState.simulateFailure` to the content composable.

## Verification Plan

### Manual Verification
1. Launch the app. It should show the "Network unavailable" error.
2. Uncheck "Enable failure".
3. Verify the loading screen appears and then the order list is displayed.
4. Check "Enable failure" again and verify the error returns.
