# Task: Fix Duplicate Key Exception in LazyColumn

- [x] Modify `OrderViewModel.kt` to prevent duplicate orders in state [x]
    - [x] Add `first()` to `getOrdersPage` collector in `loadPage`
    - [x] Implement `distinctBy { it.id }` in state update
    - [x] Manage `isLoadingMore` flag in `loadPage`
    - [x] Sync `refreshOrders` with `loadFirstPage`
- [x] Verify logic and build status [x]
- [x] Create Walkthrough [x]
