# Agenta
A authserver and skin fix for legacy versions of minecraft.

# Compatibility
Requires Java 7. (In theory this can be built for Java 5, but I lack the build tools required for that.)
If running this on Java 12 and newer, please add the following JVM argument: ```--add-exports java.base/sun.net.www.protocol.http=ALL-UNNAMED```

1) As a mod: The same Jar will run on forge, bukkit and Risugami's modloader for Minecaft versions Alpha 1.0.4 to Minecraft release 1.7.10 (These are merely the versions I tested)
2) As a JavaAgent: Add the following JVM argument: ```-javaagent:{path}/agent.jar``` (replace path with the location of the agent.jar)
3) Inline/static: In this setup, Agenta is essentially taking over the role of Launching minecraft. Add the following JVM argument and change out ```{mainClass}``` as needed depending on which minecraft version you are launching: ```-Dagenta.main.class={mainClass}```
4) As a Bukkit plugin (Tested with bukkit for Minecraft Beta 1.2_01)

# JVM/Congig settings 
Non of these are needed to launch Agenta. The available contexts are  <br>
"\*" => ALL<br>
"static" => When Agenta is launched as the main class with the expectation of chainloading another class manually. <br>
"config" => Only appears in the config file. 
<table>
<tr>
<th>Context</th><th>property</th><th>Argument</th><th>description</th>
</tr>
<tr>
<tr>
<td>*</td><td>agenta.assets.fml</td><td>-Dagenta.assets.fml={value}</td><td>A link to an archive Agenta should forward legacy forge download requests to</td>
</tr>
<tr>
<td>*</td><td>agenta.assets.index</td><td>-Dagenta.assets.index={value}</td><td>The URL for the asset index Agenta should use as a base</td>
</tr>
<tr>
<td>*</td><td>agenta.assets.routing</td><td>-Dagenta.assets.routing=true</td><td>Should agenta reroute requests the mojang's asset servers? (default: "true")</td>
</tr>
<tr>
<td>*</td><td>agenta.assets.url</td><td>-Dagenta.assets.url={value}</td><td>The URL for the resource server Agenta should pull assets from</td>
</tr>
<tr>
<td>config</td><td>agenta.config.version</td><td></td><td>The version of agenta that generated a set config file. Will be used to detect if agenta should upgrade a set config file</td>
</tr>
<tr>
<td>*</td><td>agenta.prt.color</td><td>-Dagenta.prt.color=false</td><td>Pass the value 'false' to disable colour printing.</td>
</tr>
<tr>
<td>*</td><td>agenta.prt.debug</td><td>-Dagenta.prt.debug=false</td><td>Should agenta show debug messages?</td>
</tr>
<tr>
<td>*</td><td>agenta.save.file</td><td>-Dagenta.save.file=saves.json</td><td>When handling saving for certain versions of old Minecraft that relied on long dead mojang endpoints, what should agenta call the save file? (default: "saves.json")</td>
</tr>
<tr>
<td>*</td><td>agenta.skin.resize</td><td>-Dagenta.skin.resize=true</td><td>Should agenta try to resize skins from 64x64 to 64x32 for better compatibility? (default: "true")</td>
</tr>
<tr>
<td>static</td><td>agenta.main.class</td><td>-Dagenta.main.class={value}</td><td>The class Agenta should try to load next when running in static/inline mode</td>
</tr>
</table>

# Fixes
- Skin support for pre 1.7.10 versions 
- Cape support for pre 1.7.10 versions
- Sound for versions of the game that predate 1.6
- World savings for certain pre-alpha builds (experimental)
- Forge 1.5.2 being unable to fetch it's library files (experimental)

# Note
The same jar will work for almost every version of minecraft that predates Release 1.8
