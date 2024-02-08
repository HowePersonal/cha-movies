import {MovieTableStyles} from "../styles/Movie.style"

function MovieTable({movieBanners}) {
    return (
        <MovieTableStyles>
        <table>
        <tr>
           <th>Title</th> 
           <th>Director</th> 
           <th>Year</th> 
           <th>Rating</th> 
           <th>Stars</th> 
           <th>Genres</th> 
        </tr>
        {movieBanners}
        </table>
        </MovieTableStyles>
    )
}

export default MovieTable;