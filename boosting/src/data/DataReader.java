package data;

import java.io.IOException;
import java.util.List;

public interface DataReader<T> {
    List<T> readData() throws IOException;
}
