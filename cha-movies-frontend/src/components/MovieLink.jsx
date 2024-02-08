

function MovieLink({path, id, name}) {
    return (
        <>
        <a href={path}> <p>{name}</p></a>
        </>
    )
}

export default MovieLink;