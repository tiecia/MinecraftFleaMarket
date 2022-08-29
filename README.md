![Java CI with Maven](https://github.com/tiecia/MinecraftFleaMarket/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master-release)
# MinecraftFleaMarket
This is a Spigot plugin that provides a basic peer-to-peer store system for minecraft servers.
<br/><br/>
Tested Spigot Versions: 1.15, 1.15.2, 1.16.3, 1.17, 1.18, 1.19
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
![alt text](https://github.com/tiecia/MinecraftFleaMarket/blob/master-release/images/list.png?raw=true)
<br/><br/>

```
/market list log
```
![alt text](https://github.com/tiecia/MinecraftFleaMarket/blob/master-release/images/search.png?raw=true)
<br/><br/>

```
/buy 20 4
```
Buy 20 of item 4.
<br/><br/>

```
/market balance
```
![alt text](https://github.com/tiecia/MinecraftFleaMarket/blob/master-release/images/balance.png?raw=true)
<br/><br/>
# Contributing
Contributions to this project are welcome! I have created a custom vscode devcontaier to make developing this plugin extremely easy.

The included devcontainer sets up the maven build environment, starts a minecraft server on port `25565`, and includes custom tasks for automatically building and deploying the plugin to the server.

If you would like to contribute: 
 1. Fork this repo and open it in vscode
 1. Setup docker on your machine and install https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.vscode-remote-extensionpack
 1. Open `MinecraftFleaMarket.code-workspace` and reopen the project in the container (from command palette)
 1. Make your changes
 1. Use `CTRL-SHIFT-B` to build and automatically reload plugin in the server
 1. Create a pull request with your changes

If you find an bug, issue, or have a question, please create an issue detailing your experience/question.