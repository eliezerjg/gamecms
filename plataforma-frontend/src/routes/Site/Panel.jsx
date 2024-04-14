import React from 'react'
import PanelContent from '../../components/site/PanelContent/PanelContent.jsx';
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
        name: "Painel",
        ref: "/painel"
    },
];

const Panel = () => {
    return (
        <div className='h-screen  w-full'>
            <MenuNavbar/>

            <BreadCrumbs itensCrumbs={barraCrumbs}/>


            <div className="h-5/6 paper mb-48 text-xl overflow-y-auto">
                <PanelContent/>
            </div>

            <Footer/>
        </div>
    )
}

export default Panel