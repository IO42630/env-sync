### Testing Scenario
Test two configs:
1. FileOps happen while System is down. 
1. FileOps happen while System is running.

<br>


| Symbol | Description|
---|---
`a` | File `a` in directory `A`
`b` | File `b` in directory `B`
`d(x)` | File `x` is deleted.
`c(x)` | File `x` is created.
`m(x)` | File `x` is modified.


<br>

| `Given` | | `When` | | `Then` | |
---|---|---|---|---|--- 
| `A` | `B`| `A` |  `B`|`A` |  `B`|
| `a` |  | `d(a)` |  |  |   |
| `a` | `b` | `d(a)` | `d(b)`  |  |   |


