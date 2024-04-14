import React from 'react'
import MenuNavbar from "../../components/site/MenuNavbar/MenuNavbar.jsx";
import BreadCrumbs from "../../components/site/BreadCrumbs/BreadCrumbs.jsx";
import NewsCards from "../../components/site/NewsCards/NewsCards.jsx";
import Footer from "../../components/site/Footer/Footer.jsx";


const barraCrumbs = [
    {
        type: "a",
        name: "Home",
        ref: "/"
    },
    {
        type: "a",
        name: "News",
        ref: "/news"
    },
];

const News = () => {
    return (
        <div className='h-screen overflow-y-auto w-full bg-gray-900'>
            <MenuNavbar/>

            <div className=" p-2 w-full">
                <BreadCrumbs itensCrumbs={barraCrumbs}/>
            </div>

            <div className="mb-28 text-xl  overflow-y-auto">

                <NewsCards/>
            </div>

            <Footer/>
        </div>
    );
};


export default News;
