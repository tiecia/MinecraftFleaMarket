echo "Checking for plugins directory..."
if [ -e /minecraft/plugins ]
then
    echo "Plugins directory found!"
else
    echo "Creating plugins directory"
    mkdir /minecraft/plugins
fi

cd /minecraft/plugins

echo "Checking for BileTools2..."
if [ -e /minecraft/plugins/BileTools-*.jar ]
then
    echo "BileTools2 found! Skipping download."
else
    echo "BileTools2 not found. Downloading now..."
    wget https://github.com/VolmitSoftware/BileTools/releases/download/2/BileTools-2.jar
fi

echo "Checking for PlugManX..."
if [ -e /minecraft/plugins/PlugManX-*.jar ]
then
    echo "PlugManX found! Skipping download."
else
    echo "PlugManX not found. Downloading now..."
    wget https://github.com/tiecia/PlugManX-releases/releases/download/v2.3.0/PlugManX-2.3.0.jar
fi
echo "Development plugins installed!"
echo "Initializing server...this will take a while."
/my_init