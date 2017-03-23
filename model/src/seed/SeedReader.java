package seed;

import data.DataReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SeedReader implements DataReader<Seed> {

    private static final String fileName = "resources\\seeds_dataset.txt";

    @Override
    public List<Seed> readData() throws IOException {
        List<Seed> seeds = new ArrayList<>();
        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            String nextLine;
            while ((nextLine = input.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(nextLine);
                seeds.add(readSeed(tokenizer));
            }
        }
        return seeds;
    }

    private Seed readSeed(StringTokenizer tokenizer) {
        double area = Double.parseDouble(tokenizer.nextToken());
        double perimeter = Double.parseDouble(tokenizer.nextToken());
        double compactness = Double.parseDouble(tokenizer.nextToken());
        double kernelLength = Double.parseDouble(tokenizer.nextToken());
        double kernelWidth = Double.parseDouble(tokenizer.nextToken());
        double asymmetryCoefficient = Double.parseDouble(tokenizer.nextToken());
        double kernelGrooveLength = Double.parseDouble(tokenizer.nextToken());
        int classId = Integer.parseInt(tokenizer.nextToken());

        return new Seed(
                area,
                perimeter,
                compactness,
                kernelLength,
                kernelWidth,
                asymmetryCoefficient,
                kernelGrooveLength,
                classId);
    }
}
