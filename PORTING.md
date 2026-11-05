# Gnetum: port a Fabric (Minecraft 1.20.1)

Fuente: Gnetum 1.20.1 (Forge), versión 2.5.1, licencia LGPL-3.0-only, autor decce.

## Estado

Este port **no está compilado ni probado**. Se hizo sin acceso a Minecraft, Fabric Loom ni Fabric API.
Los errores de compilación y de inyección de mixins se verán en la primera ejecución de GitHub Actions y en el juego.

## Qué cambió

| Parte | Forge | Fabric |
|---|---|---|
| Punto de entrada | `@Mod`, bus de eventos | `ClientModInitializer`, `ClientTickEvents`, `ClientPlayConnectionEvents` |
| Tecla de configuración | `RegisterKeyMappingsEvent` | `KeyBindingHelper` (tecla END, igual que antes) |
| Pantalla de configuración | `ConfigScreenHandler` | Mod Menu (`GnetumModMenu`) |
| Línea "HUD: fps" en F3 | `CustomizeGuiOverlayEvent.DebugText` | Mixin en `DebugScreenOverlay.getGameInformation` |
| Overlays del HUD | `ForgeGui` / `IGuiOverlay` | Mixin en `Gui.render` y en cada elemento vanilla (`GuiMixin`) |
| Rutas y mods cargados | `FMLPaths`, `ModList` | `FabricLoader` |
| Deslizador de enteros | `ForgeSlider` | `AbstractSliderButton` (`IntSlider`) |
| Proyección GUI | `ForgeHooksClient.getGuiFarPlane()` | Constante `11000` (mismo valor que Forge) |

## Lo que no se portó

- **Compatibilidad con Embeddium/Sodium** (página de opciones). Requiere la API de opciones de Sodium para Fabric.
- **Compatibilidad con Jade, JourneyMap y Gui Clock.** Dependían de eventos de Forge.
- **Caché por mod en HUD de terceros.** En Forge cada overlay tenía su propio evento y su mod dueño. En Fabric,
  los HUD de otros mods se dibujan en `HudRenderCallback`, dentro del mismo método que el HUD vanilla. Por eso
  aquí se dibujan sin caché. Las listas "mods (antes)/mods (después)" de la pantalla de configuración quedan vacías.
- **Caché del HUD vanilla depende de nombres de métodos.** `GuiMixin` engancha `renderCrosshair`, `renderItemHotbar`,
  etc. Esos nombres son una suposición que hay que validar contra las fuentes de Minecraft 1.20.1 (mapeos de Mojang).
  Si un nombre no existe, ese elemento se dibuja sin caché (comportamiento vanilla).

## Cómo se verifica

1. Compilar con GitHub Actions (workflow `build.yml`). Se descarga el `.jar` desde Artifacts.
2. Iniciar el juego. Si el motor del HUD falla, se registra un error en `latest.log` y el HUD vuelve a dibujarse sin caché.
3. Buscar en `latest.log` la línea `Gnetum: elementos interceptados ...`. Muestra qué elementos vanilla se
   engancharon y cuáles no.

## Versiones que hay que revisar antes de compilar

- `fabric-loom` 1.5-SNAPSHOT (`build.gradle.kts`)
- Gradle 8.8 (`gradle-wrapper.properties`)
- `fabric-loader` 0.15.11, `fabric-api` 0.92.2+1.20.1, `modmenu` 7.2.2 (`gradle.properties`)
