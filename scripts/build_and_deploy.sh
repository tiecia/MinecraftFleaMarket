rm /minecraft/plugins/MinecraftFleaMarket*.jar
rm /workspaces/MinecraftFleaMarket/target/MinecraftFleaMarket*.jar
cd /workspaces/MinecraftFleaMarket
mvn package
cp ./target/MinecraftFleaMarket*.jar /minecraft/plugins