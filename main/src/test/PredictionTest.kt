package test

import data.dataholder.DoubleDataHolder
import data.dataholder.PredictionData
import learning.LogitBoost
import learning.prediction.PredictionModel
import learning.regressors.LogisticRegressor
import sinus.SinusReader
import java.awt.*
import java.util.*
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

class PredictionTest : JComponent() {

    private val model: PredictionModel
    private var dataHolder = DoubleDataHolder(SinusReader(0.5, 2000), TRAIN_PERCENT, true, false)

    private val trainingData = ArrayList<PredictionData>()
    private val testData = ArrayList<PredictionData>()

    private var completeIterations: Int = 0

    init {
        initDataSets()

        val logitBoost = LogitBoost { LogisticRegressor(WINDOW_SIZE, 0.5, 1000) }
        model = PredictionModel(logitBoost)

        trainLoop()
    }

    private fun trainLoop() {
        trainIteration()
        if (completeIterations % 1 == 0) {
            repaint()
        }
        completeIterations++
        if (completeIterations < ITERATIONS) {
            EventQueue.invokeLater { this.trainLoop() }
        } else {
            calculateError()
            //System.exit(0);
        }
    }

    private fun calculateError() {
        var error = 0.0
        for (data in testData) {
            val networkValue = model.predictNext(data.asVector())
            error += (networkValue - data.output()) * (networkValue - data.output())
        }
        error /= testData.size.toDouble()
        System.err.printf("Finished with error = %.4f", error)
    }

    private fun trainIteration() {
        model.trainAll(trainingData)
    }

    private fun initDataSets() {
        val trainDataValues = dataHolder.getTrainData()
        val testDataValues = dataHolder.getTestData()

        run {
            var i = 0
            while (i + WINDOW_SIZE < trainDataValues.size - 1) {
                val vector = DoubleArray(WINDOW_SIZE)
                for (j in 0..WINDOW_SIZE - 1) {
                    vector[j] = trainDataValues[i + j]
                }
                trainingData.add(PredictionData(vector, trainDataValues[i + WINDOW_SIZE]))
                i++
            }
        }

        //Collections.shuffle(trainingData);

        var i = 0
        while (i + WINDOW_SIZE < testDataValues.size - 1) {
            val vector = DoubleArray(WINDOW_SIZE)
            for (j in 0..WINDOW_SIZE - 1) {
                vector[j] = testDataValues[i + j]
            }
            testData.add(PredictionData(vector, testDataValues[i + WINDOW_SIZE]))
            i++
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        drawData(g, Color.BLUE)
        drawNetworkOutputTest(g, Color.RED)

        g.drawString("Iteration = " + completeIterations, 400, 400)
    }

    private fun drawNetworkOutputTest(g: Graphics, color: Color) {
        g.color = Color.GREEN
        var x = WINDOW_SIZE
        for (predictionData in trainingData) {
            val drawX = Math.round(1.0 * x / dataHolder.data.size * width).toInt()
            val y = model.predictNext(predictionData.asVector())
            if (!java.lang.Double.isFinite(y)) {
                throw RuntimeException()
            }
            val drawY = Math.round(y * height).toInt()
            g.drawLine(drawX, drawY, drawX, drawY)
            x++
        }

        g.color = color
        x = trainingData.size + WINDOW_SIZE + 1
        for (predictionData in testData) {
            val drawX = Math.round(1.0 * x / dataHolder.data.size * width).toInt()
            val y = model.predictNext(predictionData.asVector())
            if (!java.lang.Double.isFinite(y)) {
                throw RuntimeException()
            }
            val drawY = Math.round(y * height).toInt()
            g.drawLine(drawX, drawY, drawX, drawY)
            x++
        }
    }

    private fun drawData(g: Graphics, color: Color) {
        g.color = color
        for (x in 0..dataHolder.data.size - 1) {
            val drawX = Math.round(1.0 * x / dataHolder.data.size * width).toInt()
            val y = dataHolder.data[x]
            val drawY = Math.round(y * height).toInt()
            g.drawLine(drawX, drawY, drawX, drawY)
        }
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(800, 600)
    }

    companion object {

        private val TRAIN_PERCENT = 90.0
        private val WINDOW_SIZE = 30
        private val ITERATIONS = 5000

        @JvmStatic fun main(args: Array<String>) {
            val main = PredictionTest()

            val contentPane = JPanel(BorderLayout())
            contentPane.add(main)

            val frame = JFrame()
            frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            frame.contentPane = contentPane
            frame.isLocationByPlatform = true
            frame.pack()
            frame.isVisible = true
        }
    }
}
