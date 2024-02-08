import Nmovies from "./Nmovies";
import SortTable from "../sort/SortTable";
import { PageOptionStyle } from "../styles/Other.style";

function PageOptions({setMoviePerPage, moviePerPage, setSortRating, setSortTitle, setSortOrder}) {

    return (
        <PageOptionStyle>
            <div>
                <Nmovies
                    setMoviePerPage={setMoviePerPage}
                    currentMoviePerPage={moviePerPage}
                />
            </div>
            <div>
                <SortTable
                    setSortRating={setSortRating}
                    setSortTitle={setSortTitle}
                    setSortOrder={setSortOrder}
                />
            </div>
        </PageOptionStyle>
    )
}

export default PageOptions;