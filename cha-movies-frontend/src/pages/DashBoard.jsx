import {useState, useEffect} from 'react'
import getAllTableNames from '../api/allTableNames'
import TableData from '../components/tableData'

function DashBoard() {
    const [tables, setTables] = useState([])

    useEffect(() => {
        getAllTableNames().then(tableData => {
            setTables(tableData)
        }).catch(error => {
            console.log("Error when getting all table names")
        })
    }, [])

    const tableMap = tables && tables.map(table => 
        <TableData table_name = {table.table_name}/>
    )

    return (
        <>
        <a href={"/cha-movies/_dashboard/addmovie"} >Add a movie</a>
        <a href={"/cha-movies/_dashboard/addstar"} >Add a star</a>
        <h1>MetaData</h1>
        {tableMap}
        </>
        
    )
}

export default DashBoard