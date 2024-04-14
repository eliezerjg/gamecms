import React from "react";
import i18n from "../../../lib/i18n.jsx";

function Pagination({previousPage, currentPage, nextPage,totalPages, onPageChange}) {

  const goToPage = (page) => {
        if(page === 0 && currentPage === 0 || (page === totalPages - 1 && page === currentPage))
            return;
        onPageChange(page);
  };

  const isOnLastPage = nextPage === currentPage;
  const labelForNextPage = isOnLastPage ? "" : nextPage;

  const isOnFirstPage = previousPage === 0 && currentPage === 0;
  const labelForPreviousPage = isOnFirstPage ? "" : previousPage;


  return (
      <div className="flex flex-col items-center mt-6">
        <div>
          <button className="btn btn-primary mr-3" onClick={() => goToPage(previousPage)} disabled={isOnFirstPage}>« {labelForPreviousPage || '0'} {i18n.t('site.components.Pagination.previous')}</button>
          <button className="btn btn-primary mr-3"> ({currentPage || '0'})</button>
          <button className="btn btn-primary mr-3" onClick={() => goToPage(nextPage)} disabled={isOnLastPage}>{i18n.t('site.components.Pagination.next')} {labelForNextPage || '0'} »</button>
        </div>
      </div>
  );
}

export default Pagination;
