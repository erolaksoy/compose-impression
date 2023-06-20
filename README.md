## Introduction

This library provides a way to track impressions in a lazy list using Compose. It includes an `ImpressionState` interface that defines the methods needed to track impressions, You can add validator classes that validate impressions based on your own logic.

## Usage

To use the code, first create an `ImpressionState` instance. Then, use the `impression` modifier to track impressions for specific items in your lazy list. The `impression` modifier takes three arguments:

- The key of the item to track.
- The `ImpressionState` instance to use.
- A callback function that will be called when the impression is tracked.

The following is an example of how to use the `impression` modifier to track impressions for a list of items:

```kotlin
val impressionState = rememberImpressionState(lazyListState)

LazyColumn(state = lazyListState) {
    items(list) {
        Item(
            modifier = Modifier.impression(key = "item key", impressionState = impressionState) {
                // Do something when the impression is tracked for item.
            }
        )
    }
}

```

The `impression` modifier will track the impression of each item when it is first placed in the viewport of the lazy list. The callback function will be called when the impression is tracked.


## Existing Validators

- VisibilityPercentImpressionValidator

The `VisibilityPercentImpressionValidator` class can be used to validate impressions based on the visibility percentage of an item.

To use `VisibilityPercentImpressionValidator`, first create an instance of it and pass it to the `ImpressionState` instance you created. You can set the visibility percentage threshold that you want to use to validate impressions.

```
val impressionState = rememberImpressionState(lazyListState) {
    addValidator(VisibilityPercentImpressionValidator(0.5f)) // set the visibility percentage threshold to 50%
}

```

With this configuration, when an item is 50% visible, the impression will be validated and the callback function provided to the `impression` modifier will be called.

With this configuration, the `VisibilityPercentImpressionValidator` will validate impressions for each item in the lazy list based on the visibility percentage threshold that you set.

## API Reference

The following is a reference to the APIs provided by the code:

- `ImpressionState` interface: Defines the methods needed to track impressions.
- `VisibilityPercentImpressionValidator` class: A class that can validate impressions based on the visibility percentage of an item.
- `impression` modifier: A modifier that can track impressions for specific items in a lazy list.

## Contributing

Contributions are welcome! Please open a pull request on GitHub if you would like to contribute to the code.

## License

The code is licensed under the MIT License.

## Support

If you have any questions or problems, please open an issue on GitHub.
