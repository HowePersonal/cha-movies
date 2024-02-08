import {useState, useEffect} from 'react'
import getMetaData from '../api/metaData'
import Attributes from './Attributes'

function TableData({table_name}) {

    const [meta, setMeta] = useState([])

    useEffect(() => {
        getMetaData(table_name).then(FeildData => {
            setMeta(FeildData)
        }).catch(error => {
            console.log("Error when getting all table meta data")
        })
    }, [])

    const metaMap = meta && meta.map(metadata => 
        <Attributes field = {metadata.field} type = {metadata.type}/>
    )

    return (
        <>
        <h2>{table_name}</h2>
        <tr>
            <td>field</td>
            <td>type</td>
        </tr>
        {metaMap}
        </>
        
    )
}

export default TableData;