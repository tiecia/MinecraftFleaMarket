# MinecraftFleaMarket
This is a spigot plugin for
# Usage

```
/buy <quantity> <id>
/sell <quantity> <price>
/market <list:balance:help> <search term>
```

## Buy
```
/buy <quantity> <id>
```
This command lets you buy items from the market. ID is the leftmost number in the listing.

## Sell
```
/sell <quantity> <price>
```
This command lets you post an item to the market. Note: The player only recieves money once someone buys the item.

## Market
```
/market <list:balance:help> <search term>
```
This command lets you list/search for items in the market, view your current bank balance, and link to this help page.
<br/><br/>

# Examples

```
/sell 40 1
```
Sell 40 of the item in my hand for $1.
```
/market list
```
```
/market list log
```
```
/buy 20 4
```
Buy 20 of item 4.
```
/market balance
```