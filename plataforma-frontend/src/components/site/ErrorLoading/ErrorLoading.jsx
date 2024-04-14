import loadingSpinner from '../../../../public/assets/errorLoading.png';
import React from "react";
import i18n from "../../../lib/i18n.jsx";

function ErrorLoading() {
    return (
        <div className='container mx-auto bg-base-200 p-4 rounded-lg'>
            <img className="mx-auto h-20" src={loadingSpinner}/>
            <p className='text-center mt-2 ml-2'>{i18n.t('site.components.ErrorLoading.content')}</p>
        </div>
    )
}

export default ErrorLoading