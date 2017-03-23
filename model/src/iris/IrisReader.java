package iris;

import data.DataReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class IrisReader implements DataReader<Iris> {

    private static final String fileName = "resources\\iris.data.txt";

    @Override
    public List<Iris> readData() throws IOException {
        List<Iris> irises = new ArrayList<>();
        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            String nextLine;
            while ((nextLine = input.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(nextLine, ",");
                irises.add(readIris(tokenizer));
            }
        }
        return irises;
    }

    private Iris readIris(StringTokenizer tokenizer) {
        double sepalLength = Double.parseDouble(tokenizer.nextToken());
        double sepalWidth = Double.parseDouble(tokenizer.nextToken());
        double petalLength = Double.parseDouble(tokenizer.nextToken());
        double petalWidth = Double.parseDouble(tokenizer.nextToken());
        Iris.IrisClass irisClass;
        switch (tokenizer.nextToken()) {
            case "Iris-setosa":
                irisClass = Iris.IrisClass.SETOSA;
                break;
            case "Iris-versicolor":
                irisClass = Iris.IrisClass.VERSICOLOUR;
                break;
            case "Iris-virginica":
                irisClass = Iris.IrisClass.VIRGINICA;
                break;
            default:
                throw new RuntimeException("Unexpected class");
        }
        return new Iris(
                sepalLength,
                sepalWidth,
                petalLength,
                petalWidth,
                irisClass
        );
    }
}
