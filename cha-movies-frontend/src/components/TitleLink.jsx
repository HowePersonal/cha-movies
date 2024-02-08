function TitleLink({title}) {
    return (
        <a href={"/cha-movies/title/" + title}><p>{title}</p></a>
    )
}

export default TitleLink;