import React, {useState} from 'react';
import MenuNavbar from "../../components/site/MenuNavbar/MenuNavbar.jsx";
import BreadCrumbs from "../../components/site/BreadCrumbs/BreadCrumbs.jsx";
import DownloadContent from "../../components/site/DownloadContent/DownloadContent.jsx";
import Footer from "../../components/site/Footer/Footer.jsx";


const barraCrumbs = [
    {
        type: "a",
        name: "Home",
        ref: "/"
    },
    {
        type: "a",
        name: "Download",
        ref: "/download"
    },
];

const Download = () => {


    return (
        <div>
            <MenuNavbar/>

            <div className='h-screen bg-gray-900 p-2'>
                <div className="p-2 w-full">
                    <BreadCrumbs itensCrumbs={barraCrumbs}/>
                </div>


                <DownloadContent/>
            </div>

            <Footer/>
        </div>
    );
}

export default Download;
