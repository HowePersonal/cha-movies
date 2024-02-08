import {useState} from "react";


function SortTable({setSortTitle, setSortRating, setSortOrder}){
    


    const options = [
        {label: "Title ASC Rating ASC", value: ["ASC", "ASC", "title"]},
        {label: "Title ASC Rating DESC", value: ["ASC", "DESC", "title"]},
        {label: "Title DESC Rating ASC", value: ["DESC", "ASC", "title"]},
        {label: "Title DESC Rating DESC", value: ["DESC", "DESC", "title"]},
        {label: "Rating ASC Title ASC", value: ["ASC", "ASC", "rating"]},
        {label: "Rating ASC Title DESC", value: ["ASC", "DESC", "rating"]},
        {label: "Rating DESC Title ASC", value: ["DESC", "ASC", "rating"]},
        {label: "Rating DESC Title DESC", value:["DESC", "DESC", "rating"]}
    ]

    function sortSelect(event) {
        const order = event.target.value.split(',')
        if(order[2] === "title"){
            setSortTitle(order[0])
            setSortRating(order[1])
            setSortOrder(order[2])
        }
        else{
            setSortTitle(order[1])
            setSortRating(order[0])
            setSortOrder(order[2])
        }
    }


    return (
        <div className="Sorting">
            <h4>Sort</h4>
            <select className="title-select" onChange={sortSelect}>
                {options.map(option => (
                    <option value={option.value}>{option.label}</option>
                ))}
            </select>
        </div>
    )

}

export default SortTable;