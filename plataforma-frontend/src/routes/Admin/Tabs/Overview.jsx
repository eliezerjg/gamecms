import React from 'react'
import MenuNavbar from '../../../components/admin/MenuNavbar/MenuNavbar.jsx';
import Footer from "../../../components/site/Footer/Footer.jsx";
import AsideMenu from "../../../components/admin/AsideMenu/AsideMenu.jsx";
import i18n from "../../../lib/i18n.jsx";


const Overview = () => {
    return (
        <div>
            <MenuNavbar/>

            <div className="flex flex-grow">
                <AsideMenu/>

                <div className="flex-grow conteudo-painel bg-gray-900 w-full">
                    <h2 className="text-2xl font-bold mb-3 text-white p-12">{i18n.t('admin.routes.Tabs.Overview.content')}</h2>
                    <p className="text-justify ml-12 bg-base-100 p-4 w-3/4">
                        {i18n.t('admin.routes.Tabs.Overview.text')}
                    </p>
                </div>
            </div>

            <Footer/>
        </div>
    )
}

export default Overview