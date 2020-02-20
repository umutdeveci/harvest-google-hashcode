package harvest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{

    static List<Book> books = new ArrayList<>();
    static List<Library> libraries = new ArrayList<>();
    static int maxDays;
    static int remainingDays;

    static Set<Book> alreadyScannedBooks = new HashSet<>();

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        // Get values
        try (BufferedReader br = new BufferedReader(new FileReader("dataset.txt"))) {
            maxDays = Arrays.stream(br.readLine().split(" ")).map(Integer::valueOf).collect(Collectors.toList()).get(2);
            remainingDays = maxDays;

            int bookId = 0;
            for(String s : br.readLine().split(" ")) {
                int bookValue = Integer.parseInt(s);
                Book b = new Book();
                b.value = bookValue;
                b.id = bookId++;
                books.add(b);
            }

            int i = 0;
            while (br.ready()) {
                Library lb = new Library();
                lb.id = i;
                i++;
                List<Integer> line = Arrays.stream(br.readLine().split(" "))
                    .filter(s -> !s.isEmpty())
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
                if(line.size() != 3) {
                    continue;
                }
                lb.scanCapacityPerDay = line.get(2);
                lb.signInTime = line.get(1);
                lb.books = Arrays.stream(br.readLine().split(" ")).map(Integer::valueOf)
                    .map(integer -> books.get(integer))
                    .sorted(Comparator.comparingInt(o -> o.value))
                    .collect(Collectors.toList());
                if (lb.signInTime < maxDays) {
                    libraries.add(lb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<SelectedLibrary> outputList = new ArrayList<>();

        while(true) {
            int maxValue = 0;
            SelectedLibrary selected = null;

            for(Library l : libraries) {
                SelectedLibrary candidate = selectBooks(l);
                int value = candidate.books.stream().map(book -> book.value).reduce(0, Integer::sum);

                if(value > maxValue) {
                    selected = candidate;
                    maxValue = value;
                    alreadyScannedBooks.addAll(selected.books);
                }
            }

            if(null == selected) {
                break;
            }

            outputList.add(selected);
            final SelectedLibrary finalSelected = selected;
            libraries.removeIf(library -> library.id == finalSelected.id);
        }

        try (BufferedWriter buffer = new BufferedWriter(new FileWriter("output.txt"))){
            buffer.write(String.valueOf(outputList.size()));
            buffer.newLine();

            int a = 0;
            for(SelectedLibrary library : outputList) {
                buffer.write(library.id + " " + library.books.size());
                buffer.newLine();
                buffer.write(
                    library.books.stream()
                    .map(book ->  String.valueOf(book.id))
                    .collect(Collectors.joining(" "))
                );
                a++;
                if(a != outputList.size()) {
                    buffer.newLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static SelectedLibrary selectBooks(Library l) {
        int scannableDays = remainingDays - l.signInTime;
        long numberOfBooks = scannableDays * l.scanCapacityPerDay;

        SelectedLibrary selected = new SelectedLibrary();
        selected.id = l.id;

        if(numberOfBooks > 0) {
            selected.books = l.books.stream().filter(book -> !alreadyScannedBooks.contains(book))
                .limit(numberOfBooks)
                .collect(Collectors.toSet());
        } else {
            selected.books = new HashSet<>();
        }

        return selected;
    }
}
