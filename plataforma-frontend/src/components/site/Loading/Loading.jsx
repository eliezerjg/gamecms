import loadingSpinner from '../../../../public/assets/loading.gif';
import React from "react";
import i18n from "../../../lib/i18n.jsx";

function Loading() {
    return (
        <div className='container mx-auto'>
            <img className="mx-auto h-20" src={loadingSpinner}/>
            <p className='text-center mt-2 ml-2'>{i18n.t('site.components.Loading.content')}</p>
        </div>
    )
}

export default Loading