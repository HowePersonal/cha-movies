import styled from 'styled-components'

export const ActorPageStyle = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 50px;
    max-width: 600px;
    min-height: 400px;
    margin: 0 auto;
    box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1);
    border-radius: 10px;

    .actor-name {
        font-size: 30px;
        font-weight: bold;
        margin-top: 20px;
    }

    .actor-movies {
        display: flex;
        gap: 30px;
    }
`
