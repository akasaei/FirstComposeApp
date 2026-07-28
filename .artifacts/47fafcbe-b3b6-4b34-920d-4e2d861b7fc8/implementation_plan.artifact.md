# Pass orderId to OrderDetailViewModel

The goal is to ensure the `OrderDetailViewModel` receives the `orderId` to fetch the correct data when the `OrderDetailScreen` is displayed.

## Proposed Changes

### ViewModel Layer

#### [MODIFY] [OrderDetailViewModel.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/viewmodel/OrderDetailViewModel.kt)
- Remove the `init` block that calls `fetchOrderDetail(null)` to avoid redundant/invalid calls, as the UI will now explicitly trigger the fetch with the correct `orderId`.

### UI Layer

#### [MODIFY] [OrderDetailScreen.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/ui/screens/OrderDetailScreen.kt)
- Add `LaunchedEffect(orderId)` to trigger `viewModel.fetchOrderDetail(orderId)` when the screen is composed or the ID changes.
- Fix `OrderDetailContent` by adding `orderId: String?` to its parameters to resolve the "unresolved reference" error.
- Pass `orderId` from `OrderDetailScreen` to `OrderDetailContent`.

## Verification Plan

### Manual Verification
- Navigate to the Order Detail screen from the Order Summary.
- Verify that the correct `orderId` is displayed on the screen.
- Verify that the ViewModel's `fetchOrderDetail` is called (can be checked via logs or if the UI updates with "real" data if available).
