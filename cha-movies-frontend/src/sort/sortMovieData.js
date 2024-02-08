function sortMovieData(movieData, sortOrder, sortTitle, sortRating) {

    movieData.sort((a, b) => {

        if (sortOrder === "title") {
            let titleComparison = 0;
            if (sortTitle === "ASC") {
                titleComparison = a.movie_title.localeCompare(b.movie_title);
            } else {
                titleComparison = b.movie_title.localeCompare(a.movie_title);
            }
            
            if (titleComparison !== 0) return titleComparison;


            return sortRating === "ASC" ? a.movie_rating - b.movie_rating : b.movie_rating - a.movie_rating;
        } 


        let ratingDifference = a.movie_rating - b.movie_rating;
        if (sortRating === "ASC") {
            if (ratingDifference !== 0) return ratingDifference;
        } else {
            if (ratingDifference !== 0) return -ratingDifference;
        }


        return sortTitle === "ASC" ? a.movie_title.localeCompare(b.movie_title) : b.movie_title.localeCompare(a.movie_title);
    });

    return movieData;
}


export default sortMovieData;

