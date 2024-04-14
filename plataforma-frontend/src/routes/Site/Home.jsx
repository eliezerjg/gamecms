import React, {useState, useTransition} from 'react'
import MenuNavbar from "../../components/site/MenuNavbar/MenuNavbar.jsx";
import Footer from "../../components/site/Footer/Footer.jsx";
import MiniRankingList from "../../components/site/MiniRankingList/MiniRankingList.jsx";
import i18n from "../../lib/i18n.jsx"

const siteData = JSON.parse(sessionStorage.getItem('siteData') || `{}`);


const Home = () => {
    return (
        <div className='h-screen overflow-y-auto w-full'>
            <MenuNavbar/>


            <div className='relative text-center text-sm w-full mb-12'>
                <div className='md:block md:absolute md:right-0 md:mr-12 md:w-1/2 '>
                    <MiniRankingList />
                </div>
            </div>



            <div className='text-center hidden md:block homepagetitle md:mt-[28vh]'>
                <h1>{siteData && siteData.homePageTitle}</h1>
            </div>

            <div className="container mx-auto h-5/6 paper mb-48 text-xl overflow-y-auto">
                {siteData && (
                    <div className="text-black p-24" dangerouslySetInnerHTML={{ __html: siteData.homePageText }}>
                    </div>
                )}
            </div>

            <Footer/>
        </div>
    )
}

export default Home