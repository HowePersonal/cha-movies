import styled from 'styled-components'

export const Selections = styled.div`
    width: 70%;
    display: flex;
    gap: 10px;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;

    @media (max-width: 600px) {
        width: 90%;
    }

`

export const Home = styled.div`
   display: flex;
   flex-direction: column;
   height: 100vh;
   align-items: center;
`