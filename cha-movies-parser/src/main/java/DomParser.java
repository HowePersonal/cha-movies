import Entities.Movie;
import Entities.Star;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.function.Function;

public class DomParser {
    List<Movie> movies = new ArrayList<>();
    Map<String, String> movieHistory = new HashMap<>();
    Map<String, Star> starMap = new HashMap<String, Star>();

    // statistic variables
    int inconsistentFilms = 0;
    int duplicateFilms = 0;
    int duplicateStars = 0;
    int notFoundMovies = 0;
    int notFoundStars = 0;

    Database database;

    public DomParser() throws Exception {
        database = new Database();
    }

    public void run() throws Exception {


        Thread thread1 = new Thread(() -> {
            Document movieDom;
            try {
                movieDom = parseXmlFile("stanford-movies/mains243.xml");
                parseMoviesDocument(movieDom);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                movieDom = null;
                System.gc();
            }
        });
        Thread thread2 = new Thread(() -> {
            Document starDom;
            try {
                starDom = parseXmlFile("stanford-movies/actors63.xml");
                parseStarsDocument(starDom);
                starDom = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                starDom = null;
                System.gc();
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println("Finished parsing mains243.xml and actors63.xml");
        Document castDom = parseXmlFile("stanford-movies/casts124.xml");
        parseStarsMovies(castDom);
        castDom = null;
        System.gc();

        System.out.println("Finished parsing cast124.xml");

        movieHistory.clear();
        database.batchInsertMovies(movies);
        movies.clear();
        System.gc();

        database.batchInsertActors(starMap);
        database.batchInsertActorMovies(starMap);

        printData();


    }

    private Document parseXmlFile(String path) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        Document dom = null;

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream inputStream = new FileInputStream(path);
            Reader reader = new InputStreamReader(inputStream, "ISO-8859-1");
            dom = documentBuilder.parse(new InputSource(reader));
        } catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }
        return dom;
    }

    private void parseStarsDocument(Document dom) throws Exception{
        Element documentElement = dom.getDocumentElement();

        int lastActorId = database.getLastActorID();
        NodeList actorList = documentElement.getElementsByTagName("actor");

        for (int i = 0; i < actorList.getLength(); i++) {
            Element actorElement = (Element) actorList.item(i);
            lastActorId += 1;
            Star newStar = parseStar(actorElement, lastActorId);
            if (newStar != null) {starMap.put(newStar.getName(), newStar);}
        }
    }

    private void parseStarsMovies(Document dom) {
        Element documentElement = dom.getDocumentElement();

        NodeList movieBlockList = documentElement.getElementsByTagName("filmc");

        for (int i = 0; i < movieBlockList.getLength(); i++) {
            Element filmBlockElement = (Element) movieBlockList.item(i);
            NodeList filmsList = filmBlockElement.getElementsByTagName("m");
            if (filmsList.getLength() > 0) {
                Element filmElement = (Element) filmsList.item(0);
                String movieID = getTextValue(filmElement, "f");
                if (movieHistory.containsKey(movieID)) {
                    for (int j = 0; j < filmsList.getLength(); j++) {
                        filmElement = (Element) filmsList.item(j);
                        parseStarInFilm(filmElement, movieHistory.get(movieID));
                    }
                }
                else {
                    notFoundMovies++;
                }

            }
        }
    }

    private void parseMoviesDocument(Document dom) throws Exception{

        Element documentElement = dom.getDocumentElement();

        NodeList directorList = documentElement.getElementsByTagName("directorfilms");

        int lastMovieID = database.getLastMovieID();
        for (int i = 0; i < directorList.getLength(); i++) {
            Element directorFilmsElement = (Element) directorList.item(i);

            String directorName = getTextValue(directorFilmsElement, "dirname");

            NodeList filmsList = directorFilmsElement.getElementsByTagName("film");


            for (int j = 0; j < filmsList.getLength(); j++) {
                Element filmElement = (Element) filmsList.item(j);
                Movie newMovie = parseMovie(filmElement, directorName, lastMovieID);
                if (newMovie != null) {
                    movies.add(newMovie);
                    lastMovieID++;
                }
            }
        }
    }

    private Movie parseMovie(Element element, String directorName, int lastMovieID) {
        String id = getTextValue(element, "fid");
        String movieId = "pt" + String.format("%07d", lastMovieID);


        String title = getTextValue(element, "t");
        String year = isDigit(getTextValue(element, "year"));
        if (id == null || title == null || year == null) {
            inconsistentFilms++;
            return null;
        }

        Movie newMovie = new Movie(movieId, title, year, directorName);

        NodeList genreList = element.getElementsByTagName("cat");
        for (int i = 0; i < genreList.getLength(); i++) {
            Element genreElement = (Element) genreList.item(i);
            String genre = genreElement.getTextContent();
            if (genre != null) {
                newMovie.addGenre(genre);
            }
        }
        if (newMovie.genreListSize() == 0 || directorName == null) {
            inconsistentFilms++;
            return null;
        }
        if (movieHistory.containsKey(id)) {
            duplicateFilms++;
            return null;
        }
        movieHistory.put(id, movieId);
        return newMovie;
    }

    private Star parseStar(Element element, int lastActorId) {
        String actorName = getTextValue(element, "stagename");

        if (starMap.containsKey(actorName)) {
            duplicateStars++;
            return null;
        }
        String actorDOB = isDigit(getTextValue(element, "dob"));

        String actorId = "pm" + String.format("%07d", lastActorId);
        return new Star(actorId, actorName, actorDOB);
    }

    private void parseStarInFilm(Element element, String movieId) {
        String actorName = getTextValue(element, "a");

        if (!starMap.containsKey(actorName)) {
            notFoundStars++;
        }
        else if (actorName != null) {
            starMap.get(actorName).addMovie(movieId);
        }


    }

    private String getTextValue(Element element, String tagName) {
        String textVal = null;
        try {
            NodeList nodeList = element.getElementsByTagName(tagName);
            if (nodeList.getLength() > 0) {
                textVal = nodeList.item(0).getFirstChild().getNodeValue();
            }
        }
        catch (Exception e) {
            return null;
        }
        return textVal;
    }

    private String isDigit(String value) {
        if (value == null) {return null;}

        try{
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
        return value;
    }

    public void printData() {
        System.out.println(inconsistentFilms + " films inconsistent");
        System.out.println(duplicateFilms + " films duplicate");
        System.out.println(duplicateStars + " stars duplicate");
        System.out.println(notFoundMovies + " movies not found");
        System.out.println(notFoundStars + " stars not found in cast");
    }

    public static void main(String[] args) throws Exception {
        DomParser domParserExample = new DomParser();
        domParserExample.run();

    }
}
