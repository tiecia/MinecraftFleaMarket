
rm /minecraft/plugins/MinecraftFleaMarket*.jar
rm /workspaces/MinecraftFleaMarket/target/MinecraftFleaMarket*.jar
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
cd /workspaces/MinecraftFleaMarket
mvn package
cp ./target/MinecraftFleaMarket*.jar /minecraft/plugins
export JAVA_HOME=/usr/lib/jvm/default-java/