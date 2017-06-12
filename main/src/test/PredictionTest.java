package test;

import data.dataholder.DoubleDataHolder;
import data.dataholder.PredictionData;
import finance.FinanceReader;
import learning.LogitBoost;
import learning.prediction.PredictionModel;
import learning.regressors.LogisticRegressor;
import sinus.SinusReader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PredictionTest extends JComponent {

    private final static double TRAIN_PERCENT = 90;
    private final static int WINDOW_SIZE = 30;
    private final static int ITERATIONS = 5000;

    private final PredictionModel model;
    private DoubleDataHolder dataHolder;

    private List<PredictionData> trainingData = new ArrayList<>();
    private List<PredictionData> testData = new ArrayList<>();

    private int completeIterations;

    public PredictionTest() {
        initDataSets();

        LogitBoost logitBoost = new LogitBoost(() -> new LogisticRegressor(WINDOW_SIZE, 0.5, 1000));
        model = new PredictionModel(logitBoost);

        trainLoop();
    }

    private void trainLoop() {
        trainIteration();
        if (completeIterations % 1 == 0) {
            repaint();
        }
        completeIterations++;
        if (completeIterations < ITERATIONS) {
            EventQueue.invokeLater(this::trainLoop);
        } else {
            calculateError();
            //System.exit(0);
        }
    }

    private void calculateError() {
        double error = 0;
        for (PredictionData data : testData) {
            double networkValue = model.predictNext(data.asVector());
            error += (networkValue - data.output()) * (networkValue - data.output());
        }
        error /= testData.size();
        System.err.printf("Finished with error = %.4f", error);
    }

    private void trainIteration() {
        model.trainAll(trainingData);
    }

    private void initDataSets() {
        //dataHolder = new SingleValueDataHolder(new SinusReader(1, 2500));
        dataHolder = new DoubleDataHolder(new SinusReader(0.5, 2000), TRAIN_PERCENT, true, false);

        java.util.List<Double> trainDataValues = dataHolder.getTrainData();
        java.util.List<Double> testDataValues = dataHolder.getTestData();

        for (int i = 0; i + WINDOW_SIZE < trainDataValues.size() - 1; i++) {
            double[] vector = new double[WINDOW_SIZE];
            for (int j = 0; j < WINDOW_SIZE; j++) {
                vector[j] = trainDataValues.get(i + j);
            }
            trainingData.add(new PredictionData(vector, trainDataValues.get(i + WINDOW_SIZE)));
        }

        //Collections.shuffle(trainingData);

        for (int i = 0; i + WINDOW_SIZE < testDataValues.size() - 1; i++) {
            double[] vector = new double[WINDOW_SIZE];
            for (int j = 0; j < WINDOW_SIZE; j++) {
                vector[j] = testDataValues.get(i + j);
            }
            testData.add(new PredictionData(vector, testDataValues.get(i + WINDOW_SIZE)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawData(g, Color.BLUE);
        drawNetworkOutputTest(g, Color.RED);

        g.drawString("Iteration = " + completeIterations, 400, 400);
    }

    private void drawNetworkOutputTest(Graphics g, Color color) {
        g.setColor(Color.GREEN);
        int x = WINDOW_SIZE;
        for (PredictionData predictionData : trainingData) {
            int drawX = (int) Math.round(1.0 * x / dataHolder.getData().size() * getWidth());
            double y = model.predictNext(predictionData.asVector());
            if (!Double.isFinite(y)) {
                throw new RuntimeException();
            }
            int drawY = (int) Math.round(y * getHeight());
            g.drawLine(drawX, drawY, drawX, drawY);
            x++;
        }

        g.setColor(color);
        x = trainingData.size() + WINDOW_SIZE + 1;
        for (PredictionData predictionData : testData) {
            int drawX = (int) Math.round(1.0 * x / dataHolder.getData().size() * getWidth());
            double y = model.predictNext(predictionData.asVector());
            if (!Double.isFinite(y)) {
                throw new RuntimeException();
            }
            int drawY = (int) Math.round(y * getHeight());
            g.drawLine(drawX, drawY, drawX, drawY);
            x++;
        }
    }

    private void drawData(Graphics g, Color color) {
        g.setColor(color);
        for (int x = 0; x < dataHolder.getData().size(); x++) {
            int drawX = (int) Math.round(1.0 * x / dataHolder.getData().size() * getWidth());
            double y = dataHolder.getData().get(x);
            int drawY = (int) Math.round(y * getHeight());
            g.drawLine(drawX, drawY, drawX, drawY);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public static void main(String[] args) {
        JComponent main = new PredictionTest();

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(main);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(contentPane);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }
}
