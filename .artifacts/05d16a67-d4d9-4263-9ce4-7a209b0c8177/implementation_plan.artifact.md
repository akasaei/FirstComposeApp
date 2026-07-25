# Implementation Plan - Refactor Order Fetching Logic

The current code fetches orders synchronously within a Composable, which is problematic because it's a `suspend` function and leads to UI blocking and redundant network/database calls on recomposition.

## Proposed Changes

### [Architecture]
- Introduce a `ViewModel` to handle data fetching and state management. This ensures data survives configuration changes and separates UI from business logic.

### [UI]
- Update `OrderSummaryScreen` to observe state from the `ViewModel`.
- Add a loading state to the UI to handle the asynchronous nature of `fetchOrders`.
- Rename variables to be more descriptive (plural `orders` instead of singular `order`).

## Proposed Changes

### com.ali.firstcomposeapp.viewmodel

#### [NEW] [OrderViewModel.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/viewmodel/OrderViewModel.kt)
- Create a `ViewModel` that calls `orderRepository.fetchOrders()` and exposes the result as a `StateFlow`.
- Handle loading and potentially error states.

### com.ali.firstcomposeapp

#### [MODIFY] [MainActivity.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/MainActivity.kt)
- Inject (or initialize) `OrderViewModel` in `OrderSummaryScreen`.
- Observe the `orders` state from the `ViewModel`.
- Update `OrderSummaryContent` to handle the `orders` list.
- Fix the compiler error by properly calling the `suspend` function within the `ViewModel` scope.

## Verification Plan

### Automated Tests
- Build the project to ensure the compiler error is resolved.

### Manual Verification
- Run the app and navigate to "Order summary".
- Verify that a loading state is shown (if implemented) or at least that the UI doesn't freeze.
- Verify that the order list is displayed correctly after the 1-second delay.
