import React from 'react'
import MenuNavbar from '../../../components/admin/MenuNavbar/MenuNavbar.jsx';
import Footer from "../../../components/site/Footer/Footer.jsx";
import AsideMenu from "../../../components/admin/AsideMenu/AsideMenu.jsx";
import ShopManagementContent from "../../../components/admin/ShopManagementContent/ShopManagementContent.jsx";

const ShopManagement = () => {
    return (
        <div>
            <MenuNavbar/>

            <div className='h-screen bg-gray-900 p-2 flex'>
                <AsideMenu/>
                <div className="bg-gray-900 w-full h-full overflow-auto p-12">
                    <ShopManagementContent/>
                </div>
            </div>

            <Footer/>
        </div>
    )
}

export default ShopManagement