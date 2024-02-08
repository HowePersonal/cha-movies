import {useState, useEffect} from 'react'


function Attributes({field, type}) {

    return (
        <>
        <tr>
            <td>{field}</td>
            <td>{type}</td>
        </tr>
        </>
        
    )
}

export default Attributes;