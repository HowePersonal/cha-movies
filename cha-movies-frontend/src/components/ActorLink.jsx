import {ActorLinkStyle} from '../styles/Other.style';

function ActorLink({id, name}) {
    return (
        <ActorLinkStyle>
        <a href={"/cha-movies/actors/"+id}> <p>{name}</p></a>
        </ActorLinkStyle>
    )
}

export default ActorLink;