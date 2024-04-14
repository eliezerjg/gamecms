import React from 'react'
import RankingList from '../../components/site/RankingList/RankingList'
import MenuNavbar from "../../components/site/MenuNavbar/MenuNavbar.jsx";
import BreadCrumbs from "../../components/site/BreadCrumbs/BreadCrumbs.jsx";
import Footer from "../../components/site/Footer/Footer.jsx";

const barraCrumbs = [
    {
        type: "a",
        name: "Home",
        ref: "/"
    },
    {
        type: "a",
        name: "Ranking",
        ref: "/ranking"
    },
];

const Ranking = () => {
    return (
        <div className='h-screen overflow-y-auto '>
            <MenuNavbar/>

            <div className="p-2 w-full">
                <BreadCrumbs itensCrumbs={barraCrumbs}/>
            </div>

            <RankingList/>

            <Footer/>
        </div>
    )
}

export default Ranking