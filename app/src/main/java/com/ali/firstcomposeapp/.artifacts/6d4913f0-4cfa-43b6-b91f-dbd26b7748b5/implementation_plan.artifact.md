# Fix Duplicate Key Exception in LazyColumn

The application crashes with a `java.lang.IllegalArgumentException: Key "ORD-005" was already used` because the `OrderUiState`'s `orders` list contains duplicate orders. This is caused by the `OrderViewModel`'s `loadPage` function, which appends new orders to the existing list every time the database-backed Flow emits, leading to duplicates when pagination is used or when data is refreshed.

## Proposed Changes

### [Component Name] OrderViewModel

#### [MODIFY] [OrderViewModel.kt](file:///C:/Users/alika/AndroidStudioProjects/FirstComposeApp/app/src/main/java/com/ali/firstcomposeapp/viewmodel/OrderViewModel.kt)

- **Manage Loading States**: Set `isLoading` or `isLoadingMore` to `true` before starting the data fetch in `loadPage`.
- **Prevent Duplicate Emissions**: Use `first()` on the repository's `Flow` in `loadPage` to fetch a single snapshot of the page data instead of keeping multiple active collectors that append duplicates on every database change.
- **Ensure Unique Keys**: Use `distinctBy { it.id }` when updating the `orders` list in `OrderUiState`.
- **Sync Refresh Flow**: Update `refreshOrders` to trigger `loadFirstPage()` upon successful data refresh to ensure the UI reflects the latest remote data.
- **Cleanup init**: Simplify `init` to trigger the refresh flow.

## Verification Plan

### Automated Tests
- Run the application and trigger "More..." (pagination) multiple times.
- Trigger "Refresh" and verify no crashes occur.
- Verify that the total number of items is correct and no duplicates are visible.

### Manual Verification
- Deploy the app to a device/emulator.
- Scroll to the bottom and click "More...".
- Pull to refresh (if implemented) or click "Refresh".
- Check Logcat for any `IllegalArgumentException`.
