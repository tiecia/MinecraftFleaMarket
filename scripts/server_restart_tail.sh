supervisorctl stop spigot
supervisorctl start spigot
sleep 3s
tail -f "${SPIGOT_HOME}/logs/latest.log"