

function GenreLink({id, genre}) {
    return (
        <a key={id} href={"/cha-movies/genre/" + id}><p>{genre}</p></a>
    )
}

export default GenreLink;