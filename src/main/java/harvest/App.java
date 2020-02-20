package harvest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{

    static List<Integer> books;
    static List<Library> libraries = new ArrayList<>();
    static int availableDays;

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        try (BufferedReader br = new BufferedReader(new FileReader("dataset.txt"))) {
            availableDays = Arrays.stream(br.readLine().split(" ")).map(Integer::valueOf).collect(Collectors.toList()).get(2);

            books = Arrays.stream(br.readLine().split(" ")).map(Integer::valueOf).collect(Collectors.toList());

            while (br.ready()) {
                Library lb = new Library();
                List<Integer> line = Arrays.stream(br.readLine().split(" ")).map(Integer::valueOf).collect(Collectors.toList());
                lb.scanCapacityPerDay = line.get(2);
                lb.signInTime = line.get(1);
                lb.books = Arrays.stream(br.readLine().split(" ")).map(Integer::valueOf).collect(Collectors.toList());
                if (lb.signInTime < availableDays) {
                    libraries.add(lb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
