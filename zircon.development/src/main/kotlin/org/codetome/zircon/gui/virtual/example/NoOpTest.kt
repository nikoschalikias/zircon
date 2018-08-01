package org.codetome.zircon.gui.virtual.example

import org.codetome.zircon.Stats
import org.codetome.zircon.api.data.*
import org.codetome.zircon.api.grid.TileGrid
import org.codetome.zircon.gui.swing.impl.BufferedImageCP437Tileset
import org.codetome.zircon.gui.virtual.impl.NoOpRenderer
import org.codetome.zircon.internal.graphics.DefaultLayer
import org.codetome.zircon.internal.graphics.MapTileImage
import org.codetome.zircon.internal.grid.RectangleTileGrid
import java.util.*

fun main(args: Array<String>) {

    val size = Size.create(60, 30)

    val tileset = BufferedImageCP437Tileset.rexPaint16x16()

    val tileGrid = RectangleTileGrid(
            tileset = tileset,
            size = size)

    val renderer = NoOpRenderer(tileGrid)

    val random = Random()
    val terminalWidth = size.xLength
    val terminalHeight = size.yLength
    val layerCount = 20
    val layerWidth = 20
    val layerHeight = 10
    val layerSize = Size.create(layerWidth, layerHeight)
    val filler = CharacterTile('x')

    val layers = (0..layerCount).map {

        val imageLayer = MapTileImage(layerSize, tileset)
        layerSize.fetchPositions().forEach {
            imageLayer.setTileAt(it, filler)
        }

        val layer = DefaultLayer(
                position = Position.create(
                        x = random.nextInt(terminalWidth - layerWidth),
                        y = random.nextInt(terminalHeight - layerHeight)),
                backend = imageLayer)

        tileGrid.pushLayer(layer)
        layer
    }

    val chars = listOf('a', 'b')

    var currIdx = 0
    var loopCount = 0


    while (true) {
        Stats.addTimedStatFor("terminalBenchmark") {
            val tile = CharacterTile(chars[currIdx])
            fillGrid(tileGrid, tile)
            layers.forEach {
                it.moveTo(Position.create(
                        x = random.nextInt(terminalWidth - layerWidth),
                        y = random.nextInt(terminalHeight - layerHeight)))
            }
            renderer.render()
            currIdx = if (currIdx == 0) 1 else 0
            loopCount++
        }
        if (loopCount.rem(100) == 0) {
            Stats.printStats()
        }
    }
}

private fun fillGrid(tileGrid: TileGrid<Char, out Any>, tile: Tile<Char>) {
    (0..tileGrid.getBoundableSize().yLength).forEach { y ->
        (0..tileGrid.getBoundableSize().xLength).forEach { x ->
            tileGrid.setTileAt(GridPosition(x, y), tile)
        }
    }
}
