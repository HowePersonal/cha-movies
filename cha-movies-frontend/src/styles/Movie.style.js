import styled from 'styled-components'

export const MovieTableStyles = styled.div`
    table {
        width: 100%;
        margin: 25px 0;
        font-size: 18px;
        text-align: left;
        box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
        border-radius: 10px;
        padding: 20px;
    }

    th, td {
        padding: 12px 15px;
        border-bottom: 1px solid grey;
    }

    th {
        font-size: 20px; 
        font-weight: 600;
        letter-spacing: 1px; 
    }

    th:nth-child(1), td:nth-child(1) { 
        width: 35%;
    }

    th:nth-child(2), td:nth-child(2) { 
        width: 20%;
    }

    th:nth-child(3), td:nth-child(3) { 
        width: 10%;
    }

    th:nth-child(4), td:nth-child(4) { 
        width: 10%;
    }

    th:nth-child(5), td:nth-child(5) { 
        width: 25%;
    }

    th:nth-child(6), td:nth-child(6) { 
        width: 10%;
    }

    tr td:last-child {
        border-bottom: none;
    }

`

export const MovieBannerStyles = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;

`

export const MoviePageStyle = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 50px;
    max-width: 800px;
    min-height: 400px;
    gap: 2vw;
    margin: 0 auto;
    box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1);
    border-radius: 10px;

    .movie-title {
        font-size: 30px;
        font-weight: bold;
        margin-top: 20px;
    }

    .movie-info {
        display: flex;
        gap: 50px;
    }

    .actor-list {
        display:flex;
        gap: 10px;
    }
`
