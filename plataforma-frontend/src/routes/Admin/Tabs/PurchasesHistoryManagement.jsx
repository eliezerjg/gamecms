import React from 'react';
import MenuNavbar from '../../../components/admin/MenuNavbar/MenuNavbar.jsx';
import Footer from "../../../components/site/Footer/Footer.jsx";
import AsideMenu from "../../../components/admin/AsideMenu/AsideMenu.jsx";
import PurchasesManagementContent from "../../../components/admin/PurchasesHistoryContent/PurchasesManagementContent.jsx";



const PurchasesHistoryManagement = () => {
    return (
        <div>
            <MenuNavbar/>

            <div className='h-screen bg-gray-900 p-2 flex'>
                <AsideMenu/>
                <div className="bg-gray-900 w-full h-full overflow-auto p-12">
                    <PurchasesManagementContent/>
                </div>

            </div>

            <Footer/>
        </div>
    )
}

export default PurchasesHistoryManagement;
