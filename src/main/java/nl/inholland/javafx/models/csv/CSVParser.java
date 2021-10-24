package nl.inholland.javafx.models.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import nl.inholland.javafx.models.Movie;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class CSVParser {

    public List<Movie> parseMovie(){
        List<Movie> movieList = new ArrayList<>();
        try{
            CSVReader reader=
                    new CSVReaderBuilder(new FileReader("src/main/resources/files/movies.csv")).
                            withSkipLines(0). // Skiping firstline as it is header
                            build();
            movieList=reader.readAll().stream().map(data-> {
                Movie movie= new Movie(data[0], Double.parseDouble(data[1]), Integer.parseInt(data[2]));
                return movie;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movieList;
    }
}
