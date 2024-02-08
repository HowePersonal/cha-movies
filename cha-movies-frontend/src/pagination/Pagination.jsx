import React from 'react'


function Pagination({ totalMovies, moviesPerPage, setCurrentPage, currentPage }) {
    let pages = [];

    for (let i = 1; i <= Math.ceil(totalMovies / moviesPerPage); i++) {
        pages.push(i);
    }

    return (
        <div className='pagination'>
            <button 
                onClick={() => setCurrentPage(currentPage - 1)} 
                disabled={currentPage === 1}>
                Prev
            </button>

            {/* {pages.map((page, index) => (
                <button 
                    key={index} 
                    onClick={() => setCurrentPage(page)} 
                    disabled={page === currentPage ? 'active' : ''}>
                    {page}
                </button>
            ))} */}

            <button 
                onClick={() => setCurrentPage(currentPage + 1)} 
                disabled={currentPage === pages.length}>
                Next
            </button>
        </div>
    );
};

export default Pagination;