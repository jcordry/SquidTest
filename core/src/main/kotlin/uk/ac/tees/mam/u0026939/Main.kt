package uk.ac.tees.mam.u0026939

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.async.KtxAsync
import ktx.graphics.use
import squidpony.squidgrid.mapping.DungeonGenerator
import squidpony.squidgrid.mapping.DungeonUtility
import squidpony.squidmath.RNG

class Main : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }
}

class FirstScreen : KtxScreen {
    private val image = Texture("logo.png".toInternalFile(), true).apply { setFilter(Linear, Linear) }
    private val batch = SpriteBatch()

    private lateinit var dungeon: Array<CharArray>

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        batch.use {
            it.draw(image, 100f, 160f)
        }

        dungeon.forEach {
            println(it.concatToString())
        }
    }

    override fun dispose() {
        image.disposeSafely()
        batch.disposeSafely()
    }

    override fun show() {
        super.show()

        val rng = RNG()
        val generator = DungeonGenerator(40, 25, rng)

        dungeon = generator.generate()
        dungeon = DungeonUtility.closeDoors(dungeon)
    }
}
