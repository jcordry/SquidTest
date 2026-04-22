package uk.ac.tees.mam.u0026939

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
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
    private lateinit var batch: SpriteBatch
    private lateinit var tiles: Array<Array<TextureRegion>>
    private lateinit var dungeon: Array<CharArray>
    private lateinit var camera: OrthographicCamera

    fun tileIndex(c: Char): Int = when (c) {
        '#' -> 3  // wall tile
        '.' -> 7  // floor tile
        '+' -> 0  // door tile
        '/' -> 0  // diagonal door
        '~' -> 2  // water
        '^' -> 1  // trap
        ',' -> 6  // Floor/grass
        '"' -> 5  // ?

        else -> 4
    }

    override fun render(delta: Float) {
        camera.update()
        batch.projectionMatrix = camera.combined

        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        batch.use {
            for (y in dungeon.indices) {
                for (x in dungeon[y].indices) {
                    val region = tiles[0][tileIndex(dungeon[y][x])]
                    val drawY = (dungeon.size - 1 - y) * 32f
                    it.draw(region, x * 32f, drawY)
                }
            }
        }

       dungeon.forEach {
        //    Gdx.app.log("Open Doors", it.concatToString())
           println(it.concatToString())
       }

//        dungeon = DungeonUtility.openDoors(dungeon)
//        dungeon.forEach {
//            Gdx.app.log("Close Doors", it.concatToString())
//        }




    }

    override fun dispose() {
        batch.disposeSafely()
    }

    override fun show() {
        super.show()

        val rng = RNG(654321)
        val generator = DungeonGenerator(19, 25, rng)
        generator.addDoors(12, true)
        generator.addTraps(3)
        generator.addGrass(20)
        // generator.addWater(25)

        // generate dungeon (SquidLib)
        dungeon = generator.generate()
        dungeon = DungeonUtility.closeDoors(dungeon)
        batch = SpriteBatch()

        val texture = Texture("colours.png")
        tiles = TextureRegion.split(texture, 32, 32)

        camera = OrthographicCamera()
        camera.setToOrtho(false, 800f, 600f) // window size


    }
}
