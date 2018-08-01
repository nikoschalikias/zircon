package org.codetome.zircon.api.builder.graphics

import org.codetome.zircon.api.builder.Builder
import org.codetome.zircon.api.data.Position
import org.codetome.zircon.api.data.Size
import org.codetome.zircon.api.data.Tile
import org.codetome.zircon.api.graphics.StyleSet
import org.codetome.zircon.api.graphics.TileImage
import org.codetome.zircon.api.resource.CP437TilesetResource
import org.codetome.zircon.api.tileset.Tileset
import org.codetome.zircon.internal.graphics.MapTileImage
import java.awt.image.BufferedImage

/**
 * Creates [org.codetome.zircon.api.graphics.TileImage]s.
 * Defaults:
 * - Default [Size] is `ONE` (1x1).
 * - Default `filler` is an `EMPTY` character
 */
@Suppress("ArrayInDataClass")
data class TileImageBuilder<T: Any, S: Any>(
        private var tileset: Tileset<T, S>,
        private var size: Size = Size.one(),
        private var style: StyleSet = StyleSet.defaultStyle(),
        private val tiles: MutableMap<Position, Tile<T>> = mutableMapOf()) : Builder<TileImage<T, S>> {

    fun tileset(tileset: Tileset<T, S>) = also {
        this.tileset = tileset
    }

    fun style(style: StyleSet) = also {
        this.style = style
    }

    /**
     * Sets the size for the new [TileImage].
     * Default is 1x1.
     */
    fun size(size: Size) = also {
        this.size = size
    }

    /**
     * Adds a [Tile] at the given [Position].
     */
    fun tile(position: Position, tile: Tile<T>) = also {
        require(size.containsPosition(position)) {
            "The given character's position ($position) is out create bounds for text image size: $size."
        }
        tiles[position] = tile
    }

    override fun build(): TileImage<T, S> = MapTileImage(
            size = size,
            tileset = tileset,
            styleSet = StyleSet.defaultStyle())

    override fun createCopy() = copy()

    companion object {

        /**
         * Creates a new [TileImageBuilder] to build [org.codetome.zircon.api.graphics.TileImage]s.
         */
        // TODO: generics
        fun <T: Any, S: Any> newBuilder(tileset: Tileset<T, S>) = TileImageBuilder(
                tileset = tileset)
    }
}
