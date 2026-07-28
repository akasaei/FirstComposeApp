# Fix Navigation Exception in OrderSummaryScreen

The application throws a `java.lang.IllegalArgumentException: Cannot navigate to orders/ORD-001. Navigation graph has not been set for NavController` when clicking on an order in the `OrderSummaryScreen`. This is because `OrderList` creates a new `NavController` using `rememberNavController()` instead of using the one associated with the application's `NavHost`.

## Proposed Changes

### [Component Name] UI Screens

#### [MODIFY] [OrderSummaryScreen.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/ui/screens/OrderSummaryScreen.kt)
- Update `OrderSummaryScreen` to accept `onOrderClick: (String) -> Unit`.
- Update `OrderSummaryContent` to accept `onOrderClick: (String) -> Unit`.
- Update `OrderList` to accept `onOrderClick: (String) -> Unit`.
- Remove `rememberNavController()` from `OrderList`.
- Use `onOrderClick(currentOrder.id)` in the `clickable` modifier of the order row.

### [Component Name] Navigation

#### [MODIFY] [AppNavigation.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/navigation/AppNavigation.kt)
- Update the `composable(AppDestination.Orders.route)` block to pass a navigation callback to `OrderSummaryScreen`.

#### [MODIFY] [App.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/ui/App.kt)
- Update the call to `OrderSummaryScreen` in the manual navigation block (if still needed, though it seems redundant with `AppNavigation`).
- *Note*: I will also fix the `AppNavigation` placement in `Scaffold` if it's indeed misplaced (it's in `topBar` currently).

## Verification Plan

### Manual Verification
- Deploy the app to the device.
- Navigate to the Order Summary screen.
- Click on an order.
- Verify that it navigates to the Order Detail screen without crashing.
