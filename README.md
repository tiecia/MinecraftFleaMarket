# MinecraftFleaMarket
This is a Spigot plugin that provides a basic peer-to-peer store system for minecraft servers.
<br/><br/>
Tested Minecraft Versions: 1.15.2

Note: This plugin will probably work with most Spigot server versions. If it works on a version not listed above please make a pull request adding the version to the list.
<br/><br/>

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

Hold item to sell in hand. If quantity is more than player is holding the market will search the players inventory.

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
<br/><br/>

```
/market list
```
![alt text](https://github.com/tiecia/MinecraftFleaMarket/blob/containerize/images/list.png?raw=true)
<br/><br/>

```
/market list log
```
![alt text](https://github.com/tiecia/MinecraftFleaMarket/blob/containerize/images/search.png?raw=true)
<br/><br/>

```
/buy 20 4
```
Buy 20 of item 4.
<br/><br/>

```
/market balance
```
![alt text](https://github.com/tiecia/MinecraftFleaMarket/blob/containerize/images/balance.png?raw=true)