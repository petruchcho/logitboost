import data.DataHolder;
import iris.Iris;
import iris.IrisReader;

import java.util.Arrays;

public class Runner {
    public static void main(String[] args) {
        DataHolder<Iris> irisDataHolder = new DataHolder<>(new IrisReader());
        irisDataHolder.normalize();
        for (Iris iris : irisDataHolder.getData()) {
            System.out.println(Arrays.toString(iris.asVector()));
        }
    }
}
