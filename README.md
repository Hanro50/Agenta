# Agenta
A authserver and skin fix for legacy versions of minecraft.

# JVM settings 
Non of these are needed to launch Agenta. The available contexts are  <br>
"\*" => ALL<br>
"static" => When Agenta is launched as the main class with the expectation of chainloading another class manually. 

<table>
<tr>
<th>Context</th><th>property</th><th>Argument</th><th>description</th>
</tr>
<tr>
<td>*</td><td>agenta.assets.index</td><td>-Dagenta.assets.index={value}</td><td>The URL for the asset index Agenta should use as a base</td>
</tr>
<tr>
<td>*</td><td>agenta.assets.url</td><td>-Dagenta.assets.url={value}</td><td>The URL for the resource server Agenta should pull assets from</td>
</tr>
<tr>
<td>*</td><td>agenta.assets.fml</td><td>-Dagenta.assets.fml={value}</td><td>A link to an archive Agenta should forward legacy forge download requests to</td>
</tr>
<tr>
<td>*</td><td>agenta.console.colour</td><td>-Dagenta.console.colour=false</td><td>Pass the value 'false' to disable colour printing.</td>
</tr>
<tr>
<td>static</td><td>agenta.main.class</td><td>-Dagenta.main.class={value}</td><td>The class Agenta should try to load next when running in static/inline mode</td>
</tr>
</table>
